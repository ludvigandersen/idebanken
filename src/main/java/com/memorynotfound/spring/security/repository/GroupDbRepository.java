package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class GroupDbRepository implements IGroupDbRepository{

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;


    @Override
    public void createGroup(Group group){

        String sql = "INSERT INTO idebanken.Group (group_id, group_name, locked)"+
                "VALUES (default, ?, default)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });
    }

    @Override
    public void createGroup(Group group, boolean locked) {
        String sql = "INSERT INTO idebanken.Group (group_id, group_name, locked)"+
                "VALUES (default, ?, 1)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });
    }

    @Override
    public Group read(int id) {
        String sql = "SELECT group_id, group_name FROM idebanken.Group WHERE group_id = ?";

//        String sql = "SELECT group_id, group_name,locked, DeveloperGroup.person_id, Person.first_name FROM Group" +
//                "INNER JOIN DeveloperGroup ON Group.group_id = DeveloperGroup.group_id" +
//                "INNER JOIN Person on DeveloperGroup.person_id = Person.person_id" +
//                "WHERE locked = 0 and Group.group_id = " + id;
        sqlRowSet = jdbc.queryForRowSet(sql, id);
        while (sqlRowSet.next()){

            return new Group(
                    sqlRowSet.getInt("group_id"),
                    sqlRowSet.getString("group_name")
            );
        }
        return null;
    }

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

    @Override
    public void assignGroupToIdea(int ideaId, int groupId) {
        if (!checkIfAlreadyAssigned(ideaId, groupId)) {
            String sql = "INSERT INTO GroupIdea (group_idea_id, group_id, idea_id, approved)" +
                    "VALUES (default, , ?, default)";

            jdbc.update(sql, preparedStatement -> {
                preparedStatement.setInt(1, groupId);
                preparedStatement.setInt(2, ideaId);
            });
            System.out.println("Updated");
        } else {
            System.out.println("Not updated");
        }
    }

    @Override
    public void assignPersonToGroup(int personId, int groupId) {
        String sql = "INSERT INTO DeveloperGroup (developer_group_id, person_id, group_id)"+
                "VALUES (default, ?, ?)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2, groupId);
        });
    }

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