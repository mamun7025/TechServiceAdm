package com.thikthak.app.service.geoloc;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.domain.geoloc.UserLocation;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Map;
import java.util.Optional;

@Service
public class UserLocationService {


    private final UserLocationRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public UserLocationService(UserLocationRepository repository, UserRepository userRepository){
        this.repository = repository;
        this.userRepository = userRepository;
    }


    /*public Page<UserLocation> getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);

    }*/

    public Page<UserLocation> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll((Specification<UserLocation>) (root, cq, cb) -> {
            Join<UserLocation, User> joinUser = root.join("user", JoinType.INNER);
            Predicate p = cb.conjunction();
            // always fix
//            p = cb.and(p, cb.equal(root.get("status"), 2));
            // dynamic by params
            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("city")){
                    if (!StringUtils.isEmpty(clientParams.get("city"))) {
                        p = cb.and(p, cb.like(root.get("city"), "%" + clientParams.get("city") + "%"));
                    }
                }

                if(clientParams.containsKey("user")){
                    if (!StringUtils.isEmpty(clientParams.get("user"))) {
                        User userInst = this.userRepository.getUserByUsername(clientParams.get("user"));
                        if(userInst != null){
                            p = cb.and(p, cb.equal(root.get("user"),  userInst));
                        }
                    }
                }

                if(clientParams.containsKey("userType")){
                    if (!StringUtils.isEmpty(clientParams.get("userType"))) {
                        p = cb.and(p, cb.like(joinUser.get("userType"), "%" + clientParams.get("userType") + "%"));
                    }
                }

                return p;
            }
            return p;
        }, pageable);

    }



    public UserLocation findById(Long id) throws Exception {
        Optional<UserLocation> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public UserLocation getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){
    }

    public UserLocation createOrUpdate(UserLocation entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity = repository.save(entity);

        } else {
            Optional<UserLocation> entityOptional = repository.findById(entity.getId());
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
        Optional<UserLocation> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }




}
