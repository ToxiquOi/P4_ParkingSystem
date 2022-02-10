package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class TicketDaoTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        this.ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
    }

    @AfterEach
    private void tearDownPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    void testSaveTicket() {
        Assertions.assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test
    void testSaveNullTicket() {
        Assertions.assertFalse(ticketDAO.saveTicket(null));
    }

    @Test
    void testGetTicket() {
        testSaveTicket();
        Ticket ticket1 = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        Assertions.assertEquals(1, ticket1.getId());
        Assertions.assertEquals(ticket.getVehicleRegNumber(), ticket1.getVehicleRegNumber());
        Assertions.assertEquals(ticket.getOutTime(), ticket1.getOutTime());
        Assertions.assertEquals(ticket.getParkingSpot().getId(), ticket1.getParkingSpot().getId());
    }

    @Test
    void testGetTicketFromUnknownVehicule() {
        Assertions.assertNull(ticketDAO.getTicket("ZZZZ"));
    }

    @Test
    void testUpdateExistingTicket() {
        testSaveTicket();
        ticket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        ticket.setOutTime(new Date());

        Assertions.assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    void testUpdateUnknownTicket() {
        Ticket ticket1 = new Ticket();
        ticket1.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket1.setOutTime(new Date(System.currentTimeMillis()));
        ticket1.setVehicleRegNumber("TOTO");

        Assertions.assertFalse(ticketDAO.updateTicket(ticket1));
    }

    @Test
    void testUpdateExistingUncompleteTicket() {
        testSaveTicket();
        Assertions.assertFalse(ticketDAO.updateTicket(ticket));
    }

    @Test
    void testCountingTicketContainingRegNumber() {
        testSaveTicket();
        Assertions.assertEquals(1, ticketDAO.countTicketContainingRegNumber(ticket.getVehicleRegNumber()));
        testSaveTicket();
        Assertions.assertEquals(2, ticketDAO.countTicketContainingRegNumber(ticket.getVehicleRegNumber()));
    }

    @Test
    void testCountingTicketsContainingUnknownRegNumber() {
        Assertions.assertEquals(0, ticketDAO.countTicketContainingRegNumber(ticket.getVehicleRegNumber()));
    }

    @Test
    void testCountingTicketsContainingBadValueRegNumber() {
        Assertions.assertEquals(0, ticketDAO.countTicketContainingRegNumber(null));
    }
}
