package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDbRepository implements IGroupDbRepository{

    @Autowired
    JdbcTemplate jdbc;


    @Override
    public void createGroup(Group group){

        String sql = "INSERT INTO Group(group_id, group_name)"+
                "VALUES(DEFAULT, ? )";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, group.getName());
        });


    }

}
