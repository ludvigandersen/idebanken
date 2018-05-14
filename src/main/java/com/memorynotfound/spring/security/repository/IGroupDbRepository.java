package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;

public interface IGroupDbRepository {
    void createGroup(Group group);
}
