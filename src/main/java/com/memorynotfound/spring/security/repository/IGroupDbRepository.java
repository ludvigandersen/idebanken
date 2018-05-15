package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IGroupDbRepository {
    void createGroup(Group group);
    List<Integer> getGroupIdsWithPerson(int personId);
    List<Integer> getIdeaIdsWithGroup(List<Integer> groupId);
    List<Group> getGroupsWithPersonIn(int personId);
    void assignGroupToIdea(int ideaId, int groupId);
}
