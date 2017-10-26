package com.mengyitf.model;

import java.util.ArrayList;

/**
 * Created by feifei on 2017/10/22.
 */
public class Student {
    private String studentName = null;  // 姓名
    private String studentCode = null;  // 学号
    private Float gradePoint = null;    // 绩点
    private ArrayList<String> interest = new ArrayList<>(); // 兴趣爱好
    private ArrayList<String> departmentWishes = new ArrayList<>(); // 意愿部门
    private ArrayList<String> freeTime = new ArrayList<>(); // 空闲时间
    private boolean isMatch = false;    // 是否已被匹配
    private int matchNumber = 0;    // 已匹配部门个数
    private int sameInterestsNum = 0;    // 爱好相同数
    private int sametime = 0;
    private boolean ismatch = false;

    public Student() {}

    public Student(String studentName, String studentCode, Float gradePoint, ArrayList<String> interest, ArrayList<String> departmentWishes, ArrayList<String> freeTime) {
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.gradePoint = gradePoint;
        this.interest = interest;
        this.departmentWishes = departmentWishes;
        this.freeTime = freeTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Float getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Float gradePoint) {
        this.gradePoint = gradePoint;
    }

    public ArrayList<String> getInterest() {
        return interest;
    }

    public void setInterest(ArrayList<String> interest) {
        this.interest = interest;
    }

    public void addInterest(String interest) {
        this.interest.add(interest);
    }

    public ArrayList<String> getDepartmentWishes() {
        return departmentWishes;
    }

    public void setDepartmentWishes(ArrayList<String> departmentWishes) {
        this.departmentWishes = departmentWishes;
    }

    public void addDepartmentWishe(String departmentWishe) {
        this.departmentWishes.add(departmentWishe);
    }

    public ArrayList<String> getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(ArrayList<String> freeTime) {
        this.freeTime = freeTime;
    }

    public void addFreeTime(String freeTime) {
        this.freeTime.add(freeTime);
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public void addMatchNumber(){
        matchNumber++;
    }

    public int getSameInterestsNum() {
        return sameInterestsNum;
    }

    public void addSameInterestsNum(){
        sameInterestsNum++;
    }

    public void setSameInterestsNum(int sameInterestsNum) {
        this.sameInterestsNum = sameInterestsNum;
    }

    public int getSametime() {
        return sametime;
    }

    public void setSametime(int sametime) {
        this.sametime = sametime;
    }

    public void addSametime(){
        sametime++;
    }

    public boolean getIsmatch() {
        return ismatch;
    }

    public void setIsmatch(boolean ismatch) {
        this.ismatch = ismatch;
    }
}
