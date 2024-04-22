<a name="HifJO"></a>
# 1. dcm4chee都使用了什么东西？
```shell
$ docker start ldap db arc
```
启动了ldap，db，arc<br />分别是ldap服务<br />postgresql数据库服务<br />Wildfly服务，已经把dcm4chee部署到上边
<a name="zUg3E"></a>
## ladp slapd-dcm4chee
ldap协议，openldap是ldap的一个开源实现，<br />slapd是openldap的服务端应用程名，通常也是服务启动名。

OpenLDAP是轻型目录访问协议（Lightweight Directory Access Protocol，LDAP）的自由和开源的实现，在其OpenLDAP许可证下发行，并已经被包含在众多流行的Linux发行版中。<br />它主要包括下述4个部分：<br />slapd - 独立LDAP守护服务<br />slurpd - 独立的LDAP更新复制守护服务<br />实现LDAP协议的库<br />工具软件和示例客户端


slapd带有dcm4chee-arc 5.x的默认配置和数据库


<a name="TLtRo"></a>
## db postgres-dcm4chee
postgres<br />postgresql 一种数据库系统<br />PostgreSQL不仅是一个关系数据库，而且还是一个对象关系数据库。它没有特殊许可证等牵绊，安装即可使用。<br />它具有现代数据类型，比如用于存储IP地址的Inet类型，存储JSON的，全文检索等，并且具有非常好的扩展性。
<a name="DyEWJ"></a>
## arc dcm4chee-arc-psql
wildfly<br />Wildfly是一个开源的基于JavaEE的轻量级应用服务器，目前，最新版本是Wildfly 15，wildfly遵循LGPL2.1许可，意味着可以在任何商业应用中免费使用。WildFly是一个灵活的、轻量的、强大管理能力的应用程序服务器。Wildfly是一个管理EJB的容器和服务器，但JBoss核心服务不包括支持servlet/JSP的WEB容器，一般与Tomcat或Jetty绑定使用。

psql<br />这里是PostgreSQL 数据库的工具， psql 是 PostgreSQL 中的一个命令行交互式客户端工具
<a name="LqP0h"></a>
## dcm4che-tools
dcm4che的工具，能够进行发送CT图像等操作


<a name="2iKOq"></a>
# 2. dcm4chee都完成了什么功能
<a name="wcs4j"></a>
## monitoring



<a name="81smC"></a>
## navigation

- 检查study管理
- 病人管理
- worklist工作列表查询
- <br />
<a name="ppHMX"></a>
### study

- 可以搜索查询患者检查序列
- 图像存储承诺验证
- 图像删除
- 图像下载
- 图像查看
- 查看图像字段数据
- 字段数据下载
- 检查信息编辑
- 上传DCIOM数据
- 标签参数删除

**retrieve url**<br />检索URL<br />包含图像所有数据，图像头和图像数据尾部都添加了数据

**图像级上的操作**<br />verify storage commitment 验证存储的承诺，验证存储提交？

**Storage Commitment Service？**<br />存储提交服务，存储承诺服务<br />不能完全信任发送消息的系统。所以产生了存储提交服务<br />听起来像是：当您在数据库表中插入一条记录时，您需要验证该记录是否真的被插入了，因为您不能信任数据库引擎。。。看起来一点也不奇怪？如果我不能信任数据库引擎，我将用一个可信任的数据库引擎替换它<br />Zero footprint viewer (early 2000's, still some installations around)<br />当此查看器接收图像时，它们根本不会复制到磁盘。相反，它们被保存在内存中并直接显示给用户。当查看器关闭时，图像将从接收图像的系统中消失。<br />C-Store的成功意味着：我已经收到图像，并且能够显示它们。<br />此类系统**不支持存储承诺**。<br />**C-Store成功**意味着：我已收到图像并将其插入到storage center的队列中<br />**存储承诺成功**意味着：我已收到两个Storage Center的确认，即图像已保存在磁带上。从现在起，我将负责图像的安全保护-您可以删除您的本地副本。

export instance<br />出口实例<br />选择出口商的类型:<br />同步的DICOM C-STORE出口商<br />同步的DICOM STOW-RS导出器<br />对于排队的导出器，首先创建一个导出器!<br />选择目标AETitle:<br />AS_RECEIVED<br />【成功】

send storage commitment request for this instance为此实例发送存储承诺请求<br />Request Storage Commitment of Instance from external Storage Commitment SCP从外部存储承诺SCP请求实例的存储承诺<br />错误

send instance availability notification for this instance发送此实例的实例可用性通知<br />Request Instance Availability of Instance to external Instance Availability SCP请求实例的可用性到外部实例可用性SCP

Upload image,video or pdf to study<br />Select the STOW-RS server:<br />Choose a File to upload:

download as csv


set/change expired data设置/更改过期数据<br /> Set expired date for the study.设置检查的过期日期。<br />Set exporter if you wan't to export on expiration date too.如果您不想在到期日导出，请设置导出器。


Update Study Access Control ID of the study更新研究的研究访问控制ID

Schedule Storage Commitment of matching Studies from external Storage Commitment SCP从外部存储承诺 SCP 安排匹配检查的存储承诺

Schedule Instance Availability of matching Studies to external Instance Availability SCP将匹配研究的实例可用性调度到外部实例可用性SCP

received收到<br />as_received 医学图像


iocm 医学图像<br />IOCM Operations

iocm expired【iocm过期】<br />iocm pat safety 医学图像【pat安全】<br />iocm quality【质量】<br />iocm regular use【定期使用】<br />iocm wrong mwl【iocm错误mwl】<br />都是一种一种的检查类型？




<a name="yYLMi"></a>
### patients
create patient<br />supplement issuer补充发行人

<a name="5mycm"></a>
### MWL
Modality Worklist服务，简称MWL





Change Scheduled Procedure Step Status of the matching MWL 更改匹配MWL的计划过程步骤状态

Select the Scheduled Procedure Step Status选择计划的程序步骤状态

SPS Status = Scheduled Procedure Step Status<br />包括如下；<br />scheduled预订<br />arrived到达/签到<br />ready准备好<br />started开始<br />departed离开<br />canceled取消<br />discontinued中止<br />completed完成<br />状态时序情况：<br />![](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627706524760-fc278d47-8d8e-4cee-8b2a-b67779798f47.png#averageHue=%23f1f1f1&height=597&id=ELQnA&originHeight=597&originWidth=917&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=917)

MWL dicom

<a name="scUXU"></a>
### UWL
Unified Worklist (UWL)统一工作列表（UWL）<br />**使用UWL替换导出任务队列**<br />队列导出任务的当前实现是在**对象存储到存档**或**使用 RESTful 服务**时完成的。这两个都由Wildfly的Java消息服务API（JMS）子系统进行处理，作为未来归档开发的一部分，该子系统将被**统一工作列表**完全取代。【统一工作列表取代了对象存储到存档和使用 RESTful 服务】

UWL是干什么用的呢？<br />导出收到的DICOM实例<br />使用UPS模板导出本地研究

Unified Procedure Step Service Class统一过程步骤服务类

- 统一过程步骤服务类提供简单工作列表的管理，包括创建新的工作列表项、查询工作列表以及交流进度和结果。
- **worklist工作列表**是**统一过程步骤（Unified Procedure Step，UPS）实例**的**列表**。每个UPS实例将单个请求的过程步骤的工作列表详细信息与相应执行的过程步骤的结果详细信息统一起来。程序步骤请求和执行的程序步骤之间存在一对一关系。
- **统一过程步骤实例**可用于表示**各种预定任务**，例如：图像处理、质量控制、计算机辅助检测、解释、转录、报告验证或打印。






ups 医学图像是什么？<br />Unified Procedure Step (UPS)统一过程步骤<br />干什么用的？<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627709520793-facaa6f7-ef95-40b9-925e-255521e67c48.png#averageHue=%23f6f5f5&height=650&id=eJmK9&originHeight=867&originWidth=953&originalType=binary&ratio=1&rotation=0&showTitle=false&size=78972&status=done&style=none&title=&width=715)

<a name="1OCbk"></a>
### DIFFs
Difference区别；差值；分歧<br />diffs 医学图像

对比差异，A端和B端怎么去更新，谁更新


<a name="4oauJ"></a>
### MPPS
MPPS是从影响设备端向外部信息系统中传送消息<br />SPS与MPPS类似，唯一的区别是一个是预期的操作（Scheduled），一个是具体的操作（Performed）。SPS是在医院信息系统中登记的、由医生开具的检查操作，SPS的预约流程只有在MPPS返回成功后才算一次真实的检查结束，具体的流程模型如下图所示。



<a name="wr5ij"></a>
## configuration


F12那个network吗？js和xhr都是请求类型的过滤条件，js代表直接请求的js文件，xhr代表的是ajax请求

<a name="NsRiE"></a>
# 2.X 功能完成过程
<a name="lNPTW"></a>
## Export received DICOM instances  导出收到的DICOM实例



<a name="o65Qv"></a>
# 3. win端怎么传CT图像到Ubuntu的docker容器中的dcm4chee
<a name="rRybe"></a>
# 3.X RIS项目会怎么使用dcm4chee archive
RIS和其他系统的所有通信

1. 和HIS检查信息的获取
2. 发送HIS检查完成
3. Worklist通信服务
4. 接收MPPS服务发回来的消息
5. 从PACS获取患者图像
<a name="YLNGL"></a>
# 4.dicom相关问题解决方式
打开浏览器的debug模式；查看network通信记录<br />查看ALL或者XHR过滤条件<br />ALL整个页面debug查看<br />XHR的局部页面刷新debug查看，XHR是Ajax请求的消息

<a name="LGjwB"></a>
# 5.和dcm4chee archive交互
<a name="r0UZT"></a>
## 存储图像
storescu.exe 192.168.180.128 11112 -aec DCM4CHEE -xs +sd "D:\DICOMexperiment\C\PACS\SCU\series-000001" -v
```

D:\DICOMexperiment\C\PACS\SCU\series-000001>storescu.exe 192.168.180.128 11112 -aec DCM4CHEE -xs +sd "D:\DICOMexperiment\C\PACS\SCU\series-000001" -v
I: determining input files ...
I: checking input files ...
I: Requesting Association
I: Association Accepted (Max Send PDV: 16366)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_011.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 1, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_012.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 2, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_013.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 3, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_014.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 4, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_015.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 5, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_016.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 6, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_017.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 7, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_018.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 8, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_019.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 9, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\brain_020.dcm
I: Converting transfer syntax: Little Endian Explicit -> Little Endian Explicit
I: Sending Store Request (MsgID 10, MR)
XMIT: .........
I: Received Store Response (Success)
I: Sending file: D:\DICOMexperiment\C\PACS\SCU\series-000001\thumbnail.png
W: DcmItem: Length of element (5089,474e) is odd
E: DcmElement: Unknown Tag & Data (5089,474e) larger (169478669) than remaining bytes in file
E: Bad DICOM file: D:\DICOMexperiment\C\PACS\SCU\series-000001\thumbnail.png: I/O suspension or premature end of stream
E: Store SCU Failed: 0001:000a I/O suspension or premature end of stream
I: Aborting Association

D:\DICOMexperiment\C\PACS\SCU\series-000001>
```
<a name="78NuB"></a>
## 获取图像
**流程**<br />![未命名文件.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627725832518-78810d60-f7ef-4014-b25c-618ddfa14d5e.png#averageHue=%234b4b4b&height=432&id=WX3Lv&originHeight=432&originWidth=474&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14004&status=done&style=none&title=&width=474)<br />**storescp -b STORESCP:11112**<br />**movescu -c DCM4CHEE@192.168.180.128:11112 -m StudyInstanceUID=1.113654.3.13.1026 --dest STORESCP**<br />**(0020,000D) UI [] StudyInstanceUID；这里是值为空，io异常**<br />** i/o exception: java.io.EOFException in State: Sta6**
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>movescu -c DCM4CHEE@192.168.180.128:11112 -m StudyInstanceUID=1.113654.3.13.1026 --dest STORESCP
18:09:04,922 INFO  - Initiate connection from 0.0.0.0/0.0.0.0:0 to 192.168.180.128:11112
18:09:04,930 INFO  - Established connection Socket[addr=/192.168.180.128,port=11112,localport=14434]
18:09:04,946 DEBUG - /192.168.180.1:14434->/192.168.180.128:11112(1): enter state: Sta4 - Awaiting transport connection opening to complete
18:09:04,947 INFO  - MOVESCU->DCM4CHEE(1) << A-ASSOCIATE-RQ
18:09:04,962 DEBUG - A-ASSOCIATE-RQ[
  calledAET: DCM4CHEE
  callingAET: MOVESCU
  applicationContext: 1.2.840.10008.3.1.1.1 - DICOM Application Context Name
  implClassUID: 1.2.40.0.13.1.3
  implVersionName: dcm4che-5.23.2
  maxPDULength: 16378
  maxOpsInvoked/maxOpsPerformed: 0/0
  PresentationContext[id: 1
    as: 1.2.840.10008.5.1.4.1.2.2.2 - Study Root Query/Retrieve Information Model - MOVE
    ts: 1.2.840.10008.1.2 - Implicit VR Little Endian
    ts: 1.2.840.10008.1.2.1 - Explicit VR Little Endian
    ts: 1.2.840.10008.1.2.2 - Explicit VR Big Endian (Retired)
  ]
]
18:09:04,962 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta5 - Awaiting A-ASSOCIATE-AC or A-ASSOCIATE-RJ PDU
18:09:04,965 INFO  - MOVESCU->DCM4CHEE(1) >> A-ASSOCIATE-AC
18:09:04,965 DEBUG - A-ASSOCIATE-AC[
  calledAET: DCM4CHEE
  callingAET: MOVESCU
  applicationContext: 1.2.840.10008.3.1.1.1 - DICOM Application Context Name
  implClassUID: 1.2.40.0.13.1.3
  implVersionName: dcm4che-5.23.3
  maxPDULength: 16378
  maxOpsInvoked/maxOpsPerformed: 0/0
  PresentationContext[id: 1
    result: 0 - acceptance
    ts: 1.2.840.10008.1.2 - Implicit VR Little Endian
  ]
]
18:09:04,966 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta6 - Association established and ready for data transfer
18:09:04,971 INFO  - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ[pcid=1, prior=0, dest=STORESCP
  cuid=1.2.840.10008.5.1.4.1.2.2.2 - Study Root Query/Retrieve Information Model - MOVE
  tsuid=1.2.840.10008.1.2 - Implicit VR Little Endian]
18:09:04,989 DEBUG - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ Command:
(0000,0002) UI [1.2.840.10008.5.1.4.1.2.2.2] AffectedSOPClassUID
(0000,0100) US [33] CommandField
(0000,0110) US [1] MessageID
(0000,0600) AE [STORESCP] MoveDestination
(0000,0700) US [0] Priority
(0000,0800) US [0] CommandDataSetType

18:09:04,993 DEBUG - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ Dataset:
(0000,0001) SQ [1 Items] CommandLengthToEnd
>Item #1
>(0011,3654) SQ [1 Items]
>>Item #1
>>(0000,0003) SQ [1 Items] RequestedSOPClassUID
>>>Item #1
>>>(0000,0013) SQ [1 Items]
>>>>Item #1
>>>>(0000,1026) UN []
(0008,0052) CS [STUDY] QueryRetrieveLevel
(0020,000D) UI [] StudyInstanceUID

18:09:04,997 INFO  - MOVESCU->DCM4CHEE(1): i/o exception: java.io.EOFException in State: Sta6 - Association established and ready for data transfer
18:09:04,998 INFO  - MOVESCU->DCM4CHEE(1): close Socket[addr=/192.168.180.128,port=11112,localport=14434]
18:09:05,000 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta1 - Idle
movescu: Sta1 - Idle
org.dcm4che3.net.AssociationStateException: Sta1 - Idle
        at org.dcm4che3.net.State.writeAReleaseRQ(State.java:223)
        at org.dcm4che3.net.Association.release(Association.java:335)
        at org.dcm4che3.tool.movescu.MoveSCU.close(MoveSCU.java:316)
        at org.dcm4che3.tool.movescu.MoveSCU.main(MoveSCU.java:257)

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```
**movescu -c DCM4CHEE@192.168.180.128:11112 --dest STORESCP**<br />**不指定参数 -m StudyInstanceUID=1.113654.3.13.1026会报错如下：**<br />**(0000,0902) LO [Missing StudyInstanceUID (0020,000D)] ErrorComment**
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>movescu -c DCM4CHEE@192.168.180.128:11112 --dest STORESCP
18:18:27,437 INFO  - Initiate connection from 0.0.0.0/0.0.0.0:0 to 192.168.180.128:11112
18:18:27,445 INFO  - Established connection Socket[addr=/192.168.180.128,port=11112,localport=7898]
18:18:27,465 DEBUG - /192.168.180.1:7898->/192.168.180.128:11112(1): enter state: Sta4 - Awaiting transport connection opening to complete
18:18:27,467 INFO  - MOVESCU->DCM4CHEE(1) << A-ASSOCIATE-RQ
18:18:27,479 DEBUG - A-ASSOCIATE-RQ[
  calledAET: DCM4CHEE
  callingAET: MOVESCU
  applicationContext: 1.2.840.10008.3.1.1.1 - DICOM Application Context Name
  implClassUID: 1.2.40.0.13.1.3
  implVersionName: dcm4che-5.23.2
  maxPDULength: 16378
  maxOpsInvoked/maxOpsPerformed: 0/0
  PresentationContext[id: 1
    as: 1.2.840.10008.5.1.4.1.2.2.2 - Study Root Query/Retrieve Information Model - MOVE
    ts: 1.2.840.10008.1.2 - Implicit VR Little Endian
    ts: 1.2.840.10008.1.2.1 - Explicit VR Little Endian
    ts: 1.2.840.10008.1.2.2 - Explicit VR Big Endian (Retired)
  ]
]
18:18:27,480 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta5 - Awaiting A-ASSOCIATE-AC or A-ASSOCIATE-RJ PDU
18:18:27,483 INFO  - MOVESCU->DCM4CHEE(1) >> A-ASSOCIATE-AC
18:18:27,483 DEBUG - A-ASSOCIATE-AC[
  calledAET: DCM4CHEE
  callingAET: MOVESCU
  applicationContext: 1.2.840.10008.3.1.1.1 - DICOM Application Context Name
  implClassUID: 1.2.40.0.13.1.3
  implVersionName: dcm4che-5.23.3
  maxPDULength: 16378
  maxOpsInvoked/maxOpsPerformed: 0/0
  PresentationContext[id: 1
    result: 0 - acceptance
    ts: 1.2.840.10008.1.2 - Implicit VR Little Endian
  ]
]
18:18:27,484 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta6 - Association established and ready for data transfer
18:18:27,490 INFO  - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ[pcid=1, prior=0, dest=STORESCP
  cuid=1.2.840.10008.5.1.4.1.2.2.2 - Study Root Query/Retrieve Information Model - MOVE
  tsuid=1.2.840.10008.1.2 - Implicit VR Little Endian]
18:18:27,515 DEBUG - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ Command:
(0000,0002) UI [1.2.840.10008.5.1.4.1.2.2.2] AffectedSOPClassUID
(0000,0100) US [33] CommandField
(0000,0110) US [1] MessageID
(0000,0600) AE [STORESCP] MoveDestination
(0000,0700) US [0] Priority
(0000,0800) US [0] CommandDataSetType

18:18:27,519 DEBUG - MOVESCU->DCM4CHEE(1) << 1:C-MOVE-RQ Dataset:
(0008,0052) CS [STUDY] QueryRetrieveLevel

18:18:27,530 INFO  - MOVESCU->DCM4CHEE(1) >> 1:C-MOVE-RSP[pcid=1, status=a900H, errorComment=Missing StudyInstanceUID (0020,000D)
  tsuid=1.2.840.10008.1.2 - Implicit VR Little Endian]
18:18:27,531 DEBUG - MOVESCU->DCM4CHEE(1) >> 1:C-MOVE-RSP Command:
(0000,0100) US [32801] CommandField
(0000,0120) US [1] MessageIDBeingRespondedTo
(0000,0800) US [257] CommandDataSetType
(0000,0900) US [43264] Status
(0000,0901) AT [0020000D] OffendingElement
(0000,0902) LO [Missing StudyInstanceUID (0020,000D)] ErrorComment

18:18:27,534 INFO  - MOVESCU->DCM4CHEE(1) << A-RELEASE-RQ
18:18:27,534 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta7 - Awaiting A-RELEASE-RP PDU
18:18:27,547 INFO  - MOVESCU->DCM4CHEE(1) >> A-RELEASE-RP
18:18:27,547 INFO  - MOVESCU->DCM4CHEE(1): close Socket[addr=/192.168.180.128,port=11112,localport=7898]
18:18:27,547 DEBUG - MOVESCU->DCM4CHEE(1): enter state: Sta1 - Idle

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```


storescu -cDCM4CHEE@arc:11112 /opt/dcm4che/etc/testdata/dicom<br /> <br />movescu.exe -v -S -aec DCM4CHEE -aet ACME1 -aem ACME1 --port 1234 -od D:\DICOMexperiment\C\PACS\SCU\database 192.168.180.128 11112 -k QueryRetrieveLevel=STUDY -k StudyDate=20010316
```
D:\DICOMexperiment\C\PACS\SCU\database>movescu.exe -v -S -aec DCM4CHEE -aet ACME1 -aem ACME1 --port 1234 -od D:\DICOMexperiment\C\PACS\SCU\database 192.168.180.128 11112 -k QueryRetrieveLevel=STUDY -k StudyDate=20010316
I: Requesting Association
I: Association Accepted (Max Send PDV: 16366)
I: Sending Move Request (MsgID 1)
I: Request Identifiers:
I:
I: # Dicom-Data-Set
I: # Used TransferSyntax: Little Endian Explicit
I: (0008,0020) DA [20010316]                               #   8, 1 StudyDate
I: (0008,0052) CS [STUDY]                                  #   6, 1 QueryRetrieveLevel
I:
W: Move response with error status (Failed: IdentifierDoesNotMatchSOPClass)
I: Received Final Move Response (Failed: IdentifierDoesNotMatchSOPClass)
I: Releasing Association

D:\DICOMexperiment\C\PACS\SCU\database> 
```
<a name="Lm28i"></a>
## find查询
findscu.exe -v -S -aec DCM4CHEE -aet ACME1 192.168.180.128 11112 -k QueryRetrieveLevel=STUDY -k StudyDate -k StudyDescription -k StudyInstanceUID
```

D:\DICOMexperiment\C\PACS\SCU\database>findscu.exe -v -S -aec DCM4CHEE -aet ACME1 192.168.180.128 11112 -k QueryRetrieveLevel=STUDY -k StudyDate -k StudyDescription -k StudyInstanceUID
I: Requesting Association
I: Association Accepted (Max Send PDV: 16366)
I: Sending Find Request (MsgID 1)
I: Request Identifiers:
I:
I: # Dicom-Data-Set
I: # Used TransferSyntax: Little Endian Explicit
I: (0008,0020) DA (no value available)                     #   0, 0 StudyDate
I: (0008,0052) CS [STUDY]                                  #   6, 1 QueryRetrieveLevel
I: (0008,1030) LO (no value available)                     #   0, 0 StudyDescription
I: (0020,000d) UI (no value available)                     #   0, 0 StudyInstanceUID
I:
I: ---------------------------
I: Find Response: 1 (Pending)
I:
I: # Dicom-Data-Set
I: # Used TransferSyntax: Little Endian Implicit
I: (0008,0020) DA [20170716]                               #   8, 1 StudyDate
I: (0008,0052) CS [STUDY ]                                 #   6, 1 QueryRetrieveLevel
I: (0008,0054) AE [DCM4CHEE]                               #   8, 1 RetrieveAETitle
I: (0008,0056) CS [ONLINE]                                 #   6, 1 InstanceAvailability
I: (0008,1030) LO [LEFT WRIST]                             #  10, 1 StudyDescription
I: (0020,000d) UI [1.113654.3.13.1026]                     #  18, 1 StudyInstanceUID
I:
I: ---------------------------
I: Find Response: 2 (Pending)
I:
I: # Dicom-Data-Set
I: # Used TransferSyntax: Little Endian Implicit
I: (0008,0020) DA [20010316]                               #   8, 1 StudyDate
I: (0008,0052) CS [STUDY ]                                 #   6, 1 QueryRetrieveLevel
I: (0008,0054) AE [DCM4CHEE]                               #   8, 1 RetrieveAETitle
I: (0008,0056) CS [ONLINE]                                 #   6, 1 InstanceAvailability
I: (0008,1030) LO [BRAIN ]                                 #   6, 1 StudyDescription
I: (0020,000d) UI [0.0.0.0.2.8811.20010413115754.12432 ]   #  36, 1 StudyInstanceUID
I:
I: Received Final Find Response (Success)
I: Releasing Association

D:\DICOMexperiment\C\PACS\SCU\database>
```


<a name="8Ob2o"></a>
# Ubuntu下gid是什么?
uid  用户ID号<br />GID  用户属ID号


增加用户<br />useradd:    password:<br />删除用户<br />userdel:     

finger  用户<br />表示用户状态

用户切换<br />su  -username  或者 us -l username<br />直接使用 su  可能可以切换到root用户，但是环境变量没有变<br />也就是说，用户名变成了root ，但是权限没变<br />也就是  su -用户名称


su和sudo的区别<br />su 是切换到管理员用户<br />sudo 是以管理员权限执行一个文件




————————————————<br />版权声明：本文为CSDN博主「白帽梦想家」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。<br />原文链接：[https://blog.csdn.net/sdb5858874/article/details/80499746](https://blog.csdn.net/sdb5858874/article/details/80499746)

<a name="cDWCk"></a>
# 已经安装的条件下怎么启动还停止服务器
您可以通过以下方式停止所有 3 个容器：
```shell
$ docker stop ldap db arc
```
并通过以下方式再次启动所有 3 个容器：
```shell
$ docker start ldap db arc
```
<a name="VN0wQ"></a>
# 首次启动时候的参考网址
[https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host](https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host)
<a name="NqLHe"></a>
# 首次启动时候的参考博客
[https://blog.csdn.net/zssureqh/article/details/103323715?ops_request_misc=&request_id=&biz_id=102&utm_term=dcm4chee-arc-light-5.4.1-mysql&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-6-.pc_search_result_before_js&spm=1018.2226.3001.4187](https://blog.csdn.net/zssureqh/article/details/103323715?ops_request_misc=&request_id=&biz_id=102&utm_term=dcm4chee-arc-light-5.4.1-mysql&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-6-.pc_search_result_before_js&spm=1018.2226.3001.4187)
<a name="ubwtM"></a>
# 虚拟机的桥接，NAT，仅主机模式
[https://blog.csdn.net/qq_39192827/article/details/85872025](https://blog.csdn.net/qq_39192827/article/details/85872025)<br />虚拟机桥接模式

- 虚拟机和主机拥有同一网络的IP，属于网络中的对等实体，所以win不需要有虚拟网卡，直接使用真实网卡，虚拟机使用虚拟网卡
- 这种情况虚拟机占用当前路由器下的IP
- ![](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627556854066-5fcccc28-190d-4fb5-9195-a6272ec87e37.png#averageHue=%23fcf5f5&height=675&id=yAlyM&originHeight=675&originWidth=899&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=899)

NAT模式

- NAT模式对于路由器，相当于一台机器会占用主机的物理网卡，所以主机需要有虚拟网卡，即VMnet8，进行虚拟网络内部的通信
- ![](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627556874131-f4977d5d-fb35-4c67-bf40-19021d4c896b.png#averageHue=%23fdf8f8&height=690&id=YxAe7&originHeight=690&originWidth=1125&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=1125)


仅主机模式

- 就是把内部的虚拟网络与外界网络隔离，只能通过VMnet1与主机通信，并且无法和外界通信
- ![](https://cdn.nlark.com/yuque/0/2021/png/2869223/1627557222838-2bf0baf0-4bbf-452f-a079-9af2fc6f76ee.png#averageHue=%23fcf8f8&height=673&id=Pl68l&originHeight=673&originWidth=984&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=984)
<a name="2JY5v"></a>
# 虚拟机和windows相互ping不通的问题
[https://blog.csdn.net/qq_42500577/article/details/95954948?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EsearchFromBaidu%7Edefault-2.pc_relevant_baidujshouduan&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EsearchFromBaidu%7Edefault-2.pc_relevant_baidujshouduan](https://blog.csdn.net/qq_42500577/article/details/95954948?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EsearchFromBaidu%7Edefault-2.pc_relevant_baidujshouduan&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EsearchFromBaidu%7Edefault-2.pc_relevant_baidujshouduan)

<a name="T2JF4"></a>
# 使用


