package remotesqlite.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.sql.rowset.CachedRowSet;

public class RemoteSqliteStatement implements Statement {

	private static Logger _logger = Logger.getLogger(RemoteSqliteStatement.class.getName());

	protected static MBeanServerConnection _remoteMBeanServerConnection; 
	protected String _statementId;
	
	public RemoteSqliteStatement(MBeanServerConnection remoteMBeanServerConnection, String statementId) {
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_statementId = statementId;
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
	public ResultSet executeQuery(String sql) throws SQLException {
  		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.statementExecuteQuery start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteQuery", new Object[] { _statementId, sql }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.statementExecuteQuery end");

			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			if (!_logger.getLevel().equals(Level.INFO)) { localSqliteResultSet.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int executeUpdate = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteUpdate", new Object[] { _statementId, sql }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("executeUpdate="+executeUpdate);
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate end");
			
			_logger.fine("end with return executeUpdate");
			return executeUpdate;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void close() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementClose start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementClose", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.statementClose end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
		return 0;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
        if (max < 0) throw new SQLException("max field size " + max + " cannot be negative");
	}

	@Override
	public int getMaxRows() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetMaxRows start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int getMaxRows = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetMaxRows", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("getMaxRows="+getMaxRows);
			_logger.fine("RemoteSqliteBean.statementGetMaxRows end");
			
			_logger.fine("end with return getMaxRows");
			return getMaxRows;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementSetMaxRows start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementSetMaxRows", new Object[] { _statementId, max }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.statementSetMaxRows end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetQueryTimeout start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int getQueryTimeout = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetQueryTimeout", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("getQueryTimeout="+getQueryTimeout);
			_logger.fine("RemoteSqliteBean.statementGetQueryTimeout end");
			
			_logger.fine("end with return getQueryTimeout");
			return getQueryTimeout;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementSetQueryTimeout start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementSetQueryTimeout", new Object[] { _statementId, seconds }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.statementSetQueryTimeout end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public void cancel() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementCancel start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementCancel", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.statementCancel end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement		
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecute start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean execute = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecute", new Object[] { _statementId, sql }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("execute="+execute);
			_logger.fine("RemoteSqliteBean.statementExecute end");
			
			_logger.fine("end with return execute");
			return execute;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.statementGetResultSet start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetResultSet", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.statementGetResultSet end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			if (!_logger.getLevel().equals(Level.INFO)) { localSqliteResultSet.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int getUpdateCount() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetUpdateCount start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int getUpdateCount = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetUpdateCount", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("getUpdateCount="+getUpdateCount);
			_logger.fine("RemoteSqliteBean.statementGetUpdateCount end");
			
			_logger.fine("end with return getUpdateCount");
			return getUpdateCount;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetMoreResults start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean getMoreResults = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetMoreResults", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("getMoreResults="+getMoreResults);
			_logger.fine("RemoteSqliteBean.statementGetMoreResults end");
			
			_logger.fine("end with return getMoreResults");
			return getMoreResults;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement		
        switch (direction) {
        	case ResultSet.FETCH_FORWARD:
        	case ResultSet.FETCH_REVERSE:
        	case ResultSet.FETCH_UNKNOWN:
        		// No-op: SQLite does not support a value other than FETCH_FORWARD
        		break;
        	default:
        		throw new SQLException(
                    "Unknown fetch direction "
                    	+ direction
                        + ". "
                        + "Must be one of FETCH_FORWARD, FETCH_REVERSE, or FETCH_UNKNOWN in java.sql.ResultSet");
        }				
	}

	@Override
	public int getFetchDirection() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
        return ResultSet.FETCH_FORWARD;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementSetFetchSize start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementSetFetchSize", new Object[] { _statementId, rows }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.statementSetFetchSize end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public int getFetchSize() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetFetchSize start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int getFetchSize = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetFetchSize", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("getFetchSize="+getFetchSize);
			_logger.fine("RemoteSqliteBean.statementGetFetchSize end");
			
			_logger.fine("end with return getFetchSize");
			return getFetchSize;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
		return ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public int getResultSetType() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
        return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementAddBatch start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementAddBatch", new Object[] { _statementId, sql }, new String[] {String.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.statementAddBatch end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");	
	}

	@Override
	public void clearBatch() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementClearBatch start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementClearBatch", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.statementClearBatch end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");		
	}

	@Override
	public int[] executeBatch() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecuteBatch start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int[] executeBatch = (int[])_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteBatch", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("executeBatch="+executeBatch);
			_logger.fine("RemoteSqliteBean.statementExecuteBatch end");
			
			_logger.fine("end with return executeBatch");
			return executeBatch;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.statementGetConnection start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String connectionId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetConnection", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("connectionId="+connectionId);
			_logger.fine("RemoteSqliteBean.statementGetConnection end");
			
			RemoteSqliteConnection remoteSqliteConnection = new RemoteSqliteConnection(_remoteMBeanServerConnection, connectionId);
			if (!_logger.getLevel().equals(Level.INFO)) { remoteSqliteConnection.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return RemoteSqliteConnection");
			return remoteSqliteConnection;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementGetMoreResults2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean getMoreResults = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetMoreResults2", new Object[] { _statementId, current }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("getMoreResults="+getMoreResults);
			_logger.fine("RemoteSqliteBean.statementGetMoreResults2 end");
			
			_logger.fine("end with return getMoreResults");
			return getMoreResults;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.statementGetGeneratedKeys start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementGetGeneratedKeys", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.statementGetGeneratedKeys end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			if (!_logger.getLevel().equals(Level.INFO)) { localSqliteResultSet.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int executeUpdate = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteUpdate2", new Object[] { _statementId, sql, autoGeneratedKeys }, new String[] {String.class.getName(), String.class.getName(), int.class.getName()});
			_logger.fine("executeUpdate="+executeUpdate);
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate2 end");
			
			_logger.fine("end with return executeUpdate");
			return executeUpdate;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int executeUpdate = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteUpdate3", new Object[] { _statementId, sql, columnIndexes }, new String[] {String.class.getName(), String.class.getName(), int[].class.getName()});
			_logger.fine("executeUpdate="+executeUpdate);
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate3 end");
			
			_logger.fine("end with return executeUpdate");
			return executeUpdate;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate4 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int executeUpdate = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecuteUpdate4", new Object[] { _statementId, sql, columnNames }, new String[] {String.class.getName(), String.class.getName(), String[].class.getName()});
			_logger.fine("executeUpdate="+executeUpdate);
			_logger.fine("RemoteSqliteBean.statementExecuteUpdate4 end");
			
			_logger.fine("end with return executeUpdate");
			return executeUpdate;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecute2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean execute = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecute2", new Object[] { _statementId, sql, autoGeneratedKeys }, new String[] {String.class.getName(), String.class.getName(), int.class.getName()});
			_logger.fine("execute="+execute);
			_logger.fine("RemoteSqliteBean.statementExecute2 end");
			
			_logger.fine("end with return execute");
			return execute;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecute3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean execute = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecute3", new Object[] { _statementId, sql, columnIndexes }, new String[] {String.class.getName(), String.class.getName(), int[].class.getName()});
			_logger.fine("execute="+execute);
			_logger.fine("RemoteSqliteBean.statementExecute3 end");
			
			_logger.fine("end with return execute");
			return execute;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementExecute4 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean execute = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementExecute4", new Object[] { _statementId, sql, columnNames }, new String[] {String.class.getName(), String.class.getName(), String[].class.getName()});
			_logger.fine("execute="+execute);
			_logger.fine("RemoteSqliteBean.statementExecute4 end");
			
			_logger.fine("end with return execute");
			return execute;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		//org.sqlite.jdbc3.JDBC3Statement
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public boolean isClosed() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementIsClosed start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isClosed = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementIsClosed", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("isClosed="+isClosed);
			_logger.fine("RemoteSqliteBean.statementIsClosed end");
			
			_logger.fine("end with return isClosed");
			return isClosed;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		//org.sqlite.jdbc4.JDBC4Statement
	}

	@Override
	public boolean isPoolable() throws SQLException {
		//org.sqlite.jdbc4.JDBC4Statement
		return false;
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.statementCloseOnCompletion start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementCloseOnCompletion", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.statementCloseOnCompletion end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");	
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.statementIsCloseOnCompletion start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isCloseOnCompletion = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "statementIsCloseOnCompletion", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("isCloseOnCompletion="+isCloseOnCompletion);
			_logger.fine("RemoteSqliteBean.statementIsCloseOnCompletion end");
			
			_logger.fine("end with return isCloseOnCompletion");
			return isCloseOnCompletion;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

}
