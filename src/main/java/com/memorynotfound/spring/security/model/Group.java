package com.memorynotfound.spring.security.model;

public class Group {

    private int groupId;
    private String name;
    private String resume;
    private int personId;

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, String resume) {
        this.name = name;
        this.resume = resume;
    }

    public Group(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public Group(int groupId, int personId) {
        this.groupId = groupId;
        this.personId = personId;
    }

    public Group(String groupName, int personId) {
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}


