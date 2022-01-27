package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.VehicleDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    VehicleDAO vehicleDao = new VehicleDAO();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double milliDuration = outHour - inHour;
        double hoursDuration = ((milliDuration /1000) / 60) / 60;

        // 30min reduction
        double hoursWithRemise = (hoursDuration > 0.5)? hoursDuration - 0.5 : 0;

        System.out.println(hoursWithRemise);
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice( hoursWithRemise * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice( hoursWithRemise * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}