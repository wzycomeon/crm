package com.bjcomeon.settings;

import com.bjcomeon.crm.utils.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {


        /*
        //验证失效时间
        String expireTime = "2029-10-10 10:10:10";
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);
        */

        /*
        //锁定
        String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("锁定");
        }
        */

        /*
        //ip地址
        //浏览器ip地址
        String ip = "192.168.1.3";
        //允许访问ip地址群
        String allowIps = "192.168.1.1,192.168.1.2";

        if (!allowIps.contains(ip)){
            System.out.println("访问受限");
        }else {
            System.out.println("允许访问");
        }
        */

    }
}
