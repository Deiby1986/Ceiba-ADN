package com.ventas.ventadepasajes.domain.service.purchase;

import com.ventas.ventadepasajes.domain.exceptions.ExceptionGeneral;
import com.ventas.ventadepasajes.domain.model.dto.DtoPurchase;
import com.ventas.ventadepasajes.domain.model.dto.DtoTrip;
import com.ventas.ventadepasajes.domain.model.entity.Purchase;

import com.ventas.ventadepasajes.domain.model.entity.Trip;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryPurchase;
import com.ventas.ventadepasajes.domain.port.repository.RepositoryTrip;
import com.ventas.ventadepasajes.domain.service.purchase.mapper.MapperPurchase;
import com.ventas.ventadepasajes.domain.service.trip.mapper.MapperTrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ServiceCreatePurchase {

    private RepositoryPurchase repositoryPurchase;
    private RepositoryTrip repositoryTrip;

    public ServiceCreatePurchase(RepositoryPurchase repositoryPurchase, RepositoryTrip repositoryTrip){
        this.repositoryPurchase = repositoryPurchase;
        this.repositoryTrip = repositoryTrip;
    }

    public DtoPurchase run(Purchase purchase) {
        int weekDay = 0;
        int discountPercentage = 0;
        MapperPurchase mapperPurchase = new MapperPurchase();
        Trip trip = getTrip(purchase.getIdTrip());
        if(trip.getSeatsAvailable() == 0){
            throw new ExceptionGeneral("Error: There are no free seats");
        }
        trip.setSeatsAvailable(trip.getSeatsAvailable()-1);
        trip.setSeatsSold(trip.getSeatsSold()+1);
        this.repositoryTrip.updateTrip(trip.getId(), trip);
        purchase.setTicketAmount(trip.getTicketAmount());
        double totalWithoutDiscount = purchase.getNumberPurchasedTickets() * purchase.getTicketAmount();
        try{
            purchase.setTripDate(trip.getTripDate());
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(trip.getTripDate());
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        }catch (Exception e){
            throw new ExceptionGeneral("Internal error getting the trip date");
        }

        if(purchase.getNumberPurchasedTickets()>=4 && weekDay>0) {
            if( weekDay<=4 ){
                discountPercentage = 20;
            }else{
                discountPercentage = 10;
            }
        }else if(weekDay >=1 && weekDay<=4){
            discountPercentage = 10;
        }
        else{
            purchase.setDiscountPercentage(0);
            purchase.setTotalPurchaseAmount(totalWithoutDiscount);
            purchase.setPurchaseDate(getDateNow());
        }
        purchase.setPurchaseDate(getDateNow());
        double discountAmount = totalWithoutDiscount*(discountPercentage)/100;
        purchase.setDiscountPercentage(discountPercentage);
        purchase.setTotalPurchaseAmount(totalWithoutDiscount-discountAmount);
        return mapperPurchase.entityToDto(this.repositoryPurchase.createPurchase(purchase));
    }

    public String getDateNow(){
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public Trip getTrip(long id){
        return this.repositoryTrip.searchTrip(id);
    }
}
