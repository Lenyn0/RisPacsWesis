package com.by.ris_springboot.settings.controller;

import com.by.ris_springboot.settings.domain.User;
import com.by.ris_springboot.settings.service.UserService;
import com.by.ris_springboot.utils.DateTimeUtil;
import com.by.ris_springboot.utils.MD5Util;
import com.by.ris_springboot.utils.UUIDUtil;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("settings/user/")
public class UserController{
    @Autowired
    UserService userService;

    @GetMapping("pageList.do")
    private Result pageList(HttpServletRequest request, HttpServletResponse response) {
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
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        PaginationVO<User> vo = userService.pageList(map);
        return Results.newSuccessResult(vo);
        //PrintJson.printJsonObj(response, vo);
    }

    @PostMapping("login.do")
    public Result login(User user, HttpServletRequest request) {
        try {
            //接收浏览器端的ip地址
            String ip = request.getRemoteAddr();
            //System.out.println("ip:"+ip);
            user=userService.login(user, ip);
            request.getSession().setAttribute("user", user);
            return Results.newSuccessResult(null);
        }catch (Exception e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
            return Results.newFailedResult(e.getMessage());
        }
    }


    @PostMapping("editPwd.do")
    private Result editPwd(String loginAct, String oldPwd, String newPwd) {
        System.out.println("进入到修改密码操作");
//        String loginAct = request.getParameter("loginAct");
//        String oldPwd = request.getParameter("oldPwd");
//        String newPwd = request.getParameter("newPwd");
        oldPwd = MD5Util.getMD5(oldPwd);
        newPwd = MD5Util.getMD5(newPwd);
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            userService.editPwd(loginAct,oldPwd,newPwd);
            return Results.newSuccessResult(null);
            //PrintJson.printJsonFlag(response, true);
        }catch(Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            return Results.newFailedResult(msg);
        }
    }

    @PostMapping("delete.do")
    private Result delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行用户信息的删除操作");
        String ids[] = request.getParameterValues("id");
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.delete(ids);

        return Results.newSuccessResult(null);
        //PrintJson.printJsonFlag(response, flag);
    }

    @PostMapping("editPwdByAdmin.do")
    private void editPwdByAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.editPwdByAdmin(user);
        if(flag){
            //修改成功
            //UserService us1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
            User u = userService.detail(id);
            request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
            request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
        }
    }

    @PostMapping("update.do")
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
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.update(user);
        if(flag){
            //修改成功
            //UserService us1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
            User u = userService.detail(id);
            request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
            request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
        }
    }

    @GetMapping("detail.do")
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到用户详细信息页的操作");
        String id = request.getParameter("id");
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        User u = userService.detail(id);
        request.setAttribute("u", u);//此处不取名字为user是为了防止和session域中的user重名
        request.getRequestDispatcher("/settings/qx/detail.jsp").forward(request, response);
    }


    @PostMapping("save.do")
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
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.save(user);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/settings/qx/index.jsp");
        }
    }


}




































