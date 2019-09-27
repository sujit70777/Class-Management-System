package com.myclasshelper.sujit007.myclasshelper.ClassInformation;

/**
 * Created by Sujit007 on 11/25/2016.
 */

public class ClassInfo {

    private int id;
    private String ClassName;
    private String ClassDetails;
    private int batch;
    private int semester;

    public ClassInfo(int id, String className, String classDetails, int batch, int semester) {
        this.id = id;
        ClassName = className;
        ClassDetails = classDetails;
        this.batch = batch;
        this.semester = semester;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public ClassInfo() {
    }

    public ClassInfo(int id, String className, String classDetails) {
        this.id = id;
        ClassName = className;
        ClassDetails = classDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public String getClassDetails() {

        return ClassDetails;
    }

    public void setClassDetails(String classDetails) {
        ClassDetails = classDetails;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }
}
