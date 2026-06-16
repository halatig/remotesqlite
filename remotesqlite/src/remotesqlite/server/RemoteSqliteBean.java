package remotesqlite.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.StandardMBean;
import javax.naming.NamingException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;
import jakarta.servlet.ServletContext;

/**
 * RemoteSqliteMBean implementation.
 *  
 * @author Attila Halasz
 * 
 * */
public class RemoteSqliteBean extends StandardMBean implements RemoteSqliteMBean {

	private static Logger _logger = Logger.getLogger(RemoteSqliteBean.class.getName());

	private static Map<String,Map<String, String>> _databaseMap = new LinkedHashMap<String,Map<String, String>>();

	private Map<String,Connection>			_connectionMap								= new LinkedHashMap<String,Connection>();
	private Map<String,Set<String>>			_connectionStatementIdMap					= new LinkedHashMap<String,Set<String>>();
	private Map<String,Set<String>>			_connectionDatabaseMetaDataIdMap			= new LinkedHashMap<String,Set<String>>();
	private Map<String,Set<String>>			_connectionSavepointIdMap					= new LinkedHashMap<String,Set<String>>();
	private Map<String,Statement>			_statementMap								= new LinkedHashMap<String,Statement>();
	private Map<String,Set<String>>			_statementResultSetIdMap					= new LinkedHashMap<String,Set<String>>();
	private Map<String,Set<String>>			_statementResultSetMetaDataIdMap			= new LinkedHashMap<String,Set<String>>();
	private Map<String,Set<String>>			_statementParameterMetaDataIdMap			= new LinkedHashMap<String,Set<String>>();
	private Map<String,DatabaseMetaData>	_databaseMetaDataMap						= new LinkedHashMap<String,DatabaseMetaData>();
	private Map<String,Set<String>>			_databaseMetaDataResultSetIdMap				= new LinkedHashMap<String,Set<String>>();
	private Map<String,Savepoint>			_savepointMap								= new LinkedHashMap<String,Savepoint>();
	private Map<String,ResultSet>			_resultSetMap								= new LinkedHashMap<String,ResultSet>();
	private Map<String,ParameterMetaData>	_parameterMetaDataMap						= new LinkedHashMap<String,ParameterMetaData>();

	private Map<String,CachedRowSet>		_cachedRowSetMap							= new LinkedHashMap<String,CachedRowSet>();

	/**
	 * The constructor makes a database map.
	 * 
	 * */
	public RemoteSqliteBean(ServletContext servletContext) throws NamingException {
		super(RemoteSqliteMBean.class, false);
		String level = servletContext.getInitParameter("remotesqlite.level");
		if (level!=null) { try { _logger.setLevel(Level.parse(level)); } catch (Exception e) { } }
		_logger.fine("start");

		Properties remotesqliteProperties = new Properties();
		try (InputStream remotesqliteConfig = servletContext.getResourceAsStream("/WEB-INF/remotesqlite.properties")) {
			remotesqliteProperties.load(remotesqliteConfig);
			
			for (String key : remotesqliteProperties.stringPropertyNames()) {
				if (key.equals("remotesqlite.level")) {
					level = remotesqliteProperties.getProperty(key);
					if (level!=null) { try { _logger.setLevel(Level.parse(level)); } catch (Exception e) { } }					
				} else if (key.startsWith("remotesqlite.")) {
                    String[] parts = key.split("\\.");
                    if (parts.length == 3) {
                        String database = parts[1];
                        String property = parts[2];
                        _databaseMap.putIfAbsent(database, new LinkedHashMap<String,String>());
                        _databaseMap.get(database).put(property, remotesqliteProperties.getProperty(key));
                    }
                }
            }
		} catch (IOException e) {
			_logger.info("remotesqlite.config: file not found, no database is accessible through remotesqlite.");
		}
		
		Iterator<Map.Entry<String, Map<String,String>>> iterator = _databaseMap.entrySet().iterator();
		while (iterator.hasNext()) {
		    Map.Entry<String, Map<String,String>> entry = iterator.next();
		    String key = entry.getKey();
		    Map<String,String> value = entry.getValue();
		    if (value.get("path")==null){
				_logger.warning("remotesqlite.config: Database("+key+") path property is missing, removed from list");
				iterator.remove();
				continue;
			} else {
				String databasePath = value.get("path");
				Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
				Matcher matcher = pattern.matcher(databasePath);
				StringBuilder stringBuilder = new StringBuilder();
				while (matcher.find()) {
		            String propertyValue = System.getProperty(matcher.group(1));
		            if (propertyValue == null) {
		            	propertyValue = System.getenv(matcher.group(1));
		            	if (propertyValue==null) {
		            		propertyValue = "";
		            	}
		            }
		            matcher.appendReplacement(stringBuilder, Matcher.quoteReplacement(propertyValue));
				}
				matcher.appendTail(stringBuilder);
				databasePath = stringBuilder.toString();
				
				if (!Files.exists(Paths.get(databasePath))) {
					_logger.warning("remotesqlite.config: Database("+key+") file not found, removed from list");
					iterator.remove();
					continue;
				} else {
					_databaseMap.get(key).put("path", databasePath);							
				}
			}
			if (Boolean.valueOf(value.get("authenticate"))) {
				if (value.get("user")==null || value.get("password")==null) {
					_logger.warning("remotesqlite.config: Database("+key+") authentication properties are incomplete, removed from list");
					iterator.remove();
					continue;
				}
			}
		}
		
		_logger.fine("end");
	}

	private Connection getConnection(String connectionId) throws SQLException {
		_logger.fine("start");

		Connection connection = _connectionMap.get(connectionId);
		if (connection == null) { throw new SQLException("Connection ("+connectionId+") not found."); }
		
		_logger.fine("end with Connection");
		return connection;
	}

	private Statement getStatement(String statementId) throws SQLException {
		_logger.fine("start");

		Statement statement = _statementMap.get(statementId);
		if (statement == null) { throw new SQLException("Statement ("+statementId+") not found."); }
		
		_logger.fine("end with Statement");
		return statement;
	}

	private PreparedStatement getPreparedStatement(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = (PreparedStatement)_statementMap.get(preparedStatementId);
		if (preparedStatement == null) { throw new SQLException("PreparedStatement ("+preparedStatementId+") not found."); }
		
		_logger.fine("end with PreparedStatement");
		return preparedStatement;
	}

	private DatabaseMetaData getDatabaseMetaData(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		DatabaseMetaData databaseMetaData = _databaseMetaDataMap.get(databaseMetaDataId);
		if (databaseMetaData == null) { throw new SQLException("DatabaseMetaData ("+databaseMetaDataId+") not found."); }
		
		_logger.fine("end with DatabaseMetaData");
		return databaseMetaData;
	}

	private Savepoint getSavepoint(String savepointId) throws SQLException {
		_logger.fine("start");

		Savepoint savepoint = _savepointMap.get(savepointId);
		if (savepoint == null) { throw new SQLException("Savepoint ("+savepointId+") not found."); }
		
		_logger.fine("end with Savepoint");
		return savepoint;
	}

	private ResultSet getResultSet(String resultSetId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = _resultSetMap.get(resultSetId);
		if (resultSet == null) { throw new SQLException("ResultSet ("+resultSetId+") not found."); }
		
		_logger.fine("end with ResultSet");
		return resultSet;
	}

	private ParameterMetaData getParameterMetaData(String parameterMetaDataId) throws SQLException {
		_logger.fine("start");

		ParameterMetaData parameterMetaData = _parameterMetaDataMap.get(parameterMetaDataId);
		if (parameterMetaData == null) { throw new SQLException("ParameterMetaData ("+parameterMetaDataId+") not found."); }
		
		_logger.fine("end with ParameterMetaData");
		return parameterMetaData;
	}

	private void addConnectionStatementId(String connectionId, String statementId) {
		_logger.fine("start");

		Set<String> statementIdSet = _connectionStatementIdMap.get(connectionId);
		statementIdSet.add(statementId);
		_connectionStatementIdMap.put(connectionId, statementIdSet);

		_logger.fine("end");
	}

	private void addConnectionDatabaseMetaDataId(String connectionId, String databaseMetaDataId) {
		_logger.fine("start");

		Set<String> databaseMetaDataIdSet = _connectionDatabaseMetaDataIdMap.get(connectionId);
		databaseMetaDataIdSet.add(databaseMetaDataId);
		_connectionDatabaseMetaDataIdMap.put(connectionId, databaseMetaDataIdSet);

		_logger.fine("end");
	}

	private void addConnectionSavepointId(String connectionId, String savepointId) {
		_logger.fine("start");

		Set<String> savepointIdSet = _connectionSavepointIdMap.get(connectionId);
		savepointIdSet.add(savepointId);
		_connectionSavepointIdMap.put(connectionId, savepointIdSet);

		_logger.fine("end");
	}

	private void addStatementResultSetId(String statementId, String resultSetId) {
		_logger.fine("start");

		Set<String> resultSetIdSet = _statementResultSetIdMap.get(statementId);
		resultSetIdSet.add(resultSetId);
		_statementResultSetIdMap.put(statementId, resultSetIdSet);

		_logger.fine("end");
	}

	private void addDatabaseMetaDataResultSetId(String databaseMetaDataId, String resultSetId) {
		_logger.fine("start");

		Set<String> resultSetIdSet = _databaseMetaDataResultSetIdMap.get(databaseMetaDataId);
		resultSetIdSet.add(resultSetId);
		_databaseMetaDataResultSetIdMap.put(databaseMetaDataId, resultSetIdSet);

		_logger.fine("end");
	}

	private void addPreparedStatementResultSetMetaDataId(String preparedStatementId, String resultSetMetaDataId) {
		_logger.fine("start");

		Set<String> resultSetMetaDataIdSet = _statementResultSetMetaDataIdMap.get(preparedStatementId);
		resultSetMetaDataIdSet.add(resultSetMetaDataId);
		_statementResultSetMetaDataIdMap.put(preparedStatementId, resultSetMetaDataIdSet);

		_logger.fine("end");
	}

	private void addPreparedStatementParameterMetaDataId(String preparedStatementId, String parameterMetaDataId) {
		_logger.fine("start");

		Set<String> parameterMetaDataIdSet = _statementParameterMetaDataIdMap.get(preparedStatementId);
		parameterMetaDataIdSet.add(parameterMetaDataId);
		_statementParameterMetaDataIdMap.put(preparedStatementId, parameterMetaDataIdSet);

		_logger.fine("end");
	}

	private void databaseMetaDataClose(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		Set<String> resultSetIdSet = _databaseMetaDataResultSetIdMap.get(databaseMetaDataId);
		for (String resultSetId : resultSetIdSet) {
			if (_resultSetMap.containsKey(resultSetId)) {
				resultSetClose(resultSetId);
			}
		}
		_databaseMetaDataResultSetIdMap.remove(databaseMetaDataId);

		//getDatabaseMetaData(databaseMetaDataId).close();
		_databaseMetaDataMap.remove(databaseMetaDataId);		

		_logger.fine("end");
	}

	private RowSetMetaDataImpl getRowSetMetaDataImpl(ResultSetMetaData resultSetMetaData) throws SQLException {
	    RowSetMetaDataImpl rowSetMetaDataImpl = new RowSetMetaDataImpl();
	    int columnCount = resultSetMetaData.getColumnCount();
	    rowSetMetaDataImpl.setColumnCount(columnCount);

	    for (int i = 1; i <= columnCount; i++) {
	        rowSetMetaDataImpl.setColumnName(i, resultSetMetaData.getColumnName(i));
	        rowSetMetaDataImpl.setColumnLabel(i, resultSetMetaData.getColumnLabel(i));
	        rowSetMetaDataImpl.setColumnType(i, resultSetMetaData.getColumnType(i));
	        rowSetMetaDataImpl.setColumnTypeName(i, resultSetMetaData.getColumnTypeName(i));
	        rowSetMetaDataImpl.setPrecision(i, resultSetMetaData.getPrecision(i));
	        rowSetMetaDataImpl.setScale(i, resultSetMetaData.getScale(i));
	        rowSetMetaDataImpl.setTableName(i, resultSetMetaData.getTableName(i));
	        rowSetMetaDataImpl.setCatalogName(i, resultSetMetaData.getCatalogName(i));
	        rowSetMetaDataImpl.setSchemaName(i, resultSetMetaData.getSchemaName(i));
	        rowSetMetaDataImpl.setNullable(i, resultSetMetaData.isNullable(i));
	        rowSetMetaDataImpl.setAutoIncrement(i, resultSetMetaData.isAutoIncrement(i));
	        rowSetMetaDataImpl.setCaseSensitive(i, resultSetMetaData.isCaseSensitive(i));
	        rowSetMetaDataImpl.setCurrency(i, resultSetMetaData.isCurrency(i));
	        rowSetMetaDataImpl.setSearchable(i, resultSetMetaData.isSearchable(i));
	        rowSetMetaDataImpl.setSigned(i, resultSetMetaData.isSigned(i));
	        rowSetMetaDataImpl.setColumnDisplaySize(i, resultSetMetaData.getColumnDisplaySize(i));
	    }
	    return rowSetMetaDataImpl;
	}
	
	//INTERFACE METHODS
	@Override
	public CachedRowSet getCachedRowSet(String resultSetId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = _resultSetMap.get(resultSetId);
		if (resultSet == null) { throw new SQLException("ResultSet ("+resultSetId+") not found."); }

		CachedRowSet cachedRowSet = _cachedRowSetMap.get(resultSetId);
		if (cachedRowSet == null) { throw new SQLException("CachedRowSet ("+resultSetId+") not found."); }

		_logger.fine("end with CachedRowSet");
		return cachedRowSet;				
	}

	//DRIVER
	@Override
	public String driverConnect(String database) throws NamingException, SQLException {
		_logger.fine("start");

		String[] databaseParts = database.split("\\?");
		String databaseName = databaseParts[0];
		Map<String, String> databaseProperties = _databaseMap.get(databaseName);
		if (databaseProperties == null) { throw new SQLException("Database ("+databaseName+") not found."); }

		if (Boolean.valueOf(databaseProperties.get("authenticate"))) {
			String databaseUser = "";
			String databasePassword = "";
			if (databaseParts.length > 1) {
				String[] databaseParams = databaseParts[1].split("&");
				for (String param : databaseParams) {
		            String[] keyValue = param.split("=");
		            if (keyValue.length == 2) {
		                if (keyValue[0].toLowerCase().equals("user")) {
		                	databaseUser = keyValue[1];
		                } else if (keyValue[0].toLowerCase().equals("password")) {
		                    databasePassword = keyValue[1];
		                }
		            }
		        }
			}

			if (!databaseUser.equals(databaseProperties.get("user")) || !databasePassword.equals(databaseProperties.get("password"))) {
				throw new SQLException("Database ("+databaseName+") authentication failed."); 
			}
		}
			
		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+databaseProperties.get("path"));

		String connectionId = UUID.randomUUID().toString();
		_logger.fine("connectionId="+connectionId);

		_connectionMap.put(connectionId, connection);
		_connectionStatementIdMap.put(connectionId, new LinkedHashSet<String>());
		_connectionDatabaseMetaDataIdMap.put(connectionId, new LinkedHashSet<String>());
		_connectionSavepointIdMap.put(connectionId, new LinkedHashSet<String>());
		
		_logger.fine("end with return connectionId");
		return connectionId;
	}

	//CONNECTION
	@Override
	public String connectionCreateStatement(String connectionId) throws SQLException {
		_logger.fine("start");

		Statement statement = getConnection(connectionId).createStatement();
		
		String statementId = UUID.randomUUID().toString();
		_logger.fine("statementId="+statementId);
		
		_statementMap.put(statementId, statement);
		_statementResultSetIdMap.put(statementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(statementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(statementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, statementId);

		_logger.fine("end with return statementId");
		return statementId;
	}

	@Override
	public String connectionPrepareStatement(String connectionId, String sql) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public void connectionSetAutoCommit(String connectionId, boolean autoCommit) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setAutoCommit(autoCommit);
		
		_logger.fine("end");
	}

	@Override
	public boolean connectionGetAutoCommit(String connectionId) throws SQLException {
		_logger.fine("start");

		boolean connectionGetAutoCommit = getConnection(connectionId).getAutoCommit();
		_logger.fine("connectionGetAutoCommit="+connectionGetAutoCommit);

		_logger.fine("end with return connectionGetAutoCommit");
		return connectionGetAutoCommit;
	}

	@Override
	public void connectionCommit(String connectionId) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).commit();
		
		_logger.fine("end");
	}

	@Override
	public void connectionRollback(String connectionId) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).rollback();
		
		_logger.fine("end");
	}

	@Override
	public void connectionClose(String connectionId) throws SQLException {
		_logger.fine("start");

		Set<String> statementIdSet = _connectionStatementIdMap.get(connectionId);
		for (String statementId : statementIdSet) {
			if (_statementMap.containsKey(statementId)) {
				statementClose(statementId);
			}
		}
		_connectionStatementIdMap.remove(connectionId);

		Set<String> databaseMetaDataIdSet = _connectionDatabaseMetaDataIdMap.get(connectionId);
		for (String databaseMetaDataId : databaseMetaDataIdSet) {
			//private function, called only from here
			databaseMetaDataClose(databaseMetaDataId);
		}
		_connectionDatabaseMetaDataIdMap.remove(connectionId);

		Set<String> savepointIdSet = _connectionSavepointIdMap.get(connectionId);
		for (String savepointId : savepointIdSet) {
			if (_savepointMap.containsKey(savepointId)) {
				connectionReleaseSavepoint(connectionId, savepointId);				
			}
		}
		_connectionSavepointIdMap.remove(connectionId);

		getConnection(connectionId).close();
		_connectionMap.remove(connectionId);

		_logger.fine("end");
	}

	@Override
	public boolean connectionIsClosed(String connectionId) throws SQLException {
		_logger.fine("start");

		boolean connectionIsClosed = getConnection(connectionId).isClosed();
		_logger.fine("connectionIsClosed="+connectionIsClosed);

		_logger.fine("end with return connectionIsClosed");
		return connectionIsClosed;
	}

	@Override
	public String connectionGetMetaData(String connectionId) throws SQLException {
		_logger.fine("start");

		DatabaseMetaData databaseMetaData = getConnection(connectionId).getMetaData();
		
		String databaseMetaDataId = UUID.randomUUID().toString();
		_logger.fine("databaseMetaDataId="+databaseMetaDataId);
		
		_databaseMetaDataMap.put(databaseMetaDataId, databaseMetaData);
		_databaseMetaDataResultSetIdMap.put(databaseMetaDataId, new LinkedHashSet<String>());

		addConnectionDatabaseMetaDataId(connectionId, databaseMetaDataId);

		_logger.fine("end with return databaseMetaDataId");
		return databaseMetaDataId;
	}

	@Override
	public void connectionSetReadOnly(String connectionId, boolean readOnly) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setReadOnly(readOnly);
		
		_logger.fine("end");
	}

	@Override
	public boolean connectionIsReadOnly(String connectionId) throws SQLException {
		_logger.fine("start");

		boolean connectionIsReadOnly = getConnection(connectionId).isReadOnly();
		_logger.fine("connectionIsReadOnly="+connectionIsReadOnly);

		_logger.fine("end with return connectionIsReadOnly");
		return connectionIsReadOnly;
	}

	@Override
	public void connectionSetCatalog(String connectionId, String catalog) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setCatalog(catalog);
		
		_logger.fine("end");
	}

	@Override
	public String connectionGetCatalog(String connectionId) throws SQLException {
		_logger.fine("start");

		String connectionGetCatalog = getConnection(connectionId).getCatalog();
		_logger.fine("connectionGetCatalog="+connectionGetCatalog);

		_logger.fine("end with return connectionGetCatalog");
		return connectionGetCatalog;
	}

	@Override
	public void connectionSetTransactionIsolation(String connectionId, int level) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setTransactionIsolation(level);
		
		_logger.fine("end");
	}

	@Override
	public int connectionGetTransactionIsolation(String connectionId) throws SQLException {
		_logger.fine("start");

		int connectionGetTransactionIsolation = getConnection(connectionId).getTransactionIsolation();
		_logger.fine("connectionGetTransactionIsolation="+connectionGetTransactionIsolation);

		_logger.fine("end with return connectionGetTransactionIsolation");
		return connectionGetTransactionIsolation;
	}

	@Override
	public String connectionCreateStatement2(String connectionId, int resultSetType, int resultSetConcurrency) throws SQLException {
		_logger.fine("start");

		Statement statement = getConnection(connectionId).createStatement(resultSetType, resultSetConcurrency);
		
		String statementId = UUID.randomUUID().toString();
		_logger.fine("statementId="+statementId);
		
		_statementMap.put(statementId, statement);
		_statementResultSetIdMap.put(statementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(statementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(statementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, statementId);

		_logger.fine("end with return statementId");
		return statementId;
	}

	@Override
	public String connectionPrepareStatement2(String connectionId, String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql, resultSetType, resultSetConcurrency);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public Map<String, Class<?>> connectionGetTypeMap(String connectionId) throws SQLException {
		_logger.fine("start");

		Map<String, Class<?>> connectionGetTypeMap = getConnection(connectionId).getTypeMap();

		_logger.fine("end with return connectionGetTypeMap");
		return connectionGetTypeMap;
	}

	@Override
	public void connectionSetTypeMap(String connectionId, Map<String, Class<?>> map) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setTypeMap(map);
		
		_logger.fine("end");
	}

	@Override
	public void connectionSetHoldability(String connectionId, int holdability) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).setHoldability(holdability);
		
		_logger.fine("end");
	}

	@Override
	public int connectionGetHoldability(String connectionId) throws SQLException {
		_logger.fine("start");

		int connectionGetHoldability = getConnection(connectionId).getHoldability();
		_logger.fine("connectionGetHoldability="+connectionGetHoldability);

		_logger.fine("end with return connectionGetHoldability");
		return connectionGetHoldability;
	}
	
	@Override
	public String connectionSetSavepoint(String connectionId) throws SQLException {
		_logger.fine("start");

		Savepoint savepoint = getConnection(connectionId).setSavepoint();
		
		String savepointId = UUID.randomUUID().toString();
		_logger.fine("savepointId="+savepointId);
		
		_savepointMap.put(savepointId, savepoint);

		addConnectionSavepointId(connectionId, savepointId);

		_logger.fine("end with return savepointId");
		return savepointId;
	}

	@Override
	public String connectionSetSavepoint2(String connectionId, String name) throws SQLException {
		_logger.fine("start");

		Savepoint savepoint = getConnection(connectionId).setSavepoint(name);
		
		String savepointId = UUID.randomUUID().toString();
		_logger.fine("savepointId="+savepointId);
		
		_savepointMap.put(savepointId, savepoint);

		addConnectionSavepointId(connectionId, savepointId);

		_logger.fine("end with return savepointId");
		return savepointId;
	}

	@Override
	public void connectionRollback2(String connectionId, String savepointId) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).rollback(getSavepoint(savepointId));
		
		_logger.fine("end");
	}

	@Override
	public void connectionReleaseSavepoint(String connectionId, String savepointId) throws SQLException {
		_logger.fine("start");

		getConnection(connectionId).releaseSavepoint(getSavepoint(savepointId));

		_savepointMap.remove(savepointId);
		
		_logger.fine("end");
	}

	@Override
	public String connectionCreateStatement3(String connectionId, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		_logger.fine("start");

		Statement statement = getConnection(connectionId).createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		
		String statementId = UUID.randomUUID().toString();
		_logger.fine("statementId="+statementId);
		
		_statementMap.put(statementId, statement);
		_statementResultSetIdMap.put(statementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(statementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(statementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, statementId);

		_logger.fine("end with return statementId");
		return statementId;
	}

	@Override
	public String connectionPrepareStatement3(String connectionId, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql, resultSetType, resultSetConcurrency);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public String connectionPrepareStatement4(String connectionId, String sql, int autoGeneratedKeys) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql, autoGeneratedKeys);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public String connectionPrepareStatement5(String connectionId, String sql, int[] columnIndexes) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql, columnIndexes);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public String connectionPrepareStatement6(String connectionId, String sql, String[] columnNames) throws SQLException {
		_logger.fine("start");

		PreparedStatement preparedStatement = getConnection(connectionId).prepareStatement(sql, columnNames);
		
		String preparedStatementId = UUID.randomUUID().toString();
		_logger.fine("preparedStatementId="+preparedStatementId);
		
		_statementMap.put(preparedStatementId, preparedStatement);
		_statementResultSetIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementResultSetMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());
		_statementParameterMetaDataIdMap.put(preparedStatementId, new LinkedHashSet<String>());

		addConnectionStatementId(connectionId, preparedStatementId);

		_logger.fine("end with return preparedStatementId");
		return preparedStatementId;
	}

	@Override
	public boolean connectionIsValid(String connectionId, int timeout) throws SQLException {
		_logger.fine("start");

		boolean connectionIsValid = getConnection(connectionId).isValid(timeout);
		_logger.fine("connectionIsValid="+connectionIsValid);

		_logger.fine("end with return connectionIsValid");
		return connectionIsValid;
	}

	//STATEMENT
	@Override
	public String statementExecuteQuery(String statementId, String sql) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getStatement(statementId).executeQuery(sql);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addStatementResultSetId(statementId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}
	
	@Override
	public int statementExecuteUpdate(String statementId, String sql) throws SQLException {
		_logger.fine("start");

		int statementExecuteUpdate = getStatement(statementId).executeUpdate(sql);
		_logger.fine("statementExecuteUpdate="+statementExecuteUpdate);

		_logger.fine("end with return statementExecuteUpdate");
		return statementExecuteUpdate;
	}

	@Override
	public void statementClose(String statementId) throws SQLException {
		_logger.fine("start");

		Set<String> resultSetIdSet = _statementResultSetIdMap.get(statementId);
		for (String resultSetId : resultSetIdSet) {
			if (_resultSetMap.containsKey(resultSetId)) {
				resultSetClose(resultSetId);
			}
		}
		_statementResultSetIdMap.remove(statementId);

		_statementResultSetMetaDataIdMap.remove(statementId);

		Set<String> parameterMetaDataIdSet = _statementParameterMetaDataIdMap.get(statementId);
		for (String parameterMetaDataId : parameterMetaDataIdSet) {
			_parameterMetaDataMap.remove(parameterMetaDataId);
		}
		_statementParameterMetaDataIdMap.remove(statementId);

		getStatement(statementId).close();
		_statementMap.remove(statementId);

		_logger.fine("end");
	}

	@Override
	public int statementGetMaxRows(String statementId) throws SQLException {
		_logger.fine("start");

		int statementGetMaxRows = getStatement(statementId).getMaxRows();
		_logger.fine("statementGetMaxRows="+statementGetMaxRows);

		_logger.fine("end with return statementGetMaxRows");
		return statementGetMaxRows;
	}

	@Override
	public void statementSetMaxRows(String statementId, int max) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).setMaxRows(max);
		
		_logger.fine("end");
	}

	@Override
	public int statementGetQueryTimeout(String statementId) throws SQLException {
		_logger.fine("start");

		int statementGetQueryTimeout = getStatement(statementId).getQueryTimeout();
		_logger.fine("statementGetQueryTimeout="+statementGetQueryTimeout);

		_logger.fine("end with return statementGetQueryTimeout");
		return statementGetQueryTimeout;
	}

	@Override
	public void statementSetQueryTimeout(String statementId, int seconds) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).setQueryTimeout(seconds);
		
		_logger.fine("end");
	}

	@Override
	public void statementCancel(String statementId) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).cancel();
		
		_logger.fine("end");
	}

	@Override
	public boolean statementExecute(String statementId, String sql) throws SQLException {
		_logger.fine("start");

		boolean statementExecute = getStatement(statementId).execute(sql);
		_logger.fine("statementExecute="+statementExecute);

		_logger.fine("end with return statementExecute");
		return statementExecute;
	}
	
	@Override
	public String statementGetResultSet(String statementId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getStatement(statementId).getResultSet();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addStatementResultSetId(statementId, resultSetId);
		
		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public int statementGetUpdateCount(String statementId) throws SQLException {
		_logger.fine("start");

		int statementGetUpdateCount = getStatement(statementId).getUpdateCount();
		_logger.fine("statementGetUpdateCount="+statementGetUpdateCount);

		_logger.fine("end with return statementGetUpdateCount");
		return statementGetUpdateCount;
	}

	@Override
	public boolean statementGetMoreResults(String statementId) throws SQLException {
		_logger.fine("start");

		boolean statementGetMoreResults = getStatement(statementId).getMoreResults();
		_logger.fine("statementGetMoreResults="+statementGetMoreResults);

		_logger.fine("end with return statementGetMoreResults");
		return statementGetMoreResults;
	}

	@Override
	public void statementSetFetchSize(String statementId, int rows) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).setFetchSize(rows);
		
		_logger.fine("end");
	}

	@Override
	public int statementGetFetchSize(String statementId) throws SQLException {
		_logger.fine("start");

		int statementGetFetchSize = getStatement(statementId).getFetchSize();
		_logger.fine("statementGetFetchSize="+statementGetFetchSize);

		_logger.fine("end with return statementGetFetchSize");
		return statementGetFetchSize;
	}

	@Override
	public void statementAddBatch(String statementId, String sql) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).addBatch(sql);
		
		_logger.fine("end");
	}

	@Override
	public void statementClearBatch(String statementId) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).clearBatch();
		
		_logger.fine("end");
	}

	@Override
	public int[] statementExecuteBatch(String statementId) throws SQLException {
		_logger.fine("start");

		int[] statementExecuteBatch = getStatement(statementId).executeBatch();
		_logger.fine("statementExecuteBatch="+statementExecuteBatch);

		_logger.fine("end with return statementExecuteBatch");
		return statementExecuteBatch;
	}

	@Override
	public String statementGetConnection(String statementId) throws SQLException {
		_logger.fine("start");
		
		for (Map.Entry<String,Set<String>> connectionStatementIdSet : _connectionStatementIdMap.entrySet()) {
			Set<String> statementIdSet = connectionStatementIdSet.getValue();
			if (statementIdSet.contains(statementId)) {
				String connectionId = connectionStatementIdSet.getKey();
				_logger.fine("connectionId="+connectionId);
				
				_logger.fine("end with return connectionId");
				return connectionId;
			}
		}
		throw new SQLException("Connection for Statement ("+statementId+") not found.");
	}

	@Override
	public boolean statementGetMoreResults2(String statementId, int current) throws SQLException {
		_logger.fine("start");

		boolean statementGetMoreResults = getStatement(statementId).getMoreResults(current);
		_logger.fine("statementGetMoreResults="+statementGetMoreResults);

		_logger.fine("end with return statementGetMoreResults");
		return statementGetMoreResults;
	}
	
	@Override
	public String statementGetGeneratedKeys(String statementId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getStatement(statementId).getGeneratedKeys();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addStatementResultSetId(statementId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public int statementExecuteUpdate2(String statementId, String sql, int autoGeneratedKeys) throws SQLException {
		_logger.fine("start");

		int statementExecuteUpdate = getStatement(statementId).executeUpdate(sql, autoGeneratedKeys);
		_logger.fine("statementExecuteUpdate="+statementExecuteUpdate);

		_logger.fine("end with return statementExecuteUpdate");
		return statementExecuteUpdate;
	}

	@Override
	public int statementExecuteUpdate3(String statementId, String sql, int[] columnIndexes) throws SQLException {
		_logger.fine("start");

		int statementExecuteUpdate = getStatement(statementId).executeUpdate(sql, columnIndexes);
		_logger.fine("statementExecuteUpdate="+statementExecuteUpdate);

		_logger.fine("end with return statementExecuteUpdate");
		return statementExecuteUpdate;
	}

	@Override
	public int statementExecuteUpdate4(String statementId, String sql, String[] columnNames) throws SQLException {
		_logger.fine("start");

		int statementExecuteUpdate = getStatement(statementId).executeUpdate(sql, columnNames);
		_logger.fine("statementExecuteUpdate="+statementExecuteUpdate);

		_logger.fine("end with return statementExecuteUpdate");
		return statementExecuteUpdate;
	}

	@Override
	public boolean statementExecute2(String statementId, String sql, int autoGeneratedKeys) throws SQLException {
		_logger.fine("start");

		boolean statementExecute = getStatement(statementId).execute(sql, autoGeneratedKeys);
		_logger.fine("statementExecute="+statementExecute);

		_logger.fine("end with return statementExecute");
		return statementExecute;
	}

	@Override
	public boolean statementExecute3(String statementId, String sql, int[] columnIndexes) throws SQLException {
		_logger.fine("start");

		boolean statementExecute = getStatement(statementId).execute(sql, columnIndexes);
		_logger.fine("statementExecute="+statementExecute);

		_logger.fine("end with return statementExecute");
		return statementExecute;
	}

	@Override
	public boolean statementExecute4(String statementId, String sql, String[] columnNames) throws SQLException {
		_logger.fine("start");

		boolean statementExecute = getStatement(statementId).execute(sql, columnNames);
		_logger.fine("statementExecute="+statementExecute);

		_logger.fine("end with return statementExecute");
		return statementExecute;
	}

	@Override
	public boolean statementIsClosed(String statementId) throws SQLException {
		_logger.fine("start");

		boolean statementIsClosed = getStatement(statementId).isClosed();
		_logger.fine("statementIsClosed="+statementIsClosed);

		_logger.fine("end with return statementIsClosed");
		return statementIsClosed;		
	}

	@Override
	public void statementCloseOnCompletion(String statementId) throws SQLException {
		_logger.fine("start");

		getStatement(statementId).closeOnCompletion();
		
		_logger.fine("end");
	}

	@Override
	public boolean statementIsCloseOnCompletion(String statementId) throws SQLException {
		_logger.fine("start");

		boolean statementIsCloseOnCompletion = getStatement(statementId).isCloseOnCompletion();
		_logger.fine("statementIsCloseOnCompletion="+statementIsCloseOnCompletion);

		_logger.fine("end with return statementIsCloseOnCompletion");
		return statementIsCloseOnCompletion;		
	}
	
	//PREPARED STATEMENT
	@Override
	public String preparedStatementExecuteQuery(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getPreparedStatement(preparedStatementId).executeQuery();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addStatementResultSetId(preparedStatementId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public int preparedStatementExecuteUpdate(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		int preparedStatementExecuteUpdate = getPreparedStatement(preparedStatementId).executeUpdate();
		_logger.fine("preparedStatementExecuteUpdate="+preparedStatementExecuteUpdate);

		_logger.fine("end with return preparedStatementExecuteUpdate");
		return preparedStatementExecuteUpdate;
	}

	@Override
	public void preparedStatementSetNull(String preparedStatementId, int parameterIndex, int sqlType) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNull(parameterIndex, sqlType);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBoolean(String preparedStatementId, int parameterIndex, boolean x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBoolean(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetByte(String preparedStatementId, int parameterIndex, byte x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setByte(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetShort(String preparedStatementId, int parameterIndex, short x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setShort(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetInt(String preparedStatementId, int parameterIndex, int x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setInt(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetLong(String preparedStatementId, int parameterIndex, long x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setLong(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetFloat(String preparedStatementId, int parameterIndex, float x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setFloat(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetDouble(String preparedStatementId, int parameterIndex, double x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setDouble(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBigDecimal(String preparedStatementId, int parameterIndex, BigDecimal x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBigDecimal(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetString(String preparedStatementId, int parameterIndex, String x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setString(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBytes(String preparedStatementId, int parameterIndex, byte[] x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBytes(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetDate(String preparedStatementId, int parameterIndex, Date x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setDate(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetTime(String preparedStatementId, int parameterIndex, Time x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setTime(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetTimestamp(String preparedStatementId, int parameterIndex, Timestamp x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setTimestamp(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetAsciiStream(String preparedStatementId, int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setAsciiStream(parameterIndex, x, length);
		
		_logger.fine("end");		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void preparedStatementSetUnicodeStream(String preparedStatementId, int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setUnicodeStream(parameterIndex, x, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBinaryStream(String preparedStatementId, int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBinaryStream(parameterIndex, x, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementClearParameters(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).clearParameters();
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetObject(String preparedStatementId, int parameterIndex, Object x, int targetSqlType) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setObject(parameterIndex, x, targetSqlType);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetObject2(String preparedStatementId, int parameterIndex, Object x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setObject(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public boolean preparedStatementExecute(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		boolean preparedStatementExecute = getPreparedStatement(preparedStatementId).execute();
		_logger.fine("preparedStatementExecute="+preparedStatementExecute);

		_logger.fine("end with return preparedStatementExecute");
		return preparedStatementExecute;
	}

	@Override
	public void preparedStatementAddBatch(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).addBatch();
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetCharacterStream(String preparedStatementId, int parameterIndex, Reader reader, int length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setCharacterStream(parameterIndex, reader, length);
		
		_logger.fine("end");		
	}

	@Override
	public RowSetMetaDataImpl preparedStatementGetMetaData(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		ResultSetMetaData resultSetMetaData = getPreparedStatement(preparedStatementId).getMetaData();
		
		String resultSetMetaDataId = UUID.randomUUID().toString();
		_logger.fine("resultSetMetaDataId="+resultSetMetaDataId);

		addPreparedStatementResultSetMetaDataId(preparedStatementId, resultSetMetaDataId);

		RowSetMetaDataImpl rowSetMetaDataImpl = getRowSetMetaDataImpl(resultSetMetaData);

		_logger.fine("end with return RowSetMetaDataImpl");
		return rowSetMetaDataImpl;
	}

	@Override
	public void preparedStatementSetDate2(String preparedStatementId, int parameterIndex, Date x, Calendar cal) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setDate(parameterIndex, x, cal);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetTime2(String preparedStatementId, int parameterIndex, Time x, Calendar cal) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setTime(parameterIndex, x, cal);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetTimestamp2(String preparedStatementId, int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setTimestamp(parameterIndex, x, cal);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNull2(String preparedStatementId, int parameterIndex, int sqlType, String typeName) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNull(parameterIndex, sqlType, typeName);
		
		_logger.fine("end");		
	}

	@Override
	public String preparedStatementGetParameterMetaData(String preparedStatementId) throws SQLException {
		_logger.fine("start");

		ParameterMetaData parameterMetaData = getPreparedStatement(preparedStatementId).getParameterMetaData();
		
		String parameterMetaDataId = UUID.randomUUID().toString();
		_logger.fine("parameterMetaDataId="+parameterMetaDataId);
		
		_parameterMetaDataMap.put(parameterMetaDataId, parameterMetaData);

		addPreparedStatementParameterMetaDataId(preparedStatementId, parameterMetaDataId);

		_logger.fine("end with return parameterMetaDataId");
		return parameterMetaDataId;
	}

	@Override
	public void preparedStatementSetRowId(String preparedStatementId, int parameterIndex, RowId x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setRowId(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNString(String preparedStatementId, int parameterIndex, String value) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNString(parameterIndex, value);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNCharacterStream(String preparedStatementId, int parameterIndex, Reader value, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNCharacterStream(parameterIndex, value, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNClob(String preparedStatementId, int parameterIndex, NClob value) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNClob(parameterIndex, value);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetClob(String preparedStatementId, int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setClob(parameterIndex, reader, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBlob(String preparedStatementId, int parameterIndex, InputStream inputStream, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBlob(parameterIndex, inputStream, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNClob2(String preparedStatementId, int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNClob(parameterIndex, reader, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetSQLXML(String preparedStatementId, int parameterIndex, SQLXML xmlObject) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setSQLXML(parameterIndex, xmlObject);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetObject3(String preparedStatementId, int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetAsciiStream2(String preparedStatementId, int parameterIndex, InputStream x, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setAsciiStream(parameterIndex, x, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBinaryStream2(String preparedStatementId, int parameterIndex, InputStream x, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBinaryStream(parameterIndex, x, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetCharacterStream2(String preparedStatementId, int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setCharacterStream(parameterIndex, reader, length);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetAsciiStream3(String preparedStatementId, int parameterIndex, InputStream x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setAsciiStream(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBinaryStream3(String preparedStatementId, int parameterIndex, InputStream x) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBinaryStream(parameterIndex, x);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetCharacterStream3(String preparedStatementId, int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setCharacterStream(parameterIndex, reader);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNCharacterStream2(String preparedStatementId, int parameterIndex, Reader value) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNCharacterStream(parameterIndex, value);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetClob2(String preparedStatementId, int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setClob(parameterIndex, reader);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetBlob2(String preparedStatementId, int parameterIndex, InputStream inputStream) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setBlob(parameterIndex, inputStream);
		
		_logger.fine("end");		
	}

	@Override
	public void preparedStatementSetNClob3(String preparedStatementId, int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		getPreparedStatement(preparedStatementId).setNClob(parameterIndex, reader);
		
		_logger.fine("end");		
	}
	
	//DATABASE META DATA
	@Override
	public String databaseMetaDataGetURL(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		String databaseMetaDataGetURL = getDatabaseMetaData(databaseMetaDataId).getURL();
		_logger.fine("databaseMetaDataGetURL="+databaseMetaDataGetURL);

		_logger.fine("end with return databaseMetaDataGetURL");
		return databaseMetaDataGetURL;
	}

	@Override
	public boolean databaseMetaDataIsReadOnly(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		boolean databaseMetaDataIsReadOnly = getDatabaseMetaData(databaseMetaDataId).isReadOnly();
		_logger.fine("databaseMetaDataIsReadOnly="+databaseMetaDataIsReadOnly);

		_logger.fine("end with return databaseMetaDataIsReadOnly");
		return databaseMetaDataIsReadOnly;
	}

	@Override
	public String databaseMetaDataGetDatabaseProductVersion(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		String databaseMetaDataGetDatabaseProductVersion = getDatabaseMetaData(databaseMetaDataId).getDatabaseProductVersion();
		_logger.fine("databaseMetaDataGetDatabaseProductVersion="+databaseMetaDataGetDatabaseProductVersion);

		_logger.fine("end with return databaseMetaDataGetDatabaseProductVersion");
		return databaseMetaDataGetDatabaseProductVersion;
	}

	@Override
	public boolean databaseMetaDataSupportsFullOuterJoins(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		boolean databaseMetaDataSupportsFullOuterJoins = getDatabaseMetaData(databaseMetaDataId).supportsFullOuterJoins();
		_logger.fine("databaseMetaDataSupportsFullOuterJoins="+databaseMetaDataSupportsFullOuterJoins);

		_logger.fine("end with return databaseMetaDataSupportsFullOuterJoins");
		return databaseMetaDataSupportsFullOuterJoins;
	}

	@Override
	public String databaseMetaDataGetProcedures(String databaseMetaDataId, String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getProcedures(catalog, schemaPattern, procedureNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetProcedureColumns(String databaseMetaDataId, String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetTables(String databaseMetaDataId, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getTables(catalog, schemaPattern, tableNamePattern, types);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}
	
	@Override
	public String databaseMetaDataGetSchemas(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getSchemas();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetCatalogs(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getCatalogs();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetTableTypes(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getTableTypes();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetColumns(String databaseMetaDataId, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetColumnPrivileges(String databaseMetaDataId, String catalog, String schema, String table, String columnNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getColumns(catalog, schema, table, columnNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetTablePrivileges(String databaseMetaDataId, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getTablePrivileges(catalog, schemaPattern, tableNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetBestRowIdentifier(String databaseMetaDataId, String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getBestRowIdentifier(catalog, schema, table, scope, nullable);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetVersionColumns(String databaseMetaDataId, String catalog, String schema, String table) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getVersionColumns(catalog, schema, table);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetPrimaryKeys(String databaseMetaDataId, String catalog, String schema, String table) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getPrimaryKeys(catalog, schema, table);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetImportedKeys(String databaseMetaDataId, String catalog, String schema, String table) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getImportedKeys(catalog, schema, table);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetExportedKeys(String databaseMetaDataId, String catalog, String schema, String table) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getExportedKeys(catalog, schema, table);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetCrossReference(String databaseMetaDataId, String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetTypeInfo(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getTypeInfo();
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetIndexInfo(String databaseMetaDataId, String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getIndexInfo(catalog, schema, table, unique, approximate);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);
	
		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetUDTs(String databaseMetaDataId, String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getUDTs(catalog, schemaPattern, typeNamePattern, types);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetConnection(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");
		
		for (Map.Entry<String,Set<String>> connectionDatabaseMetaDataIdSet : _connectionDatabaseMetaDataIdMap.entrySet()) {
			Set<String> databaseMetaDataIdSet = connectionDatabaseMetaDataIdSet.getValue();
			if (databaseMetaDataIdSet.contains(databaseMetaDataId)) {
				String connectionId = connectionDatabaseMetaDataIdSet.getKey();
				_logger.fine("connectionId="+connectionId);
				
				_logger.fine("end with return connectionId");
				return connectionId;
			}
		}
		throw new SQLException("Connection for DatabaseMetaData ("+databaseMetaDataId+") not found.");
	}

	@Override
	public String databaseMetaDataGetSuperTypes(String databaseMetaDataId, String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getSuperTypes(catalog, schemaPattern, typeNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetSuperTables(String databaseMetaDataId, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getSuperTables(catalog, schemaPattern, tableNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}

	@Override
	public String databaseMetaDataGetAttributes(String databaseMetaDataId, String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
		_logger.fine("start");

		ResultSet resultSet = getDatabaseMetaData(databaseMetaDataId).getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
		
		String resultSetId = UUID.randomUUID().toString();
		_logger.fine("resultSetId="+resultSetId);
		
		_resultSetMap.put(resultSetId, resultSet);

		addDatabaseMetaDataResultSetId(databaseMetaDataId, resultSetId);

		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		_cachedRowSetMap.put(resultSetId, cachedRowSet);

		_logger.fine("end with return resultSetId");
		return resultSetId;
	}
	
	@Override
	public int databaseMetaDataGetDatabaseMajorVersion(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		int databaseMetaDataGetDatabaseMajorVersion = getDatabaseMetaData(databaseMetaDataId).getDatabaseMajorVersion();
		_logger.fine("databaseMetaDataGetDatabaseMajorVersion="+databaseMetaDataGetDatabaseMajorVersion);

		_logger.fine("end with return databaseMetaDataGetDatabaseMajorVersion");
		return databaseMetaDataGetDatabaseMajorVersion;
	}

	@Override
	public int databaseMetaDataGetDatabaseMinorVersion(String databaseMetaDataId) throws SQLException {
		_logger.fine("start");

		int databaseMetaDataGetDatabaseMinorVersion = getDatabaseMetaData(databaseMetaDataId).getDatabaseMinorVersion();
		_logger.fine("databaseMetaDataGetDatabaseMinorVersion="+databaseMetaDataGetDatabaseMinorVersion);

		_logger.fine("end with return databaseMetaDataGetDatabaseMinorVersion");
		return databaseMetaDataGetDatabaseMinorVersion;
	}

	//SAVEPOINT
	@Override
	public int savepointGetSavepointId(String savepointId) throws SQLException {
		_logger.fine("start");

		int savepointGetSavepointId = getSavepoint(savepointId).getSavepointId();
		_logger.fine("savepointGetSavepointId="+savepointGetSavepointId);

		_logger.fine("end with return savepointGetSavepointId");
		return savepointGetSavepointId;
	}

	@Override
	public String savepointGetSavepointName(String savepointId) throws SQLException {
		_logger.fine("start");

		String savepointGetSavepointName = getSavepoint(savepointId).getSavepointName();
		_logger.fine("savepointGetSavepointName="+savepointGetSavepointName);

		_logger.fine("end with return savepointGetSavepointName");
		return savepointGetSavepointName;
	}

	//PARAMETER META DATA
	@Override
	public int parameterMetaDataGetParameterCount(String parameterMetaDataId) throws SQLException {
		_logger.fine("start");

		int parameterMetaDataGetParameterCount = getParameterMetaData(parameterMetaDataId).getParameterCount();
		_logger.fine("parameterMetaDataGetParameterCount="+parameterMetaDataGetParameterCount);

		_logger.fine("end with return parameterMetaDataGetParameterCount");
		return parameterMetaDataGetParameterCount;
	}

	@Override
	public int parameterMetaDataGetParameterType(String parameterMetaDataId, int param) throws SQLException {
		_logger.fine("start");

		int parameterMetaDataGetParameterType = getParameterMetaData(parameterMetaDataId).getParameterType(param);
		_logger.fine("parameterMetaDataGetParameterType="+parameterMetaDataGetParameterType);

		_logger.fine("end with return parameterMetaDataGetParameterType");
		return parameterMetaDataGetParameterType;
	}

	@Override
	public String parameterMetaDataGetParameterTypeName(String parameterMetaDataId, int param) throws SQLException {
		_logger.fine("start");

		String parameterMetaDataGetParameterTypeName = getParameterMetaData(parameterMetaDataId).getParameterTypeName(param);
		_logger.fine("parameterMetaDataGetParameterTypeName="+parameterMetaDataGetParameterTypeName);

		_logger.fine("end with return parameterMetaDataGetParameterTypeName");
		return parameterMetaDataGetParameterTypeName;
	}

	@Override
	public String parameterMetaDataGetParameterClassName(String parameterMetaDataId, int param) throws SQLException {
		_logger.fine("start");

		String parameterMetaDataGetParameterClassName = getParameterMetaData(parameterMetaDataId).getParameterClassName(param);
		_logger.fine("parameterMetaDataGetParameterClassName="+parameterMetaDataGetParameterClassName);

		_logger.fine("end with return parameterMetaDataGetParameterClassName");
		return parameterMetaDataGetParameterClassName;
	}
	
	//RESULT SET
	@Override
	public void resultSetClose(String resultSetId) throws SQLException {
		_logger.fine("start");

		getResultSet(resultSetId).close();
		_resultSetMap.remove(resultSetId);

		_cachedRowSetMap.remove(resultSetId);

		_logger.fine("end");
	}

	@Override
	public void resultSetSetFetchSize(String resultSetId, int rows) throws SQLException {
		_logger.fine("start");

		getResultSet(resultSetId).setFetchSize(rows);

		_logger.fine("end");
	}

	@Override
	public int resultSetGetFetchSize(String resultSetId) throws SQLException {
		_logger.fine("start");

		int resultSetGetFetchSize = getResultSet(resultSetId).getFetchSize();
		_logger.fine("resultSetGetFetchSize="+resultSetGetFetchSize);

		_logger.fine("end with return resultSetGetFetchSize");
		return resultSetGetFetchSize;
	}

	@Override
	public String resultSetGetStatement(String resultSetId) throws SQLException {
		_logger.fine("start");
		
		for (Map.Entry<String,Set<String>> statementResultSetIdSet : _statementResultSetIdMap.entrySet()) {
			Set<String> resultSetIdSet = statementResultSetIdSet.getValue();
			if (resultSetIdSet.contains(resultSetId)) {
				String statementId = statementResultSetIdSet.getKey();
				_logger.fine("statementId="+statementId);
				
				_logger.fine("end with return statementId");
				return statementId;
			}
		}
		throw new SQLException("Statement for ResultSet ("+resultSetId+") not found.");	
	}

}
