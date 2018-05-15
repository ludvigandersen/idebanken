package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
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
                    sqlRowSet.getInt("idea_id"),
                    sqlRowSet.getString("idea_name"),
                    sqlRowSet.getString("idea_description"),
                    sqlRowSet.getInt("idea_person"),
                    LocalDate.parse(sqlRowSet.getString("date"))
            ));

        }
        return ideas;
    }

    @Override
<<<<<<< HEAD
    public List<Idea> getIdea(int id) {
        List<Idea> ideas = new ArrayList<>();
        String sql = "SELECT * FROM idebanken.Idea WHERE idea_person=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);
        while (sqlRowSet.next()){

                ideas.add(new Idea(
                        sqlRowSet.getString("idea_name"),
                        sqlRowSet.getString("idea_description"),
                        sqlRowSet.getInt("idea_person"),
                        LocalDate.parse(sqlRowSet.getString("date"))
                ));

        }
        return ideas;
=======
    public Idea getIdea(int id) {
        String sql = "SELECT * FROM idebanken.Idea WHERE idea_id=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);

        while (sqlRowSet.next()){
            return new Idea(
                    sqlRowSet.getInt("idea_id"),
                    sqlRowSet.getString("idea_name"),
                    sqlRowSet.getString("idea_description"),
                    sqlRowSet.getInt("idea_person"),
                    LocalDate.parse(sqlRowSet.getString("date"))
            );

        }
        return null;
>>>>>>> 87bf0a217fa5eb24fe8aa374bba05f2d6b14b131
    }

    @Override
    public List<Idea> getAssignedIdeas(List<Integer> groups) {
        List<Idea> ideas = new ArrayList<>();

        String groupString = "";
        boolean check = true;
        for (Integer i: groups){
            if (check){
                groupString += i;
                check = false;
            } else {
                groupString += ", " + i;
            }
        }
        String sql = "SELECT Idea.idea_id, Idea.idea_name, Idea.idea_description, Idea.idea_person, Idea.date\n" +
                "FROM idebanken.Idea INNER JOIN GroupIdea ON Idea.idea_id = GroupIdea.idea_id \n" +
                "WHERE GroupIdea.approved = 1 AND GroupIdea.group_id IN (?)";
        sqlRowSet = jdbc.queryForRowSet(sql, groupString);

        while (sqlRowSet.next()){
            ideas.add(new Idea(
                    sqlRowSet.getInt("idea_id"),
                    sqlRowSet.getString("idea_name"),
                    sqlRowSet.getString("idea_description"),
                    sqlRowSet.getInt("idea_person"),
                    LocalDate.parse(sqlRowSet.getString("date"))
            ));

        }
        return ideas;
    }

    @Override
    public List<Idea> getAppliedIdeas(List<Integer> groups) {
        List<Idea> ideas = new ArrayList<>();

        String groupString = "";
        boolean check = true;
        for (Integer i: groups){
            if (check){
                groupString += i;
                check = false;
            } else {
                groupString += ", " + i;
            }
        }

        String sql = "SELECT Idea.idea_id, Idea.idea_name, Idea.idea_description, Idea.idea_person, Idea.date\n" +
                "FROM idebanken.Idea INNER JOIN GroupIdea ON Idea.idea_id = GroupIdea.idea_id \n" +
                "WHERE GroupIdea.approved = 0 AND GroupIdea.group_id IN (?)";
        sqlRowSet = jdbc.queryForRowSet(sql, groupString);

        while (sqlRowSet.next()){
            ideas.add(new Idea(
                    sqlRowSet.getInt("idea_id"),
                    sqlRowSet.getString("idea_name"),
                    sqlRowSet.getString("idea_description"),
                    sqlRowSet.getInt("idea_person"),
                    LocalDate.parse(sqlRowSet.getString("date"))
            ));

        }
        return ideas;
    }
}