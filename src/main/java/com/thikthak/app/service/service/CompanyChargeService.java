package com.thikthak.app.service.service;

import com.thikthak.app.domain.service.ServiceOrders;
import com.thikthak.app.repository.service.ServiceOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class CompanyChargeService {


    private final ServiceOrdersRepository repository;

    @Autowired
    public CompanyChargeService(ServiceOrdersRepository repository) {
        this.repository = repository;
    }


    public Page<ServiceOrders> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll((Specification<ServiceOrders>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            // always fix
            p = cb.and(p, cb.equal(root.get("status"), 2));
            // dynamic by params
            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("city")){
                    if (!StringUtils.isEmpty(clientParams.get("city"))) {
                        p = cb.and(p, cb.like(root.get("city"), "%" + clientParams.get("city") + "%"));
                    }
                }

                if(clientParams.containsKey("orderCode")){
                    if (!StringUtils.isEmpty(clientParams.get("orderCode"))) {
                        p = cb.and(p, cb.like(root.get("orderCode"), "%" + clientParams.get("orderCode") + "%"));
                    }
                }

                if(clientParams.containsKey("clientName")){
                    if (!StringUtils.isEmpty(clientParams.get("clientName"))) {
                        p = cb.and(p, cb.like(root.get("clientUserName"), "%" + clientParams.get("clientName") + "%"));
                    }
                }

                if(clientParams.containsKey("technicianName")){
                    if (!StringUtils.isEmpty(clientParams.get("technicianName"))) {
                        p = cb.and(p, cb.like(root.get("technicianUserName"), "%" + clientParams.get("technicianName") + "%"));
                    }
                }

                if(clientParams.containsKey("orderDateFrom") && clientParams.containsKey("orderDateTo")){
                    String orderPlaceTimeFrom = clientParams.get("orderDateFrom");
                    String orderPlaceTimeTo = clientParams.get("orderDateTo");
                    if(!orderPlaceTimeFrom.equals("") && !orderPlaceTimeTo.equals("")){
                        Date orderPlaceTimeFromDate = new Date();
                        Date orderPlaceTimeToDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            orderPlaceTimeFromDate = sdf.parse(orderPlaceTimeFrom);
                            orderPlaceTimeToDate = sdf.parse(orderPlaceTimeTo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(Objects.nonNull(clientParams.get("orderDateFrom"))  && Objects.nonNull(clientParams.get("orderDateTo")) ){
                            p = cb.and(p, cb.between(root.get("orderPlaceTime"), orderPlaceTimeFromDate, orderPlaceTimeToDate));
                        }
                    }

                }


                return p;
            }
            return p;
        }, pageable);

    }






}
