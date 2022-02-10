package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    private DataBaseConfig dataBaseConfig = DataBaseConfig.getInstance();

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Get the next parking spot available
     *
     * @param parkingType @enum ParkingType
     * @return id of the next available parking or -1 if an problem was occurred
     */
    public int getNextAvailableSlot(ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;

        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }

        return result;
    }

    /**
     * Updating a ParkingSpot availability
     *
     * @param parkingSpot @class ParkingSpot; the parking to update
     * @return false if an error was occurred
     */
    public boolean updateParking(ParkingSpot parkingSpot) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;

        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            ps.setString(3, parkingSpot.getParkingType().toString());
            int updateRowCount = ps.executeUpdate();

            result = (updateRowCount == 1);
        } catch (Exception ex) {
            logger.error("Error updating parking info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }

        return result;
    }
}
