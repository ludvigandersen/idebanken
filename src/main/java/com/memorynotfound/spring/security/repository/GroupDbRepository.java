package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Person;
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

        String sql = "INSERT INTO Group(group_id, group_name)"+
                "VALUES(DEFAULT, ? )";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });


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
    public List<Integer> getGroupIdsWithIdea(List<Integer> groupId) {
        List<Integer> groupIds = new ArrayList<>();
        String sql = "SELECT idea_id FROM idebanken.GroupIdea WHERE group_id IN (?)";
        sqlRowSet = jdbc.queryForRowSet(sql, groupId);

        while (sqlRowSet.next()){
            groupIds.add(sqlRowSet.getInt("idea_id"));
        }
        return groupIds;
    }
}