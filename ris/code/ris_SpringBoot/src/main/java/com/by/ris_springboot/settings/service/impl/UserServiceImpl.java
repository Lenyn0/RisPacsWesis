package com.by.ris_springboot.settings.service.impl;



import com.by.ris_springboot.exception.EditPwdException;
import com.by.ris_springboot.exception.LoginException;
import com.by.ris_springboot.settings.dao.UserDao;
import com.by.ris_springboot.settings.domain.User;
import com.by.ris_springboot.settings.service.UserService;
import com.by.ris_springboot.utils.DateTimeUtil;
import com.by.ris_springboot.utils.MD5Util;
import com.by.ris_springboot.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public User login(User user, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", user.getLoginAct());
        map.put("loginPwd", MD5Util.getMD5(user.getLoginPwd()));
        user = userDao.login(map);
        if(user==null){
            throw new LoginException("账号密码错误");
        }
        //如果程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下验证其他3项信息
        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        //如果是root账户不用检查下面各项，账号密码正确直接通过
        if(user.getLoginAct().equals("root")){
            return user;
        }
        //判断账号是否失效
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        //判断ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;
    }

    public void editPwd(String loginAct, String oldPwd, String newPwd) throws EditPwdException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", oldPwd);
        User user = userDao.login(map);
        if(user==null){
            throw new EditPwdException("原密码错误");
        }
        //如果程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下执行
        //验证失效时间
        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("id", user.getId());
        map1.put("newPwd", newPwd);
        int flag = userDao.editPwd(map1);
        if(flag==0){
            throw new EditPwdException("修改密码失败");
        }
    }

    public PaginationVO<User> pageList(Map<String, Object> map) {
        int total = userDao.getTotalByCondition(map);
        List<User> userList = userDao.getUserListByCondition(map);
        PaginationVO<User> vo = new PaginationVO<User>();
        vo.setTotal(total);
        vo.setDataList(userList);
        return vo;
    }

    public boolean save(User user) {
        boolean flag = true;
        int count = userDao.save(user);
        if(count!=1){
            flag=false;
        }
        return flag;
    }



    public List<User> getUserList() {
        List<User> uList = userDao.getUserList();
        return uList;
    }

    public List<User> getUserListByprivileges(String privileges) {
        List<User> uList = userDao.getUserListByprivileges(privileges);
        return uList;
    }

    public String getIdByName(String id) {
        String userId = userDao.getIdByName(id);
        System.out.println(userId);
        return userId;
    }

    public List<User> getByPrivileges(String privileges) {
        List<User> uList = userDao.getByPrivileges(privileges);
        System.out.println(uList);
        return uList;
    }

    public User detail(String id) {
        User user = userDao.detail(id);
        String lockState2Char;
        if("1".equals(user.getLockState())){
            lockState2Char = "启用";
        }else if("0".equals(user.getLockState())){
            lockState2Char = "锁定";
        }else{
            lockState2Char = "";
        }
        user.setLockState(lockState2Char);
        String privileges = "";
        if(!("".equals(user.getPrivileges()))){
            if(user.getPrivileges().contains("1")){
                privileges += "登记员,";
            }
            if(user.getPrivileges().contains("2")){
                privileges += "检查技师,";
            }
            if(user.getPrivileges().contains("3")){
                privileges += "报告医生,";
            }
            if(user.getPrivileges().contains("4")){
                privileges += "报告审核医生,";
            }
            if(user.getPrivileges().contains("6")){
                privileges += "科室主任,";
            }if(user.getPrivileges().contains("5")){
                privileges += "系统管理员,";
            }
            privileges = privileges.substring(0,privileges.length()-1);
        }
        user.setPrivileges(privileges);
        return user;
    }

    public boolean update(User user) {
        boolean flag = true;
        int count = userDao.update(user);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean editPwdByAdmin(User user) {
        boolean flag = true;
        int count = userDao.editPwdByAdmin(user);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public String getNameByID(String id) {
        String Name = userDao.getNameByID(id);
        return Name;
    }


    public boolean delete(String[] ids) {
        boolean flag = true;
        int count = userDao.delete(ids);
        if(count!=ids.length){
            flag = false;
        }
        return flag;
    }

}




































