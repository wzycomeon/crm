package com.bjcomeon.crm.web.listener;

import com.bjcomeon.crm.settings.domain.DicValue;
import com.bjcomeon.crm.settings.service.DicService;
import com.bjcomeon.crm.settings.service.Impl.DicServiceImpl;
import com.bjcomeon.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*
        event：给参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数取得什么对象
     */
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            业务层有7个List
            可以打包成一个map
         */
       Map<String, List<DicValue>> map = ds.getAll();
       //将map解析为上下文域对象中保存的键值对
       Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

        //-------------------------------------------------------------------------------------------
        //数据字典处理完毕后，处理stage2Possibility.properties文件
        /*
            处理stage2Possibility.properties文件步骤：
                （1）解析该文件，将属性文件中的键值对关系处理为java中的键值对关系（map）
                 Map<String(阶段stage),String(可能性possibility)> pMap = ....
                pMap.put("01资质审查",10);
                pMap.put("02需求分析",25);
                pMap.put("07...",...);

                pMap保存值之后，放在服务器缓存中
                application.setAttribute("pMap",pMap);
         */
        //解析properties文件
        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
    }
}
