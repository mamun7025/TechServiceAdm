package com.thikthak.app.service.service;

import com.thikthak.app.domain.service.ProdServicePriceRateList;
import com.thikthak.app.repository.service.ProdServicePriceRateListRepository;
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
public class ProdServicePriceRateListService {

    private final ProdServicePriceRateListRepository repository;

    @Autowired
    public ProdServicePriceRateListService(ProdServicePriceRateListRepository repository){
        this.repository = repository;
    }


    public List<ProdServicePriceRateList> getAll() {
        List<ProdServicePriceRateList> result = repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Page<ProdServicePriceRateList > getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);

    }


    public ProdServicePriceRateList findById(Long id) throws Exception {
        Optional<ProdServicePriceRateList> entity = repository.findById(id);

        if(entity.isPresent()) {
            return entity.get();
        } else {
            throw new Exception("No record exist for given id");
        }
    }

    public ProdServicePriceRateList getById(Long id) throws Exception {
        return this.findById(id);
    }


    public void setAttributeForCreateUpdate(){

    }

    public ProdServicePriceRateList createOrUpdate(ProdServicePriceRateList entity) {

        this.setAttributeForCreateUpdate();

        if(entity.getId() == null) {
            entity = repository.save(entity);

        } else {
            Optional<ProdServicePriceRateList> entityOptional = repository.findById(entity.getId());
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
        Optional<ProdServicePriceRateList> entity = repository.findById(id);

        if(entity.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new Exception("No record exist for given id");
        }
    }
}
