package com.thikthak.app.repository.service;

import com.thikthak.app.domain.service.ServiceOrders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrdersRepository extends JpaRepository<ServiceOrders, Long>, JpaSpecificationExecutor<ServiceOrders> {

    List<ServiceOrders> findAllByTechnicianUserIsNull();
//    List<ServiceOrders> findAllByIsScheduleOrderFalseAndTechnicianUserIsNull();
    List<ServiceOrders> findAllByIsScheduleOrderFalseAndTechnicianUserIsNull( Pageable pageable );
    List<ServiceOrders> findAllByIsScheduleOrderAndTechnicianUserIsNull(Boolean isScheduleOrder, Pageable pageable);

}
