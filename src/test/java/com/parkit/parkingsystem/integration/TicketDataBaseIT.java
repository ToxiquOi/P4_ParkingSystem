package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(1);
    }

    @AfterEach
    private void tearDownPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    void testTicketSavedWhenVehicleIncoming() throws SQLException, ClassNotFoundException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        int result = 0;

        ResultSet rs = dataBaseTestConfig
                .getConnection()
                .prepareStatement("select count(0) from ticket where OUT_TIME IS NULL and VEHICLE_REG_NUMBER='ABCDEF'")
                .executeQuery();

        if (rs.next()) {
            result = rs.getInt(1);
        }

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        Assertions.assertEquals(1, result);
    }

    @Test
    void testTicketUpdatedWhenVehicleExiting() throws SQLException, ClassNotFoundException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();

        int result = 0;

        ResultSet rs = dataBaseTestConfig
                .getConnection()
                .prepareStatement("select count(0) from ticket where OUT_TIME IS NOT NULL and VEHICLE_REG_NUMBER='ABCDEF'")
                .executeQuery();

        if (rs.next()) {
            result = rs.getInt(1);
        }

        verify(parkingSpotDAO, Mockito.times(2)).updateParking(any(ParkingSpot.class));
        Assertions.assertEquals(1, result);
    }
}
