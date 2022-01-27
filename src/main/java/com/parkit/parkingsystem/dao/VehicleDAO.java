package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class VehicleDAO {

    private static final Logger logger = LogManager.getLogger("VehicleDAO");

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

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

}
