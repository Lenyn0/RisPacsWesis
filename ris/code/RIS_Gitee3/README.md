#目前实现登记、签到、检查、报告四大模块

#人脸识别,使用虹软科技的人脸识别SDK【网址：https://ai.arcsoft.com.cn/product/arcface.html】
SDK选择：
    平台-->Window X64
    版本-->V3.0
    语言-->Java
记住APP_ID和SDK_KEY激活要用
解压SDK，将里面的Jar包拷贝至项目的lib目录下，修改arcsoft.properties文件arcface-sdk.sdk-lib-path、arcface-sdk.app-id、arcface-sdk.sdk-key
之后在 项目结构->库 中 新建项目库（小➕）->Java->RIS->lib->arcsoft-sdk-face-3.0.0.0.jar,确定后，应用。

