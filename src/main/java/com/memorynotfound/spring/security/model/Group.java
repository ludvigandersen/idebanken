package com.memorynotfound.spring.security.model;

public class Group {

    private String name;
    private String resume;

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, String resume) {
        this.name = name;
        this.resume = resume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}


