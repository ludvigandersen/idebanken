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
/**
 * @author Christoffer
 */

@Repository
public class IdeaDbRepository implements IIdeaDbRepository{

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    /**
     * Her modtager vi al den data idépersonen har indtastet i form af en Idea idea.
     * Vi bruger herefter en INSERT sql statement til at indsætte idéen i databasen
     */
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
    /**
     * Her modtager vi al den data idépersonen har indtastet i form af en Idea idea.
     * Vi bruger herefter en UPDATE sql statement til at opdatere idéens data i databasen
     */
    @Override
    public void updateIdea(Idea idea) {
        String sql = "UPDATE Idea SET idea_name = ?, idea_description = ? WHERE idea_id = ?";

            jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, idea.getIdeaName());
            preparedStatement.setString(2, idea.getIdeaDescription());
            preparedStatement.setInt(3,idea.getIdeaId());
        });
    }
    /**
     * Her modtager vi idéens ideaId og brugen den til at finde den rigtige idé der skal slettes
     */
    @Override
    public void deleteIdea(int id) {
        String sql = "DELETE FROM Idea WHERE idea_id = ?";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setInt(1,id);
        });
    }

    /**
     * Her henter vi alle idéerne i databasen og indsætter dem i en liste, som vi derefter sender tilbage
     * for at kunne vise listen.
     */
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

    /**
     * Her har vi lavet en metode der kan finde alle idéer der er lavet af en specifik idéperson.
     * Det gør via. det personId som vi modtager fra controlleren. Herefter laver vi et SELECT statement
     * til at finde dem, herefter indsætter vi dem i en liste.
     */
    @Override
    public List<Idea> getIdeaList(int id) {
        List<Idea> ideas = new ArrayList<>();
        String sql = "SELECT * FROM idebanken.Idea WHERE idea_person=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);
        while (sqlRowSet.next()) {

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
    /**
     * Her har vi en metoden der kan finde en idé ud fra ideaId'en
     */
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