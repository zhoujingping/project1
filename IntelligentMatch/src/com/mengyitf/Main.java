package com.mengyitf;

import com.mengyitf.config.Config;
import com.mengyitf.service.DepartmentOfStudentMatching;
import com.mengyitf.service.impl.DepartmentOfStudentMatchingImpl;

import javax.xml.crypto.Data;

public class Main {
    public static void main(String[] args) {

        Config config = new Config(true);
        CreateInportFile createInportFile = new CreateInportFile(config);
        DepartmentOfStudentMatching dosm = new DepartmentOfStudentMatchingImpl();
        dosm.setConfig(config);
        dosm.init();
        dosm.matching();
        dosm.print();
    }
}
