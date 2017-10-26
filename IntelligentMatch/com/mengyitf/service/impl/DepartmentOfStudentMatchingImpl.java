package com.mengyitf.service.impl;

import com.mengyitf.config.Config;
import com.mengyitf.config.MatchIO;
import com.mengyitf.model.D_S_Match;
import com.mengyitf.model.Department;
import com.mengyitf.model.Log;
import com.mengyitf.model.Student;
import com.mengyitf.service.DepartmentOfStudentMatching;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by feifei on 2017/10/23.
 */
public class DepartmentOfStudentMatchingImpl implements DepartmentOfStudentMatching {
    public static final int gradePoint = 1;
    public static final int interest = 2;
    public static final int freeTime = 3;
    private Config config = null;
    private ArrayList<Department> departments = null;
    private ArrayList<Student> students = null;
    private ArrayList<D_S_Match> d_s_matches = new ArrayList<>();
    private String d_s_matchesStr = "/";
    private Log log = new Log();

    public DepartmentOfStudentMatchingImpl(){}

    /*参数是否初始化*/
    public DepartmentOfStudentMatchingImpl(Boolean goInit){
        if (goInit) init();
    }

    /*初始化数据-获得部门和学生信息*/
    public void init(){
        if (config==null) config = new Config(true);
        MatchIO matchIO = new MatchIO(config,true);
        if (departments==null) departments = matchIO.getDepartments();
        if (students==null) students = matchIO.getStudents();
    }

    /*匹配方法*/
    public void matching(){
        boolean error = false;
        if (departments==null || departments.size()==0){
            log.writeLog("没有部门");
            error = true;
        }
        if (students==null || students.size()==0){
            log.writeLog("没有学生");
            error = true;
        }
        if (error) {
            
            System.exit(1);
        }

        // 志愿优先
        firstChoice();

        // 其他优先
        for (int i = 0;i<config.getPriority().size();i++){
            try {
                if (Integer.parseInt(config.getPriority().get(i)) > 0 && Integer.parseInt(config.getPriority().get(i)) < 4){
                    matchingBy(Integer.parseInt(config.getPriority().get(i)));
                }
            }catch (NumberFormatException nfe){
                switch (config.getPriority().get(i)){
                    case "gradePoint":{
                        matchingBy(1);
                        break;
                    }
                    case "interest":{
                        matchingBy(2);
                        break;
                    }
                    case "freeTime":{
                        matchingBy(3);
                        break;
                    }
                    default:{
                        othersPriority(config.getPriority().get(i));
                    }
                }
            }

        }
        // 筛选15个
        ArrayList<D_S_Match> d_s_matchesCopy = new ArrayList<>();
        for (int i = 0;i<d_s_matches.size();i++){
            ArrayList<Student> studentsTemp = d_s_matches.get(i).getStudents();
            if (d_s_matches.get(i).getDepartment().getStudentLimit()>=studentsTemp.size()){
                for (int j=0;j<studentsTemp.size();j++){
                    if (!studentsTemp.get(j).getIsmatch())studentsTemp.get(j).setIsmatch(true);
                }
                continue;
            }
            else d_s_matchesCopy.add(d_s_matches.get(i));
        }
        Random random = new Random();
        while (d_s_matchesCopy.size()>0){
            int j = random.nextInt(d_s_matchesCopy.size());
            D_S_Match d_s_matchCopy = new D_S_Match();
            d_s_matchCopy.setDepartment(d_s_matchesCopy.get(j).getDepartment());
            ArrayList<Student> studentsTemp = d_s_matchesCopy.get(j).getStudents();
            while (d_s_matchCopy.getDepartment().getStudentLimit()>d_s_matchCopy.getStudents().size()){
                int k = random.nextInt(studentsTemp.size());
                d_s_matchCopy.addStudents(studentsTemp.get(k));
                if (!studentsTemp.get(k).getIsmatch())studentsTemp.get(k).setIsmatch(true);
                studentsTemp.remove(k);
            }
            d_s_matchesCopy.get(j).setStudents(d_s_matchCopy.getStudents());
            d_s_matchesCopy.get(j).setStudentStr(d_s_matchCopy.getStudentStr());
            d_s_matchesCopy.remove(j);
        }
        
    }

    // 志愿优先
    private void firstChoice(){
        boolean isChange = false;
        /*轮训5次意愿*/
        for (int index = 0;index<5;index++){
            isChange = true;    //志愿改变
            /*遍历每个学生*/
            for (int i = 0;i<students.size() && departments.size()>0; ++i){
                // 监测意愿部门是否还有
                try {
                    students.get(i).getDepartmentWishes().get(index);
                }catch (IndexOutOfBoundsException ioobe){
                    continue;
                }
                /*遍历每个部门*/
                for (int j = 0;j<departments.size();++j){
                    /*监测匹配是否存在*/
                    try{
                        d_s_matches.get(j);
                    }catch (IndexOutOfBoundsException ioobe){
                        /*不存在则建立一个匹配*/
                        D_S_Match dsm = new D_S_Match();
                        departments.get(j).setIsmatch(true);
                        dsm.setDepartment(departments.get(j));
                        d_s_matches.add(dsm);
                        if (!d_s_matchesStr.matches("/"+departments.get(j).getDepartmentCode()+"/")){
                            d_s_matchesStr = d_s_matchesStr + departments.get(j).getDepartmentCode() + "/";
                        }

                    }
                    //  志愿级别改变，判断人数是否够了
                    if(isChange && d_s_matches.get(j).getStudents().size()>=d_s_matches.get(j).getDepartment().getStudentLimit())break;
                    isChange = false;
                    /*意愿部门匹配部门*/
                    String[] dw = students.get(i).getDepartmentWishes().get(index).split("/");
                    if ((dw.length==1 && dw[0].equals(departments.get(j).getDepartmentName())) || (dw.length==2 && dw[0].equals(departments.get(j).getDepartmentCode()))){
                        if (!d_s_matches.get(j).getStudentStr().matches("/"+students.get(i).getStudentCode()+"/")){
                            d_s_matches.get(j).addStudents(students.get(i));
                            break;
                        }
                    }
                }
            }
        }
    }

    // 绩点，兴趣，空闲时间优先
    private void matchingBy(int type){
        ArrayList<D_S_Match> d_s_matchesCopy = new ArrayList<>();
        for (int i = 0;i<d_s_matches.size();i++){
            if (d_s_matches.get(i).getDepartment().getStudentLimit()>=d_s_matches.get(i).getStudents().size())continue;
            else d_s_matchesCopy.add(d_s_matches.get(i));
        }
        switch (type){
            case DepartmentOfStudentMatchingImpl.gradePoint:{
                sortByGradePoint(d_s_matchesCopy);
                for (int i = 0;i<d_s_matchesCopy.size();i++){
                    ArrayList<Student> studentsCopy = d_s_matchesCopy.get(i).getStudents();
                    float point = studentsCopy.get(d_s_matchesCopy.get(i).getDepartment().getStudentLimit()-1).getGradePoint();
                    boolean isdelete = false;
                    for (int j = d_s_matchesCopy.get(i).getDepartment().getStudentLimit();j<studentsCopy.size();j++){
                        if (point==studentsCopy.get(j).getGradePoint())continue;
                        else if (!isdelete) {
                            isdelete = true;
                            studentsCopy.remove(j);
                        }else studentsCopy.remove(j);
                    }
                }
                break;
            }
            case DepartmentOfStudentMatchingImpl.interest:{
                for (int i = 0;i<d_s_matchesCopy.size();i++){
                    ArrayList<Student> studentsCopy = d_s_matchesCopy.get(i).getStudents();
                    for (int j = 0;j<studentsCopy.size();j++){
                        ArrayList<String> cs = d_s_matchesCopy.get(i).getDepartment().getCharacteristics();
                        ArrayList<String> is = (ArrayList<String>)studentsCopy.get(j).getInterest().clone();
                        for (int v = 0;v<cs.size();v++){
                            for (int k=0;k<is.size();) {
                                if (cs.get(v).equals(is.get(k))){
                                    studentsCopy.get(j).addSameInterestsNum();
                                    is.remove(k);
                                    break;
                                }
                                k++;
                            }
                        }
                    }
                    sortByInterestsNum(studentsCopy);
                    float point = studentsCopy.get(d_s_matchesCopy.get(i).getDepartment().getStudentLimit()-1).getSameInterestsNum();
                    boolean isdelete = false;
                    for (int j = d_s_matchesCopy.get(i).getDepartment().getStudentLimit();j<studentsCopy.size();j++){
                        if (point==studentsCopy.get(j).getSameInterestsNum())continue;
                        else if (!isdelete) {
                            isdelete = true;
                            studentsCopy.remove(j);
                        }else studentsCopy.remove(j);
                    }
                }
                break;
            }
            case DepartmentOfStudentMatchingImpl.freeTime:{
                for (int i = 0;i<d_s_matchesCopy.size();i++){
                    ArrayList<Student> studentsCopy = d_s_matchesCopy.get(i).getStudents();
                    for (int j = 0;j<studentsCopy.size();j++){
                        ArrayList<String> cs = d_s_matchesCopy.get(i).getDepartment().getRoutineActivityTime();
                        ArrayList<String> is = (ArrayList<String>)studentsCopy.get(j).getFreeTime().clone();
                        for (int v = 0;v<cs.size();v++){
                            for (int k=0;k<is.size();) {
                                if (cs.get(v).equals(is.get(k))){
                                    studentsCopy.get(j).addSametime();
                                    is.remove(k);
                                    break;
                                }
                                k++;
                            }
                        }
                    }
                    sortBySametime(studentsCopy);
                    float point = studentsCopy.get(d_s_matchesCopy.get(i).getDepartment().getStudentLimit()-1).getSametime();
                    boolean isdelete = false;
                    for (int j = d_s_matchesCopy.get(i).getDepartment().getStudentLimit();j<studentsCopy.size();j++){
                        if (point==studentsCopy.get(j).getSametime())continue;
                        else if (!isdelete) {
                            isdelete = true;
                            studentsCopy.remove(j);
                        }else studentsCopy.remove(j);
                    }
                }
                break;
            }
        }

    }

    // 其他优先级-可扩充
    public <T> void othersPriority(T priority){}

    /*按兴趣相同数降序排序*/
    private void sortBySametime(ArrayList<Student> studentsCopy){
        Collections.sort(studentsCopy, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return ((Integer)s2.getSametime()).compareTo(s1.getSametime());
            }
        });
    }

    /*按兴趣相同数降序排序*/
    private void sortByInterestsNum(ArrayList<Student> studentsCopy){
        Collections.sort(studentsCopy, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return ((Integer)s2.getSameInterestsNum()).compareTo(s1.getSameInterestsNum());
            }
        });
    }
    /*按绩点降序排序*/
    private void sortByGradePoint(ArrayList<D_S_Match> d_s_matchesCopy){
        for (int i = 0;i<d_s_matchesCopy.size();i++){
            Collections.sort(d_s_matchesCopy.get(i).getStudents(), new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return s2.getGradePoint().compareTo(s1.getGradePoint());
                }
            });
        }
    }

    public void print(){
        try {
            File output = new File(config.getOutputfilePath());
            if (!output.exists()) output.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            bw.write("-----------------已匹配表单-----------------");
            bw.newLine();
            for (int i=0;i<d_s_matches.size();i++){
                Department departmentTemp = d_s_matches.get(i).getDepartment();
                ArrayList<Student> studentsTemp = d_s_matches.get(i).getStudents();
                bw.newLine();
                bw.write("部门名称："+departmentTemp.getDepartmentName());
                bw.newLine();
                bw.write("部门编号："+departmentTemp.getDepartmentCode());
                bw.newLine();
                bw.write("部门应招收人数："+departmentTemp.getStudentLimit());
                bw.newLine();
                bw.write("部门实招收人数："+studentsTemp.size());
                bw.newLine();
                bw.write("部门常规活动时间：");
                bw.newLine();
                for (int j=0;j<departmentTemp.getRoutineActivityTime().size();j++){
                    bw.write("\t"+departmentTemp.getRoutineActivityTime().get(j));
                    bw.newLine();
                }
                bw.write("学生名单：");
                bw.newLine();
                for (int j=0;j<studentsTemp.size();j++){
                    bw.write("\t姓名："+studentsTemp.get(j).getStudentName());
                    bw.newLine();
                    bw.write("\t学号："+studentsTemp.get(j).getStudentCode());
                    bw.newLine();
                    bw.write("\t绩点："+studentsTemp.get(j).getGradePoint());
                    bw.newLine();
                    bw.write("\t空闲时间：");
                    bw.newLine();
                    for (int l=0;l<studentsTemp.get(j).getFreeTime().size();l++){
                        bw.write("\t\t"+studentsTemp.get(j).getFreeTime().get(l));
                        bw.newLine();
                    }
                    bw.newLine();
                }
                bw.write("--------------------------------------------");
                bw.newLine();
            }
            bw.newLine();
            bw.write("-----------------未匹配部门-----------------");
            bw.newLine();
            int num = 0;
            for (int i = 0;i<departments.size();i++){
                Department departmentTemp = departments.get(i);
                if (!departments.get(i).isIsmatch()){
                    bw.write("部门名称："+departmentTemp.getDepartmentName());
                    bw.newLine();
                    bw.write("部门编号："+departmentTemp.getDepartmentCode());
                    bw.newLine();
                    bw.write("部门学生上限数："+departmentTemp.getStudentLimit());
                    bw.newLine();
                    bw.write("部门常规活动时间：");
                    bw.newLine();
                    for (int j=0;j<departmentTemp.getRoutineActivityTime().size();j++){
                        bw.write("\t"+departmentTemp.getRoutineActivityTime().get(j));
                        bw.newLine();
                    }
                    bw.newLine();
                    bw.write("--------------------------------------------");
                    bw.newLine();
                    num++;
                }
            }
            bw.write("数量："+num);
            bw.newLine();
            bw.write("-----------------未匹配学生-----------------");
            bw.newLine();
            num = 0;
            for (int i = 0;i<students.size();i++){
                Student studentTemp = students.get(i);
                if (!studentTemp.getIsmatch()){
                    bw.write("姓名："+studentTemp.getStudentName());
                    bw.newLine();
                    bw.write("学号："+studentTemp.getStudentCode());
                    bw.newLine();
                    bw.write("绩点："+studentTemp.getGradePoint());
                    bw.newLine();
                    bw.write("空闲时间：");
                    bw.newLine();
                    for (int l=0;l<studentTemp.getFreeTime().size();l++){
                        bw.write("\t"+studentTemp.getFreeTime().get(l));
                        bw.newLine();
                    }
                    bw.newLine();
                    bw.write("--------------------------------------------");
                    bw.newLine();
                    num++;
                }
            }
            bw.write("数量："+num);
            bw.newLine();
            bw.close();
            log.writeLog("已成功输出文件:"+config.getOutputfilePath());
        }catch (IOException ioe){
            log.writeLog(ioe);
        }
        
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
