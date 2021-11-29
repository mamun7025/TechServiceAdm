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
import java.util.*;

@Service
public class ServiceOrdersService {

    public Map<String, String> clientParams;

    private final ServiceOrdersRepository repository;

    @Autowired
    public ServiceOrdersService(ServiceOrdersRepository repository){
        this.repository = repository;
    }


    public List<ServiceOrders> getAll() {
        List<ServiceOrders> result = repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Page< ServiceOrders > getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        this.clientParams = clientParams;

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        // return repository.findAll(pageable);
        return repository.findAll((Specification<ServiceOrders>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){


                if(clientParams.containsKey("orderPlaceTimeFrom") && clientParams.containsKey("orderPlaceTimeTo")){
                    String orderPlaceTimeFrom = clientParams.get("orderPlaceTimeFrom");
                    String orderPlaceTimeTo = clientParams.get("orderPlaceTimeTo");
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
                        if(Objects.nonNull(clientParams.get("orderPlaceTimeFrom"))  && Objects.nonNull(clientParams.get("orderPlaceTimeTo")) ){
                            p = cb.and(p, cb.between(root.get("orderPlaceTime"), orderPlaceTimeFromDate, orderPlaceTimeToDate));
                        }
                    }

                }
                if(clientParams.containsKey("orderCode")){
                    if (!StringUtils.isEmpty(clientParams.get("orderCode"))) {
                        p = cb.and(p, cb.like(root.get("orderCode"), "%" + clientParams.get("orderCode") + "%"));
                    }
                }

                // Integer status = 0; // Pending-0, Accept-1, Complete-2 Reject-3
                if(clientParams.containsKey("adv_byChooseParam")){
                    if (!StringUtils.isEmpty(clientParams.get("adv_byChooseParam"))) {
                        String adv_byChooseParam = clientParams.get("adv_byChooseParam");
                        if(Objects.equals(adv_byChooseParam, "adv_today_cs")){
                            p = cb.and(p, cb.equal(root.get("status"), 2));
                            p = cb.and(p, cb.between(root.get("orderPlaceTime"), new Date(), new Date()));

                        } else if(Objects.equals(adv_byChooseParam, "adv_today_ps")){
                            p = cb.and(p, cb.equal(root.get("status"), 0));
                            p = cb.and(p, cb.between(root.get("orderPlaceTime"), new Date(), new Date()));

                        } else if(Objects.equals(adv_byChooseParam, "adv_all_cs")){
                            p = cb.and(p, cb.equal(root.get("status"), 2));

                        } else if(Objects.equals(adv_byChooseParam, "adv_all_ps")){
                            p = cb.and(p, cb.equal(root.get("status"), 0));

                        } else if(Objects.equals(adv_byChooseParam, "adv_all_as")){
                            p = cb.and(p, cb.equal(root.get("status"), 1));

                        } else if(Objects.equals(adv_byChooseParam, "adv_all_rs")){
                            p = cb.and(p, cb.equal(root.get("status"), 3));

                        }
                    }
                }

                // cq.orderBy(cb.desc(root.get("name")), cb.asc(root.get("id")));
                return p;
            }

            return null;
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

    public ServiceOrders getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){
    }

    public ServiceOrders createOrUpdate(ServiceOrders entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity = repository.save(entity);

        } else {
            Optional<ServiceOrders> entityOptional = repository.findById(entity.getId());
            if(entityOptional.isPresent()) {
//                SystemMenu editEntity = entityOptional.get();
//                editEntity.setDisplayName(entity.getDisplayName());
//                editEntity.setPhoneNumber(entity.getPhoneNumber());
//                editEntity = repository.save(editEntity);
//                return editEntity;
                entity = repository.save(entity);
            }
        }
        return entity;

    }


    public void deleteById(Long id) throws Exception {
        Optional<ServiceOrders> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }



}
