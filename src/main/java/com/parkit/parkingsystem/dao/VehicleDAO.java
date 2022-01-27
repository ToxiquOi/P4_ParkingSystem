package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VehicleDAO {

    private static final Logger logger = LogManager.getLogger("VehicleDAO");

    private DataBaseConfig dataBaseConfig = DataBaseConfig.getInstance();

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public boolean createVehicleIfNotExist(String regNumber) {
        boolean operationResult = false;
        Connection con = null;
        PreparedStatement ps = null;
        try  {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_VEHICLE_IF_NOT_EXIST);

            ps.setString(1, regNumber);

            operationResult = ps.execute();
        } catch (Exception ex) {
            logger.error("Unable to process vehicle", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }

        return operationResult;
    }

    public boolean isFirstUseOfParking(String regNumber) {
        boolean operationResult = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try  {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.VEHICLE_EXIST);

            ps.setString(1, regNumber);

            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("vechicleExist rs.next()");
                operationResult = rs.getBoolean(1);
            }

        } catch (Exception ex) {
            logger.error("Unable to fetch vehicle", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }

        System.out.println("vechicleExist :" + operationResult);
        return operationResult;
    }

}
