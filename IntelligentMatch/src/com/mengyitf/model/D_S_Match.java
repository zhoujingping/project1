package com.mengyitf.model;

import java.util.ArrayList;

/**
 * Created by feifei on 2017/10/23.
 */

/*记录一个部门匹配多个学生信息*/
public class D_S_Match {
    private Department department;
    private ArrayList<Student> students = new ArrayList<>();
    private String studentStr = "/";

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void addStudents(Student student) {
        this.students.add(student);
        this.addStudentStr(student.getStudentCode());
    }

    public void deleteStudent(Integer index){
        students.remove(index);
    }

    public void addStudentStr(String string) {
        studentStr = studentStr + string +"/";
    }

    public String getStudentStr() {
        return studentStr;
    }

    public void setStudentStr(String studentStr) {
        this.studentStr = studentStr;
    }

}
