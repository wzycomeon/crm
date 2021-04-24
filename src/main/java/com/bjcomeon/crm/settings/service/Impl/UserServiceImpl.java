package com.bjcomeon.crm.settings.service.Impl;

import com.bjcomeon.crm.exception.LoginException;
import com.bjcomeon.crm.settings.dao.UserDao;
import com.bjcomeon.crm.settings.domain.User;
import com.bjcomeon.crm.settings.service.UserService;
import com.bjcomeon.crm.utils.DateTimeUtil;
import com.bjcomeon.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {
       private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

       public User login(String loginAct, String loginPwd, String ip) throws LoginException {

              Map<String,String> map = new HashMap<String, String>();
              map.put("loginAct",loginAct);
              map.put("loginPwd",loginPwd);

              User user = userDao.login(map);
              if (user == null){
                     throw new LoginException("账号密码错误，请重新输入！");
              }
              //如果程序能够成功的执行到该行，说明账号密码正确
              //需要继续向下验证其他3项信息
              //验证失效时间
              String expireTime = user.getExpireTime();
              String currentTime = DateTimeUtil.getSysTime();
              if (expireTime.compareTo(currentTime)<0){
                     throw new LoginException("账号已失效，请联系管理员！");
              }
              //判断锁定状态
              String lockState = user.getLockState();
              if ("0".equals(lockState)){
                     throw new LoginException("账号已锁死，请联系管理员！");
              }
              //判断ip地址
              String allowIps = user.getAllowIps();
              if (!allowIps.contains(ip)){
                     throw new LoginException("IP地址受限，请联系管理员！");
              }

              return user;
       }

       public List<User> getUerList() {

              List<User> uList = userDao.getUserList();

              return uList;
       }

       public Boolean editpwd(String id, String oldPwd, String newPwd, String confirmPwd) throws LoginException {

              boolean flag = true;
              Map<String,String> map = new HashMap<String, String>();
              map.put("id",id);
              map.put("loginPwd",oldPwd);
              map.put("newPwd",newPwd);

              int count = userDao.editpwd(map);
              if (count != 1){
                     flag = false;
                     throw new LoginException("原密码错误，请重新输入！");
              }

              return flag;
       }
}
