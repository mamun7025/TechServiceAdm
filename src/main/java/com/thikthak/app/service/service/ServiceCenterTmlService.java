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
import java.util.Optional;

@Service
public class ServiceCenterTmlService {


    private final ServiceOrdersRepository repository;

    @Autowired
    public ServiceCenterTmlService(ServiceOrdersRepository repository) {
        this.repository = repository;
    }


    public Page<ServiceOrders> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll((Specification<ServiceOrders>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            // always fix
            p = cb.and(p, cb.equal(root.get("servicePartsRequired"), true));
            p = cb.and(p, cb.equal(root.get("isAgreed"), true));
//            p = cb.and(p, cb.equal(root.get("servicePartsProvided"), false));
//            p = cb.and(p, cb.equal(root.get("servicePartsRequiredAcknlg"), true));
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("servicePartsProvided")){
                    if (!StringUtils.isEmpty(clientParams.get("servicePartsProvided")) && clientParams.get("servicePartsProvided").equals("1")) {
                        p = cb.and(p, cb.equal(root.get("servicePartsProvided"), true));
                    }
                }
            } else {
                p = cb.and(p, cb.equal(root.get("servicePartsProvided"), false));
            }

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



    public ServiceOrders findById(Long id) throws Exception {
        Optional<ServiceOrders> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }





}
