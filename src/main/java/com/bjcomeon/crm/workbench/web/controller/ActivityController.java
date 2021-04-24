package com.bjcomeon.crm.workbench.web.controller;

import com.bjcomeon.crm.settings.domain.User;
import com.bjcomeon.crm.settings.service.Impl.UserServiceImpl;
import com.bjcomeon.crm.settings.service.UserService;
import com.bjcomeon.crm.utils.*;
import com.bjcomeon.crm.vo.PaginationVO;
import com.bjcomeon.crm.workbench.dao.ActivityDao;
import com.bjcomeon.crm.workbench.domain.Activity;
import com.bjcomeon.crm.workbench.domain.ActivityRemark;
import com.bjcomeon.crm.workbench.service.ActivityService;
import com.bjcomeon.crm.workbench.service.Impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("欢迎进入市场活动控制器");

        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        }else if("/workbench/activity/save.do".equals(path)) {
            save(request, response);
        }else if("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        }else if("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        }else if("/workbench/activity/update.do".equals(path)) {
            update(request, response);
        }else if("/workbench/activity/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(request, response);
        }else if("/workbench/activity/deleteRemart.do".equals(path)) {
            deleteRemart(request, response);
        }else if("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }else if("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        //修改时间：当前的系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录的用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加备注操作");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        //创建时间：当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录的用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setNoteContent(noteContent);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemart(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行删除备注信息操作");

        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id，取得备注信息列表");

        String activityId = request.getParameter("activityId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页的操作");

        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);
        request.setAttribute("a",a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }


    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间：当前的系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录的用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setOwner(owner);
        a.setName(name);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(a);
        PrintJson.printJsonFlag(response,flag);
    }


    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询用户信息列表和根据市场活动id查询单条记录");

        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /*
            uList
                a
               以上两项信息使用率不高，返回使用map打包即可

            总结：
                controller调用service的方法，返回值是什么：
                     前端要什么，我们就从service层取什么
         */
        Map<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }


    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");

        String ids[] = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }


    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行条件、分页查询市场活动列表的操作");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            前端要：市场活动信息列表
                   查询的总条数

                   业务层拿到以上两条信息后，做返回（ map / vo ）

                   map:
                        map.put("dataList":dataList)
                        map.put("total":total)
                        PrintJSON  map --> json
                        {"total":total,"dataList":[{市场活动1}，{2}...]}

                    vo：
                        PaginationVO<T>
                            private int total;
                            private List<T> dataList

                    PaginationVO<Activity> vo = new PaginationVO<>;
                    vo.setTotal(total);
                    vo.setDataList(dataList);
                    PrintJSON  vo --> json
                    {"total":total,"dataList":[{市场活动1}，{2}...]}

                    将来分页查询，每个模块都有，所以我们选择使用一个通用VO，操作起来更方便
         */
        PaginationVO<Activity> vo = as.pageList(map);

        //vo --> json : "{"total":total,"dataList":[{市场活动1}，{2}...]}"
        PrintJson.printJsonObj(response,vo);
    }


    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录的用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity activity  = new Activity();
        activity.setId(id);
        activity.setCost(cost);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setDescription(description);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUerList();
        PrintJson.printJsonObj(response, uList);

    }
}
