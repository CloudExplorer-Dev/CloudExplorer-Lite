<p align="center"><a href="https://fit2cloud.com/cloudexplorer-lite/index.html
        "><img src="https://fit2cloud.com/cloudexplorer-lite/docs/img/CloudExplorer-Lite-logo.jpg" alt="cloudexplorer-lite" width="300px"  /></a></p>
        <h3 align="center">开源的轻量版云管平台</h3>
        <p align="center">
          <a href="https://www.gnu.org/licenses/old-licenses/gpl-3.0"><img src="https://img.shields.io/github/license/CloudExplorer-Dev/CloudExplorer-Lite?color=%231890FF" alt="License: GPL v3"></a>
          <a href="https://app.codacy.com/gh/dataease/dataease?utm_source=github.com&utm_medium=referral&utm_content=dataease/dataease&utm_campaign=Badge_Grade_Dashboard"><img src="https://app.codacy.com/project/badge/Grade/da67574fd82b473992781d1386b937ef" alt="Codacy"></a>
          <a href="https://github.com/CloudExplorer-Dev/CloudExplorer-Lite/releases/latest"><img src="https://img.shields.io/github/v/release/CloudExplorer-Dev/CloudExplorer-Lite" alt="Latest release"></a>
          <a href="https://github.com/CloudExplorer-Dev/CloudExplorer-Lite"><img src="https://img.shields.io/github/stars/CloudExplorer-Dev/CloudExplorer-Lite?color=%231890FF&style=flat-square" alt="Stars"></a>
        
</p>
<hr/>

## 什么是 CloudExplorer-Lite？
CloudExplorer-Lite 是开源的轻量版多云管理平台；   
CloudExplorer-Lite 脱胎于飞致云创始软件产品 CloudExplorer 多云管理平台，支持对接纳管主流的公有云和私有云基础设施，提供开箱即用的云主机管理、云账单、运营分析和安全合规等基本功能，同时还可提供强大的扩展能力以满足企业的定制化需求。

![ce-主页](https://fit2cloud.com/cloudexplorer-lite/images/overview.png)

## CloudExplorer-Lite 产品特性

**多云对接能力**
  
CloudExplorer-Lite支持对接纳管市场上主流的公有云和私有云基础设施，包括阿里云、腾讯云、华为云、VMware、OpenStack等。
![ce-云账号](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/云账号.png)
**开箱即用的功能**

CloudExplorer-Lite提供了诸多开箱即用的功能，比如云主机管理、云账单、运营分析和安全合规等，能够满足大部分企业在云基础设施管理方面的基本需求。

 - **云主机：** CloudExplorer-Lite提供统一的云主机的生命周期管理操作界面；基于用户管理体系，用户可创建、可操作的资源都具备隔离性。
  ![ce-云主机管理](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/云主机管理.png)
 - **云账单：** CloudExplorer-Lite提供统一的费用管理、费用分摊、自定义多维度账单功能，可以作为企业的云资源成本分析中心，帮助企业用户有效降低云上资源的成本管理难度。
   ![ce-云账单](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/云账单.png)
 - **运营分析：** CloudExplorer-Lite能够对企业现有的云资源从分布、容量、使用情况等多个维度进行分析，并提供资源优化建议。
   ![ce-运营分析](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/运营分析.png)
 - **安全合规：** CloudExplorer-Lite支持一键扫描多个云平台的多种云资源，可灵活定义规则。
   ![ce-安全合规-一键扫描](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/安全合规-一键扫描.png)
   ![ce-安全合规-总览](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/安全合规-总览.png)

**扩展能力**

CloudExplorer Lite提供了强大的多租户体系和模块化能力，可以满足企业的定制化需求。
 - **多租户体系：** 作为云管平台的核心能力之一，CloudExplorer-Lite为用户提供多租户、多角色的管理模型。系统初始化了三种内置角色，分别为系统管理员、组织管理员和普通用户，用户可以根据企业的实际情况进行灵活的租户体系映射。
![ce-管理模型](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/管理模型.png)
 - **模块化设计：** CloudExplorer-Lite 采用模块化设计，即插即用，企业可以轻松部署不同模块，从而扩大对云基础设施的管理范围。
![ce-模块管理](https://fit2cloud.com/cloudexplorer-lite/docs/img/index/模块管理.png)



## 5 快速开始

**在线体验**

-   环境地址：<https://cloudexplorer-lite-demo.fit2cloud.com/>
-   用户名：demo
-   密码：cloudexplorer

**一键安装**

仅需两步快速安装 CloudExplorer-Lite：

1. 准备一台不小于 8C16G 的 Linux 主机；
2. 以 root 用户执行如下命令一键安装 CloudExplorer-Lite。

``` 
/bin/bash -c "$(curl -fsSL https://resource.fit2cloud.com/cloudexplorer-lite/installer/releases/latest/quick_start.sh)"
```

**学习资料**

-   [在线文档](https://fit2cloud.com/cloudexplorer-lite/docs/)

**加入微信交流群**

<img src="https://fit2cloud.com/cloudexplorer-lite/images/wechat-group.png" width="156" height="156"/>

## CloudExplorer-Lite 的技术栈

-   前端：[Vue3.js](https://cn.vuejs.org/)、[Element Plus](https://element-plus.org/zh-CN/)、[TypeScript](https://www.tslang.cn/)
-   图库：[Apache ECharts](https://github.com/apache/echarts)
-   后端：[Spring Boot](https://spring.io/projects/spring-boot)
-   中间件：[MySQL](https://www.mysql.com/)  
-   基础设施：[Docker](https://www.docker.com/)



## License

Copyright (c) 2014-2023 飞致云 FIT2CLOUD, All rights reserved.

Licensed under The GNU General Public License version 3 (GPLv3)  (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

<https://www.gnu.org/licenses/gpl-3.0.html>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
