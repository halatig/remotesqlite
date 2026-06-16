package remotesqlite.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * The remote SQLite JDBC driver.
 * 
 * @author Attila Halasz
 * 
 * */
public class RemoteSqliteDriver implements Driver {

	private static Logger _logger = Logger.getLogger(RemoteSqliteDriver.class.getName());

	private static String PREFIX = "jdbc:remotesqlite:";
	private static Pattern URL = Pattern.compile(PREFIX+"(.+):([0-9]+):(.+)", Pattern.CASE_INSENSITIVE);

	private static DriverPropertyInfo[] DRIVER_PROPERTY_INFO = new DriverPropertyInfo[3];
    static {
    	DRIVER_PROPERTY_INFO[0] = new DriverPropertyInfo("user","");
    	DRIVER_PROPERTY_INFO[0].description = "JMX user";
    	DRIVER_PROPERTY_INFO[0].required = true;

    	DRIVER_PROPERTY_INFO[1] = new DriverPropertyInfo("password","");
    	DRIVER_PROPERTY_INFO[1].description = "JMX password";
    	DRIVER_PROPERTY_INFO[1].required = true;

    	DRIVER_PROPERTY_INFO[2] = new DriverPropertyInfo("remotesqlite.level","INFO");
    	DRIVER_PROPERTY_INFO[2].description = "Logging level.";
    	DRIVER_PROPERTY_INFO[2].required = false;

		try {
            DriverManager.registerDriver(new RemoteSqliteDriver());
        } catch (SQLException e) {
        	_logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    //INTERFACE METHODS
	/**
	 * Creates a remote SQLite Connection.
	 * 
	 * @param url the remote database environment URL in form: jdbc:remotesqlite:jmx_rmi_host:jmx_rmi_port:database, where database must match a preconfigured environment on server (database='name?user=u&password=p')
	 * @param info logging property
	 * @return a remote SQlite Connection
	 * @throws SQLException if remote connection fails or if the remote endpoint has JDBC error
	 * 
	 */
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		String level = (String)info.get("remotesqlite.level");
		if (level!=null) { try { _logger.setLevel(Level.parse(level)); } catch (Exception e) { } }
		_logger.fine("start");

		_logger.fine("url="+url);
		for (Map.Entry<Object, Object> infoItem : info.entrySet()) {
			_logger.fine("info["+infoItem.getKey()+"]="+infoItem.getValue());
		}

		String user = (String)info.get("user");
		_logger.fine("user="+user);
		if (user==null) { throw new SQLException("JMX user property is missing."); }

		String password = (String)info.get("password");
		_logger.fine("password="+password);
		if (password==null) { throw new SQLException("JMX password property is missing."); }

		Map<String,String[]> jmxEnvironment = new HashMap<String,String[]>();
		jmxEnvironment.put(JMXConnector.CREDENTIALS, new String[] { user, password } );

		Matcher urlMatcher = URL.matcher(url);
		if (!urlMatcher.find()) { throw new SQLException(url+" does not match pattern ["+URL.pattern()+"]"); }

		String JMXURL = "service:jmx:rmi:///jndi/rmi://" + urlMatcher.group(1) + ":" + urlMatcher.group(2) + "/jmxrmi";
		_logger.fine("JMXURL="+JMXURL);
		String database = urlMatcher.group(3);
		_logger.fine("database="+database);

		try {
			JMXServiceURL JMXServiceURL = new JMXServiceURL(JMXURL);
			JMXConnector JMXConnector = JMXConnectorFactory.connect(JMXServiceURL, jmxEnvironment);
			MBeanServerConnection remoteMBeanServerConnection = JMXConnector.getMBeanServerConnection();

			_logger.fine("RemoteSqliteBean.driverConnect start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String connectionId = (String)remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "driverConnect", new Object[] { database }, new String[] {String.class.getName()});
			_logger.fine("connectionId="+connectionId);
			_logger.fine("RemoteSqliteBean.driverConnect end");

			RemoteSqliteConnection remoteSqliteConnection = new RemoteSqliteConnection(remoteMBeanServerConnection, connectionId);
			remoteSqliteConnection.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteConnection");
			return remoteSqliteConnection;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	/**
	 * A jdbc:remotesqlite: prefix examination.
	 * 
	 * @param url the remote database environment URL in form: jdbc:remotesqlite:*
	 * @return logical value, the result of the examination
	 * @throws SQLException by interface definition
	 * 
	 */
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.toLowerCase().startsWith(PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		if (acceptsURL(url)) {
			return DRIVER_PROPERTY_INFO;			
		} else {
			return new DriverPropertyInfo[0];
		}
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return _logger;
	}

}
