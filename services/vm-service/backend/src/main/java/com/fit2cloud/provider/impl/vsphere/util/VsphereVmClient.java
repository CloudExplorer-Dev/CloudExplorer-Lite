package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.constants.Language;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.utils.ClsApiClient;
import com.fit2cloud.common.provider.impl.vsphere.utils.VapiAuthenticationHelper;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.impl.vsphere.entity.VsphereTemplate;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmCreateRequest;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vcenter.ovf.DiskProvisioningType;
import com.vmware.vcenter.ovf.LibraryItemTypes;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: LiuDi
 * Date: 2022/9/22 11:06 AM
 */
@Slf4j
public class VsphereVmClient extends VsphereClient {

    public VsphereVmClient(VsphereCredential credential, String regionId, Language lang) {
        super(credential.getVCenterIp(), credential.getVUserName(), credential.getVPassword(), regionId, Optional.ofNullable(lang).orElse(Language.zh_CN));
    }

    /**
     * 获取云主机
     *
     * @return 云主机列表
     */
    public List<VirtualMachine> listVirtualMachines() {
        List<VirtualMachine> list = listResources(VirtualMachine.class);
        List<VirtualMachine> vmList = new ArrayList<>();
        for (VirtualMachine vm : list) {
            VirtualMachineConfigInfo cfg = vm.getConfig();
            if (cfg == null || !cfg.isTemplate()) {
                vmList.add(vm);
            }
        }
        return vmList;
    }

    /**
     * 获取镜像
     *
     * @return 镜像列表
     */
    public List<VirtualMachine> listTemplates() {
        List<VirtualMachine> list = listResources(VirtualMachine.class);
        List<VirtualMachine> templateList = new ArrayList<>();
        for (VirtualMachine vm : list) {
            VirtualMachineConfigInfo cfg = vm.getConfig();
            if (cfg != null && cfg.isTemplate()) {
                templateList.add(vm);
            }
        }
        return templateList;
    }

    /**
     * 获取云主机创建时间
     *
     * @param mor
     * @param runtime
     * @return
     */
    public Date getVmCreateDate(ManagedObjectReference mor, VirtualMachineRuntimeInfo runtime) {
        try {
            // reference: http://asvignesh.in/how-to-get-a-virtual-machine-created-time/
            final EventFilterSpec eventFilterSpec = new EventFilterSpec();
            eventFilterSpec.setEventTypeId(new String[]{"VmCreatedEvent", "VmBeingDeployedEvent", "VmRegisteredEvent", "VmClonedEvent"});

            EventFilterSpecByEntity entity = new EventFilterSpecByEntity();
            entity.setEntity(mor);
            EventFilterSpecRecursionOption recOption = EventFilterSpecRecursionOption.self;
            entity.setRecursion(recOption);

            eventFilterSpec.setEntity(entity);
            final Event[] events = getSi().getEventManager().queryEvents(eventFilterSpec);
            if (events != null) {
                for (Event event : events) {
                    Calendar calendar = event.createdTime;
                    if (calendar == null) {
                        continue;
                    } else {
                        return calendar.getTime();
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        try {
            if (runtime != null) {
                Calendar bootTime = runtime.getBootTime();
                if (bootTime != null) {
                    return bootTime.getTime();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }

    public VirtualMachine getTemplateFromAll(String name) {
        return getResourceFromAll(VirtualMachine.class, name);
    }

    /**
     * 获取云主机具有的磁盘
     *
     * @param vm 云主机实例
     * @return 云主机的磁盘列表
     */
    public List<VirtualDisk> getVirtualDisks(VirtualMachine vm) {
        List<VirtualDisk> disks = new ArrayList<>();
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null && devices.length > 0) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        disks.add((VirtualDisk) device);
                    }
                }
            }
        }
        return disks;
    }

    /**
     * 关闭电源
     *
     * @param uuid
     * @return
     */
    public boolean powerOff(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        //电源已是关闭状态
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is power off!");
        }
        try {
            Task task = virtualMachine.powerOffVM_Task();
            String result = task.waitForTask();
            if (!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(), result)) {
                throw new RuntimeException("TaskInfo - " + task.getTaskInfo().getDescription());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 开机
     *
     * @param uuid
     * @return
     */
    public boolean powerOn(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOn.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is power on!");
        }
        try {
            HostSystem hostSystem = getHost(virtualMachine);
            Task task = virtualMachine.powerOnVM_Task(hostSystem);
            String result = task.waitForTask();
            if (!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(), result)) {
                throw new RuntimeException("TaskInfo - " + task.getTaskInfo().getDescription());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 关机
     *
     * @param uuid
     * @return
     */
    public boolean shutdownInstance(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is shutdown!");
        }
        try {
            virtualMachine.shutdownGuest();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 重启
     *
     * @param uuid
     * @return
     */
    public boolean reboot(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is shutdown!");
        }
        try {
            virtualMachine.rebootGuest();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param uuid
     * @return
     */
    public boolean deleteInstance(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOn.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is running!");
        }
        try {
            Task task = virtualMachine.destroy_Task();
            String result = task.waitForTask();
            if (!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(), result)) {
                throw new RuntimeException("TaskInfo - " + task.getTaskInfo().getDescription());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 硬重启
     * 先关闭电源再打开
     *
     * @param uuid
     * @return
     */
    public boolean hardReboot(String uuid) {
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if (StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(), virtualMachine.getRuntime().getPowerState().name())) {
            throw new RuntimeException("The current state of the virtual machine is shutdown!");
        }
        try {
            Task powerOffTask = virtualMachine.powerOffVM_Task();
            String powerOffResult = powerOffTask.waitForTask();
            if (!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(), powerOffResult)) {
                throw new RuntimeException("hardReboot  powerOffTaskInfo - " + powerOffTask.getTaskInfo().getDescription());
            }
            HostSystem hostSystem = getHost(virtualMachine);
            Task powerOnTask = virtualMachine.powerOnVM_Task(hostSystem);
            String powerOnResult = powerOnTask.waitForTask();
            if (!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(), powerOnResult)) {
                throw new RuntimeException("hardReboot  powerOnTaskInfo - " + powerOnTask.getTaskInfo().getDescription());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public VirtualMachine createVm(VsphereVmCreateRequest request) {
        try {
            // init host / computeResource / resourcePool / datacenter / folder
            request.setRegionId(request.getRegion());

            int currentIndex = request.getIndex();

            String datastoreName = request.getDatastore();
            String clusterName = request.getCluster();
            String vmName = request.getServerInfos().get(currentIndex).getName();
            String templateName = request.getTemplate();
            String folderName = request.getFolder();
            String locationType = request.getComputeConfig().getLocation();

            long memory = request.getRam() * 1024L;
            int cpu = request.getCpu();

            String diskType = request.getDiskType();

            List<VsphereVmCreateRequest.NetworkAdapter> networkAdapters = request.getNetworkConfigs().get(currentIndex).getAdapters();


            Datacenter datacenter = getDataCenter(request.getRegion());
            ClusterComputeResource cluster = getCluster(clusterName);

            Folder folder = null;
            if (StringUtils.isNotBlank(folderName) && !VsphereClient.FOLDER_ROOT.equals(folderName)) {
                folder = getFolder(folderName);
            }
            if (folder == null) {
                folder = datacenter.getVmFolder();
            }

            int cpuSockets = 1;

            HostSystem hostSystem = null;
            ResourcePool resourcePool = null;

            if (StringUtils.equals("host", locationType)) {
                hostSystem = getHost(request.getComputeConfig().getName());
                cpuSockets = hostSystem.getSummary().getHardware().getNumCpuPkgs();
            } else if (StringUtils.equals("pool", locationType)) {
                resourcePool = getResourcePool(request.getComputeConfig().getName());
            }

            Datastore datastore = getDatastore(datastoreName, datacenter);


            VirtualMachine template = getTemplateFromAll(templateName);

            //todo 内容库需要处理
            long diskSize = 0;

            if (template == null && templateName.contains(VsphereTemplate.SEPARATOR)) {
                /*从内容库部署*/
                VirtualMachine vm = deployVMFromLibraryItem(request, hostSystem, resourcePool, folder, datastore);
                if (vm == null)
                    vm = getVirtualMachine(vmName);

                return reconfigVm(vm, cpu, memory, diskSize, diskType, true, true, "Created-by-FIT2CLOUD-from-template:" + templateName, null, null, null);
            } else if (template == null && !templateName.contains(VsphereTemplate.SEPARATOR)) {
                throw new RuntimeException("模版不存在");
            }

            VirtualMachineConfigInfo templateConfig = template.getConfig();
            VirtualDevice[] devices = templateConfig.getHardware().getDevice();
            List<VsphereVmCreateRequest.DiskConfig> diskConfigs = request.getDisks();

            StoragePlacementSpec spSpec = null;


            // TODO 未处理内容库创建的情况
            long diskSum = 0;
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualDisk vd) {
                    long capacityInKB = vd.getCapacityInKB();
                    diskSum += capacityInKB / 1024 / 1024;

                }
            }


            // init VirtualMachineConfigSpec
            VirtualMachineConfigSpec config = new VirtualMachineConfigSpec();
            config.setName(vmName);
            config.setAnnotation("Created-by-FIT2CLOUD-from-template:" + templateName);
            // 当用户输入备注的时候设置
//            if (StringUtils.isNotBlank(request.getRemark())) {
//                config.setAnnotation(request.getRemark());
//            }
            config.setMemoryMB(memory);
            config.setNumCPUs(cpu);
            config.setCpuHotAddEnabled(true);
            config.setMemoryHotAddEnabled(true);
            config.setNumCoresPerSocket((int) Math.ceil((double) cpu / (double) cpuSockets));

            //关闭vm_tools自动升级
            ToolsConfigInfo tools = new ToolsConfigInfo();
            tools.setToolsUpgradePolicy("manual");
            config.setTools(tools);

            VirtualMachineCloneSpec spec = new VirtualMachineCloneSpec();

            String os = templateConfig.getGuestFullName();
            String osType = "Linux";
            if (StringUtils.containsIgnoreCase(os, "Win")) {
                osType = "Windows";
            }
            String vCenterVersion = this.getVCenterVersion();
            log.info("vCenter Version: " + vCenterVersion);
            // vcenter 5.5 以上使用自定义规范
            if (this.isUseCustomSpec()) {
                spec.setCustomization(getCustomizationSpec(osType, request));
            }
            spec.setPowerOn(true);
            spec.setTemplate(false);

            // 存储集群
            ManagedEntity parent = datastore.getParent();
            VirtualMachineRelocateSpec location = new VirtualMachineRelocateSpec();
            if (parent instanceof StoragePod) {
                String dsClusterName = parent.getName();
                StoragePod sp = getStoragePod(dsClusterName);
                StorageDrsPodSelectionSpec drsPodSpec = new StorageDrsPodSelectionSpec();
                drsPodSpec.setStoragePod(sp.getMOR());
                spSpec = new StoragePlacementSpec();
                spSpec.setPodSelectionSpec(drsPodSpec);
                spSpec.setVm(template.getMOR());
                spSpec.setCloneName(vmName);
                spSpec.setType(StoragePlacementSpecPlacementType.clone.name());
                spSpec.setFolder(folder.getMOR());
                spSpec.setCloneSpec(spec);
            } else {
                location.setDatastore(datastore.getMOR());
            }

            // 磁盘设置
            List<VirtualDeviceConfigSpec> machineSpecs = new ArrayList<>();

            List<Integer> controllerKeys = new ArrayList<>();
            // 原有的磁盘格式
            String oldDiskType = DiskType.DEFAULT;
            List<VirtualMachineRelocateSpecDiskLocator> diskLocator = new ArrayList<>();
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualDisk vd) {
                    controllerKeys.add(device.getControllerKey());
                    VirtualDeviceBackingInfo backing = device.getBacking();
                    if (backing instanceof VirtualDiskFlatVer2BackingInfo backingInfo) {
                        if (Boolean.TRUE.equals(backingInfo.getThinProvisioned())) {
                            oldDiskType = DiskType.THIN;
                        } else if (Boolean.TRUE.equals(backingInfo.getEagerlyScrub())) {
                            oldDiskType = DiskType.EAGER_ZEROED;
                        } else {
                            oldDiskType = DiskType.THICK;
                        }
                        // 按照 diskType 设置模板的配置
                        // 对模板上的每一块磁盘进行设置
                        // vc上存储集群无法修改原有磁盘的格式，会导致报错 Datastore unspecified for at least one disk in SDRS-disabled VM
                        if (!(parent instanceof StoragePod) && !StringUtils.equals(diskType, DiskType.DEFAULT)) {
                            backingInfo.setThinProvisioned(diskType.equalsIgnoreCase("thin"));
                            backingInfo.setEagerlyScrub(diskType.equalsIgnoreCase("THICK"));
                            VirtualMachineRelocateSpecDiskLocator locator = new VirtualMachineRelocateSpecDiskLocator();
                            locator.setDatastore(datastore.getMOR());
                            locator.setDiskBackingInfo(backingInfo);
                            locator.setDiskId(vd.getKey());
                            diskLocator.add(locator);
                        }
                    }
                }
            }
            if (diskLocator.size() > 0) {
                location.setDisk(diskLocator.toArray(new VirtualMachineRelocateSpecDiskLocator[0]));
            }
            // 使用镜像里的磁盘格式
            if (StringUtils.equals(diskType, DiskType.DEFAULT)) {
                diskType = oldDiskType;
            }

            //todo 替换为传入的盘list
            if (diskConfigs.size() > controllerKeys.size()) {

                int key = -1;

                List<Integer> unitNumberList = new ArrayList<>();
                for (final VirtualDevice device : devices) {
                    if (device.getUnitNumber() != null && controllerKeys.contains(device.getControllerKey())) {
                        unitNumberList.add(device.getUnitNumber());
                        if (device.getKey() > key) {
                            key = device.getKey();
                        }
                    }
                }

                for (int i = controllerKeys.size(); i < diskConfigs.size(); i++) {

                    diskSize = diskConfigs.get(i).getSize();
                    VirtualDeviceConnectInfo virtualDeviceConnectInfo = new VirtualDeviceConnectInfo();
                    virtualDeviceConnectInfo.setStartConnected(true);
                    virtualDeviceConnectInfo.setConnected(true);
                    virtualDeviceConnectInfo.setAllowGuestControl(false);

                    int unitNumber = 0;
                    for (int j = 0; j < 16; j++) {
                        if (!unitNumberList.contains(j)) {
                            unitNumber = j;
                            unitNumberList.add(unitNumber);
                            break;
                        }
                    }

                    VirtualDisk vd = new VirtualDisk();
                    VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();
                    diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
                    diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
                    vd.setCapacityInKB(diskSize * 1024 * 1024);
                    vd.setConnectable(virtualDeviceConnectInfo);
                    vd.setUnitNumber(unitNumber);
                    key++; //设置唯一key
                    vd.setKey(key);
                    vd.setControllerKey(controllerKeys.get(0));

                    VirtualDiskFlatVer2BackingInfo backinginfo = new VirtualDiskFlatVer2BackingInfo();
//                    backinginfo.setDatastore(datastore);
                    backinginfo.setFileName(""); // 坑
                    backinginfo.setDiskMode(VirtualDiskMode.persistent.name());
                    if (DiskType.THICK.equalsIgnoreCase(diskType)) {
                        backinginfo.setThinProvisioned(Boolean.FALSE);
                    } else if (DiskType.THIN.equalsIgnoreCase(diskType)) {
                        backinginfo.setThinProvisioned(Boolean.TRUE);
                    } else if (DiskType.EAGER_ZEROED.equalsIgnoreCase(diskType)) {
                        backinginfo.setEagerlyScrub(Boolean.TRUE);
                    }
                    vd.setBacking(backinginfo);

                    diskSpec.setDevice(vd);
                    machineSpecs.add(diskSpec);
                }
            }


            // 网络设置
            //当在存储群集中创建虚拟机时，网络选择为模版的网络时需注意，当网络不合法时，创建虚拟机将会失败，错误提示形如：无法访问虚拟机配置，无法访问文件[datastore1 (35)]
            //当时出现该错误提示时使用的模版的网络为vCenter的管理网络，不知道这个网络和datastore1 (35)有啥关系
            VirtualDevice[] virtualDevices = templateConfig.getHardware().getDevice();
            List<VirtualEthernetCard> virtualEthernetCards = new ArrayList<>();
            for (VirtualDevice virtualDevice : virtualDevices) {
                if (virtualDevice instanceof VirtualEthernetCard) {
                    virtualEthernetCards.add((VirtualEthernetCard) virtualDevice);
                }
            }
            // 获取网卡信息
            List<VirtualDeviceConfigSpec> virtualDeviceConfigSpecs = getVirtualDeviceConfigSpecs(virtualEthernetCards, networkAdapters, request.getComputeConfig().getLocation(), hostSystem, resourcePool, cluster);
            machineSpecs.addAll(virtualDeviceConfigSpecs);

            if (machineSpecs.size() > 0) {
                config.setDeviceChange(machineSpecs.toArray(new VirtualDeviceConfigSpec[0]));
            }

            if (hostSystem != null && !StringUtils.equals(ResourceConstants.DRS, locationType)) {
                ManagedObjectReference hostMor = hostSystem.getMOR();
                location.setHost(hostMor);
                log.info("set vm [" + vmName + "] to host: " + hostSystem.getName());
            }
            location.setPool(cluster.getResourcePool().getMOR());
            if (resourcePool != null) {
                location.setPool(resourcePool.getMOR());
            }

            spec.setLocation(location);
            spec.setConfig(config);

            Task task;
            log.info("begin to create vm : " + vmName);
            if (spSpec != null) {
                ManagedObjectReference storageResourceMOR = getSi().getServiceContent().getStorageResourceManager();
                StorageResourceManager resourceManager = new StorageResourceManager(getSi().getServerConnection(), storageResourceMOR);

                //需要打开存储群集的DRS
                StoragePlacementResult result = resourceManager.recommendDatastores(spSpec);

                ClusterRecommendation recomandations[] = result.getRecommendations();
                log.info("recomandations->");
                String[] strs = new String[1];
                for (ClusterRecommendation c : recomandations) {
                    log.info(c.getKey());
                    log.info("" + c.getRating());
                    log.info(c.getReason());
                    log.info(c.getReasonText());
                    log.info(c.getType());
                    strs[0] = c.getKey();
                }
                task = resourceManager.applyStorageDrsRecommendation_Task(strs);
            } else {
                task = template.cloneVM_Task(folder, vmName, spec);
            }

            String status = task.waitForTask();
            if (!status.equals(Task.SUCCESS)) {
                log.info("failed to create vm : " + vmName + ", errorMsg : " + task.getTaskInfo().getError().getLocalizedMessage());
                throw new RuntimeException("failed to create vm! error: " + task.getTaskInfo().getError().getLocalizedMessage());
            } else {
                log.info("success to create vm : " + vmName);
                VirtualMachine vm = getVirtualMachine(vmName);
                int count = 160;
                while (count-- > 0) {
                    try {
                        if (isUseCustomSpec()) {
                            if (checkCustomizationEvent(vm)) {
                                log.info("vm: " + vmName + "created，password valid，custom definition completed.");
                            } else {
                                log.debug("vm: " + vmName + "created，password valid，，custom definition uncompleted.");
                                continue;
                            }
                        }
                        return vm;
                    } catch (Exception e) {
                        log.info(e.toString());
                    } finally {
                        Thread.sleep(3000);
                    }
                }
                if (count <= 0) {
                    log.info("Verify os username password timeout");
                    return vm;
                }

                return vm;
            }
        } catch (VmConfigFault e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Virtual machine configuration error!" + e.getLocalizedMessage(), e);
        } catch (InvalidState e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Invalid state!" + e.getLocalizedMessage(), e);
        } catch (InvalidDatastore e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Invalid data storage settings!" + e.getLocalizedMessage(), e);
        } catch (RuntimeException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error creating virtual machine!" + e.getMessage(), e);
        }
    }

    /* 内容库start */

    /**
     * 内容库部署
     */
    public VirtualMachine deployVMFromLibraryItem(VsphereVmCreateRequest request, HostSystem host, ResourcePool resourcePool, Folder folder, Datastore datastore) {
//        ClsApiClient clsApiClient = request.getClsApiClient();

        int currentIndex = request.getIndex();

        String clusterId = request.getCluster();
        String vmName = request.getServerInfos().get(currentIndex).getName();
        String diskType = request.getDiskType();
        String templateName = request.getTemplate();

        VsphereCredential vsphereCredential = request.getVsphereCredential();
        String server = vsphereCredential.getVCenterIp();
        String userName = vsphereCredential.getVUserName();
        String password = vsphereCredential.getVPassword();
        VapiAuthenticationHelper vapiAuthHelper = new VapiAuthenticationHelper();
        try {
            StubConfiguration stubConfiguration = vapiAuthHelper.loginByUsernameAndPassword(server, userName, password, ContentLibaryUtils.buildHttpConfiguration());
            ClsApiClient client = new ClsApiClient(vapiAuthHelper.getStubFactory(), stubConfiguration);

            String[] ids = templateName.split(VsphereTemplate.SEPARATOR);
            if (ids.length != 2 && StringUtils.isBlank(ids[1])) {
                throw new RuntimeException("Template ID error！");
            }
            String itemId = ids[1];

            ClusterComputeResource cluster = this.getCluster(clusterId);
            if (cluster == null)
                throw new RuntimeException("Cluster can not be empty！");
            LibraryItemTypes.DeploymentTarget target = new LibraryItemTypes.DeploymentTarget();
            ManagedObjectReference clusterMoRef = cluster.getResourcePool().getMOR();
            target.setResourcePoolId(clusterMoRef.getVal());
            if (host != null)
                target.setHostId(host.getMOR().getVal());
            if (resourcePool != null) {
                target.setResourcePoolId(resourcePool.getMOR().getVal());
            }
            if (folder != null)
                target.setFolderId(folder.getMOR().getVal());

            LibraryItemTypes.OvfSummary summary = client.ovfLibraryItemService().filter(itemId, target);
            if (summary == null) {
                throw new RuntimeException("The selected location cannot access the template of the selected content library");
            }

            LibraryItemTypes.ResourcePoolDeploymentSpec spec = new LibraryItemTypes.ResourcePoolDeploymentSpec();
            spec.setAcceptAllEULA(true); /* only this field is mandatory in the spec */
            spec.setName(vmName);
            spec.setAnnotation("This is a VM created using LibraryItem interface");

            ManagedEntity parent = datastore.getParent();
            if (parent instanceof StoragePod) {
                spec.setDefaultDatastoreId(parent.getMOR().getVal());
            } else {
                spec.setDefaultDatastoreId(datastore.getMOR().getVal());
            }


            if (DiskType.THIN.equalsIgnoreCase(diskType))
                spec.setStorageProvisioning(DiskProvisioningType.thin);
            if (DiskType.THICK.equalsIgnoreCase(diskType))
                spec.setStorageProvisioning(DiskProvisioningType.thick);
            if (DiskType.EAGER_ZEROED.equalsIgnoreCase(diskType))
                spec.setStorageProvisioning(DiskProvisioningType.eagerZeroedThick);


            /**end createspec*/

            LibraryItemTypes.DeploymentResult result =
                    client.ovfLibraryItemService().deploy(null, itemId, target, spec);

            // display result of the operation
            if (result.getSucceeded()) {
                String messageOnSuccess = "OVF item deployment succeeded." +
                        "\nDeployment information:" + " " + result.getResourceId().getType()
                        + " " + result.getResourceId().getId();
                String messageOnFailure = "OVF item deployment failed.";
                OvfUtil.displayOperationResult(result.getSucceeded(),
                        result.getError(), messageOnSuccess, messageOnFailure);
                return getVirtualMachineByMor(result.getResourceId().getId());
            } else {
                throw new RuntimeException(result.getError().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            vapiAuthHelper.logout();
        }
    }

    public VirtualMachine reconfigVm(VirtualMachine vm, int cpuCount, long memory, long diskSize, String diskType, boolean isPowerOn, boolean isNetworkOn, String remark, String profileId, String[] networksToAdd, Long[] diskToAdd) throws Exception {

        String instanceId = vm.getConfig().getInstanceUuid();

        VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();

        VirtualMachineDefinedProfileSpec profileSpec = null;
        if (StringUtils.isNotBlank(profileId) && !FLAG_FOR_NULL_VALUE.equals(profileId)) {
            profileSpec = new VirtualMachineDefinedProfileSpec();
            profileSpec.setProfileId(profileId);
            vmConfigSpec.setVmProfile(new VirtualMachineProfileSpec[]{profileSpec});
        }

        if (StringUtils.isNotBlank(remark)) {
            vmConfigSpec.setAnnotation(remark);
        }
        if (shutdownGuest(instanceId)) {
            if (stopVm(instanceId)) {
                if (memory > 0) {
                    vmConfigSpec.setMemoryMB(memory);
                }
                if (cpuCount > 0) {
                    vmConfigSpec.setNumCPUs(cpuCount);
                    vmConfigSpec.setCpuHotAddEnabled(true);
                    vmConfigSpec.setNumCoresPerSocket(cpuCount);
                }

                // 磁盘设置
                List<VirtualDeviceConfigSpec> machineSpecs = new ArrayList<>();
                if (diskSize > 0) {
                    addDiskSpec(machineSpecs, vm, profileSpec, diskType, diskSize, diskToAdd);
                }

                // TODO 网络设置
//                if (network != null && network.trim().length() > 0 && !FLAG_FOR_NULL_VALUE.equalsIgnoreCase(network)) {
//                    addNetwokSpec(machineSpecs, vm, network, networkCardName, isNetworkOn);
//                }
                if (networksToAdd != null && networksToAdd.length > 0) {
                    for (int i = 0; i < networksToAdd.length; i++) {
                        addNetwokSpec(machineSpecs, vm, networksToAdd[i], FLAG_FOR_NULL_VALUE, isNetworkOn);
                    }
                }

                if (machineSpecs.size() > 0) {
                    vmConfigSpec.setDeviceChange(machineSpecs.toArray(new VirtualDeviceConfigSpec[machineSpecs.size()]));
                }

                Task task = vm.reconfigVM_Task(vmConfigSpec);
                String status = task.waitForTask();
                if (!Task.SUCCESS.equals(status)) {
                    throw new RuntimeException("虚拟配置修改失败! 错误信息: " + task.getTaskInfo().getError().getLocalizedMessage());
                }
                if (isPowerOn) {
                    if (!startVm(instanceId)) {
                        throw new RuntimeException("启动虚拟机失败! instanceId: " + instanceId);
                    }
                }
                return getVirtualMachineById(instanceId);
            } else {
                throw new RuntimeException("停止虚拟机失败! instanceId: " + instanceId);
            }
        } else {
            throw new RuntimeException("关闭虚拟机系统失败! instanceId: " + instanceId);
        }
    }

    public VirtualMachine getVirtualMachineByMor(String mor) {
        if (mor != null) {
            List<VirtualMachine> vms = listVirtualMachines();
            for (VirtualMachine vm : vms) {
                if (vm.getMOR() == null || vm.getMOR().getVal() == null) {
                    continue;
                }
                if (mor.equals(vm.getMOR().getVal())) {
                    return vm;
                }
            }
        }
        return null;
    }

    public Datastore getDatastoreByMor(String datastoreMor) {
        List<Datastore> datastores = listDataStores();
        if (datastores != null) {
            for (Datastore datastore : datastores) {
                if (datastore.getMOR().getVal().equals(datastoreMor)) {
                    return datastore;
                }
            }
        }
        return null;
    }

    private boolean shutdownGuest(String instanceId) {
        try {
            VirtualMachine vm = getVirtualMachineById(instanceId);
            if (vm != null) {
                VirtualMachineRuntimeInfo runtime = vm.getRuntime();
                if (runtime != null) {
                    VirtualMachinePowerState state = runtime.getPowerState();
                    if (state == VirtualMachinePowerState.poweredOn) {
                        log.info("begin to shutdown vm'OS : " + instanceId);
                        vm.shutdownGuest();
                        int count = 0;
                        while (count++ < 60) {
                            Thread.sleep(1000 * 10);
                            String guestState = vm.getGuest().getGuestState();
                            log.info(guestState);
                            if ("NotRunning".equalsIgnoreCase(guestState)) {
                                return true;
                            }
                        }
                        return true;
                    } else {
                        return true;
                    }
                } else {
                    // TODO runtime?
                }
            }
        } catch (InvalidState e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("invalid state!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error turning off the guest operating system!" + e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean stopVm(String instanceId) {
        try {
            VirtualMachine vm = getVirtualMachineById(instanceId);
            if (vm != null) {
                VirtualMachineRuntimeInfo runtime = vm.getRuntime();

                if (runtime != null) {
                    VirtualMachinePowerState state = runtime.getPowerState();
                    if (state != VirtualMachinePowerState.poweredOff) {
                        log.info("begin to stop vm : " + instanceId);
                        Task task = vm.powerOffVM_Task();
                        String status = task.waitForTask();
                        if (!status.equals("") && !status.equals(Task.SUCCESS)) {
                            throw new RuntimeException("failed to stop vm : " + instanceId + ", errorMsg : " + task.getTaskInfo().getError().getLocalizedMessage());
                        } else {
                            log.info("success to stop vm : " + instanceId);
                            return true;
                        }
                    } else {
                        return true;
                    }
                } else {
                    // TODO runtime?
                }
            }
        } catch (InvalidState e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("invalid state!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("failed to stop vm!" + e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean startVm(String instanceId) {
        try {
            VirtualMachine vm = getVirtualMachineById(instanceId);
            if (vm != null) {
                VirtualMachineRuntimeInfo runtime = vm.getRuntime();
                if (runtime != null) {
                    VirtualMachinePowerState state = runtime.getPowerState();
                    if (state == VirtualMachinePowerState.poweredOff) {
                        log.info("begin to start vm : " + instanceId);
                        Task task = vm.powerOnVM_Task(getHost(vm));
                        String status = task.waitForTask();
                        if (!status.equals("") && !status.equals(Task.SUCCESS)) {
                            log.info("failed to start vm : " + instanceId + ", errorMsg : " + task.getTaskInfo().getError().getLocalizedMessage());
                            return false;
                        } else {
                            log.info("success to start vm : " + instanceId);
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } catch (VmConfigFault e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Virtual machine configuration error!" + e.getLocalizedMessage(), e);
        } catch (InvalidState e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Invalid state!" + e.getLocalizedMessage(), e);
        } catch (InvalidDatastore e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Invalid data storage settings!" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("Error starting virtual machine!" + e.getLocalizedMessage(), e);
        }
        return false;
    }


    //todo 磁盘需要根据传进来的改
    private void addDiskSpec(List<VirtualDeviceConfigSpec> machineSpecs, VirtualMachine template, VirtualMachineDefinedProfileSpec profileSpec, String diskType, Long diskSize, Long[] diskToAdd) {
        VirtualDevice[] devices = template.getConfig().getHardware().getDevice();
        int controllerKey = 0;
        long diskSum = 0;
        // 原有的磁盘格式
        String oldDiskType = DiskType.DEFAULT;
        for (VirtualDevice device : devices) {
            if (device instanceof VirtualDisk) {

                // 更改所有磁盘的profile
                VirtualDeviceConfigSpec tmpSpec = new VirtualDeviceConfigSpec();
                tmpSpec.setDevice(device);
                tmpSpec.setOperation(VirtualDeviceConfigSpecOperation.edit);
                tmpSpec.setFileOperation(null);
                if (profileSpec != null) {
                    tmpSpec.setProfile(new VirtualMachineDefinedProfileSpec[]{profileSpec});
                }
                machineSpecs.add(tmpSpec);

                controllerKey = device.getControllerKey();
                long capacityInKB = ((VirtualDisk) device).getCapacityInKB();
                diskSum = diskSum + capacityInKB / 1024 / 1024;// GB
                VirtualDeviceBackingInfo backing = device.getBacking();
                if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
                    VirtualDiskFlatVer2BackingInfo virtualDiskFlatVer2BackingInfo = (VirtualDiskFlatVer2BackingInfo) backing;
                    if (Boolean.TRUE.equals(virtualDiskFlatVer2BackingInfo.getThinProvisioned())) {
                        oldDiskType = DiskType.THIN;
                    } else if (Boolean.TRUE.equals(virtualDiskFlatVer2BackingInfo.getEagerlyScrub())) {
                        oldDiskType = DiskType.THICK;
                    } else {
                        oldDiskType = DiskType.EAGER_ZEROED;
                    }
                }
            }
        }
        // 使用镜像里的磁盘格式
        if (StringUtils.isBlank(diskType)) {
            diskType = oldDiskType;
        }
        if (diskSum < diskSize) {
            diskSize = diskSize - diskSum;
            if (diskToAdd == null) {
                diskToAdd = new Long[]{diskSize};
            }
            VirtualDiskFlatVer2BackingInfo backinginfo = new VirtualDiskFlatVer2BackingInfo();
            backinginfo.setFileName(""); // 坑
            backinginfo.setDiskMode(VirtualDiskMode.persistent.name());
            if (DiskType.THICK.equals(diskType)) {
                backinginfo.setThinProvisioned(Boolean.FALSE);
            } else if (DiskType.THIN.equals(diskType)) {
                backinginfo.setThinProvisioned(Boolean.TRUE);
            } else if (DiskType.EAGER_ZEROED.equals(diskType)) {
                backinginfo.setEagerlyScrub(Boolean.TRUE);
            }

            List<Integer> unitNumberList = new ArrayList<Integer>();
            for (final VirtualDevice device : devices) {
                if (device.getUnitNumber() != null && device.getControllerKey().equals(controllerKey)) {
                    unitNumberList.add(device.getUnitNumber());
                }
            }

            for (int i = 0; i < diskToAdd.length; i++) {
                VirtualDeviceConnectInfo virtualDeviceConnectInfo = new VirtualDeviceConnectInfo();
                virtualDeviceConnectInfo.setStartConnected(true);
                virtualDeviceConnectInfo.setConnected(true);
                virtualDeviceConnectInfo.setAllowGuestControl(false);
                int unitNumber = 0;

                for (int j = 0; j < 16; j++) {
                    if (!unitNumberList.contains(j)) {
                        unitNumber = j;
                        unitNumberList.add(j);
                        break;
                    }
                }
                VirtualDisk vd = new VirtualDisk();

                VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();
                diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
                diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
                vd.setCapacityInKB(diskToAdd[i] * 1024 * 1024);
                vd.setConnectable(virtualDeviceConnectInfo);
                vd.setUnitNumber(unitNumber);
//                vd.setKey(-1);
                vd.setControllerKey(controllerKey);

                vd.setBacking(backinginfo);

                if (profileSpec != null) {
                    diskSpec.setProfile(new VirtualMachineProfileSpec[]{profileSpec});
                }

                diskSpec.setDevice(vd);
                machineSpecs.add(diskSpec);
            }
        }
    }

    /**
     * 网络设置
     * //当在存储群集中创建虚拟机时，网络选择为模版的网络时需注意，当网络不合法时，创建虚拟机将会失败，错误提示形如：无法访问虚拟机配置，无法访问文件[datastore1 (35)]
     * //当时出现该错误提示时使用的模版的网络为vCenter的管理网络，不知道这个网络和datastore1 (35)有啥关系
     *
     * @param machineSpecs
     * @param template
     * @param network
     * @param networkCardName
     * @param isNetworkOn
     * @throws Exception
     */
    //todo 网卡需要根据传进来的改
    private void addNetwokSpec(List<VirtualDeviceConfigSpec> machineSpecs, VirtualMachine template, String network, String networkCardName, boolean isNetworkOn) throws Exception {
        Network net = getNetworkByMor(network);

        VirtualEthernetCard veCard = null;
        VirtualEthernetCard tmpCard = null;

        int controllerKey = 0;
        if (net == null) {
            System.out.println("cannot find network :::::::::: " + network);
        } else {
            VirtualDevice[] virtualDevices = template.getConfig().getHardware().getDevice();
            for (VirtualDevice virtualDevice : virtualDevices) {
                if (virtualDevice.getControllerKey() != null) {
                    controllerKey = virtualDevice.getControllerKey();
                }
                if (virtualDevice instanceof VirtualEthernetCard) {
                    tmpCard = (VirtualEthernetCard) virtualDevice;
                    if (FLAG_FOR_NULL_VALUE.equals(networkCardName) || (networkCardName != null && networkCardName.trim().length() > 0 && !networkCardName.equals(tmpCard.getDeviceInfo().getLabel()))) {
                        continue;
                    }
                    veCard = tmpCard;
                    break;
                }
            }
        }

        VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();

        if (veCard == null) {
            veCard = new VirtualVmxnet3();

            nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

            veCard.setControllerKey(tmpCard == null ? controllerKey : tmpCard.getControllerKey());

        } else {
            nicSpec.setOperation(VirtualDeviceConfigSpecOperation.edit);
            veCard.getDeviceInfo().setSummary(network);
        }

        ManagedObjectReference netMor = net.getMOR();

        if (netMor.getVal().startsWith("network")) {
            VirtualEthernetCardNetworkBackingInfo nicBacking = new VirtualEthernetCardNetworkBackingInfo();
            nicBacking.setDeviceName(net.getName());
            nicBacking.setNetwork(netMor);
            veCard.setBacking(nicBacking);
        } else {
            if (net instanceof DistributedVirtualPortgroup) {
                VirtualEthernetCardDistributedVirtualPortBackingInfo nicBacking = new VirtualEthernetCardDistributedVirtualPortBackingInfo();
                DistributedVirtualSwitchPortConnection connection = new DistributedVirtualSwitchPortConnection();
                //                                    connection.setPortgroupKey(netMor.getVal());
                connection.setPortgroupKey(((DistributedVirtualPortgroup) net).getKey());

                ManagedObjectReference mor = ((DistributedVirtualPortgroup) net).getConfig()
                        .getDistributedVirtualSwitch();
                DistributedVirtualSwitch dvs = new DistributedVirtualSwitch(net.getServerConnection(),
                        mor);
                connection.setSwitchUuid(dvs.getUuid());
                nicBacking.setPort(connection);
                veCard.setBacking(nicBacking);
            } else {
                System.out.println("unsupported network type :::::::::: " + net);
                return;
            }
        }

        VirtualDeviceConnectInfo connectInfo = new VirtualDeviceConnectInfo();
        connectInfo.setStartConnected(isNetworkOn);
        /*connectInfo.setAllowGuestControl(true);
        connectInfo.setConnected(false);
        connectInfo.setStatus(VirtualDeviceConnectInfoStatus.untried.name());*/
        veCard.setConnectable(connectInfo);

        nicSpec.setDevice(veCard);

        machineSpecs.add(nicSpec);
    }

    /* 内容库end */

    /**
     * 检查自定义规范执行是否执行完成
     */
    private boolean checkCustomizationEvent(VirtualMachine vm) throws RemoteException {
        final EventFilterSpec eventFilterSpec = new EventFilterSpec();
        eventFilterSpec.setEventTypeId(new String[]{"CustomizationStartedEvent", "CustomizationSucceeded"});

        EventFilterSpecByEntity entity = new EventFilterSpecByEntity();
        entity.setEntity(vm.getMOR());
        EventFilterSpecRecursionOption recOption = EventFilterSpecRecursionOption.self;
        entity.setRecursion(recOption);

        eventFilterSpec.setEntity(entity);
        ServiceInstance si = getSi();
        Event[] events = si.getEventManager().queryEvents(eventFilterSpec);
        return events != null && events.length == 2;
    }

    private CustomizationSpec getCustomizationSpec(String osType, VsphereVmCreateRequest request) {
        String hostname = request.getServerInfos().get(request.getIndex()).getHostname();
        CustomizationSpec customSpec = new CustomizationSpec();

        if (StringUtils.equals("Linux", osType)) {
            final CustomizationLinuxPrep linuxPrep = new CustomizationLinuxPrep();
            final CustomizationFixedName custName = new CustomizationFixedName();
            custName.setName(hostname);
            linuxPrep.setDomain("hostname.com");
            linuxPrep.setHostName(custName);
            linuxPrep.setHwClockUTC(true);
            customSpec.setIdentity(linuxPrep);
            customSpec = customizationSpecIpSetting(customSpec, null, request);
        } else {
            final CustomizationSysprep sysprep = this.setWindowsSysprep(hostname, request);
            CustomizationWinOptions customizationWinOptions = new CustomizationWinOptions();
            customizationWinOptions.setChangeSID(true);
            customizationWinOptions.setDeleteAccounts(false);
            customSpec.setOptions(customizationWinOptions);
            customSpec.setIdentity(sysprep);
            customSpec = customizationSpecIpSetting(customSpec, CustomizationNetBIOSMode.disableNetBIOS, request);
        }
        return customSpec;
    }

    private CustomizationSysprep setWindowsSysprep(final String hostname, VsphereVmCreateRequest request) {
        int index = request.getIndex();
        VsphereVmCreateRequest.ServerInfo serverInfo = request.getServerInfos().get(index);
        boolean isAddDomain = serverInfo.isAddDomain();
        String domain = serverInfo.getDomain();
        String domainAdmin = serverInfo.getDomainAdmin();
        String domainPassword = serverInfo.getDomainPassword();
        final CustomizationSysprep sysprep = new CustomizationSysprep();
        final CustomizationGuiUnattended cgu = new CustomizationGuiUnattended();
        cgu.setAutoLogon(false);
        cgu.setAutoLogonCount(0);
        cgu.setTimeZone(210);
        final CustomizationFixedName custFixedName = new CustomizationFixedName();
        custFixedName.setName(hostname);
        final CustomizationUserData customizationUserData = new CustomizationUserData();
        customizationUserData.setFullName("myteam");
        customizationUserData.setOrgName("mycompany");
        customizationUserData.setComputerName(custFixedName);
        customizationUserData.setProductId("");
        final CustomizationIdentification ci = new CustomizationIdentification();
        if (isAddDomain && StringUtils.isNotBlank(domainAdmin) && StringUtils.isNotBlank(domain) && StringUtils.isNotBlank(domainPassword)) {
            CustomizationPassword customizationPassword = new CustomizationPassword();
            customizationPassword.setPlainText(true);
            ci.setJoinDomain(domain);
            ci.setDomainAdmin(domainAdmin);
            customizationPassword.setValue(domainPassword);
            ci.setDomainAdminPassword(customizationPassword);
        } else {
            ci.setJoinWorkgroup("WORKGROUP");
        }
        CustomizationPassword customizationPassword = new CustomizationPassword();
        customizationPassword.setPlainText(true);

        //todo windows 设置密码
        customizationPassword.setValue("password");

        cgu.setPassword(customizationPassword);
        sysprep.setGuiUnattended(cgu);
        sysprep.setIdentification(ci);
        sysprep.setUserData(customizationUserData);

        return sysprep;
    }

    private CustomizationSpec customizationSpecIpSetting(CustomizationSpec custSpec, CustomizationNetBIOSMode customizationNetBIOSMode, VsphereVmCreateRequest request) {
        if (custSpec == null) {
            throw new RuntimeException("Custom specification cannot be empty");
        }
        int index = request.getIndex();
        VsphereVmCreateRequest.NetworkConfig config = request.getNetworkConfigs().get(index);
        List<VsphereVmCreateRequest.NetworkAdapter> adapters = config.getAdapters();

        List<CustomizationAdapterMapping> nicList = new ArrayList<>();
        CustomizationGlobalIPSettings gIp = new CustomizationGlobalIPSettings();
        ArrayList<String> allDnsList = new ArrayList<>();
        if (StringUtils.isNotBlank(config.getDns1())) {
            allDnsList.add(config.getDns1());
        }
        if (StringUtils.isNotBlank(config.getDns2())) {
            allDnsList.add(config.getDns2());
        }

        for (VsphereVmCreateRequest.NetworkAdapter networkConfig : adapters) {
            String ipType = StringUtils.defaultString(networkConfig.getIpType(), ResourceConstants.ipv4);

            CustomizationIPSettings ip = new CustomizationIPSettings();
            if (customizationNetBIOSMode != null) {
                ip.setNetBIOS(customizationNetBIOSMode);
            }
            CustomizationAdapterMapping nic = new CustomizationAdapterMapping();

            String gateway = networkConfig.getGateway();
            String ipAddress = networkConfig.getIpAddr();
            String mask = networkConfig.getNetmask();


            boolean setIpv4 = ipType.equalsIgnoreCase(ResourceConstants.ipv4) || ipType.equalsIgnoreCase(ResourceConstants.DualStack);
            boolean setIpv6 = ipType.equalsIgnoreCase(ResourceConstants.ipv6) || ipType.equalsIgnoreCase(ResourceConstants.DualStack);
            if (networkConfig.isDhcp()) {
                if (setIpv4) {
                    CustomizationDhcpIpGenerator dhcpIp = new CustomizationDhcpIpGenerator();
                    ip.setIp(dhcpIp);
                }
                if (setIpv6) {
                    CustomizationDhcpIpV6Generator dhcpIpv6 = new CustomizationDhcpIpV6Generator();
                    CustomizationIPSettingsIpV6AddressSpec ipv6 = new CustomizationIPSettingsIpV6AddressSpec();
                    ipv6.setIp(ArrayUtils.toArray(dhcpIpv6));
                    ip.setIpV6Spec(ipv6);
                }
            } else {
                if (setIpv4) {
                    ip.setGateway(new String[]{gateway});
                    CustomizationFixedIp fixedIP = new CustomizationFixedIp();
                    fixedIP.setIpAddress(ipAddress);
                    ip.setIp(fixedIP);
                    ip.setSubnetMask(mask);
                }
                if (setIpv6) {
                    CustomizationFixedIpV6 fixedIpv6 = new CustomizationFixedIpV6();
                    fixedIpv6.setIpAddress(networkConfig.getIpAddrV6());
                    fixedIpv6.setSubnetMask(Integer.parseInt(networkConfig.getNetmaskV6()));

                    CustomizationIPSettingsIpV6AddressSpec ipv6 = new CustomizationIPSettingsIpV6AddressSpec();
                    ipv6.setIp(ArrayUtils.toArray(fixedIpv6));
                    if (StringUtils.isNotBlank(networkConfig.getGatewayV6())) {
                        ipv6.setGateway(ArrayUtils.toArray(networkConfig.getGatewayV6()));
                    }
                    ip.setIpV6Spec(ipv6);
                }

            }
            nic.setAdapter(ip);
            nicList.add(nic);
        }

        gIp.setDnsServerList(allDnsList.toArray(new String[]{}));
        custSpec.setGlobalIPSettings(gIp);
        custSpec.setNicSettingMap(nicList.toArray(new CustomizationAdapterMapping[]{}));
        return custSpec;
    }

    /**
     * @param templateVirtualEthernetCards 镜像模版网卡
     * @param adapters                     网卡
     * @param computeType                  计算资源类型
     * @param hostSystem                   宿主机
     * @param resourcePool                 资源池
     * @param cluster                      集群
     * @return 创建虚拟机需要的网卡
     */
    private List<VirtualDeviceConfigSpec> getVirtualDeviceConfigSpecs(List<VirtualEthernetCard> templateVirtualEthernetCards, List<VsphereVmCreateRequest.NetworkAdapter> adapters, String computeType, HostSystem hostSystem, ResourcePool resourcePool, ComputeResource cluster) throws Exception {
        // 网卡去重 注意这个地方不能去重
        AtomicInteger atomicInteger = new AtomicInteger(0);

        List<Network> networks = new ArrayList<>();

        if (StringUtils.equals("host", computeType)) {
            networks.addAll(List.of(hostSystem.getNetworks()));
        } else if (StringUtils.equals("pool", computeType)) {
            networks.addAll(List.of(resourcePool.getOwner().getNetworks()));
        } else {
            HostSystem[] hosts = cluster.getHosts();
            for (HostSystem host : hosts) {
                networks.addAll(List.of(host.getNetworks()));
            }
        }

        List<VirtualDeviceConfigSpec> list = new ArrayList<>();

        List<VirtualDeviceConfigSpec> addCards = adapters.stream().map(adapter -> {

            String networkId = adapter.getVlan();
            Network availableNetwork = null;
            for (Network network : networks) {
                if (StringUtils.equals(networkId, network.getMOR().getVal())) {
                    availableNetwork = network;
                    break;
                }
            }
            if (availableNetwork == null) {
                return null;
            }

            VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
            nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
            VirtualEthernetCard nic = new VirtualVmxnet3();
            nic.setKey(atomicInteger.getAndIncrement());
            nicSpec.setDevice(nic);
            nic.setAddressType("generated");
            setNetworkBacking(nic, availableNetwork);
            return nicSpec;
        }).filter(Objects::nonNull).toList();

        // 删除模版的网卡
        List<VirtualDeviceConfigSpec> collect = templateVirtualEthernetCards.stream().map(templateVirtualEthernetCard -> {
            VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
            nicSpec.setDevice(templateVirtualEthernetCard);
            nicSpec.setOperation(VirtualDeviceConfigSpecOperation.remove);
            return nicSpec;
        }).toList();


        list.addAll(addCards);
        list.addAll(collect);


        return list;
    }

    /**
     * 设置网络 网卡信息
     *
     * @param device           设备对象
     * @param availableNetwork 网络信息
     */
    private void setNetworkBacking(VirtualDevice device, Network availableNetwork) {
        ManagedObjectReference netMor = availableNetwork.getMOR();
        if (netMor.getVal().startsWith("network")) {
            VirtualEthernetCardNetworkBackingInfo nicBacking = new VirtualEthernetCardNetworkBackingInfo();
            nicBacking.setDeviceName(availableNetwork.getName());
            nicBacking.setNetwork(netMor);
            device.setBacking(nicBacking);
        } else {
            DistributedVirtualSwitchPortConnection networkCardCollection = getNetworkCardCollection(availableNetwork);
            VirtualEthernetCardDistributedVirtualPortBackingInfo nicBacking = new VirtualEthernetCardDistributedVirtualPortBackingInfo();
            nicBacking.setPort(networkCardCollection);
            device.setBacking(nicBacking);
        }
    }

    /**
     * 获取当前网卡的连接
     *
     * @param availableNetwork 网卡信息
     * @return 连接对象
     */
    private DistributedVirtualSwitchPortConnection getNetworkCardCollection(Network availableNetwork) {
        if (availableNetwork instanceof DistributedVirtualPortgroup) {
            DistributedVirtualSwitchPortConnection connection = new DistributedVirtualSwitchPortConnection();
            ManagedObjectReference mor = ((DistributedVirtualPortgroup) availableNetwork).getConfig().getDistributedVirtualSwitch();
            DistributedVirtualSwitch dvs = new DistributedVirtualSwitch(availableNetwork.getServerConnection(), mor);
            connection.setSwitchUuid(dvs.getUuid());
            connection.setPortgroupKey(((DistributedVirtualPortgroup) availableNetwork).getKey());
            return connection;
        } else {
            log.info("unsupported network type :::::::::: " + availableNetwork);
        }
        return null;
    }

    public void connectVirtualEthernetCard(VirtualMachine vm) {
        try {
            VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
            List<VirtualEthernetCard> virtualEthernetCardList = VsphereUtil.getVirtualEthernetCardsByVm(vm);
            List<VirtualDeviceConfigSpec> deviceConfigSpecs = new ArrayList<>();
            for (VirtualEthernetCard card : virtualEthernetCardList) {
                VirtualDeviceConnectInfo connectable = card.getConnectable();
                if (connectable != null && connectable.isConnected()) {
                    continue;
                }
                VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
                nicSpec.setDevice(card);
                nicSpec.setOperation(VirtualDeviceConfigSpecOperation.edit);

                VirtualDeviceConnectInfo virtualDeviceConnectInfo = new VirtualDeviceConnectInfo();
                virtualDeviceConnectInfo.setConnected(true);
                virtualDeviceConnectInfo.setStartConnected(true);
                virtualDeviceConnectInfo.setAllowGuestControl(true);

                card.setConnectable(virtualDeviceConnectInfo);
                deviceConfigSpecs.add(nicSpec);
            }

            vmConfigSpec.setDeviceChange(deviceConfigSpecs.toArray(new VirtualDeviceConfigSpec[0]));
            Task task = vm.reconfigVM_Task(vmConfigSpec);
            task.waitForTask();
        } catch (Exception e) {
            log.error("Error connecting the network manually after creating the machine: " + ExceptionUtils.getStackTrace(e));
        }
    }

}
