package com.fit2cloud.common.provider.impl.proxmox.client.entity.constants;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/19  11:39}
 * {@code @Version 1.0}
 * {@code @注释:
 * 'Linux': [
 * { desc: '6.x - 2.6 Kernel', val: 'l26' },
 * { desc: '2.4 Kernel', val: 'l24' },
 * ],
 * 'Microsoft Windows': [
 * { desc: '11/2022', val: 'win11' },
 * { desc: '10/2016/2019', val: 'win10' },
 * { desc: '8.x/2012/2012r2', val: 'win8' },
 * { desc: '7/2008r2', val: 'win7' },
 * { desc: 'Vista/2008', val: 'w2k8' },
 * { desc: 'XP/2003', val: 'wxp' },
 * { desc: '2000', val: 'w2k' },
 * ],
 * 'Solaris Kernel': [
 * { desc: '-', val: 'solaris' },
 * ],
 * 'Other': [
 * { desc: '-', val: 'other' },
 * ],}
 */
public class OsType {

    public static final Map<String, String> osType = Map.of("l26", "Linux 6.x - 2.6 Kernel",
            "l24", "Linux 2.4 Kernel",
            "win11", "Microsoft Windows 11/2022",
            "win10", "Microsoft Windows 10/2016/2019",
            "win8", "Microsoft Windows 8.x/2012/2012r2",
            "win7", "Microsoft Windows 7/2008r2",
            "w2k8", "Microsoft Windows Vista/2008",
            "wxp", "Microsoft Windows XP/2003",
            "w2k", "Microsoft Windows 2000",
            "solaris", "Solaris Kernel");
}
