package com.memorynotfound.spring.security.repository;


import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IIdeaDbRepository {
    void createIdea(Idea idea);
    void updateIdea(Person person, int id);
    void deleteIdea(int id);
    List<Idea> getAllIdeas();
    Idea getIdea(int id);
}