package com.bjpowernode.crm.settings.web.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.bjpowernode.crm.enums.ErrorCodeEnum;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.FaceEngineService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.FaceEngineServiceImpl;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.vo.Results;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author 北京动力节点
 */
public class UserController extends HttpServlet {
    ResourceBundle rb = ResourceBundle.getBundle("arcsoft");
    public String sdkLibPath = rb.getString("arcface-sdk.sdk-lib-path");
    public String appId = rb.getString("arcface-sdk.app-id");
    public String sdkKey = rb.getString("arcface-sdk.sdk-key");
    public Integer threadPoolSize = Integer.valueOf(rb.getString("arcface-sdk.thread-pool-size"));
    private ExecutorService executorService;
    private GenericObjectPool<FaceEngine> faceEngineObjectPool;
    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        faceEngineObjectPool = new GenericObjectPool(new FaceEngineFactory(sdkLibPath, appId, sdkKey, engineConfiguration), poolConfig);//底层库算法对象池

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/faceLogin.do".equals(path)){
            faceLogin(request,response);
        }else if("/settings/user/editPwd.do".equals(path)){
            editPwd(request,response);
        }else if("/settings/user/addFace.do".equals(path)){
            addFace(request,response);
        }else if("/settings/user/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/settings/user/save.do".equals(path)){
            save(request,response);
        }else if("/settings/user/detail.do".equals(path)){
            detail(request,response);
        }else if("/settings/user/update.do".equals(path)){
            update(request,response);
        }else if("/settings/user/editPwdByAdmin.do".equals(path)){
            editPwdByAdmin(request,response);
        }else if("/settings/user/delete.do".equals(path)){
            delete(request,response);
        }else if("/settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void addFace(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到执行添加人脸信息操作");
        Map reqMap = FromDataUtil.getFilePath(request);
        String id = reqMap.get("id").toString();
        String image = reqMap.get("image").toString();

        byte[] decode = Base64.decode(Base64Util.base64Process(image));
        ImageInfo imageInfo = ImageFactory.getRGBData(decode);

        //人脸特征获取
        FaceEngineService faceEngineService = (FaceEngineService) ServiceFactory.getService(new FaceEngineServiceImpl());
        byte[] bytes = new byte[0];
        //faceEngineService.init();
        bytes = faceEngineService.extractFaceFeature(faceEngineObjectPool,imageInfo);

        if (bytes == null) {
            PrintJson.printJsonObj(response, Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED));
        }else{
            User user = new User();
            user.setId(id);
            user.setFaceFeature(bytes);
            UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
            us.addFace(user);
            PrintJson.printJsonObj(response, Results.newSuccessResult(""));
        }
    }

    private void faceLogin(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到执行添加人脸信息操作");
        Map reqMap = FromDataUtil.getFilePath(request);
        String image = reqMap.get("image").toString();
        byte[] decode = Base64.decode(Base64Util.base64Process(image));
        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);
        //人脸特征获取
        FaceEngineService faceEngineService = (FaceEngineService) ServiceFactory.getService(new FaceEngineServiceImpl());
        byte[] bytes = new byte[0];
        //faceEngineService.init();
        bytes = faceEngineService.extractFaceFeature(faceEngineObjectPool,imageInfo);
        if (bytes == null) {
            PrintJson.printJsonObj(response, Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED));
        }else{
            //人脸比对，获取比对结果
            List<User> userList = faceEngineService.compareFaceFeature(faceEngineObjectPool,executorService,bytes);

            if (CollectionUtil.isNotEmpty(userList)) {
                User user = userList.get(0);
                request.getSession().setAttribute("user", user);
                PrintJson.printJsonObj(response, Results.newSuccessResult(""));
            }else{
                PrintJson.printJsonObj(response, Results.newFailedResult(ErrorCodeEnum.FACE_DOES_NOT_MATCH));
            }
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行用户信息的删除操作");
        String ids[] = request.getParameterValues("id");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = us.delete(ids);
        PrintJson.printJsonFlag(response, flag);
    }

    private void editPwdByAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到管理员修改用户密码操作");
        String id = request.getParameter("PwdOfid");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //String confirmPwd = request.getParameter("confirmPwd");//前端已经验证了，确认的密码是和登录密码是一致的
        String editTime = DateTimeUtil.getSysTime();
        String editBy = request.getParameter("editPwdBy");
        User user = new User();
        user.setId(id);
        user.setLoginPwd(loginPwd);
        user.setEditTime(editTime);
        user.setEditBy(editBy);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = us.editPwdByAdmin(user);
        if(flag){
            //修改成功
            UserService us1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
            User u = us1.detail(id);
            request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
            request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到修改用户操作");
        String id = request.getParameter("id");
        String loginAct = request.getParameter("loginAct");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String lockState = request.getParameter("lockState");
        String lockState2Char;
        if("启用".equals(lockState)){
            //启用则状态是1
            lockState2Char = "1";
        }else if("锁定".equals(lockState)){
            //禁用则状态是0
            lockState2Char = "0";
        }else{
            //为空则为未填写
            lockState2Char = "";
        }
        String expireTime = request.getParameter("expireTime");
        String allowIps = request.getParameter("allowIps");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = request.getParameter("editBy");
        String privileges = request.getParameter("privileges");
        User user = new User();
        user.setId(id);
        user.setLoginAct(loginAct);
        user.setName(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setLockState(lockState2Char);
        user.setExpireTime(expireTime);
        user.setAllowIps(allowIps);
        user.setEditTime(editTime);
        user.setEditBy(editBy);
        user.setPrivileges(privileges);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = us.update(user);
        if(flag){
            //修改成功
            UserService us1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
            User u = us1.detail(id);
            request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
            request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到用户详细信息页的操作");
        String id = request.getParameter("id");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        User u = us.detail(id);
        request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
        request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表的操作【结合条件查询+分页查询】");
        String username = request.getParameter("username");
        String lockState = request.getParameter("lockState");
        String lockState2Char;
        if("启用".equals(lockState)){
            //启用则状态是1
            lockState2Char = "1";
        }else if("锁定".equals(lockState)){
            //禁用则状态是0
            lockState2Char = "0";
        }else{
            //为空则为页面展示
            lockState2Char = "";
        }
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", username);
        map.put("lockState2Char", lockState2Char);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        PaginationVO<User> vo = us.pageList(map);
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到添加用户操作");
        String id = UUIDUtil.getUUID();
        String loginAct = request.getParameter("loginAct");
        String username = request.getParameter("username");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //String confirmPwd = request.getParameter("confirmPwd");//前端已经验证了，确认的密码是和登录密码是一致的
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String lockState = request.getParameter("lockState");
        String lockState2Char;
        if("启用".equals(lockState)){
            //启用则状态是1
            lockState2Char = "1";
        }else if("锁定".equals(lockState)){
            //禁用则状态是0
            lockState2Char = "0";
        }else{
            //为空则为未填写
            lockState2Char = "";
        }
        String expireTime = request.getParameter("expireTime");
        String allowIps = request.getParameter("allowIps");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = request.getParameter("createBy");
        String privileges = request.getParameter("privileges");
        User user = new User();
        user.setId(id);
        user.setLoginAct(loginAct);
        user.setName(username);
        user.setLoginPwd(loginPwd);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setLockState(lockState2Char);
        user.setExpireTime(expireTime);
        user.setAllowIps(allowIps);
        user.setCreateTime(createTime);
        user.setCreateBy(createBy);
        user.setPrivileges(privileges);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = us.save(user);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/settings/qx/index.jsp");
        }
    }

    private void editPwd(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改密码操作");
        String loginAct = request.getParameter("loginAct");
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        oldPwd = MD5Util.getMD5(oldPwd);
        newPwd = MD5Util.getMD5(newPwd);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            us.editPwd(loginAct,oldPwd,newPwd);
            PrintJson.printJsonFlag(response, true);
        }catch(Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip:"+ip);
        //未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = us.login(loginAct,loginPwd,ip);
            //System.out.println("用户权限是"+user.getPrivileges());
            request.getSession().setAttribute("user", user);
            //System.out.println(user.getPrivileges());
            //如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            //表示登录成功
            /*
                {"success":true}
             */
            PrintJson.printJsonFlag(response, true);
        }catch(Exception e){
            e.printStackTrace();
            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*
                {"success":true,"msg":?}
             */
            String msg = e.getMessage();
            /*
                我们现在作为contoller，需要为ajax请求提供多项信息
                可以有两种手段来处理：
                    （1）将多项信息打包成为map，将map解析为json串
                    （2）创建一个Vo
                            private boolean success;
                            private String msg;
                    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                    如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了
             */
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}




































