package com.repository;

import com.model.Group;
import com.model.Person;

import java.util.List;
/**
 * Dette er vores interface for GroupDbRepository
 * @author Christoffer
 * @author Nicolai
 */

public interface IGroupDbRepository {
    void createGroup(Group group);
    void createGroup(Group group, boolean locked);
    void deleteGroup(int id);
    List<Person> read(int id);
    int findGroup(String name);
    void updateGroup(String name, int id);
    String findGroupName(int Id);
    void addMember(int groupId, int personId);
    List<Integer> getGroupIdsWithPerson(int personId);
    List<Integer> getIdeaIdsWithGroup(List<Integer> groupId);
    List<Group> getDeveloperGroupsWithPersonId(int personId);
    List<Group> getGroupsWithPersonIn(int personId);
    void assignGroupToIdea(int ideaId, int groupId);
    void assignPersonToGroup(int personId, int groupId);
    int getGroupIdWithName(String email);
}
