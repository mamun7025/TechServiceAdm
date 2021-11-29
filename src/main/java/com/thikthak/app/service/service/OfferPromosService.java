package com.thikthak.app.service.service;

import com.thikthak.app.domain.service.OfferPromos;
import com.thikthak.app.repository.service.OfferPromosRepository;
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
public class OfferPromosService {

    private final OfferPromosRepository repository;

    @Autowired
    public OfferPromosService (OfferPromosRepository repository){
        this.repository = repository;
    }


    public List<OfferPromos> getAll() {
        List<OfferPromos> result = repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Page< OfferPromos > getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);

    }


    public OfferPromos findById(Long id) throws Exception {
        Optional<OfferPromos> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public OfferPromos getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){
    }

    public OfferPromos createOrUpdate(OfferPromos entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity = repository.save(entity);

        } else {
            Optional<OfferPromos> entityOptional = repository.findById(entity.getId());
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
        Optional<OfferPromos> entity = repository.findById(id);

        if(entity.isPresent()) {
            // Option-1
            // repository.deleteById(id); // working by overriding and customizing this method in repository

            // Option-2
            OfferPromos entityInst = entity.get();
            entityInst.setOrganization(null);
            entityInst.setPromoUser(null);
            repository.delete(entityInst);
        } else {
            throw new Exception("No record exist for given id");
        }
    }




}
