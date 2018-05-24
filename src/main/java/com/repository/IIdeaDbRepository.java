package com.repository;


import com.model.Idea;

import java.util.List;
/**
 * Dette er vores interface for IdeaDbRepository
 * @author Christoffer
 */

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