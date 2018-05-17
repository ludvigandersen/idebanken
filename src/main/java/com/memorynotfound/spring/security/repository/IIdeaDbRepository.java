package com.memorynotfound.spring.security.repository;


import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IIdeaDbRepository {
    void createIdea(Idea idea);
    void updateIdea(Idea idea);
    void deleteIdea(int id);
    List<Idea> getAllIdeas();
    List<Idea> getIdeaList(int id);
    Idea getIdea(int id);
    List<Idea> getAssignedIdeas(List<Integer> groups);
    List<Idea> getAppliedIdeas(List<Integer> groups);
}