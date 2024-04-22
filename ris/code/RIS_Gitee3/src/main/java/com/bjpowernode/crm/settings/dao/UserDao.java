package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public interface UserDao {


    User login(Map<String, String> map);

    List<User> getUserList();

    List<User> getByPrivileges(String privileges);

    String getIdByName(String id);

    String getNameByID(String id);

    List<User> getUserListByprivileges(String privileges);

    int editPwd(Map<String, String> map);

    int save(User user);

    int getTotalByCondition(Map<String, Object> map);

    List<User> getUserListByCondition(Map<String, Object> map);

    User detail(String id);

    int update(User user);

    int delete(String[] ids);

    int editPwdByAdmin(User user);

    void addFace(User user);
}
