package com.by.ris_springboot.settings.dao;



import com.by.ris_springboot.settings.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
