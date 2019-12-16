package com.ventas.ventadepasajes.infrastructure.adapter.repository;

import com.ventas.ventadepasajes.domain.model.entity.Driver;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryDriver;
import com.ventas.ventadepasajes.infrastructure.entity.EntityDriver;
import com.ventas.ventadepasajes.infrastructure.jparepository.JpaDriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RepositoryDriverImpl implements RepositoryDriver {

    private ModelMapper modelMapper = new ModelMapper();
    private JpaDriverRepository jpaDriverRepository;

    public RepositoryDriverImpl(JpaDriverRepository jpaDriverRepository){this.jpaDriverRepository = jpaDriverRepository;}

    @Override
    public Driver createDriver(Driver driver) {
        EntityDriver entityDriver = this.modelMapper.map(driver, EntityDriver.class);
        EntityDriver entityDriverSaved = this.jpaDriverRepository.save(entityDriver);
        return new Driver(entityDriverSaved.getId(), entityDriverSaved.getName(), entityDriverSaved.getLastName(), entityDriverSaved.getIdentification());
    }

    @Override
    public List<Driver> listDriver() {
        List<EntityDriver> listEntity = this.jpaDriverRepository.findAll();
        return listEntity.stream().map(e->Driver.valueOf(e)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteDriver(Long id) {
        try{
            this.jpaDriverRepository.deleteById(id);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Driver updateDriver(Long id, Driver newDriver) {
        EntityDriver entityDriver = this.modelMapper.map(newDriver, EntityDriver.class);
        EntityDriver entityDriverUpdated =  jpaDriverRepository.findById(id)
                .map(driver ->{
                    driver.setName(newDriver.getName());
                    driver.setLastName(newDriver.getLastName());
                    driver.setIdentification(newDriver.getIdentification());
                    return jpaDriverRepository.save(driver);
                })
                .orElseGet(()->{
                    entityDriver.setId(id);
                    entityDriver.setName(newDriver.getName());
                    entityDriver.setLastName(newDriver.getLastName());
                    entityDriver.setIdentification(newDriver.getIdentification());
                    return jpaDriverRepository.save(entityDriver);
                });
        return new Driver(entityDriverUpdated.getId(), entityDriverUpdated.getName(), entityDriverUpdated.getLastName(), entityDriverUpdated.getIdentification());
    }


}
