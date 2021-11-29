package com.thikthak.app.repository.base;

import com.thikthak.app.domain.base.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
