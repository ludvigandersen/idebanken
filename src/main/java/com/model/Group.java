package com.model;

/**
 * @author Nicolai
 */
public class Group {

    private int groupId;
    private String name;
    private String resume;

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