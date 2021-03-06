package com.jango.assignmenttracker.repository;

import com.jango.assignmenttracker.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    @Query(value = "SELECT * FROM roles", nativeQuery = true)
    List<Roles> findAllRoles();

    Roles findByName(String name);

}
