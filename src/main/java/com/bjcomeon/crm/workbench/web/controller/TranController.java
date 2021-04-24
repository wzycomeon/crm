package com.bjcomeon.crm.workbench.web.controller;

import com.bjcomeon.crm.settings.domain.User;
import com.bjcomeon.crm.settings.service.Impl.UserServiceImpl;
import com.bjcomeon.crm.settings.service.UserService;
import com.bjcomeon.crm.utils.DateTimeUtil;
import com.bjcomeon.crm.utils.PrintJson;
import com.bjcomeon.crm.utils.ServiceFactory;
import com.bjcomeon.crm.utils.UUIDUtil;
import com.bjcomeon.crm.workbench.domain.Tran;
import com.bjcomeon.crm.workbench.domain.TranHistory;
import com.bjcomeon.crm.workbench.service.CustomerService;
import com.bjcomeon.crm.workbench.service.Impl.ClueServiceImpl;
import com.bjcomeon.crm.workbench.service.Impl.CustomerServiceImpl;
import com.bjcomeon.crm.workbench.service.Impl.TranServiceImpl;
import com.bjcomeon.crm.workbench.service.TranService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("欢迎进入交易控制器");

        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        }else if("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        }else if("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request, response);
        }else if("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }else if("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }


    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计图标的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行变更阶段操作");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setMoney(money);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易Id取得相应的历史列表");

        String tranId = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
        //阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        //遍历交易历史列表
        for(TranHistory th:thList){
            //根据每条交易历史，取出每一个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        System.out.println("跳转到详细信息页");
        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.detail(id);
        //处理可能性
        /*
            阶段t
                阶段和可能性之间的对应关系 pMap
         */
        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t",t);
        //要用request域存值，所以只能用转发
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        System.out.println("执行交易的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//此处还只有客户名称，还没有Id
        String stage  = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);
        if (flag){
            //如果添加交易成功，跳转到列表页
            //不能用请求转发，因为程序完成后，路径不会刷新，最终还是会回到/workbench/transaction/save.do
            //其二，没有request域与之搭配
            //request.getRequestDispatcher("/workbench/transaction/index.jsp").forward(request,response);

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);
    }


    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUerList();

        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }



}
