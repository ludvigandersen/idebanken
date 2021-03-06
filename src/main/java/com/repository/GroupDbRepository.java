package com.repository;

import com.model.Group;
import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoffer
 * @author Mikkel
 * @author Nicolai
 */

@Repository
public class GroupDbRepository implements IGroupDbRepository {

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    /**
     * Her opretter vi en gruppe med det indtastet gruppenavn.
     */
    @Override
    public void createGroup(Group group){

        String sql = "INSERT INTO idebanken.Group (group_id, group_name, locked)"+
                "VALUES (default, ?, default)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });
    }

    /**
     * Her modtager vi gruppens groupId, som vi bruger i et DELETE statement for at slette den rigtige.
     */
    @Override
    public void deleteGroup(int id) {
        jdbc.update("DELETE FROM idebanken.Group WHERE group_id = ?", id);
    }

    /**
     * Her g&oslash;r vi brug af groupId og personId for at tilf&oslash;je udvikler til en gruppe.
     */
    @Override
    public void addMember(int groupId, int personId){
        String sql = "INSERT INTO idebanken.DeveloperGroup "+
                "SET DeveloperGroup.developer_group_id = default, " +
                "DeveloperGroup.person_id = (SELECT Person.person_id FROM idebanken.Person WHERE Person.person_id = ?), " +
                "DeveloperGroup.group_id = (SELECT Group.group_id FROM idebanken.Group WHERE Group.group_id = ?)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2, groupId);
        });

    }

    /**
     * Her kan vi modtager vi et navn og gruppens groupId, som vi bruger i et UPDATE statement for
     * at opdatere gruppens data.
     */
    @Override
    public void updateGroup(String name, int id) {
        String sql = "UPDATE idebanken.Group SET group_name = ? WHERE group_id = ?";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
        });
    }

    /**
     * Metoden bliver brugt til at inds&aelig;tte en gruppe i databasen, som kun m&aring; have 1 Person tilknyttet.
     */
    @Override
    public void createGroup(Group group, boolean locked) {
        String sql = "INSERT INTO idebanken.Group (group_id, group_name, locked)"+
                "VALUES (default, ?, 1)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });
    }

    /**
     * Her henter vi gruppens detaljer og inds&aelig;tter i en liste ved brug af groupId
     */
    @Override
    public List<Person> read(int id) {
        List<Person> personList = new ArrayList<>();
        String sql = "SELECT Group.group_id, Group.group_name, Group.locked, DeveloperGroup.person_id, " +
                "Person.first_name, Person.last_name, Person.email, city.city FROM idebanken.Group " +
                "INNER JOIN idebanken.DeveloperGroup ON Group.group_id = DeveloperGroup.group_id " +
                "INNER JOIN idebanken.Person ON DeveloperGroup.person_id = Person.person_id " +
                "INNER JOIN idebanken.city ON Person.zip_code = city.zip_code " +
                "WHERE Group.group_id = ?" ;
        sqlRowSet = jdbc.queryForRowSet(sql, id);
        while (sqlRowSet.next()){

            personList.add(new Person(
                    sqlRowSet.getString("first_name"),
                    sqlRowSet.getString("last_name"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("city")
                    ));
            System.out.println(sqlRowSet.getString("first_name"));
        }
        return personList;
    }

    /**
     * Her modtager vi gruppens navn, som vi derefter kan bruge for at finde gruppens groupId
     */
    @Override
    public int findGroup(String name){
        String sql = "SELECT Group.group_id" +
                " FROM idebanken.Group WHERE Group.group_name = ?";

        sqlRowSet = jdbc.queryForRowSet(sql, name);
        while (sqlRowSet.next()){
            return sqlRowSet.getInt("group_id");
        }
        return 0;
    }

    /**
     * Her modtager vi gruppens groupId, som vi derefter kan bruge for at finde gruppens navn
     */
    @Override
    public String findGroupName(int id){
        String sql = "SELECT Group.group_name" +
                " FROM idebanken.Group WHERE Group.group_id = ?";

        sqlRowSet = jdbc.queryForRowSet(sql, id);
        while (sqlRowSet.next()){
            return sqlRowSet.getString("group_name");
        }
        return null;
    }

    /**
     * Bruges til at lave en liste med flere gruppe id´er som en person er tilknyttet.
     */
    @Override
    public List<Integer> getGroupIdsWithPerson(int personId) {
        List<Integer> groupIds = new ArrayList<>();
        String sql = "SELECT group_id FROM idebanken.DeveloperGroup WHERE person_id=?";
        sqlRowSet = jdbc.queryForRowSet(sql, personId);

        while (sqlRowSet.next()){
            groupIds.add(sqlRowSet.getInt("group_id"));
        }
        return groupIds;
    }

    /**
     * Bruges til at hente en liste med id&eacute; id´er, hvor grupper i listen med gruppe id´er er tilknyttet.
     */
    @Override
    public List<Integer> getIdeaIdsWithGroup(List<Integer> groupId) {
        List<Integer> groupIds = new ArrayList<>();
        String ids = "";
        boolean check = true;
        for (Integer i: groupId){
            if (check){
                ids += i;
                check = false;
            } else {
                ids += ", " + i;
            }
        }
        String sql = "SELECT idea_id FROM idebanken.GroupIdea WHERE group_id IN (?)";
        sqlRowSet = jdbc.queryForRowSet(sql, ids);

        while (sqlRowSet.next()){
            groupIds.add(sqlRowSet.getInt("idea_id"));
        }
        return groupIds;
    }

    /**
     * Her henter vi gruppeliste med personId
     */
    @Override
    public List<Group> getDeveloperGroupsWithPersonId(int personId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT Group.group_id, Group.group_name,Group.locked, DeveloperGroup.person_id FROM idebanken.Group " +
                "INNER JOIN DeveloperGroup ON Group.group_id = DeveloperGroup.group_id " +
                "WHERE Group.locked = 0 and DeveloperGroup.person_id = ?";
        sqlRowSet = jdbc.queryForRowSet(sql, personId);

        while (sqlRowSet.next()){
            groups.add(new Group(
                    sqlRowSet.getInt("group_id"),
                    sqlRowSet.getString("group_name")));
        }

        return groups;
    }

    /**
     * Returnerer en liste med gruppe id´er som en person er tilknyttet.
     */
    @Override
    public List<Group> getGroupsWithPersonIn(int personId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT Group.group_id, Group.group_name, DeveloperGroup.person_id FROM idebanken.Group " +
                "INNER JOIN DeveloperGroup ON Group.group_id = DeveloperGroup.group_id " +
                "WHERE DeveloperGroup.person_id = ?";
        sqlRowSet = jdbc.queryForRowSet(sql, personId);

        while (sqlRowSet.next()){
            groups.add(new Group(
                            sqlRowSet.getInt("group_id"),
                            sqlRowSet.getString("group_name")));
        }

        return groups;
    }

    /**
     * Bruges til at tilknytte en gruppe til en id&eacute;.
     */
    @Override
    public void assignGroupToIdea(int ideaId, int groupId) {
        if (!checkIfAlreadyAssigned(ideaId, groupId)) {
            String sql = "INSERT INTO GroupIdea (group_idea_id, group_id, idea_id, approved)" +
                    "VALUES (default, ?, ?, default)";

            jdbc.update(sql, preparedStatement -> {
                preparedStatement.setInt(1, groupId);
                preparedStatement.setInt(2, ideaId);
            });
            System.out.println("Updated");
        } else {
            System.out.println("Not updated");
        }
    }

    /**
     * Tilf&oslash;jer en person til en gruppe.
     */
    @Override
    public void assignPersonToGroup(int personId, int groupId) {
        String sql = "INSERT INTO DeveloperGroup (developer_group_id, person_id, group_id)"+
                "VALUES (default, ?, ?)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2, groupId);
        });
    }

    /**
     * Hent gruppe id ud fra en String.
     */
    @Override
    public int getGroupIdWithName(String email) {
        int groupId = 0;
        String sql = "SELECT group_id FROM idebanken.Group WHERE group_name=?";
        sqlRowSet = jdbc.queryForRowSet(sql, email);

        while (sqlRowSet.next()){
            groupId = sqlRowSet.getInt("group_id");
        }
        return groupId;
    }

    /**
     * Bruges til at checke om en gruppe allerede er tilknyttet en id&eacute;.
     */
    private boolean checkIfAlreadyAssigned(int ideaId, int groupId){
        List<Integer> assigned = new ArrayList<>();
        String sql = "SELECT group_idea_id FROM GroupIdea WHERE idea_id = ? AND group_id = ?";
        sqlRowSet = jdbc.queryForRowSet(sql, ideaId, groupId);

        while (sqlRowSet.next()){
            assigned.add(sqlRowSet.getInt("group_id"));
        }

        if (assigned.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}