package remotesqlite.client;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;

public class RemoteSqlitePreparedStatement extends RemoteSqliteStatement implements PreparedStatement {

	private static Logger _logger = Logger.getLogger(RemoteSqlitePreparedStatement.class.getName());

	public RemoteSqlitePreparedStatement(MBeanServerConnection remoteMBeanServerConnection, String preparedStatementId) {
		super(remoteMBeanServerConnection,preparedStatementId);
	}

	public void setLoggerLevel(Level level) {
		_logger.setLevel(level);
	}

	//INTERFACE METHODS
	@Override
	public ResultSet executeQuery() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.preparedStatementExecuteQuery start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementExecuteQuery", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.preparedStatementExecuteQuery end");
			
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
	public int executeUpdate() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.preparedStatementExecuteUpdate start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int executeUpdate = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementExecuteUpdate", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("executeUpdate="+executeUpdate);
			_logger.fine("RemoteSqliteBean.preparedStatementExecuteUpdate end");
			
			_logger.fine("end with return executeUpdate");
			return executeUpdate;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNull start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNull", new Object[] { _statementId, parameterIndex, sqlType }, new String[] {String.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNull end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBoolean start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBoolean", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), boolean.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBoolean end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetByte start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetByte", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), byte.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetByte end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetShort start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetShort", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), short.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetShort end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetInt start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetInt", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetInt end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetLong start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetLong", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetLong end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetFloat start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetFloat", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), float.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetFloat end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetDouble start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetDouble", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), double.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetDouble end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBigDecimal start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBigDecimal", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), BigDecimal.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBigDecimal end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetString start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetString", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetString end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBytes start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBytes", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), byte[].class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBytes end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetDate start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetDate", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), Date.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetDate end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetTime start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetTime", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), Time.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetTime end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetTimestamp start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetTimestamp", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), Timestamp.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetTimestamp end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetAsciiStream", new Object[] { _statementId, parameterIndex, x, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetUnicodeStream start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetUnicodeStream", new Object[] { _statementId, parameterIndex, x, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetUnicodeStream end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBinaryStream", new Object[] { _statementId, parameterIndex, x, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void clearParameters() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementClearParameters start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementClearParameters", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementClearParameters end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetObject", new Object[] { _statementId, parameterIndex, x, targetSqlType }, new String[] {String.class.getName(), int.class.getName(), Object.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetObject2", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), Object.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public boolean execute() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.preparedStatementExecuteUpdate start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean execute = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementExecute", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("execute="+execute);
			_logger.fine("RemoteSqliteBean.preparedStatementExecuteUpdate end");
			
			_logger.fine("end with return execute");
			return execute;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void addBatch() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementAddBatch start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementAddBatch", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementAddBatch end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetCharacterStream", new Object[] { _statementId, parameterIndex, reader, length }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.preparedStatementGetMetaData start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		RowSetMetaDataImpl rowSetMetaDataImpl = (RowSetMetaDataImpl)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementGetMetaData", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementGetMetaData end");

			LocalSqliteResultSetMetaData localSqliteResultSetMetaData = new LocalSqliteResultSetMetaData(rowSetMetaDataImpl);
			if (!_logger.getLevel().equals(Level.INFO)) { localSqliteResultSetMetaData.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return LocalSqliteResultSetMetaData");
			return localSqliteResultSetMetaData;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetDate2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetDate2", new Object[] { _statementId, parameterIndex, x, cal }, new String[] {String.class.getName(), int.class.getName(), Date.class.getName(), Calendar.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetDate2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetTime2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetTime2", new Object[] { _statementId, parameterIndex, x, cal }, new String[] {String.class.getName(), int.class.getName(), Time.class.getName(), Calendar.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetTime2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetTimestamp2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetTimestamp2", new Object[] { _statementId, parameterIndex, x, cal }, new String[] {String.class.getName(), int.class.getName(), Timestamp.class.getName(), Calendar.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetTimestamp2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNull2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNull2", new Object[] { _statementId, parameterIndex, sqlType, typeName }, new String[] {String.class.getName(), int.class.getName(), int.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNull2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.preparedStatementGetParameterMetaData start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String parameterMetaDataId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementGetParameterMetaData", new Object[] { _statementId }, new String[] {String.class.getName()});
			_logger.fine("parameterMetaDataId="+parameterMetaDataId);
			_logger.fine("RemoteSqliteBean.preparedStatementGetParameterMetaData end");
			
			RemoteSqliteParameterMetaData remoteSqliteParameterMetaData = new RemoteSqliteParameterMetaData(_remoteMBeanServerConnection, parameterMetaDataId);
			if (!_logger.getLevel().equals(Level.INFO)) { remoteSqliteParameterMetaData.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return RemoteSqliteParameterMetaData");
			return remoteSqliteParameterMetaData;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetRowId start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetRowId", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), RowId.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetRowId end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNString start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNString", new Object[] { _statementId, parameterIndex, value }, new String[] {String.class.getName(), int.class.getName(), String.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNString end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNCharacterStream start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNCharacterStream", new Object[] { _statementId, parameterIndex, value, length }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNCharacterStream end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNClob", new Object[] { _statementId, parameterIndex, value }, new String[] {String.class.getName(), int.class.getName(), NClob.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetClob start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetClob", new Object[] { _statementId, parameterIndex, reader, length }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetClob end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBlob start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBlob", new Object[] { _statementId, parameterIndex, inputStream, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBlob end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNClob2", new Object[] { _statementId, parameterIndex, reader, length }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetSQLXML start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetSQLXML", new Object[] { _statementId, parameterIndex, xmlObject }, new String[] {String.class.getName(), int.class.getName(), SQLXML.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetSQLXML end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetObject3", new Object[] { _statementId, parameterIndex, x, targetSqlType, scaleOrLength }, new String[] {String.class.getName(), int.class.getName(), Object.class.getName(), int.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetObject3 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetAsciiStream2", new Object[] { _statementId, parameterIndex, x, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBinaryStream2", new Object[] { _statementId, parameterIndex, x, length }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetCharacterStream2", new Object[] { _statementId, parameterIndex, reader, length }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName(), long.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetAsciiStream3", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetAsciiStream3 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBinaryStream3", new Object[] { _statementId, parameterIndex, x }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBinaryStream3 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetCharacterStream3", new Object[] { _statementId, parameterIndex, reader }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetCharacterStream3 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNCharacterStream2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNCharacterStream2", new Object[] { _statementId, parameterIndex, value }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNCharacterStream2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetClob2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetClob2", new Object[] { _statementId, parameterIndex, reader }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetClob2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetBlob2 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetBlob2", new Object[] { _statementId, parameterIndex, inputStream }, new String[] {String.class.getName(), int.class.getName(), InputStream.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetBlob2 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob3 start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "preparedStatementSetNClob3", new Object[] { _statementId, parameterIndex, reader }, new String[] {String.class.getName(), int.class.getName(), Reader.class.getName()});
			_logger.fine("RemoteSqliteBean.preparedStatementSetNClob3 end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	//INVALID STATEMENT METHODS
	@Override
	public boolean execute(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}
	
	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
    public long executeLargeUpdate(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
    }

	@Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
    }

	@Override
	public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
    }

	@Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
    }

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		//org.sqlite.jdbc3.JDBC3PreparedStatement
		throw new SQLException("method cannot be called on a PreparedStatement");
	}

}
