package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FareCalculatorService {

    private static final Logger logger = LogManager.getLogger("FareCalculatorService");

    private final TicketDAO ticketDAO;

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double milliDuration = outHour - inHour;
        double hoursDuration = ((milliDuration / 1000) / 60) / 60;

        // 30min reduction
        double hoursWithRemise = (hoursDuration > 0.5) ? hoursDuration - 0.5 : 0;

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                double price = hoursWithRemise * Fare.CAR_RATE_PER_HOUR;
                ticket.setPrice(calcRemise(price, ticket.getVehicleRegNumber()));
                break;
            }
            case BIKE: {
                double price = hoursWithRemise * Fare.BIKE_RATE_PER_HOUR;
                ticket.setPrice(calcRemise(price, ticket.getVehicleRegNumber()));
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    private double calcRemise(double price, String vehicleRegNumber) {
        double remise = 0;
        if (ticketDAO.countTicketContainingRegNumber(vehicleRegNumber) > 2) {
            logger.info("Your a regular user, we offer to you 5 percent reduction on the total cost of your ticket");
            if(price > 0.0)
                remise = (price / 100) * 5;
        }
        return price - remise;
    }
}