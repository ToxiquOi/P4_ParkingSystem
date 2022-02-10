package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDaoTest {

    private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private ParkingSpotDAO parkingSpotDAO;
    private DataBasePrepareService dataBasePrepareService;


    @BeforeEach
    private void setUpPerTest() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterEach
    private void tearDownPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    void testGetNextCarAvailableSlot() {
        Assertions.assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    void testGetNextBikeAvailableSlot() {
        Assertions.assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
    }

    @Test
    void testGetNextUnknownAvailableSlot() {
        Assertions.assertEquals(0, parkingSpotDAO.getNextAvailableSlot(ParkingType.UNKNOWN));
    }

    @Test
    void testGetNextAvailableSlotWithoutConnection() {
        parkingSpotDAO.setDataBaseConfig(null);
        Assertions.assertThrows(NullPointerException.class, () -> parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    void testUpdateCarParking() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Assertions.assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    void testUpdateBikeParking() {
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        Assertions.assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    void testUpdateUnknownParking() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN, false);
        Assertions.assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
}
