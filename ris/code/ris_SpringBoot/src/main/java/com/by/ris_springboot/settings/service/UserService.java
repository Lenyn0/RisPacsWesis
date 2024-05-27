package com.by.ris_springboot.settings.service;


import com.by.ris_springboot.exception.EditPwdException;
import com.by.ris_springboot.exception.LoginException;
import com.by.ris_springboot.settings.domain.User;
import com.by.ris_springboot.vo.PaginationVO;

import java.util.List;
import java.util.Map;


public interface UserService {

    User login(User user, String ip) throws LoginException;

    void editPwd(String loginAct, String oldPwd, String newPwd)throws EditPwdException;

    PaginationVO<User> pageList(Map<String, Object> map);



    List<User> getUserList();

    List<User> getUserListByprivileges(String privileges);

    String getIdByName(String userName);

    List<User> getByPrivileges(String privileges);

    boolean save(User user);

    User detail(String id);

    boolean update(User user);

    boolean delete(String[] ids);

    boolean editPwdByAdmin(User user);

    String getNameByID(String id);

}
