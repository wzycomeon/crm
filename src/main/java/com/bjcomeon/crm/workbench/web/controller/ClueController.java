package com.bjcomeon.crm.workbench.web.controller;

import com.bjcomeon.crm.settings.domain.User;
import com.bjcomeon.crm.settings.service.Impl.UserServiceImpl;
import com.bjcomeon.crm.settings.service.UserService;
import com.bjcomeon.crm.utils.DateTimeUtil;
import com.bjcomeon.crm.utils.PrintJson;
import com.bjcomeon.crm.utils.ServiceFactory;
import com.bjcomeon.crm.utils.UUIDUtil;
import com.bjcomeon.crm.vo.PaginationVO;
import com.bjcomeon.crm.workbench.domain.Activity;
import com.bjcomeon.crm.workbench.domain.ActivityRemark;
import com.bjcomeon.crm.workbench.domain.Clue;
import com.bjcomeon.crm.workbench.domain.Tran;
import com.bjcomeon.crm.workbench.service.ActivityService;
import com.bjcomeon.crm.workbench.service.ClueService;
import com.bjcomeon.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjcomeon.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("欢迎进入线索控制器");

        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        }else if("/workbench/clue/save.do".equals(path)) {
            save(request, response);
        }else if("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        }else if("/workbench/clue/unbund.do".equals(path)) {
            unbund(request, response);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueID.do".equals(path)) {
            getActivityListByNameAndNotByClueID(request, response);
        }else if("/workbench/clue/bund.do".equals(path)) {
            bund(request, response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        }else if("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行线索转换的操作");

        String clueId = request.getParameter("clueId");

        Tran t = null;

        //因为要将createBy传给业务层，所以把createBy写在if外
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        //接收是否需要创建交易的标记
        String flag = request.getParameter("flag");
        //如果需要创建交易
        if ("a".equals(flag)){

            t = new Tran();
            //接收交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expecteDate = request.getParameter("expecteDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
            t.setMoney(money);
            t.setName(name);
            t.setActivityId(activityId);
            t.setExpectedDate(expecteDate);
            t.setStage(stage);
        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
            为业务层传递的参数：
              ① 必须传递 clueId ，有了clueId我们才知道要转换哪条记录
              ② 必须传递 参数t ，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收的t也有可能是null）
         */
        boolean zhflag = cs.convert(clueId,t,createBy);

        if (zhflag){
            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询）");
        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }


    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动的操作");
        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueID(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询+排除掉已经关联指定线索的列表）");
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String, String>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByNameAndNotByClueID(map);

        PrintJson.printJsonObj(response,aList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联关系解除操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }


    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索ID传讯关联的市场活动列表");
        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }


    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        System.out.println("跳转到线索的详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }


    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索的添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(c);
        PrintJson.printJsonFlag(response,flag);
    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUerList();
        PrintJson.printJsonObj(response,uList);
    }



}
