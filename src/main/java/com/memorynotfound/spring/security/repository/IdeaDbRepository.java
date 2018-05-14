package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IdeaDbRepository implements IIdeaDbRepository{

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    @Override
    public void createIdea(Idea idea) {
        jdbc.update("INSERT INTO Idea (idea_id, idea_name, idea_description, idea_person, date)" +
                        "VALUES (default, ?,?,?, default,?)",
                new Object[]{
                        idea.getIdeaName(), idea.getIdeaDescription(), idea.getIdeaPerson(), idea.getDate()
                });
    }
    @Override
    public void updateIdea(Person person, int id) {

    }

    @Override
    public void deleteIdea(int id) {

    }

    @Override
    public List<Idea> getAllIdeas() {
        return null;
    }

    @Override
    public Idea getIdea(int id) {
        return null;
    }
}
