package org.openstack4j.openstack.provider;

import com.google.common.collect.Maps;
import org.openstack4j.api.APIProvider;
import org.openstack4j.api.artifact.ArtifactService;
import org.openstack4j.api.artifact.ToscaTemplatesArtifactService;
import org.openstack4j.api.barbican.BarbicanService;
import org.openstack4j.api.barbican.ContainerService;
import org.openstack4j.api.barbican.SecretService;
import org.openstack4j.api.compute.QuotaSetService;
import org.openstack4j.api.compute.*;
import org.openstack4j.api.compute.ext.*;
import org.openstack4j.api.dns.v2.DNSService;
import org.openstack4j.api.dns.v2.RecordsetService;
import org.openstack4j.api.exceptions.ApiNotFoundException;
import org.openstack4j.api.gbp.*;
import org.openstack4j.api.heat.*;
import org.openstack4j.api.identity.v2.ServiceManagerService;
import org.openstack4j.api.identity.v2.TenantService;
import org.openstack4j.api.identity.v3.GroupService;
import org.openstack4j.api.identity.v3.UserService;
import org.openstack4j.api.identity.v3.*;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.api.image.v2.TaskService;
import org.openstack4j.api.magnum.MagnumService;
import org.openstack4j.api.manila.*;
import org.openstack4j.api.murano.v1.*;
import org.openstack4j.api.networking.*;
import org.openstack4j.api.networking.ext.*;
import org.openstack4j.api.octavia.OctaviaService;
import org.openstack4j.api.placement.PlacementService;
import org.openstack4j.api.placement.ext.ResourceProviderService;
import org.openstack4j.api.sahara.*;
import org.openstack4j.api.senlin.*;
import org.openstack4j.api.storage.*;
import org.openstack4j.api.storage.ext.BlockStorageServiceService;
import org.openstack4j.api.tacker.*;
import org.openstack4j.api.telemetry.*;
import org.openstack4j.api.trove.*;
import org.openstack4j.api.workflow.*;
import org.openstack4j.openstack.artifact.internal.ArtifactServiceImpl;
import org.openstack4j.openstack.artifact.internal.ToscaTemplatesArtifactServiceImpl;
import org.openstack4j.openstack.barbican.internal.BarbicanServiceImpl;
import org.openstack4j.openstack.barbican.internal.ContainerServiceImpl;
import org.openstack4j.openstack.barbican.internal.SecretServiceImpl;
import org.openstack4j.openstack.compute.internal.QuotaSetServiceImpl;
import org.openstack4j.openstack.compute.internal.*;
import org.openstack4j.openstack.compute.internal.ext.*;
import org.openstack4j.openstack.dns.v2.internal.DNSServiceImpl;
import org.openstack4j.openstack.dns.v2.internal.RecordsetServiceImpl;
import org.openstack4j.openstack.gbp.internal.*;
import org.openstack4j.openstack.heat.internal.*;
import org.openstack4j.openstack.identity.v2.internal.ServiceManagerServiceImpl;
import org.openstack4j.openstack.identity.v2.internal.TenantServiceImpl;
import org.openstack4j.openstack.identity.v3.internal.GroupServiceImpl;
import org.openstack4j.openstack.identity.v3.internal.*;
import org.openstack4j.openstack.image.internal.ImageServiceImpl;
import org.openstack4j.openstack.image.v2.internal.TaskServiceImpl;
import org.openstack4j.openstack.magnum.internal.MagnumServiceImpl;
import org.openstack4j.openstack.manila.internal.*;
import org.openstack4j.openstack.murano.v1.internal.*;
import org.openstack4j.openstack.networking.internal.*;
import org.openstack4j.openstack.networking.internal.ext.*;
import org.openstack4j.openstack.octavia.internal.OctaviaServiceImpl;
import org.openstack4j.openstack.placement.internal.PlacementServiceImpl;
import org.openstack4j.openstack.placement.internal.ext.ResourceProviderServiceImpl;
import org.openstack4j.openstack.sahara.internal.*;
import org.openstack4j.openstack.senlin.internal.*;
import org.openstack4j.openstack.storage.block.internal.*;
import org.openstack4j.openstack.storage.object.internal.ObjectStorageAccountServiceImpl;
import org.openstack4j.openstack.storage.object.internal.ObjectStorageContainerServiceImpl;
import org.openstack4j.openstack.storage.object.internal.ObjectStorageObjectServiceImpl;
import org.openstack4j.openstack.storage.object.internal.ObjectStorageServiceImpl;
import org.openstack4j.openstack.tacker.internal.VimServiceImpl;
import org.openstack4j.openstack.tacker.internal.VnfServiceImpl;
import org.openstack4j.openstack.tacker.internal.VnfdServiceImpl;
import org.openstack4j.openstack.telemetry.internal.*;
import org.openstack4j.openstack.trove.internal.*;
import org.openstack4j.openstack.workflow.internal.*;

import java.util.Map;

/**
 * Simple API Provider which keeps internally Maps interface implementations as singletons
 *
 * @author Jeremy Unruh
 */
public class DefaultAPIProvider implements APIProvider {

    private static final Map<Class<?>, Class<?>> bindings = Maps.newHashMap();
    private static final Map<Class<?>, Object> instances = Maps.newConcurrentMap();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        bind(org.openstack4j.api.identity.v2.IdentityService.class, org.openstack4j.openstack.identity.v2.internal.IdentityServiceImpl.class);
        bind(TenantService.class, TenantServiceImpl.class);
        bind(ServiceManagerService.class, ServiceManagerServiceImpl.class);
        bind(org.openstack4j.api.identity.v2.UserService.class, org.openstack4j.openstack.identity.v2.internal.UserServiceImpl.class);
        bind(org.openstack4j.api.identity.v2.RoleService.class, org.openstack4j.openstack.identity.v2.internal.RoleServiceImpl.class);
        bind(org.openstack4j.api.identity.v3.IdentityService.class, org.openstack4j.openstack.identity.v3.internal.IdentityServiceImpl.class);
        bind(ServiceEndpointService.class, ServiceEndpointServiceImpl.class);
        bind(CredentialService.class, CredentialServiceImpl.class);
        bind(UserService.class, UserServiceImpl.class);
        bind(ProjectService.class, ProjectServiceImpl.class);
        bind(RoleService.class, RoleServiceImpl.class);
        bind(DomainService.class, DomainServiceImpl.class);
        bind(GroupService.class, GroupServiceImpl.class);
        bind(PolicyService.class, PolicyServiceImpl.class);
        bind(RegionService.class, RegionServiceImpl.class);
        bind(TokenService.class, TokenServiceImpl.class);
        bind(ComputeService.class, ComputeServiceImpl.class);
        bind(FlavorService.class, FlavorServiceImpl.class);
        bind(ComputeImageService.class, ComputeImageServiceImpl.class);
        bind(ServerService.class, ServerServiceImpl.class);
        bind(QuotaSetService.class, QuotaSetServiceImpl.class);
        bind(HostService.class, HostServiceImpl.class);
        bind(NetworkingService.class, NetworkingServiceImpl.class);
        bind(PlacementService.class, PlacementServiceImpl.class);
        bind(ResourceProviderService.class, ResourceProviderServiceImpl.class);
        bind(NetworkService.class, NetworkServiceImpl.class);
        bind(ServiceFunctionChainService.class, ServiceFunctionChainServiceImpl.class);
        bind(FlowClassifierService.class, FlowClassifierServiceImpl.class);
        bind(PortPairService.class, PortPairServiceImpl.class);
        bind(PortPairGroupService.class, PortPairGroupServiceImpl.class);
        bind(PortChainService.class, PortChainServiceImpl.class);
        bind(ArtifactService.class, ArtifactServiceImpl.class);
        bind(ToscaTemplatesArtifactService.class, ToscaTemplatesArtifactServiceImpl.class);
        bind(SubnetService.class, SubnetServiceImpl.class);
        bind(AvailabilityZoneService.class, AvailabilityZoneServiceImpl.class);
        bind(PortService.class, PortServiceImpl.class);
        bind(RouterService.class, RouterServiceImpl.class);
        bind(OctaviaService.class, OctaviaServiceImpl.class);
        bind(org.openstack4j.api.octavia.LoadBalancerV2Service.class, org.openstack4j.openstack.octavia.internal.LoadBalancerV2ServiceImpl.class);
        bind(org.openstack4j.api.octavia.ListenerV2Service.class, org.openstack4j.openstack.octavia.internal.ListenerV2ServiceImpl.class);
        bind(org.openstack4j.api.octavia.LbPoolV2Service.class, org.openstack4j.openstack.octavia.internal.LbPoolV2ServiceImpl.class);
        bind(org.openstack4j.api.octavia.HealthMonitorV2Service.class, org.openstack4j.openstack.octavia.internal.HealthMonitorV2ServiceImpl.class);
        bind(ImageService.class, ImageServiceImpl.class);
        bind(BlockStorageService.class, BlockStorageServiceImpl.class);
        bind(BlockVolumeService.class, BlockVolumeServiceImpl.class);
        bind(BlockVolumeSnapshotService.class, BlockVolumeSnapshotServiceImpl.class);
        bind(BlockVolumeBackupService.class, BlockVolumeBackupServiceImpl.class);
        bind(ComputeSecurityGroupService.class, ComputeSecurityGroupServiceImpl.class);
        bind(KeypairService.class, KeypairServiceImpl.class);
        bind(NetFloatingIPService.class, FloatingIPServiceImpl.class);
        bind(ComputeFloatingIPService.class, ComputeFloatingIPServiceImpl.class);
        bind(SecurityGroupService.class, SecurityGroupServiceImpl.class);
        bind(SecurityGroupRuleService.class, SecurityGroupRuleServiceImpl.class);
        bind(TelemetryService.class, TelemetryServiceImpl.class);
        bind(MeterService.class, MeterServiceImpl.class);
        bind(SampleService.class, SampleServiceImpl.class);
        bind(AlarmService.class, AlarmServiceImpl.class);
        bind(EventService.class, EventServiceImpl.class);
        bind(CapabilitiesService.class, CapabilitiesServiceImpl.class);
        bind(ResourceService.class, ResourceServiceImpl.class);
        bind(HypervisorService.class, HypervisorServiceImpl.class);
        bind(ZoneService.class, ZoneServiceImpl.class);
        bind(CinderZoneService.class, CinderZoneServiceImpl.class);
        bind(HeatService.class, HeatServiceImpl.class);
        bind(SenlinService.class, SenlinServiceImpl.class);
        bind(SenlinPolicyService.class, SenlinPolicyServiceImpl.class);
        bind(SenlinVersionService.class, SenlinVersionServiceImpl.class);
        bind(SenlinActionService.class, SenlinActionServiceImpl.class);
        bind(SenlinBuildInfoService.class, SenlinBuildInfoServiceImpl.class);
        bind(SenlinClusterService.class, SenlinClusterServiceImpl.class);
        bind(SenlinClusterPolicyService.class, SenlinClusterPolicyServiceImpl.class);
        bind(SenlinEventService.class, SenlinEventServiceImpl.class);
        bind(SenlinNodeService.class, SenlinNodeServiceImpl.class);
        bind(SenlinProfileService.class, SenlinProfileServiceImpl.class);
        bind(SenlinProfileTypeService.class, SenlinProfileTypeServiceImpl.class);
        bind(SenlinPolicyTypeService.class, SenlinPolicyTypeServiceImpl.class);
        bind(SenlinReceiverService.class, SenlinReceiverServiceImpl.class);
        bind(SenlinWebHookService.class, SenlinWebHookServiceImpl.class);
        bind(StackService.class, StackServiceImpl.class);
        bind(TemplateService.class, TemplateServiceImpl.class);
        bind(EventsService.class, EventsServiceImpl.class);
        bind(ResourcesService.class, ResourcesServiceImpl.class);
        bind(MigrationService.class, MigrationServiceImpl.class);
        bind(SoftwareConfigService.class, SoftwareConfigServiceImpl.class);
        bind(ObjectStorageService.class, ObjectStorageServiceImpl.class);
        bind(ObjectStorageAccountService.class, ObjectStorageAccountServiceImpl.class);
        bind(ObjectStorageContainerService.class, ObjectStorageContainerServiceImpl.class);
        bind(ServerGroupService.class, ServerGroupServiceImpl.class);
        bind(ObjectStorageObjectService.class, ObjectStorageObjectServiceImpl.class);
        bind(NetQuotaService.class, NetQuotaServiceImpl.class);
        bind(InterfaceService.class, InterfaceServiceImpl.class);
        bind(InstanceActionsService.class, InstanceActionsServiceImpl.class);
        bind(FloatingIPDNSService.class, FloatingIPDNSServiceImpl.class);
        bind(FloatingIPDNSDomainService.class, FloatingIPDNSDomainServiceImpl.class);
        bind(FloatingIPDNSEntryService.class, FloatingIPDNSEntryServiceImpl.class);
        bind(HostAggregateService.class, HostAggregateServiceImpl.class);
        bind(MemberService.class, MemberServiceImpl.class);
        bind(VipService.class, VipServiceImpl.class);
        bind(HealthMonitorService.class, HealthMonitorServiceImpl.class);
        bind(LbPoolService.class, LbPoolServiceImpl.class);
        bind(LoadBalancerService.class, LoadBalancerServiceImpl.class);
        bind(BlockVolumeTransferService.class, BlockVolumeTransferServiceImpl.class);
        bind(SaharaPluginService.class, SaharaPluginServiceImpl.class);
        bind(SaharaImageService.class, SaharaImageServiceImpl.class);
        bind(SaharaService.class, SaharaServiceImpl.class);
        bind(ClusterService.class, ClusterServiceImpl.class);
        bind(AppCatalogService.class, MuranoService.class);
        bind(MuranoEnvironmentService.class, MuranoEnvironmentServiceImpl.class);
        bind(MuranoSessionService.class, MuranoSessionServiceImpl.class);
        bind(MuranoApplicationService.class, MuranoApplicationServiceImpl.class);
        bind(MuranoDeploymentService.class, MuranoDeploymentServiceImpl.class);
        bind(MuranoActionService.class, MuranoActionServiceImpl.class);
        bind(ClusterTemplateService.class, ClusterTemplateServiceImpl.class);
        bind(NodeGroupTemplateService.class, NodeGroupTemplateServiceImpl.class);
        bind(DataSourceService.class, DataSourceServiceImpl.class);
        bind(JobBinaryInternalService.class, JobBinaryInternalServiceImpl.class);
        bind(JobBinaryService.class, JobBinaryServiceImpl.class);
        bind(JobService.class, JobServiceImpl.class);
        bind(JobExecutionService.class, JobExecutionServiceImpl.class);
        bind(ShareService.class, ShareServiceImpl.class);
        bind(SecurityServiceService.class, SecurityServiceServiceImpl.class);
        bind(ShareSnapshotService.class, ShareSnapshotServiceImpl.class);
        bind(ShareNetworkService.class, ShareNetworkServiceImpl.class);
        bind(SharesService.class, SharesServiceImpl.class);
        bind(ShareServerService.class, ShareServerServiceImpl.class);
        bind(ShareInstanceService.class, ShareInstanceServiceImpl.class);
        bind(ShareTypeService.class, ShareTypeServiceImpl.class);
        bind(SchedulerStatsService.class, SchedulerStatsServiceImpl.class);
        bind(org.openstack4j.api.manila.QuotaSetService.class, org.openstack4j.openstack.manila.internal.QuotaSetServiceImpl.class);
        bind(GbpService.class, GbpServiceImpl.class);
        bind(ExternalPolicyService.class, ExternalPolicyServiceImpl.class);
        bind(ExternalSegmentService.class, ExternalSegmentServiceImpl.class);
        bind(org.openstack4j.api.gbp.GroupService.class, org.openstack4j.openstack.gbp.internal.GroupServiceImpl.class);
        bind(L2policyService.class, L2policyServiceImpl.class);
        bind(L3policyService.class, L3policyServiceImpl.class);
        bind(NatPoolService.class, NatPoolServiceImpl.class);
        bind(NetworkService.class, NetworkServiceImpl.class);
        bind(PolicyActionService.class, PolicyActionServiceImpl.class);
        bind(PolicyRuleService.class, PolicyRuleServiceImpl.class);
        bind(PolicyRuleSetService.class, PolicyRuleSetServiceImpl.class);
        bind(PolicyTargetService.class, PolicyTargetServiceImpl.class);
        bind(PolicyClassifierService.class, PolicyClassifierServiceImpl.class);
        bind(ServicechainService.class, ServicechainServiceImpl.class);
        bind(ServiceProfileService.class, ServiceProfileServiceImpl.class);
        bind(BlockQuotaSetService.class, BlockQuotaSetServiceImpl.class);
        bind(FirewallAsService.class, FirewallAsServiceImpl.class);
        bind(FirewallService.class, FirewallServiceImpl.class);
        bind(FirewallRuleService.class, FirewallRuleServiceImpl.class);
        bind(FirewallPolicyService.class, FirewallPolicyServiceImpl.class);
        bind(NetworkPolicyService.class, NetworkPolicyServiceImpl.class);
        bind(LbaasV2Service.class, LbaasV2ServiceImpl.class);
        bind(LoadBalancerV2Service.class, LoadBalancerV2ServiceImpl.class);
        bind(ListenerV2Service.class, ListenerV2ServiceImpl.class);
        bind(HealthMonitorV2Service.class, HealthMonitorV2ServiceImpl.class);
        bind(LbPoolV2Service.class, LbPoolV2ServiceImpl.class);
        bind(TroveService.class, TroveServiceImpl.class);
        bind(InstanceFlavorService.class, DBFlavorServiceImpl.class);
        bind(DatastoreService.class, DBDatastoreServiceImpl.class);
        bind(DatabaseService.class, DBDatabaseServiceImpl.class);
        bind(org.openstack4j.api.trove.UserService.class, DBUserServiceImpl.class);
        bind(InstanceService.class, DBInstanceServiceImpl.class);
        bind(SchedulerStatsGetPoolService.class, SchedulerStatsGetPoolServiceImpl.class);
        bind(BarbicanService.class, BarbicanServiceImpl.class);
        bind(ContainerService.class, ContainerServiceImpl.class);
        bind(SecretService.class, SecretServiceImpl.class);
        bind(TackerService.class, TackerServiceImpl.class);
        bind(VnfdService.class, VnfdServiceImpl.class);
        bind(VnfService.class, VnfServiceImpl.class);
        bind(VimService.class, VimServiceImpl.class);
        bind(AgentService.class, AgentServiceImpl.class);
        bind(org.openstack4j.api.image.v2.ImageService.class, org.openstack4j.openstack.image.v2.internal.ImageServiceImpl.class);
        bind(TaskService.class, TaskServiceImpl.class);
        bind(TaskService.class, TaskServiceImpl.class);
        bind(ServerTagService.class, ServerTagServiceImpl.class);
        bind(TelemetryAodhService.class, TelemetryAodhServiceImpl.class);
        bind(AlarmAodhService.class, AlarmAodhServiceImpl.class);
        bind(ServicesService.class, ServicesServiceImpl.class);
        bind(BlockStorageServiceService.class, BlockStorageServiceServiceImpl.class);
        bind(MagnumService.class, MagnumServiceImpl.class);
        bind(WorkflowService.class, WorkflowServiceImpl.class);
        bind(WorkflowDefinitionService.class, WorkflowDefinitionServiceImpl.class);
        bind(DNSService.class, DNSServiceImpl.class);
        bind(org.openstack4j.api.dns.v2.ZoneService.class, org.openstack4j.openstack.dns.v2.internal.ZoneServiceImpl.class);
        bind(RecordsetService.class, RecordsetServiceImpl.class);
        bind(WorkflowService.class, WorkflowServiceImpl.class);
        bind(WorkflowDefinitionService.class, WorkflowDefinitionServiceImpl.class);
        bind(WorkbookDefinitionService.class, WorkbookDefinitionServiceImpl.class);
        bind(ActionDefinitionService.class, ActionDefinitionServiceImpl.class);
        bind(WorkflowExecutionService.class, WorkflowExecutionServiceImpl.class);
        bind(TaskExecutionService.class, TaskExecutionServiceImpl.class);
        bind(ActionExecutionService.class, ActionExecutionServiceImpl.class);
        bind(WorkflowEnvironmentService.class, WorkflowEnvironmentServiceImpl.class);
        bind(CronTriggerService.class, CronTriggerServiceImpl.class);
        bind(NetworkIPAvailabilityService.class, NetworkIPAvailabilityServiceImpl.class);
        bind(TrunkService.class, TrunkServiceImpl.class);
        bind(ServerActionsService.class, NovaServerActionsService.class);
        bind(NeutronResourceTagService.class, NeutronResourceTagServiceImpl.class);
        bind(PortForwardingService.class, PortForwardingServiceImpl.class);
        bind(NetQosPolicyService.class, NetQosPolicyServiceImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> api) {
        if (instances.containsKey(api))
            return (T) instances.get(api);

        if (bindings.containsKey(api)) {
            try {
                T impl = (T) bindings.get(api).newInstance();
                instances.put(api, impl);
                return impl;
            } catch (Exception e) {
                throw new ApiNotFoundException("API Not found for: " + api.getName(), e);
            }
        }
        throw new ApiNotFoundException("API Not found for: " + api.getName());
    }

    private void bind(Class<?> api, Class<?> impl) {
        bindings.put(api, impl);
    }
}
