package com.thikthak.app.service.service;

import com.thikthak.app.domain.service.ServiceItems;
import com.thikthak.app.repository.service.ServiceItemsRepository;
import com.thikthak.app.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceItemsService {


    private final ServiceItemsRepository repository;

    @Autowired
    public ServiceItemsService(ServiceItemsRepository repository){
        this.repository = repository;
    }


    public List<ServiceItems> getAll() {
        List<ServiceItems> result = repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Page< ServiceItems > getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);

    }


    public ServiceItems findById(Long id) throws Exception {
        Optional<ServiceItems> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public ServiceItems getById(Long id) throws Exception {
        return this.findById(id);
    }


    public ServiceItems setAttributeForCreateUpdate(ServiceItems tnxEntity, ServiceItems dbEntity){
        String itemName = tnxEntity.getItemName();
        String itemDescription = tnxEntity.getItemDescription();
        if(itemDescription == null || Objects.equals(itemDescription, "")) tnxEntity.setItemDescription(itemName);

        String loginUser = UserUtil.getLoginUser();
        if(tnxEntity.getId() == null) {
            // rActiveOperation = Create
            tnxEntity.setCreationUser(loginUser);
            tnxEntity.setCreationDateTime(new Date());
        } else {
            // rActiveOperation = Update
            dbEntity.setLastUpdateUser(loginUser);
            dbEntity.setLastUpdateDateTime(new Date());
            return dbEntity;
        }

        return tnxEntity;
    }


    public ServiceItems createOrUpdate(ServiceItems entity) {

        // entity = this.setAttributeForCreateUpdate(entity);

        if(entity.getId() == null) {
            entity = this.setAttributeForCreateUpdate(entity, null);
            entity = repository.save(entity);

        } else {
            Optional<ServiceItems> entityOptional = repository.findById(entity.getId());
            if(entityOptional.isPresent()) {
                ServiceItems editEntity = entityOptional.get();
                editEntity = this.setAttributeForCreateUpdate(entity, editEntity);
                // others attribute set
                editEntity.setItemCode(entity.getItemCode());
                editEntity.setItemName(entity.getItemName());
                editEntity.setItemDescription(entity.getItemDescription());
                editEntity.setIconName(entity.getIconName());
                editEntity.setIconImgPath(entity.getIconImgPath());
                editEntity.setMinPrice(entity.getMinPrice());
                editEntity.setRegularPrice(entity.getRegularPrice());
                editEntity.setSequence(entity.getSequence());
                editEntity.setActive(entity.getActive());
                editEntity = repository.save(editEntity);
                return editEntity;
                //entity = repository.save(entity);
            }
        }
        return entity;

    }


    public void deleteById(Long id) throws Exception {
        Optional<ServiceItems> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }



}
