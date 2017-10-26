package com.mengyitf.config;

import com.mengyitf.model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by feifei on 2017/10/22.
 */

/*匹配的输入输出类*/
public class MatchIO {
    private ArrayList<Department> departments = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private Config config;
    private Log log = new Log();

    /*参数-配置，是否初始化*/
    public MatchIO(Config config,Boolean isInit){
        this.config = config;
        if (isInit) getInit();
    }

    /*参数-配置*/
    public MatchIO(Config config){
        this.config = config;
    }

    public MatchIO(){}

    /*初始化函数-获得文件中的部门和学生信息*/
    public void getInit(){
        try{
            File importfile = new File(config.getImportfilePath());
            BufferedReader br = new BufferedReader(new FileReader(importfile));
            String str = null;  // 从文件读出来的字符串
            String status = null;   // 判断是属于部门还是学生
            Boolean isNew = true;   // 判断是否重新开始构建部门或者学生
            Department department = null;   // 一个部门信息
            Student student = null; // 一个学生信息
            int rows = 0;   // 文件读的行数
            boolean dataError = false;  //  判断数据异常-即不是想要的数据
            while ((str = br.readLine()) != null){
                rows++;
                /*数据出错，拿到字符是'/'，拿到字符是空，拿到字符是注释'#'开头*/
                if((dataError&&!str.trim().equals("/")) || str.trim().equals("") || (str.trim().indexOf("#")!=-1&&str.substring(0,str.trim().indexOf("#")).equals(""))){
                    continue;
                }
                /*状态不是部门时，拿到开始部门标识*/
                else if((status==null||!status.equals("Department"))&&str.trim().equals("Department")){
                    status = "Department";
                }
                /*状态不是学生时，拿到开始学生标识*/
                else if((status==null||!status.equals("Student"))&&str.trim().equals("Student")){
                    status = "Student";
                }
                /*状态不是空时*/
                else if(status!=null){
                    String[] info = null;
                    String type;
                    if (str.indexOf(":")!=-1){
                        type = str.substring(0,str.indexOf(":")).trim();    // ':'之前是数据类型
                        Pattern pattern = Pattern.compile(" "); // 用于按照空格进行正则匹配
                        info = pattern.split(str.substring(str.indexOf(":")+1).trim()); // ':'以后的数据按照空格分割字符串
                    }else {
                        type = str.trim();
                    }
                    switch (status){
                        /*标识为部门*/
                        case "Department":{
                            /*判断是否需要new一个部门*/
                            if (isNew){
                                department = new Department();
                                isNew = false;
                            }
                            /*根据数据类型录入数据*/
                            switch (type){
                                case "DepartmentName":{
                                    /*使用replaceAll("\"(.*)\"","$1")去掉头尾的双引号*/
                                    department.setDepartmentName(info[0].replaceAll("\"(.*)\"","$1"));
                                    break;
                                }
                                case "DepartmentCode":{
                                    department.setDepartmentCode(info[0].replaceAll("\"(.*)\"","$1"));
                                    break;
                                }
                                case "StudentLimit":{
                                    Integer num = Integer.parseInt(info[0].replaceAll("\"(.*)\"","$1"));
                                    /*限制学生数上限不对*/
                                    if (num<0 || num>15){
                                        dataError = true;
                                        log.writeLog("第 "+rows+" 行的部门学生数上限不对");
                                        break;
                                    }
                                    department.setStudentLimit(num);
                                    break;
                                }
                                case "Characteristics":{
                                    for (int i=0;i<info.length;i++) {
                                        info[i] = info[i].replaceAll("\"(.*)\"","$1");
                                        /*info中不是空数据则录入*/
                                        if (!info[i].equals(""))department.addCharacteristic(info[i]);
                                    }
                                    break;
                                }
                                case "RoutineActivityTime":{
                                    for (int i=0;i<info.length;i++) {
                                        info[i] = info[i].replaceAll("\"(.*)\"","$1");
                                        if (!info[i].equals(""))department.addRoutineActivityTime(info[i]);
                                    }
                                    if (department.getRoutineActivityTime().size()==0){
                                        dataError = true;
                                        log.writeLog("第 "+rows+" 行的部门没有时间不对");
                                    }
                                    break;
                                }
                                /*结束字符*/
                                case "/":{
                                    /*数据异常时不操作，否则操作*/
                                    if (dataError) dataError = false;
                                    else if (!isNew){
                                        isNew = true;
                                        if (department.getDepartmentName()==null || department.getDepartmentName().equals("")){
                                            log.writeLog("第 "+rows+" 行部门名称为空");
                                            break;
                                        }else if(department.getDepartmentCode()==null || department.getDepartmentCode().equals("")){
                                            log.writeLog("第 "+rows+" 行部门编号为空");
                                            break;
                                        }else if (department.getStudentLimit() == null || department.getStudentLimit()==0){
                                            log.writeLog("第 "+rows+" 行部门的学生数上限为空");
                                            break;
                                        }else if (department.getCharacteristics().size()==0){
                                            log.writeLog("第 "+rows+" 行部门的特点标签个数为0");
                                            break;
                                        }else if (department.getRoutineActivityTime().size()==0){
                                            log.writeLog("第 "+rows+" 行部门的常规活动时间段个数为0");
                                            break;
                                        }
                                        departments.add(department);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        /*学生操作-规则同部门*/
                        case "Student":{
                            if (isNew){
                                student = new Student();
                                isNew = false;
                            }
                            switch (type){
                                case "StudentName":{
                                    student.setStudentName(info[0].replaceAll("\"(.*)\"","$1"));
                                    break;
                                }
                                case "StudentCode":{
                                    student.setStudentCode(info[0].replaceAll("\"(.*)\"","$1"));
                                    break;
                                }
                                case "GradePoint":{
                                    student.setGradePoint(Float.parseFloat(info[0].replaceAll("\"(.*)\"","$1")));
                                    break;
                                }
                                case "Interest":{
                                    for (int i=0;i<info.length;i++) {
                                        info[i] = info[i].replaceAll("\"(.*)\"","$1");
                                        if (!info[i].equals(""))student.addInterest(info[i]);
                                    }
                                    break;
                                }
                                case "DepartmentWishes":{
                                    for (int i=0;i<info.length;i++) {
                                        info[i] = info[i].replaceAll("\"(.*)\"","$1");
                                        if (!info[i].equals(""))student.addDepartmentWishe(info[i]);
                                    }
                                    /*意愿部门个数大于0小于等于5*/
                                    if (student.getDepartmentWishes().size()>5){
                                        log.writeLog("第 "+rows+" 行学生意愿个数大于5个");
                                        dataError = true;
                                        break;
                                    }else if (student.getDepartmentWishes().size() == 0){
                                        log.writeLog("第 "+rows+" 行学生意愿个数为空");
                                        dataError = true;
                                        break;
                                    }
                                    break;
                                }
                                case "FreeTime":{
                                    for (int i=0;i<info.length;i++) {
                                        info[i] = info[i].replaceAll("\"(.*)\"","$1");
                                        if (!info[i].equals(""))student.addFreeTime(info[i]);
                                    }
                                    if (student.getFreeTime().size()==0){
                                        log.writeLog("第 "+rows+" 行学生时间个数为空");
                                        dataError = true;
                                    }
                                    break;
                                }
                                /*结束*/
                                case "/":{
                                    if (dataError) dataError = false;
                                    else if (!isNew){
                                        isNew = true;
                                        if (student.getStudentName()==null || student.getStudentName().equals("")){
                                            log.writeLog("第 "+rows+" 行学生姓名为空");
                                            break;
                                        }else if(student.getStudentCode()==null || student.getStudentCode().equals("")){
                                            log.writeLog("第 "+rows+" 行学生学号为空");
                                            break;
                                        }else if (student.getGradePoint() == null || student.getGradePoint()==0){
                                            log.writeLog("第 "+rows+" 行学生绩点为空");
                                            break;
                                        }else if (student.getDepartmentWishes().size()==0){
                                            log.writeLog("第 "+rows+" 行学生意愿个数为0");
                                            break;
                                        }else if (student.getFreeTime().size()==0){
                                            log.writeLog("第 "+rows+" 行学生空闲时间段个数为0");
                                            break;
                                        }
                                        students.add(student);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            log.writeLog("已成功录入文件:"+config.getImportfilePath());
            
        }catch (IOException ioe){
            log.writeLog(ioe);
            System.exit(1);
        }
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

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
