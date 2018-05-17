package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IGroupDbRepository {
    void createGroup(Group group);
    void createGroup(Group group, boolean locked);
    Group read(int id);
    int findGroup(String name);
    void addMember(int groupId, int personId);
    List<Integer> getGroupIdsWithPerson(int personId);
    List<Integer> getIdeaIdsWithGroup(List<Integer> groupId);
    List<Group> getDeveloperGroupsWithPersonId(int personId);
    List<Group> getGroupsWithPersonIn(int personId);
    void assignGroupToIdea(int ideaId, int groupId);
    void assignPersonToGroup(int personId, int groupId);
    int getGroupIdWithName(String email);
}
