package com.model;

import java.time.LocalDate;

/**
 * @author Christoffer
 */
public class Idea {
    private int ideaId;
    private String ideaName;
    private String ideaDescription;
    private int ideaPerson;
    private LocalDate date;

    public Idea(int ideaId, String ideaName, String ideaDescription) {
        this.ideaId = ideaId;
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
    }

    public Idea(String ideaName, String ideaDescription, int ideaPerson, LocalDate date) {
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
        this.ideaPerson = ideaPerson;
        this.date = date;
    }

    public Idea(int ideaId, String ideaName, String ideaDescription, int ideaPerson, LocalDate date) {
        this.ideaId = ideaId;
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
        this.ideaPerson = ideaPerson;
        this.date = date;
    }

    public String getIdeaName() {
        return ideaName;
    }

    public void setIdeaName(String ideaName) {
        this.ideaName = ideaName;
    }

    public String getIdeaDescription() {
        return ideaDescription;
    }

    public void setIdeaDescription(String ideaDescription) {
        this.ideaDescription = ideaDescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getIdeaPerson() {
        return ideaPerson;
    }

    public void setIdeaPerson(int ideaPerson) {
        this.ideaPerson = ideaPerson;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }
}