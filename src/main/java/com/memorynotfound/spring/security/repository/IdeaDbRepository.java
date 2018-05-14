package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IdeaDbRepository implements IIdeaDbRepository{

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    @Override
    public void createIdea(Idea idea) {
        String sql = "INSERT INTO Idea (idea_id, idea_name, idea_description, idea_person, date) " +
                    "VALUES (default, ?,?,?,?)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, idea.getIdeaName());
            preparedStatement.setString(2, idea.getIdeaDescription());
            preparedStatement.setInt(3, idea.getIdeaPerson());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
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
        List<Idea> ideas = new ArrayList<>();
        String sql = "SELECT * FROM idebanken.Idea";
        sqlRowSet = jdbc.queryForRowSet(sql);

        while (sqlRowSet.next()){
            ideas.add(new Idea(
                    sqlRowSet.getString("idea_name"),
                    sqlRowSet.getString("idea_description"),
                    sqlRowSet.getInt("idea_person"),
                    LocalDate.parse(sqlRowSet.getString("date"))
            ));

        }
        return ideas;
    }

    @Override
    public Idea getIdea(int id) {
        return null;
    }
}
