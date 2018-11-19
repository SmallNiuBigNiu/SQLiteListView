package cn.gdcp.sqlitelistview;

import java.io.Serializable;

/**
 * Created by acer on 2018/11/19.
 */

public class Student implements Serializable {
    private String stuno;
    private String name;
    private int age;

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Student(String stuno, String name, int age) {
        this.stuno = stuno;
        this.name = name;
        this.age = age;
    }

    public Student() {
    }
}
