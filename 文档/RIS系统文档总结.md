<a name="FGpFn"></a>
## 可能的漏洞
和PACS交互中，可能存在漏洞，也就是跳过RIS系统，直接和PACS通讯获取或者注入数据。<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/2869223/1676989152486-c9408166-f91c-4ea5-ab58-c12330859ed5.png#averageHue=%23f9f7f6&clientId=u3ccd76c9-e6cf-4&from=paste&height=457&id=ua76df70b&originHeight=571&originWidth=779&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=66467&status=done&style=none&taskId=u4b505619-9c76-441c-a074-3e9b95e72af&title=&width=623.2)
<a name="d6a85"></a>
## RIS系统
**放射科信息系统(Radiology Information System, RIS)，英文缩写RIS**，**是医院重要的医学影像学信息系统之一，**它与PACS系统共同构成医学影像学的信息化环境。放射科信息系统是基于医院影像科室工作流程的任务执行过程管理的计算机信息系统，主要实现医学影像学检验工作流程的计算机网络化控制、管理和医学图文信息的共享，并在此基础上实现远程医疗。

先看一下系统演示


[RIS系统流程图.pdf](https://www.yuque.com/attachments/yuque/0/2022/pdf/2869223/1664437415346-a1c77023-55da-41bf-9594-8649c3e19584.pdf)

<a name="vDOKW"></a>
### JavaWeb项目实战-企业级CRM项目-CRM客户管理系统
[https://www.bilibili.com/video/BV1fT4y1E7a6](https://www.bilibili.com/video/BV1fT4y1E7a6)

<a name="YrYSk"></a>
### mysql数据库文件
[Dump20220517.sql](https://www.yuque.com/attachments/yuque/0/2022/sql/2869223/1664437602905-6788cc81-1a3a-4a9c-bef6-9dcf811a8688.sql?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2022%2Fsql%2F2869223%2F1664437602905-6788cc81-1a3a-4a9c-bef6-9dcf811a8688.sql%22%2C%22name%22%3A%22Dump20220517.sql%22%2C%22size%22%3A29456%2C%22ext%22%3A%22sql%22%2C%22source%22%3A%22%22%2C%22status%22%3A%22done%22%2C%22download%22%3Atrue%2C%22type%22%3A%22%22%2C%22mode%22%3A%22title%22%2C%22taskId%22%3A%22ued83c359-aea7-47eb-a905-83341a31c00%22%2C%22taskType%22%3A%22transfer%22%2C%22id%22%3A%22ue48386c1%22%2C%22card%22%3A%22file%22%7D)

<a name="iw2LM"></a>
### 项目zip包
[RIS_Gitee3.zip](https://www.yuque.com/attachments/yuque/0/2022/zip/2869223/1664437602936-bd116126-5ab5-4e76-972c-20e485380acd.zip?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2022%2Fzip%2F2869223%2F1664437602936-bd116126-5ab5-4e76-972c-20e485380acd.zip%22%2C%22name%22%3A%22RIS_Gitee3.zip%22%2C%22size%22%3A137887027%2C%22ext%22%3A%22zip%22%2C%22source%22%3A%22%22%2C%22status%22%3A%22done%22%2C%22download%22%3Atrue%2C%22type%22%3A%22application%2Fx-zip-compressed%22%2C%22mode%22%3A%22title%22%2C%22taskId%22%3A%22uceb94317-ba60-41a0-9203-2786f930260%22%2C%22taskType%22%3A%22transfer%22%2C%22id%22%3A%22ube58ee13%22%2C%22card%22%3A%22file%22%7D)

![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1652784922553-275bca87-6d87-4b27-b5eb-dffbe3e70407.png#averageHue=%23faf9f8&clientId=u743746b3-493b-4&errorMessage=unknown%20error&from=paste&height=559&id=u7081ac61&originHeight=699&originWidth=404&originalType=binary&ratio=1&rotation=0&showTitle=false&size=32248&status=error&style=none&taskId=ua7d7b253-738a-4f79-aeda-f589f9fb459&title=&width=323.2)

<a name="jPE0W"></a>
### RIS项目部署过程
[https://www.yuque.com/docs/share/41762ad2-ac39-4d4b-b951-46dc50a87700?#](https://www.yuque.com/docs/share/41762ad2-ac39-4d4b-b951-46dc50a87700?#) 

1. 需要部署PACS系统, 即DCM4CHEE项目(可以在本机的虚拟机测试)
2. 需要部署RIS系统
3. 配置DICOM Web
<a name="QaHY7"></a>
## 相关资料
<a name="Aywrc"></a>
### 《系统设计文档 V2》
[https://www.yuque.com/docs/share/4ad3cba7-8b68-4726-b7a3-d8b4d3bb40de?#](https://www.yuque.com/docs/share/4ad3cba7-8b68-4726-b7a3-d8b4d3bb40de?#) 

<a name="inr9Q"></a>
### RIS系统设计原型论文
> 中小型医院RIS需求分析和系统设计的研究_张磊.caj


[基于DCMTK的PACS系统设计与实现-匿名.pdf](https://www.yuque.com/attachments/yuque/0/2022/pdf/2869223/1664437732969-cdef2d15-c6df-4180-9d97-4e685a1dc627.pdf)

<a name="OOAsK"></a>
### dcm4chee-arc-light项目
可以理解为一个PACS系统, 能够进行医学影像的存取<br />可以使用虚拟机进行安装/测试

docker安装, 我是通过这种方式使用的<br />[https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host](https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host)<br />我自己的搭建过程记录
> [https://www.yuque.com/docs/share/9e50a230-e39a-40d6-a2ef-61f662d0d91e?#](https://www.yuque.com/docs/share/9e50a230-e39a-40d6-a2ef-61f662d0d91e?#) 


其他安装方式, 也可以看一看有没有更好的使用方法<br />[https://github.com/dcm4che/dcm4chee-arc-light/wiki/Running-on-Docker](https://github.com/dcm4che/dcm4chee-arc-light/wiki/Running-on-Docker)<br />[https://github.com/dcm4che/dcm4chee-arc-light/wiki](https://github.com/dcm4che/dcm4chee-arc-light/wiki)

<a name="vxAGH"></a>
### dcm4che工具
是一个Java实现的DICOM协议工具, 在RIS系统的源码中你会有一部分代码看不懂的, 那部分代码应该就是我参照dcm4che源码写进去的<br />[https://github.com/dcm4che/dcm4che](https://github.com/dcm4che/dcm4che)

<a name="g08vl"></a>
### Weasis 
Weasis是一种DICOM图像查看器，可用作桌面应用程序或基于web的应用程序。<br />[https://github.com/nroduit/Weasis](https://github.com/nroduit/Weasis)

<a name="F57uq"></a>
### 关于DICOM博客
[zssure的博客_CSDN博客-DICOM,DICOM医学图像处理,医疗资讯领域博主](https://blog.csdn.net/zssureqh?type=blog)<br />[我的DICOM学习之路_DICOM医学影像的博客-CSDN博客](https://blog.csdn.net/tianma2012/article/details/121182403?spm=1001.2014.3001.5501)<br />[啸鸢的博客_CSDN博客-DCM4CHEE Dicom医学影像处理,设计模式,java领域博主](https://blog.csdn.net/lemin_zhao?type=blog)<br />[DCM4CHE_不会吉他的肌肉男不是好的挨踢男的博客-CSDN博客](https://blog.csdn.net/u010101193/category_10131121.html?spm=1001.2014.3001.5482)

<a name="umevx"></a>
### weasis插件开发
[https://www.yuque.com/docs/share/0893416e-ea3a-405f-90cf-97fb6f552f95?#](https://www.yuque.com/docs/share/0893416e-ea3a-405f-90cf-97fb6f552f95?#) <br />Weasis源码中有一个archetype文件夹, 里面有weasis插件的demo工程<br />需要先配置好weasis源码环境
<a name="df9Pa"></a>
## 接下来工作

1. 先把项目配置好运行起来(因为后续的交互等实现需 还是要熟悉环境的)
2. 熟悉RIS系统源码
3. 开始添加检索功能, 检索功能介绍: ****, 使用weasis插件完成, 熟悉插件weasis插件开发

<a name="LAMz5"></a>
## new 接下来工作
<a name="X19pu"></a>
### 需要完成的内容-方法1
![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665802113314-8f673af9-feda-4080-be4e-9dc36e697ef5.png#clientId=u28066876-fb3c-4&from=paste&height=864&id=u95fff120&originHeight=1080&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=429690&status=done&style=none&taskId=uf2287bfd-6ab2-4cad-9ae1-8f3e7570e1c&title=&width=1536)
<a name="O1SSJ"></a>
### 方法2
![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665802318451-5298e070-3132-4f97-a23e-cf2fdd490f4a.png#clientId=u28066876-fb3c-4&from=paste&height=864&id=uc2343019&originHeight=1080&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=171166&status=done&style=none&taskId=ud58c6b03-0e4a-4d35-8621-2d8dba2403a&title=&width=1536)
<a name="da1uw"></a>
### DICOM相关工具开发
病人no-检查studyUID,-seriesUID-instanceUID<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665802507937-81bea50e-a347-417f-ab61-5b9276f37624.png#clientId=u28066876-fb3c-4&from=paste&height=864&id=uc8db6de6&originHeight=1080&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=377113&status=done&style=none&taskId=u9da94f78-5d47-468c-ae66-0634895c6d2&title=&width=1536)
<a name="SEsVe"></a>
#### 1 先熟悉相关工具使用情况
[https://www.yuque.com/docs/share/826a73b0-844a-4d2e-a2d8-a0fe9d1c648b?#](https://www.yuque.com/docs/share/826a73b0-844a-4d2e-a2d8-a0fe9d1c648b?#) 《dcm4che工具测试》
<a name="IrjFi"></a>
#### 2 调试熟悉工具实现流程
![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665802995376-669d8f70-b5d3-47fd-80d4-795e8587eb62.png#clientId=u28066876-fb3c-4&from=paste&height=761&id=ubd5cfa30&originHeight=951&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=152186&status=done&style=none&taskId=u744685ca-8958-4cf3-93f5-407118dea8f&title=&width=1536)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665803018743-c5e3f559-a033-43a8-931e-f8a687c0501e.png#clientId=u28066876-fb3c-4&from=paste&height=761&id=u16668dde&originHeight=951&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=116839&status=done&style=none&taskId=u24d626b5-cada-4770-b3da-66f6b65e664&title=&width=1536)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665803063761-4f05878a-a088-4800-b172-bc7b5326a8fc.png#clientId=u28066876-fb3c-4&from=paste&height=761&id=ub5b2ae2a&originHeight=951&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=95975&status=done&style=none&taskId=ue89016e5-66bf-4f75-8419-2b165ded3c8&title=&width=1536)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665803080344-314a7ac6-eef4-4167-b01a-c3c229af5f15.png#clientId=u28066876-fb3c-4&from=paste&height=761&id=ua4242eb7&originHeight=951&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=73987&status=done&style=none&taskId=u68b2a667-9438-478b-a321-d5401f5e1fd&title=&width=1536)
<a name="bFExf"></a>
### 现在DICOM图像获取方法
![image.png](https://cdn.nlark.com/yuque/0/2022/png/2869223/1665802012252-44d7696e-fa37-48da-8ec4-d83187481b0b.png#clientId=u28066876-fb3c-4&from=paste&height=666&id=u31347c9f&originHeight=832&originWidth=1671&originalType=binary&ratio=1&rotation=0&showTitle=false&size=187095&status=done&style=none&taskId=u80f7d9cb-680f-4e3a-aa5c-f6fdca3521f&title=&width=1336.8)










