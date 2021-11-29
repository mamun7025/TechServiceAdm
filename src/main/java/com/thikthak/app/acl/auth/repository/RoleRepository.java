package com.thikthak.app.acl.auth.repository;

import com.thikthak.app.acl.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByAuthority(String authority);
}
