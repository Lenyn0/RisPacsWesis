<a name="VeFLV"></a>
## 项目使用到的端口号记录
| 端口号 | 用途 |
| --- | --- |
| **3306** | **MySQL数据库使用** |
| **80** | **Web使用** |
| 389 | ldap使用 |
| 5432 | db使用 |
| 8080 | dcm4chee-arc-psql使用 |
| 8443 | dcm4chee-arc-psql使用 |
| 9990 | dcm4chee-arc-psql使用 |
| 9993 | dcm4chee-arc-psql使用 |
| 11112 | dcm4chee-arc-psql使用 |
| 2762 | dcm4chee-arc-psql使用 |
| 2575 | dcm4chee-arc-psql使用 |
| 12575 | dcm4chee-arc-psql使用 |
| 11113 | RIS系统使用 |
| 2576 | RIS系统使用 |

<a name="kxnj2"></a>
## PACS系统部署在CentOS上
<a name="PFjN5"></a>
### PACS项目部署参考地址2个
[https://blog.csdn.net/zssureqh/article/details/103323715?ops_request_misc=&request_id=&biz_id=102&utm_term=dcm4chee-arc-light-5.4.1-mysql&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-6-.pc_search_result_before_js&spm=1018.2226.3001.4187](https://blog.csdn.net/zssureqh/article/details/103323715?ops_request_misc=&request_id=&biz_id=102&utm_term=dcm4chee-arc-light-5.4.1-mysql&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-6-.pc_search_result_before_js&spm=1018.2226.3001.4187)
<a name="Rhzrc"></a>
### Use Docker Command Line
[https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host](https://github.com/dcm4che/dcm4chee-arc-light/wiki/Run-minimum-set-of-archive-services-on-a-single-host)
<a name="U8bOL"></a>
#### 1 Create an user-defined bridge network
> docker network create dcm4chee_network

<a name="trySt"></a>
#### 2 Start OpenLDAP Server
> docker run --network=dcm4chee_network --name ldap \
>            -p 389:389 \
>            -v /var/local/dcm4chee-arc/ldap:/var/lib/openldap/openldap-data \
>            -v /var/local/dcm4chee-arc/slapd.d:/etc/openldap/slapd.d \
>            -d dcm4che/slapd-dcm4chee:2.4.57-24.1

<a name="Fretk"></a>
#### 3 Start PostgreSQL Server
**CentOS系统一定会出现问题所以不运行下面的命令**
> docker run --network=dcm4chee_network --name db \
>            -p 5432:5432 \
>            -e POSTGRES_DB=pacsdb \
>            -e POSTGRES_USER=pacs \
>            -e POSTGRES_PASSWORD=pacs \
>            -v /etc/localtime:/etc/localtime:ro \
>            -v /etc/timezone:/etc/timezone:ro \
>            -v /var/local/dcm4chee-arc/db:/var/lib/postgresql/data \
>            -d dcm4che/postgres-dcm4chee:13.3-24

**可能出现的问题：**
> docker: Error response from daemon: OCI runtime create failed: container_linux.go:380: starting container process caused: process_linux.go:545: container init caused: rootfs_linux.go:76: mounting "/etc/timezone" to rootfs at "/etc/timezone" caused: mount through procfd: not a directory: unknown: Are you trying to mount a directory onto a file (or vice-versa)? Check if the specified host path exists and is the expected type.

**解决方法：**<br />[https://blog.csdn.net/qq_24084605/article/details/104632687?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link](https://blog.csdn.net/qq_24084605/article/details/104632687?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link)<br />**先进行时区设置**
> # 原因是centos7.6中/etc/timezone是一个文件夹,而不是一个文件,执行如下命令:
> echo 'Asia/Shanghai' > /etc/timezone/timezone

[https://blog.csdn.net/kq1983/article/details/89913861](https://blog.csdn.net/kq1983/article/details/89913861)<br />**修改后的命令**
> docker run --network=dcm4chee_network --name db \
>            -p 5432:5432 \
>            -e POSTGRES_DB=pacsdb \
>            -e POSTGRES_USER=pacs \
>            -e POSTGRES_PASSWORD=pacs \
>            -v /etc/localtime:/etc/localtime:ro \
>            -v /etc/timezone/timezone:/etc/timezone:ro \
>            -v /var/local/dcm4chee-arc/db:/var/lib/postgresql/data \
>            -d dcm4che/postgres-dcm4chee:13.3-24

<a name="omu9G"></a>
#### 4 Start Wildfly with deployed dcm4che Archive 5 application
> docker run --network=dcm4chee_network --name arc \
>            -p 8080:8080 \
>            -p 8443:8443 \
>            -p 9990:9990 \
>            -p 9993:9993 \
>            -p 11112:11112 \
>            -p 2762:2762 \
>            -p 2575:2575 \
>            -p 12575:12575 \
>            -e POSTGRES_DB=pacsdb \
>            -e POSTGRES_USER=pacs \
>            -e POSTGRES_PASSWORD=pacs \
>            -e WILDFLY_WAIT_FOR="ldap:389 db:5432" \
>            -v /etc/localtime:/etc/localtime:ro \
>            -v /etc/timezone/timezone:/etc/timezone:ro \
>            -v /var/local/dcm4chee-arc/wildfly:/opt/wildfly/standalone \
>            -d dcm4che/dcm4chee-arc-psql:5.24.1

**可能出现的错误**
> docker: Error response from daemon: OCI runtime create failed: container_linux.go:380: starting container process caused: process_linux.go:545: container init caused: rootfs_linux.go:76: mounting "/etc/timezone/timezone" to rootfs at "/etc/timezone" caused: stat /etc/timezone/timezone: not a directory: unknown: Are you trying to mount a directory onto a file (or vice-versa)? Check if the specified host path exists and is the expected type.

**错误后使用的命令**
> docker run --network=dcm4chee_network --name arc \
>            -p 8080:8080 \
>            -p 8443:8443 \
>            -p 9990:9990 \
>            -p 9993:9993 \
>            -p 11112:11112 \
>            -p 2762:2762 \
>            -p 2575:2575 \
>            -p 12575:12575 \
>            -e POSTGRES_DB=pacsdb \
>            -e POSTGRES_USER=pacs \
>            -e POSTGRES_PASSWORD=pacs \
>            -e WILDFLY_WAIT_FOR="ldap:389 db:5432" \
>            -v /etc/localtime:/etc/localtime:ro \
>            -v /etc/timezone:/etc/timezone:ro \
>            -v /var/local/dcm4chee-arc/wildfly:/opt/wildfly/standalone \
>            -d dcm4che/dcm4chee-arc-psql:5.24.1

> 为了防止删除容器后存储的数据保存在宿主机上，做一下操作
> $ sudo -i //永久切换到root账户
> # mkdir -p /var/local/dcm4chee-arc/storage
> # chown 1023:1023 /var/local/dcm4chee-arc/storage
> # exit //退出root账户

<a name="Gl8XP"></a>
#### 5 (Optional) Send CT images to the Archive using dcm4che/dcm4che-tools
> docker run --rm --network=dcm4chee_network dcm4che/dcm4che-tools storescu -cDCM4CHEE@arc:11112 /opt/dcm4che/etc/testdata/dicom

<a name="fW8my"></a>
#### 6 web进行查看
The received images should show up in the UI of the Archive at [http://39.101.179.169:8080/dcm4chee-arc/ui2](http://39.101.179.169/) or [https://39.101.179.169:8443/dcm4chee-arc/ui2](https://localhost:8443/dcm4chee-arc/ui2:)
<a name="hSBMD"></a>
#### 7 其他
You may stop all 3 containers by:<br />**$ docker stop ldap db arc**

and start all 3 containers again by:<br />**$ docker start ldap db arc**

You may delete the stopped containers by<br />**$ docker rm -v ldap db arc**

You may delete the created bridge network by<br />**$ docker network rm dcm4chee_network**

<a name="zGr2E"></a>
#### 删除镜像
docker rmi : 删除本地一个或多个镜像。
> 强制删除镜像unoob/ubuntu:v4
> docker rmi -f runoob/ubuntu:v4
> docker rmi -f

<a name="nbJvR"></a>
#### 查看所有镜像
> docker images

<a name="UeTwF"></a>
#### 查看正在运行容器
> docker ps

<a name="oPl3t"></a>
#### 停止正在运行容器
> docker stop ldap db arc

<a name="RqrXZ"></a>
#### 删除容器
> docker rm -v ldap db arc

<a name="APkrI"></a>
#### 启动容器
> docker start ldap db arc

<a name="o8hYP"></a>
### docker安装使用的仓库地址
使用的是官方的地址，是国外的如果速度比较慢就需要换成阿里镜像
<a name="e8urY"></a>
### docker安装和使用
[https://www.bilibili.com/video/BV1og4y1q7M4?spm_id_from=333.999.0.0](https://www.bilibili.com/video/BV1og4y1q7M4?spm_id_from=333.999.0.0)
<a name="uLSMC"></a>
#### 文档地址
[https://docs.docker.com/engine/install/centos/](https://docs.docker.com/engine/install/centos/)
<a name="mYSkP"></a>
#### 查看已存在的镜像
> docker images

<a name="r0IBA"></a>
#### 删除镜像
> docker rmi 镜像id

<a name="AH0ZO"></a>
#### 查看所有的容器
> docker ps -a

<a name="Vndl6"></a>
#### 查看正在运行的容器
> docker ps

<a name="SWQQ2"></a>
#### 停止三个容器
> docker stop ldap db arc

**启动docker**
> docker启动       
> systemctl start docker【CentOS 7】
> service docker start【CentOS 6】
> 
> 重启docker服务
> systemctl restart  docker
> sudo service docker restart
> 
> 关闭docker    
> systemctl stop docker
> service docker stop
> 
> 查看是否启动成功
> docker ps -a

<a name="gXRsa"></a>
#### 启动三个容器
> docker start ldap db arc

<a name="jNVNl"></a>
#### 删除容器
> docker rm -v ldap db arc

> docker rm 4b94b2071f58

<a name="lZ7MY"></a>
## RIS系统部署在CentOS上
**工具、环境安装文档**<br />链接：[https://pan.baidu.com/s/1YtFrldYbx-_OJnoEJUKyzQ](https://pan.baidu.com/s/1YtFrldYbx-_OJnoEJUKyzQ) <br />提取码：ty6u <br />--来自百度网盘超级会员V4的分享
<a name="ztsoY"></a>
### 1 安装jdk、配置环境变量
[https://www.bilibili.com/video/BV1fT4y1E7a6?p=176](https://www.bilibili.com/video/BV1fT4y1E7a6?p=176)
> vim /ect/profile
> #添加下面的三行配置信息
> export JAVA_HOME=/usr/local/jdk1.8.0_121
> export PATH=$JAVA_HOME/bin:$PATH
> export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/jre/lib/rt.jar
> #添加过配置信息，更新配置文件
> source /etc/profile

<a name="dfQVm"></a>
### 2 安装tomcat
[https://www.bilibili.com/video/BV1fT4y1E7a6?p=178](https://www.bilibili.com/video/BV1fT4y1E7a6?p=178)
<a name="LKT7M"></a>
### 3 搭建MySQL环境
[https://www.bilibili.com/video/BV1fT4y1E7a6?p=179](https://www.bilibili.com/video/BV1fT4y1E7a6?p=179)<br />进入mysql<br />**启动mysql**
> ./mysqld_safe &

**停止mysql**
> ./mysqladmin -uroot  -p  shutdown
> enter password：输入密码关闭13445923

**查看mysql进程，已经没有mysqld_safe**
> ps -ef | grep msyql

![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1635565079198-221ad3dd-7706-4582-baab-ef522d18bbb7.png#clientId=u266ea6a7-a23e-4&from=paste&id=u3181970e&originHeight=66&originWidth=574&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14607&status=done&style=none&taskId=u0bd7c6bf-39b1-4fa2-8321-8e57d55b33b&title=)<br />**防火墙【连接不到数据库就是防火墙的问题】**<br />连接错误：可能是Linux的防火墙起作用。可以将防火墙先关闭<br />操作防火墙的命令：<br />查看防火墙状态：systemctl status firewalld<br />让防火墙可用：systemctl enable firewalld<br />让防火墙不可用：systemctl disable firewalld<br />开启防火墙：systemctl start firewalld<br />禁用防火墙：systemctl stop firewalld<br />**防火墙开放端口：（开放端口后需重载防火墙）**
> [root@WSS bin]# firewall-cmd --zone=public --add-port=9999/tcp --permanent
> success
> [root@WSS bin]# firewall-cmd --reload
> success
> [root@WSS bin]#
> 命令含义：
> –zone #作用域
> –add-port=80/tcp #添加端口，格式为：端口/通讯协议
> –permanent #永久生效，没有此参数重启后失效
> firewall-cmd --reload 并不中断用户连接，即不丢失状态信息


<a name="h69Wd"></a>
### 4 项目部署
[https://www.bilibili.com/video/BV1fT4y1E7a6?p=182](https://www.bilibili.com/video/BV1fT4y1E7a6?p=182)<br />可能遇到的问题
> 项目移到linux环境下时tomcat报错 java.util.zip.ZipException: invalid END header
> [https://ask.csdn.net/questions/738046](https://ask.csdn.net/questions/738046)
> 从windows传输到linux系统传输方式有问题：若是FTP,改成bin模式试试?

<a name="JLC76"></a>
### 5 项目启动和停止
<a name="jzE1t"></a>
#### tomcat后台启动
> ./startup.sh

<a name="PpymE"></a>
#### 停止
> ./shutdown.sh

<a name="Z1H8E"></a>
#### tomcat前台启动；查看日志使用
> ./catalina.sh run
> [http://1.116.91.38:8081/crm-1.0-SNAPSHOT](http://1.116.91.38:8081/crm-1.0-SNAPSHOT)

<a name="W80yG"></a>
#### 查看端口占用
> netstat -tunlp | grep 端口号

<a name="qJUEl"></a>
#### 杀死进程
> kill -9 1777[进程号]

<a name="gd7QW"></a>
### 6 其他
<a name="pHYN2"></a>
#### tomcat更改启动端口
[https://www.cnblogs.com/wwho/p/14331482.html](https://www.cnblogs.com/wwho/p/14331482.html)<br />更改了tomcat端口为8081、dicom:11112端口位11113、hl7:2575端口2576
> [root@localhost apache-tomcat-8.5.41]# cd conf/
> [root@localhost conf]# pwd
> /usr/local/tomcat/apache-tomcat-8.5.41/conf
> [root@localhost conf]# vi server.xml #把8080改为8081
> 更新资源
> 

<a name="jY6iS"></a>
#### mysql数据库乱码问题
需要在linux中配置一下字符集文件my.cnf，该文件可能不存在需要创建/etc/my.cnf
> vim /etc/my.cnf

添加如下字段
> [mysqld]
> character-set-server=utf8
> init_connect='SET NAMES utf8'

关闭mysql服务
> #./mysqladmin -uroot  -p  shutdown
> #检查关闭情况
> #ps -ef | grep msyql
> 密码 13445923
> root       497 30998  0 21:53 pts/2    00:00:00 grep --color=auto msyql
> !!! 注意这个不是，这个是root进程不要杀死
> 如果还有进行就杀死进程
> #kill -9 30998

再打开mysql服务
> ./mysqld_safe &

配置之后关闭mysql服务，然后再次打开<br />[https://www.cnblogs.com/bayolante/p/13410471.html](https://www.cnblogs.com/bayolante/p/13410471.html)<br />[https://blog.csdn.net/zhaoliwen/article/details/107188506](https://blog.csdn.net/zhaoliwen/article/details/107188506)

**人脸识别问题视频无法使用的问题**<br />整合trtc遇到的坑：＜ERROR＞ navigator.mediaDevices is undefined<br />[https://blog.csdn.net/qq_41973208/article/details/107950274](https://blog.csdn.net/qq_41973208/article/details/107950274)<br />[https://www.baidu.com/s?ie=UTF-8&wd=TypeError%3A%20navigator.mediaDevices%20is%20undefined](https://www.baidu.com/s?ie=UTF-8&wd=TypeError%3A%20navigator.mediaDevices%20is%20undefined)
<a name="ogdVa"></a>
## 配置DICOMWeb
配置了DICOMWeb这样才能够在web端查看DICOM影像<br />[https://nroduit.github.io/en/getting-started/dcm4chee/](https://nroduit.github.io/en/getting-started/dcm4chee/)
<a name="EXQVL"></a>
### weasis-pacs-connector下载
[https://sourceforge.net/projects/dcm4che/files/Weasis/weasis-pacs-connector/7.1.2/](https://sourceforge.net/projects/dcm4che/files/Weasis/weasis-pacs-connector/7.1.2/)
<a name="Dko2J"></a>
### 配置WildFly
**需要在linux中配置WildFly**，然后才能够进入后台进行控制<br />后台地址：
> [https://39.101.179.169:9993/](https://10.128.217.225:9993/)
> 账号：ManagementRealm
> 密码：root


**配置方式：**<br />以进入容器的方式运行，然后在bin中执行./add-user.sh<br />进入容器运行的命令：
> docker exec -it 容器id号 /bin/bash
> 8accfaaa2926
> docker exec -it 8accfaaa2926 /bin/bash

![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1632365293895-54467d5c-1a25-43b8-bf28-deeefa722081.png#clientId=u63612a97-ee99-4&from=paste&height=306&id=u19a7d5b4&originHeight=612&originWidth=1054&originalType=binary&ratio=1&rotation=0&showTitle=false&size=108220&status=done&style=none&taskId=ufa7fe9a1-75df-4589-84b1-8dfa64b313a&title=&width=527)
```
root@2a6832121f0a:/# add-user.sh 

What type of user do you wish to add? 
 a) Management User (mgmt-users.properties) 
 b) Application User (application-users.properties)
(a): a

Enter the details of the new user to add.
Using realm 'ManagementRealm' as discovered from the existing property files.
Username : ManagementRealm
Password recommendations are listed below. To modify these restrictions edit the add-user.properties configuration file.
 - The password should be different from the username
 - The password should not be one of the following restricted values {root, admin, administrator}
 - The password should contain at least 8 characters, 1 alphabetic character(s), 1 digit(s), 1 non-alphanumeric symbol(s)
Password : 
WFLYDM0097: Password should not be equal to 'root', this value is restricted.
Are you sure you want to use the password entered yes/no? y
Re-enter Password : 
What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[  ]: 
About to add user 'ManagementRealm' for realm 'ManagementRealm'
Is this correct yes/no? y
Added user 'ManagementRealm' to file '/opt/wildfly/standalone/configuration/mgmt-users.properties'
Added user 'ManagementRealm' to file '/opt/wildfly/domain/configuration/mgmt-users.properties'
Added user 'ManagementRealm' with groups  to file '/opt/wildfly/standalone/configuration/mgmt-groups.properties'
Added user 'ManagementRealm' with groups  to file '/opt/wildfly/domain/configuration/mgmt-groups.properties'
Is this new user going to be used for one AS process to connect to another AS process? 
e.g. for a slave host controller connecting to the master or for a Remoting connection for server to server Jakarta Enterprise Beans calls.
yes/no? y
To represent the user add the following to the server-identities definition <secret value="cm9vdA==" />
root@2a6832121f0a:/# 
```
**线上第二次配置的log日志**
```
[root@iZ8vb8c2ok2gb1tejtqd9pZ ~]# docker exec -it 8accfaaa2926 /bin/bash
root@8accfaaa2926:/# add-user.sh 

What type of user do you wish to add? 
 a) Management User (mgmt-users.properties) 
 b) Application User (application-users.properties)
(a): a

Enter the details of the new user to add.
Using realm 'ManagementRealm' as discovered from the existing property files.
Username : ManagementRealm
Password recommendations are listed below. To modify these restrictions edit the add-user.properties configuration file.
 - The password should be different from the username
 - The password should not be one of the following restricted values {root, admin, administrator}
 - The password should contain at least 8 characters, 1 alphabetic character(s), 1 digit(s), 1 non-alphanumeric symbol(s)
Password : 
WFLYDM0097: Password should not be equal to 'root', this value is restricted.
Are you sure you want to use the password entered yes/no? y
Re-enter Password : 
What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[  ]: 
About to add user 'ManagementRealm' for realm 'ManagementRealm'
Is this correct yes/no? y
Added user 'ManagementRealm' to file '/opt/wildfly/standalone/configuration/mgmt-users.properties'
Added user 'ManagementRealm' to file '/opt/wildfly/domain/configuration/mgmt-users.properties'
Added user 'ManagementRealm' with groups  to file '/opt/wildfly/standalone/configuration/mgmt-groups.properties'
Added user 'ManagementRealm' with groups  to file '/opt/wildfly/domain/configuration/mgmt-groups.properties'
Is this new user going to be used for one AS process to connect to another AS process? 
e.g. for a slave host controller connecting to the master or for a Remoting connection for server to server Jakarta Enterprise Beans calls.
yes/no? y
To represent the user add the following to the server-identities definition <secret value="cm9vdA==" />
```
<a name="OM3Vj"></a>
### 配置dcm4chee-arc-ui
> IID_STUDY_URL=../../weasis-pacs-connector/weasis?studyUID={{studyUID}}&cdb&access_token={{access_token}}
> IID_URL_TARGET=_self
> IID_PATIENT_URL=../../weasis-pacs-connector/weasis?patientID={{patientID}}&cdb&access_token={{access_token}}

![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1632795647619-189e90cf-9ca4-402d-a31f-793e06033d6d.png#clientId=u9881eb6c-9f7b-4&from=paste&height=521&id=u7f8be49f&originHeight=1042&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=860946&status=done&style=none&taskId=u0152567c-a175-43d2-b74a-4a977eaa025&title=&width=960)
<a name="kszTV"></a>
## CentOS设置桥接模式联网
桥接模式联网能够使其他局域网内的主机连接到虚拟机
<a name="rTz6a"></a>
### 1 虚拟网络编辑器设置桥接网卡
[https://blog.csdn.net/lelelemenglele/article/details/109814665](https://blog.csdn.net/lelelemenglele/article/details/109814665)
<a name="c1y5R"></a>
### 2 虚拟机内部编辑ifcfg-eth33网卡配置文件
**编辑命令**
> sudo -i
> vim /etc/sysconfig/network-scripts/ifcfg-ens33
> vi /etc/sysconfig/network-scripts/ifcfg-ens33
> #rm /etc/sysconfig/network-scripts/ifcfg-eth33
> cd /etc/sysconfig/network-scripts/
> cp /etc/sysconfig/network-scripts/ifcfg-eth33 /etc/sysconfig/network-scripts/ifcfg-eth33.backup

**需要修改的字段**
> # 设备工作模式 dhcp|static|none 动态|静态|静态
> BOOTPROTO=static
> # 设置系统启动时是否激活网卡
> ONBOOT=yes
> # 设置静态的ip地址
> IPADDR=10.128.217.225
> # 网关 与 pc 中的ipv4 网关相同
> GATEWAY=10.128.192.1
> # dns 地址 与pc中的ipv4 dns相同
> DNS1=10.3.9.45
> # 子网掩码 与pc中的ipv4子网掩码相同
> NETMASK=255.255.192.0

[https://www.cnblogs.com/dearjohn/p/15032616.html](https://www.cnblogs.com/dearjohn/p/15032616.html)<br />[https://blog.csdn.net/kai3123919064/article/details/109567876](https://blog.csdn.net/kai3123919064/article/details/109567876)

编辑项目有4个

1. ip地址
2. 子网掩码
3. DNS服务器
4. 网关
<a name="BePqc"></a>
### 3 重启网卡
**命令**
> service network restart #这个命令基本成功不了


可能遇到的问题
> centOS 7下无法启动网络（service network start）错误解决办法（应该是最全的了。。。）
> [https://blog.csdn.net/weiyongle1996/article/details/75128239](https://blog.csdn.net/weiyongle1996/article/details/75128239)
> 1.和 NetworkManager 服务有冲突，这个好解决，直接关闭 NetworkManger 服务就好了，** service NetworkManager stop**，并且禁止开机启动 **chkconfig NetworkManager off** 。之后重启就好了。

<a name="ECIx4"></a>
## 注意事项

1. 关闭CentOS防火墙，不然数据库连接不到
2. 在数据库中添加登录端的允许ip
3. 如果启动项目失败
> 查看tomcat日志
> Linux下启动Tomcat启动并显示控制台日志信息
> [https://www.cnblogs.com/jtlgb/p/6029374.html](https://www.cnblogs.com/jtlgb/p/6029374.html)

<a name="wM7vO"></a>
## 关闭了防火墙依然是ping不通问题
[https://blog.csdn.net/qq_39497869/article/details/106471818](https://blog.csdn.net/qq_39497869/article/details/106471818)
> route add 10.128.239.230 10.128.192.1 -p
> route add 要访问的主机ip 你本机默认的网关 -p”

<a name="cCW3Q"></a>
## 开启虚拟化技术
![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1632733791148-25804775-b3ac-4f85-9d2d-190dae8064c9.png#clientId=ufa9f008d-4ef3-4&from=paste&height=243&id=u98489b10&originHeight=486&originWidth=880&originalType=binary&ratio=1&rotation=0&showTitle=false&size=491477&status=done&style=none&taskId=u82d4162c-0103-4696-a257-cc9063a76eb&title=&width=440)
<a name="onPcV"></a>
## linux查看资源使用情况命令
Linux查看内存和cpu利用率的命令（亲测有效）<br />[https://blog.csdn.net/qq_32670879/article/details/85259916](https://blog.csdn.net/qq_32670879/article/details/85259916)
> top

<a name="NYegV"></a>
## 云服务器
![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1632796266400-6e2610fb-5bb7-4503-8b27-051156a5d818.png#clientId=u9881eb6c-9f7b-4&from=paste&height=123&id=u3950ce9e&originHeight=246&originWidth=1623&originalType=binary&ratio=1&rotation=0&showTitle=false&size=35942&status=done&style=none&taskId=uc0004e58-1f51-4931-b232-a10befaef8c&title=&width=811.5)<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1632796520445-cefdeef9-d172-4d31-a2eb-69429a81827b.png#clientId=u9881eb6c-9f7b-4&from=paste&height=603&id=u05537769&originHeight=803&originWidth=737&originalType=binary&ratio=1&rotation=0&showTitle=false&size=41337&status=done&style=none&taskId=u30fdb0e7-dc63-4e4d-8276-6a0dbe26048&title=&width=553)
<a name="XToLM"></a>
## 发送图像命令
storescu -c DCM4CHEE@39.101.179.169:11112 D:\归档资料\DCMTK库相关资料\images2\series-000001

<a name="npyYL"></a>
## 图像后台查看网址
 [http://1.116.91.38:8080/dcm4chee-arc/ui2](http://localhost:8080/dcm4chee-arc/ui2)
<a name="iyY6K"></a>
## 检查HL7功能
> hl7snd -c 39.101.179.169:2576 message.hl7
> hl7snd -c localhost:2576 message.hl7

<a name="Ey6HL"></a>
## 检查worklist功能
> findscu -c DCMQRSCP@39.101.179.169:11113 -- .\mwl.xml
> findscu -c DCMQRSCP@localhost:11113 -- .\mwl.xml
> findscu -c DCMQRSCP@10.128.239.230:11113 -m StudyInstanceUID=1.113654.3.13.1026











