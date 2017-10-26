package com.mengyitf.service;

import com.mengyitf.config.Config;
import com.mengyitf.model.Department;
import com.mengyitf.model.Student;

import java.util.ArrayList;

/**
 * Created by feifei on 2017/10/23.
 */
public interface DepartmentOfStudentMatching {
    void init();
    void matching();
    void print();
    <T> void othersPriority(T priority);
    void setConfig(Config config);
    Config getConfig();
    ArrayList<Department> getDepartments();
    void setDepartments(ArrayList<Department> departments);
    ArrayList<Student> getStudents();
    void setStudents(ArrayList<Student> students);
}
