package com.thikthak.app.repository.system;

import com.thikthak.app.domain.system.VisitorsLog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorsLogRepository extends JpaRepository<VisitorsLog,Long>, JpaSpecificationExecutor<VisitorsLog> {

}
