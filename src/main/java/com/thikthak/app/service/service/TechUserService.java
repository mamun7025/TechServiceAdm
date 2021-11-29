package com.thikthak.app.service.service;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.domain.UserRole;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.acl.auth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TechUserService {

    private final UserRepository repository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public TechUserService(UserRepository repository, UserRoleRepository userRoleRepository) {
        this.repository = repository;
        this.userRoleRepository = userRoleRepository;
    }


    public Page<User> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll((Specification<User>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            // always fix
            p = cb.and(p, cb.like(root.get("userType"), "Technician"));
            // dynamic by params
            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("city")){
                    if (!StringUtils.isEmpty(clientParams.get("city"))) {
                        p = cb.and(p, cb.like(root.get("city"), "%" + clientParams.get("city") + "%"));
                    }
                }

                return p;
            }
            return p;
        }, pageable);

    }



    public User findById(Long id) throws Exception {
        Optional<User> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public User getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){
        System.out.println("Abc");
    }

    public User createOrUpdate(User entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity.setPassword(new BCryptPasswordEncoder().encode( entity.getPassword()));
            entity = repository.save(entity);

        } else {
            Optional<User> entityOptional = repository.findById(entity.getId());
            if(entityOptional.isPresent()) {
                User editEntity = entityOptional.get();
                String oldPassword = editEntity.getPassword();
                String tnxPassword = entity.getPassword();
                if(!oldPassword.equals(tnxPassword)) entity.setPassword(new BCryptPasswordEncoder().encode( entity.getPassword()));
//                editEntity.setDisplayName(entity.getDisplayName());
//                editEntity.setPhoneNumber(entity.getPhoneNumber());
//                editEntity = repository.save(editEntity);
//                return editEntity;
                List<UserRole> userRoleList = userRoleRepository.getAllByUser(entity);
                entity = repository.save(entity);
                // after save-push roles again
                for (UserRole userRole : userRoleList) {
                    UserRole userRole1 = new UserRole();
                    userRole1.setRole(userRole.getRole());
                    userRole1.setUser(userRole.getUser());
                    userRoleRepository.save(userRole1);
                }
            }
        }
        return entity;

    }

    public void deleteById(Long id) throws Exception {
        Optional<User> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }




}
