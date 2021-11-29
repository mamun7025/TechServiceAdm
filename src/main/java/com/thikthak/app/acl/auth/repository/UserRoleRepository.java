package com.thikthak.app.acl.auth.repository;

import com.thikthak.app.acl.auth.domain.Role;
import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> getAllByUser(User user);
    UserRole getByUserAndRole(User user, Role role);

}
