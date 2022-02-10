package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    private static DataBaseConfig INSTANCE = null;

    public static DataBaseConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseConfig();
        }

        return INSTANCE;
    }

    protected DataBaseConfig() {
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.debug("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod", "root", "root");
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.debug("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.debug("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.debug("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
