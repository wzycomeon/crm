package com.bjcomeon.crm.settings.web.controller;

import com.bjcomeon.crm.settings.domain.User;
import com.bjcomeon.crm.settings.service.Impl.UserServiceImpl;
import com.bjcomeon.crm.settings.service.UserService;
import com.bjcomeon.crm.utils.MD5Util;
import com.bjcomeon.crm.utils.PrintJson;
import com.bjcomeon.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DicController extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("欢迎进入数据字典数据控制器");

        String path = request.getServletPath();
        if ("/settings/user/xxx.do".equals(path)){
            //login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }

    }




}
