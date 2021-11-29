package com.thikthak.app.acl.auth.service;

import com.thikthak.app.acl.auth.domain.UserRole;
import com.thikthak.app.acl.auth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleRepository repository;

    @Autowired
    public UserRoleService(UserRoleRepository repository){
        this.repository = repository;
    }


    public List<UserRole> getAll() {
        List<UserRole> result = repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Page< UserRole > getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);

    }


    public UserRole findById(Long id) throws Exception {
        Optional<UserRole> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public UserRole getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){
    }

    public UserRole createOrUpdate(UserRole entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity = repository.save(entity);

        } else {
            Optional<UserRole> entityOptional = repository.findById(entity.getId());
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
        Optional<UserRole> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }



}
