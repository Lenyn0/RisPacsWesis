package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.EditPwdException;
import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();

    List<User> getUserListByprivileges(String privileges);

    String getIdByName(String userName);

    List<User> getByPrivileges(String privileges);

    void editPwd(String loginAct, String oldPwd, String newPwd)throws EditPwdException;

    boolean save(User user);

    PaginationVO<User> pageList(Map<String, Object> map);

    User detail(String id);

    boolean update(User user);

    boolean delete(String[] ids);

    boolean editPwdByAdmin(User user);

    String getNameByID(String id);

    void addFace(User users);
}
