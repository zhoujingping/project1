package com.mengyitf.model;

import java.util.ArrayList;

/**
 * Created by feifei on 2017/10/22.
 */
public class Department {
    private String departmentName;  // 名称
    private String departmentCode;  // 编号
    private Integer studentLimit;   // 限制学生数[0-15]
    private ArrayList<String> characteristics = new ArrayList<>();  // 特点标签
    private ArrayList<String> routineActivityTime = new ArrayList<>();  // 常规活动时间
    private boolean ismatch = false;

    public Department() {}

    public Department(String departmentName, String departmentCode, Integer studentLimit,ArrayList<String> characteristics, ArrayList<String> routineActivityTime) {
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.studentLimit = studentLimit;
        this.characteristics = characteristics;
        this.routineActivityTime = routineActivityTime;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Integer getStudentLimit() {
        return studentLimit;
    }

    public void setStudentLimit(Integer studentLimit) {
        this.studentLimit = studentLimit;
    }

    public ArrayList<String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(ArrayList<String> characteristics) {
        this.characteristics = characteristics;
    }

    public void addCharacteristic(String characteristic) {
        this.characteristics.add(characteristic);
    }

    public ArrayList<String> getRoutineActivityTime() {
        return routineActivityTime;
    }

    public void setRoutineActivityTime(ArrayList<String> routineActivityTime) {
        this.routineActivityTime = routineActivityTime;
    }

    public void addRoutineActivityTime(String routineActivityTime) {
        this.routineActivityTime.add(routineActivityTime);
    }

    public boolean isIsmatch() {
        return ismatch;
    }

    public void setIsmatch(boolean ismatch) {
        this.ismatch = ismatch;
    }
}
