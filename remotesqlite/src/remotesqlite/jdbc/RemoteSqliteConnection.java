package remotesqlite.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RemoteSqliteConnection implements Connection {

	private static Logger _logger = Logger.getLogger(RemoteSqliteConnection.class.getName());

	private static MBeanServerConnection _remoteMBeanServerConnection; 
	private String _connectionId;

	public RemoteSqliteConnection(MBeanServerConnection remoteMBeanServerConnection, String connectionId) {
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_connectionId = connectionId;
	}

	public void setLoggerLevel(Level level) {
		_logger.setLevel(level);
	}

	//INTERFACE METHODS
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return iface.cast(this);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	@Override
	public Statement createStatement() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionCreateStatement start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String statementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionCreateStatement", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("statementId="+statementId);
			_logger.fine("RemoteSqliteBean.connectionCreateStatement end");

			RemoteSqliteStatement remoteSqliteStatement = new RemoteSqliteStatement(_remoteMBeanServerConnection, statementId);
			remoteSqliteStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteStatement");
			return remoteSqliteStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement", new Object[] { _connectionId, sql }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
		throw new SQLException("SQLite does not support Stored Procedures");
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return sql;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetAutoCommit start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetAutoCommit", new Object[] { _connectionId, autoCommit }, new String[] {String.class.getName(), boolean.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetAutoCommit end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetAutoCommit start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean getAutoCommit = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetAutoCommit", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("getAutoCommit="+getAutoCommit);
			_logger.fine("RemoteSqliteBean.connectionGetAutoCommit end");

			_logger.fine("end with return getAutoCommit");
			return getAutoCommit;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void commit() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionCommit start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionCommit", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionCommit end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void rollback() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionRollback start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionRollback", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionRollback end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void close() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionClose start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionClose", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionClose end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public boolean isClosed() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionIsClosed start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isClosed = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionIsClosed", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("isClosed="+isClosed);
			_logger.fine("RemoteSqliteBean.connectionIsClosed end");

			_logger.fine("end with return isClosed");
			return isClosed;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetMetaData start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String databaseMetaDataId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetMetaData", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("databaseMetaDataId="+databaseMetaDataId);
			_logger.fine("RemoteSqliteBean.connectionGetMetaData end");

			RemoteSqliteDatabaseMetaData remoteSqliteDatabaseMetaData = new RemoteSqliteDatabaseMetaData(_remoteMBeanServerConnection, databaseMetaDataId);
			remoteSqliteDatabaseMetaData.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteDatabaseMetaData");
			return remoteSqliteDatabaseMetaData;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetReadOnly start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetReadOnly", new Object[] { _connectionId, readOnly }, new String[] {String.class.getName(), boolean.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetReadOnly end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionIsReadOnly start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isReadOnly = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionIsReadOnly", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("isReadOnly="+isReadOnly);
			_logger.fine("RemoteSqliteBean.connectionIsReadOnly end");

			_logger.fine("end with return isReadOnly");
			return isReadOnly;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetCatalog start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetCatalog", new Object[] { _connectionId, catalog }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetCatalog end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public String getCatalog() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetCatalog start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String getCatalog = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetCatalog", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("getCatalog="+getCatalog);
			_logger.fine("RemoteSqliteBean.connectionGetCatalog end");

			_logger.fine("end with return getCatalog");
			return getCatalog;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetTransactionIsolation start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetTransactionIsolation", new Object[] { _connectionId, level }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetTransactionIsolation end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetTransactionIsolation start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getTransactionIsolation = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetTransactionIsolation", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("getTransactionIsolation="+getTransactionIsolation);
			_logger.fine("RemoteSqliteBean.connectionGetTransactionIsolation end");

			_logger.fine("end with return getTransactionIsolation");
			return getTransactionIsolation;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionCreateStatement2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String statementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionCreateStatement2", new Object[] { _connectionId, resultSetType, resultSetConcurrency }, new String[] {String.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("statementId="+statementId);
			_logger.fine("RemoteSqliteBean.connectionCreateStatement2 end");

			RemoteSqliteStatement remoteSqliteStatement = new RemoteSqliteStatement(_remoteMBeanServerConnection, statementId);
			remoteSqliteStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteStatement");
			return remoteSqliteStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement2", new Object[] { _connectionId, sql, resultSetType, resultSetConcurrency }, new String[] {String.class.getName(), String.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement2 end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
		throw new SQLException("SQLite does not support Stored Procedures");
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetTypeMap start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		@SuppressWarnings("unchecked")
			Map<String, Class<?>> getTypeMap = (Map<String, Class<?>>)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetTypeMap", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionGetTypeMap end");

			_logger.fine("end with return getTypeMap");
			return getTypeMap;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetTypeMap start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetTypeMap", new Object[] { _connectionId, map }, new String[] {String.class.getName(), Map.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetTypeMap end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionSetHoldability start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetHoldability", new Object[] { _connectionId, holdability }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionSetHoldability end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public int getHoldability() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionGetHoldability start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getHoldability = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionGetHoldability", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("getHoldability="+getHoldability);
			_logger.fine("RemoteSqliteBean.connectionGetHoldability end");

			_logger.fine("end with return getHoldability");
			return getHoldability;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionSetSavepoint start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String savepointId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetSavepoint", new Object[] { _connectionId }, new String[] {String.class.getName()});
			_logger.fine("savepointId="+savepointId);
			_logger.fine("RemoteSqliteBean.connectionSetSavepoint end");

			RemoteSqliteSavepoint remoteSqliteSavepoint = new RemoteSqliteSavepoint(_remoteMBeanServerConnection, savepointId);
			remoteSqliteSavepoint.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteSavepoint");
			return remoteSqliteSavepoint;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionSetSavepoint2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String savepointId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionSetSavepoint2", new Object[] { _connectionId, name }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("savepointId="+savepointId);
			_logger.fine("RemoteSqliteBean.connectionSetSavepoint2 end");

			RemoteSqliteSavepoint remoteSqliteSavepoint = new RemoteSqliteSavepoint(_remoteMBeanServerConnection, savepointId);
			remoteSqliteSavepoint.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteSavepoint");
			return remoteSqliteSavepoint;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionRollback2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionRollback2", new Object[] { _connectionId, ((RemoteSqliteSavepoint)savepoint).getId() }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionRollback2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.connectionReleaseSavepoint start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionReleaseSavepoint", new Object[] { _connectionId, ((RemoteSqliteSavepoint)savepoint).getId() }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.connectionReleaseSavepoint end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionCreateStatement3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String statementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionCreateStatement3", new Object[] { _connectionId, resultSetType, resultSetConcurrency, resultSetHoldability }, new String[] {String.class.getName(), int.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("statementId="+statementId);
			_logger.fine("RemoteSqliteBean.connectionCreateStatement3 end");

			RemoteSqliteStatement remoteSqliteStatement = new RemoteSqliteStatement(_remoteMBeanServerConnection, statementId);
			remoteSqliteStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqliteStatement");
			return remoteSqliteStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement3", new Object[] { _connectionId, resultSetType, resultSetConcurrency, resultSetHoldability }, new String[] {String.class.getName(), int.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement3 end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
		throw new SQLException("SQLite does not support Stored Procedures");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement4 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement4", new Object[] { _connectionId, autoGeneratedKeys }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement4 end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement5 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement5", new Object[] { _connectionId, columnIndexes }, new String[] {String.class.getName(), int[].class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement5 end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionPrepareStatement6 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String preparedStatementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionPrepareStatement6", new Object[] { _connectionId, columnNames }, new String[] {String.class.getName(), String[].class.getName()});
			_logger.fine("preparedStatementId="+preparedStatementId);
			_logger.fine("RemoteSqliteBean.connectionPrepareStatement6 end");

			RemoteSqlitePreparedStatement remoteSqlitePreparedStatement = new RemoteSqlitePreparedStatement(_remoteMBeanServerConnection, preparedStatementId);
			remoteSqlitePreparedStatement.setLoggerLevel(_logger.getLevel());

			_logger.fine("end with return RemoteSqlitePreparedStatement");
			return remoteSqlitePreparedStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.connectionIsValid start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isValid = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "connectionIsValid", new Object[] { _connectionId, timeout }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("isValid="+isValid);
			_logger.fine("RemoteSqliteBean.connectionIsValid end");

			_logger.fine("end with return isValid");
			return isValid;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		//org.sqlite.jdbc4.JDBC4Connection
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		//org.sqlite.jdbc4.JDBC4Connection
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		//org.sqlite.jdbc4.JDBC4Connection
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		//org.sqlite.jdbc4.JDBC4Connection
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		//org.sqlite.jdbc4.JDBC4Connection
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Connection
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		//org.sqlite.SQLiteConnection
	}

	@Override
	public String getSchema() throws SQLException {
		//org.sqlite.SQLiteConnection
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		//org.sqlite.SQLiteConnection
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		//org.sqlite.SQLiteConnection
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		//org.sqlite.SQLiteConnection
		return 0;
	}

}
