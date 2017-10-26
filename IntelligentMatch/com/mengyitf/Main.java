package com.mengyitf;

import com.mengyitf.config.Config;
import com.mengyitf.model.Log;
import com.mengyitf.service.DepartmentOfStudentMatching;
import com.mengyitf.service.impl.DepartmentOfStudentMatchingImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Log log = new Log();
        log.writeLog("-------------------------------------------");
        log.writeLog(format.format(new Date().getTime()));
        log.writeLog("");
        log.writeLog("");
        Config config = new Config(true);
        long oldtime = new Date().getTime();
        CreateInportFile createInportFile = new CreateInportFile(config);
        long newtime = new Date().getTime();
        log.writeLog("生成输入文件时间为: "+(newtime-oldtime)+" 毫秒");
        DepartmentOfStudentMatching dosm = new DepartmentOfStudentMatchingImpl();
        dosm.setConfig(config);
        dosm.init();
        oldtime = new Date().getTime();
        dosm.matching();
        newtime = new Date().getTime();
        log.writeLog("匹配执行时间为: "+(newtime-oldtime)+" 毫秒");
        oldtime = new Date().getTime();
        dosm.print();
        newtime = new Date().getTime();
        log.writeLog("生成输出文件时间为: "+(newtime-oldtime)+" 毫秒");
        log.writeLog("");
        log.writeLog("");
    }
}
