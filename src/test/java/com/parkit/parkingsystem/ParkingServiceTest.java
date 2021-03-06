package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    void processIncomingVehicleTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    void processExitingVehicleTest() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);


        parkingService.processExitingVehicle();


        verify(ticketDAO, Mockito.times(1)).getTicket(any(String.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    void getNextCarParkingNumberIfAvailable() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot rqParkingSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertEquals(parkingSpot.getId(), rqParkingSpot.getId());
        Assertions.assertEquals(parkingSpot.getParkingType(), rqParkingSpot.getParkingType());
        Assertions.assertEquals(parkingSpot.isAvailable(), rqParkingSpot.isAvailable());
    }

    @Test
    void getNextBikeParkingNumberIfAvailable() {
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        ParkingSpot rqParkingSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertEquals(parkingSpot.getId(), rqParkingSpot.getId());
        Assertions.assertEquals(parkingSpot.getParkingType(), rqParkingSpot.getParkingType());
        Assertions.assertEquals(parkingSpot.isAvailable(), rqParkingSpot.isAvailable());
    }

    @Test
    void getNextParkingNumberIfAvailableBadInput() {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    void getNextParkingNumberIfAvailableErrorFetching() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

        Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
    }
}
