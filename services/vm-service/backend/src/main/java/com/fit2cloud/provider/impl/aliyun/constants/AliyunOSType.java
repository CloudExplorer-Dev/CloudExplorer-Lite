package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/16 2:03 PM
 */
public enum AliyunOSType {
    AlibabaCloudLinux("Alibaba Cloud Linux"),
    AnolisOS("Anolis OS"),
    CentOS("CentOS"),
    WindowsServer(" Windows Server"),
    RedHat("Red Hat"),
    SUSE("SUSE"),
    Linux("Linux"),
    Ubuntu("Ubuntu"),
    Debian("Debian"),
    Fedora("Fedora"),
    FedoraCoreOS("Fedora CoreOS"),
    OpenSUSE("OpenSUSE"),
    FreeBSD("FreeBSD"),
    RockyLinux("Rocky Linux"),
    CentOSStream("CentOS Stream"),
    AlmaLinux("AlmaLinux");

    private String displayValue;

    public String getDisplayValue() {
        return displayValue;
    }

    AliyunOSType(String displayValue) {
        this.displayValue = displayValue;
    }
}
