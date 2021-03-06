package com.ventas.ventadepasajes.domain.service.trip;

import com.ventas.ventadepasajes.domain.exceptions.ExceptionGeneral;
import com.ventas.ventadepasajes.domain.model.entity.Trip;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryDriver;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryTrip;

public class ServiceCreateTrip {

    private RepositoryTrip repositoryTrip;
    private RepositoryDriver repositoryDriver;

    public ServiceCreateTrip(RepositoryTrip repositoryTrip, RepositoryDriver repositoryDriver){
        this.repositoryTrip = repositoryTrip;
        this.repositoryDriver = repositoryDriver;
    }

    public Trip run(Trip trip){
        if(!driverExists(trip.getIdDriver())){
            throw new ExceptionGeneral("Error: There are no drivers with id = " + trip.getIdDriver());
        }else{
            return this.repositoryTrip.createTrip(trip);
        }
    }

    private boolean driverExists(Long id){
        return this.repositoryDriver.searchDriver(id);
    }
}
