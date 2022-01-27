package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.VehicleDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private VehicleDAO vehicleDao;

    public FareCalculatorService(VehicleDAO vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

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
                ticket.setPrice(calcRemise(hoursWithRemise * Fare.CAR_RATE_PER_HOUR, ticket.getVehicleRegNumber()));
                break;
            }
            case BIKE: {
                ticket.setPrice(calcRemise(hoursWithRemise * Fare.BIKE_RATE_PER_HOUR, ticket.getVehicleRegNumber()));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    private double calcRemise(double price, String vehicleRegNumber) {
        double remise = 0;
        if(!vehicleDao.isFirstUseOfParking(vehicleRegNumber)) {
            remise = (price / 100) * 5;
        }
        return price - remise;
    }
}