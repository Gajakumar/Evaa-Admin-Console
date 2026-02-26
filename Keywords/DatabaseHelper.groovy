import java.sql.DriverManager as DriverManager
import java.sql.Connection as Connection
import com.kms.katalon.core.annotation.Keyword

import internal.GlobalVariable

import java.sql.*

public class DatabaseHelper {

	private static String dbURL ;

	public DatabaseHelper(String host,String port , String db ) {
		dbURL  = "jdbc:mysql://$host:$port/$db";
	}

	private static Connection connection = null

	/**
	 * Open and return a connection to database
	 * @param dataFile absolute file path of DB
	 * @return Connection object
	 */
	@Keyword
	def connectDB(String user, String pass) {
		Class.forName("org.sqlite.JDBC")

		if (connection != null && !connection.isClosed()) {
			connection.close()
		}
		connection = DriverManager.getConnection(dbURL, user, pass)
		return connection
	}

	/**
	 * Execute SELECT query
	 * @param queryString SQL SELECT query
	 * @return ResultSet
	 */
	@Keyword
	def executeQuery(String queryString) {
		Statement statement = connection.createStatement()
		ResultSet resultSet = statement.executeQuery(queryString)
		return resultSet
	}

	/**
	 * Execute INSERT / UPDATE / DELETE query
	 * @param queryString SQL query
	 * @return rows affected
	 */
	@Keyword
	def executeUpdate(String queryString) {
		Statement statement = connection.createStatement()
		int rowsAffected = statement.executeUpdate(queryString)
		statement.close()
		return rowsAffected
	}

	/**
	 * Execute generic SQL (DDL or DML)
	 * @param queryString SQL statement
	 * @return true if ResultSet returned, false otherwise
	 */
	@Keyword
	def execute(String queryString) {
		Statement statement = connection.createStatement()
		boolean result = statement.execute(queryString)
		statement.close()
		return result
	}

	/**
	 * Get single value from query (COUNT, SUM, MAX, etc.)
	 * @param queryString SQL query
	 * @return Object value
	 */
	@Keyword
	def getSingleValue(String queryString) {
		Statement statement = connection.createStatement()
		ResultSet rs = statement.executeQuery(queryString)
		Object value = null
		if (rs.next()) {
			value = rs.getObject(1)
		}
		rs.close()
		statement.close()
		return value
	}

	/**
	 * Close database connection
	 */
	@Keyword
	def closeDatabaseConnection() {
		if (connection != null && !connection.isClosed()) {
			connection.close()
		}
		connection = null
	}
}
