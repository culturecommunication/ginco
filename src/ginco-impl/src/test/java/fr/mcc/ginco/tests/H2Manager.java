package fr.mcc.ginco.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Manager {
	private static final Logger LOGGER = LoggerFactory.getLogger(H2Manager.class);
	public static final String DBUNIT_DRIVER_CLASS = "org.h2.Driver";
	public static final String DBUNIT_CONNECTION_URL = "jdbc:h2:mem:test_db";
	private static final String DBUNIT_CONNECTION_INITURL = DBUNIT_CONNECTION_URL + ";INIT=RUNSCRIPT FROM 'src/test/resources/create_tables.sql';DB_CLOSE_DELAY=-1";
	public static final String DBUNIT_USERNAME = "";
	public static final String DBUNIT_PASSWORD = "";
	static {
		LOGGER.debug("init database");
		try {
			Class.forName(DBUNIT_DRIVER_CLASS);
			DriverManager.getConnection(DBUNIT_CONNECTION_INITURL, DBUNIT_USERNAME, DBUNIT_PASSWORD);
			LOGGER.debug("Database initialized with success");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/* GETTER AND SETTER */
	public static Connection getConnection() throws SQLException {
		LOGGER.trace("getConnection");
		return DriverManager.getConnection(DBUNIT_CONNECTION_URL, DBUNIT_USERNAME, DBUNIT_PASSWORD);
	}
}