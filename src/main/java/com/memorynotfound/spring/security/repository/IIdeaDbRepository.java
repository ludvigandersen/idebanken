package com.memorynotfound.spring.security.repository;


import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IIdeaDbRepository {
    void createIdea(Idea idea);
    void updateIdea(Person person, int id);
    void deleteIdea(int id);
    List<Idea> getAllIdeas();
<<<<<<< HEAD
    List<Idea> getIdea(int id);
    List<Idea> getAssignedIdeas();
    List<Idea> getAppliedIdeas();
=======
    Idea getIdea(int id);
    List<Idea> getAssignedIdeas(List<Integer> groups);
    List<Idea> getAppliedIdeas(List<Integer> groups);
>>>>>>> 87bf0a217fa5eb24fe8aa374bba05f2d6b14b131
}