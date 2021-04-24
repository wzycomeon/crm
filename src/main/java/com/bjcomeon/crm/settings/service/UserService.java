package com.bjcomeon.crm.settings.service;

import com.bjcomeon.crm.exception.LoginException;
import com.bjcomeon.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUerList();

}
