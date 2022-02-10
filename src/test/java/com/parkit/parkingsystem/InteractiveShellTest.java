package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

    @Mock
    private InputReaderUtil inputReaderUtil;
    @Mock
    private ParkingService parkingService;

    private InteractiveShell shell;

    @BeforeEach
    void setUp() {
        shell = new InteractiveShell(inputReaderUtil, parkingService);
    }

    @Test
    void loadInterfaceFirstOption() {
        when(inputReaderUtil.readSelection()).thenReturn(1, 3);

        shell.loadInterface();

        verify(parkingService, times(1)).processIncomingVehicle();
    }

    @Test
    void loadInterfaceSecondOption() {
        when(inputReaderUtil.readSelection()).thenReturn(2, 3);

        shell.loadInterface();

        verify(parkingService, times(1)).processExitingVehicle();
    }


    @Test
    void loadInterfaceUnknownOption() {
        when(inputReaderUtil.readSelection()).thenReturn(4, 3);

        shell.loadInterface();
    }
}