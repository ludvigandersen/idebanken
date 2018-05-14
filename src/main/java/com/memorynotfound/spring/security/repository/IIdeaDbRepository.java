package com.memorynotfound.spring.security.repository;

<<<<<<< HEAD
public interface IIdeaDbRepository {
=======
import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IIdeaDbRepository {
    void createIdea(Idea idea);
    void updateIdea(Person person, int id);
    void deleteIdea(int id);
    List<Idea> getAllIdeas();
    Idea getIdea(int id);

>>>>>>> efa0a0b8eee722ea95c3b3796abafb78d2ad7035
}
