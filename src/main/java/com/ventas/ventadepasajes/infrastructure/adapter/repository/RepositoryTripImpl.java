package com.ventas.ventadepasajes.infrastructure.adapter.repository;

import com.ventas.ventadepasajes.domain.exceptions.ExceptionGeneral;
import com.ventas.ventadepasajes.domain.model.entity.Trip;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryTrip;
import com.ventas.ventadepasajes.infrastructure.adapter.repository.mapper.MapperTrip;
import com.ventas.ventadepasajes.infrastructure.entity.EntityTrip;
import com.ventas.ventadepasajes.infrastructure.jparepository.JpaTripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositoryTripImpl implements RepositoryTrip {

    private JpaTripRepository jpaTripRepository;
    private Logger logger = LoggerFactory.getLogger(RepositoryTripImpl.class);
    private MapperTrip mapperTrip = new MapperTrip();

    public RepositoryTripImpl(JpaTripRepository jpaTripRepository){this.jpaTripRepository = jpaTripRepository;}

    @Override
    public Trip createTrip(Trip trip) {
        EntityTrip entityTrip = this.mapperTrip.modelToEntity(trip);
        EntityTrip entityTripSaved = this.jpaTripRepository.save(entityTrip);
        return new Trip(entityTripSaved.getId(), entityTripSaved.getSeatsAvailable(), entityTripSaved.getSeatsSold(), entityTripSaved.getStartCity(), entityTripSaved.getEndCity(), entityTripSaved.getIdDriver(), entityTripSaved.getTripDate(), entityTrip.getTicketAmount());
    }

    @Override
    public List<Trip> listTrip() {
        return this.mapperTrip.entityToModelList(this.jpaTripRepository.findAll());
    }

    @Override
    public boolean deleteTrip(long id) {

        try{
            this.jpaTripRepository.deleteById(id);
            return true;
        }catch (Exception e){
            logger.info("Error deleting trip");
            return false;
        }
    }

    @Override
    public Trip updateTrip(long id, Trip newTrip) {
        EntityTrip entityTrip = this.mapperTrip.modelToEntity(newTrip);
        EntityTrip entityTripUpdated = this.jpaTripRepository.findById(id)
                .map(trip ->{
                    trip.setId(newTrip.getId());
                    trip.setSeatsAvailable(newTrip.getSeatsAvailable());
                    trip.setSeatsSold(newTrip.getSeatsSold());
                    trip.setStartCity(newTrip.getStartCity());
                    trip.setEndCity(newTrip.getEndCity());
                    trip.setTicketAmount(newTrip.getTicketAmount());
                    trip.setIdDriver(newTrip.getIdDriver());
                    return jpaTripRepository.save(trip);
                }).orElseGet(()->{
                    entityTrip.setId(id);
                    entityTrip.setSeatsAvailable(newTrip.getSeatsAvailable());
                    entityTrip.setSeatsSold(newTrip.getSeatsSold());
                    entityTrip.setStartCity(newTrip.getStartCity());
                    entityTrip.setEndCity(newTrip.getEndCity());
                    entityTrip.setIdDriver(newTrip.getIdDriver());
                    entityTrip.setTicketAmount(newTrip.getTicketAmount());
                    return jpaTripRepository.save(entityTrip);
                });
        return new Trip(entityTripUpdated.getId(), entityTrip.getSeatsAvailable(), entityTrip.getSeatsSold(), entityTripUpdated.getStartCity(), entityTrip.getEndCity(), entityTrip.getIdDriver(), entityTrip.getTripDate(), entityTrip.getTicketAmount());
    }

    @Override
    public Trip searchTrip(long id){
        try{
            return this.mapperTrip.entityToModel(this.jpaTripRepository.searchTrip(id));
        }catch (Exception e){
            throw new ExceptionGeneral("There are not trips with id " + id);
        }
    }
}
