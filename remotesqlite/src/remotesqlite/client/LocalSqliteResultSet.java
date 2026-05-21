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
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;

public class LocalSqliteResultSet implements ResultSet {

	private static Logger _logger = Logger.getLogger(LocalSqliteResultSet.class.getName());

	private static CachedRowSet _cachedRowSet;
	private static MBeanServerConnection _remoteMBeanServerConnection; 
	private String _resultSetId;

	public LocalSqliteResultSet(CachedRowSet cachedRowSet, MBeanServerConnection remoteMBeanServerConnection, String resultSetId) {
		_cachedRowSet = cachedRowSet;
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_resultSetId = resultSetId;
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
	public boolean next() throws SQLException {
		return _cachedRowSet.next();
	}

	@Override
	public void close() throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.resultSetClose start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "resultSetClose", new Object[] { _resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.resultSetClose end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public boolean wasNull() throws SQLException {
		return _cachedRowSet.wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return _cachedRowSet.getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return _cachedRowSet.getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return _cachedRowSet.getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return _cachedRowSet.getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return _cachedRowSet.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return _cachedRowSet.getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return _cachedRowSet.getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return _cachedRowSet.getDouble(columnIndex);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return _cachedRowSet.getBigDecimal(columnIndex, scale);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return _cachedRowSet.getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return _cachedRowSet.getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return _cachedRowSet.getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return _cachedRowSet.getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return _cachedRowSet.getAsciiStream(columnIndex);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return _cachedRowSet.getUnicodeStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return _cachedRowSet.getBinaryStream(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return _cachedRowSet.getString(columnLabel);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return _cachedRowSet.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return _cachedRowSet.getByte(columnLabel);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return _cachedRowSet.getShort(columnLabel);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return _cachedRowSet.getInt(columnLabel);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return _cachedRowSet.getLong(columnLabel);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return _cachedRowSet.getFloat(columnLabel);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return _cachedRowSet.getDouble(columnLabel);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return _cachedRowSet.getBigDecimal(columnLabel, scale);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return _cachedRowSet.getBytes(columnLabel);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return _cachedRowSet.getDate(columnLabel);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return _cachedRowSet.getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return _cachedRowSet.getTimestamp(columnLabel);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return _cachedRowSet.getAsciiStream(columnLabel);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return _cachedRowSet.getUnicodeStream(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return _cachedRowSet.getBinaryStream(columnLabel);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
	}

	@Override
	public String getCursorName() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return null;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			_logger.fine("start");

			RowSetMetaDataImpl rowSetMetaDataImpl = (RowSetMetaDataImpl)_cachedRowSet.getMetaData();
			LocalSqliteResultSetMetaData localSqliteResultSetMetaData = new LocalSqliteResultSetMetaData(rowSetMetaDataImpl);
			if (!_logger.getLevel().equals(Level.INFO)) { localSqliteResultSetMetaData.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return LocalSqliteResultSetMetaData");
			return localSqliteResultSetMetaData;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return _cachedRowSet.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return _cachedRowSet.getObject(columnLabel);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return _cachedRowSet.findColumn(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return _cachedRowSet.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return _cachedRowSet.getCharacterStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return _cachedRowSet.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return _cachedRowSet.getBigDecimal(columnLabel);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return _cachedRowSet.isBeforeFirst();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return _cachedRowSet.isAfterLast();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return _cachedRowSet.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void beforeFirst() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public void afterLast() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public boolean first() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public boolean last() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public int getRow() throws SQLException {
		return _cachedRowSet.getRow();	
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public boolean previous() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        // Only FORWARD_ONLY ResultSets exist in SQLite, so only FETCH_FORWARD is permitted
        if (direction != ResultSet.FETCH_FORWARD) {
            throw new SQLException("only FETCH_FORWARD direction supported");
        }
		_cachedRowSet.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        return ResultSet.FETCH_FORWARD;
    }

	@Override
	public void setFetchSize(int rows) throws SQLException {
		_logger.fine("start");

		try {
			_logger.fine("RemoteSqliteBean.resultSetSetFetchSize start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "resultSetSetFetchSize", new Object[] { _resultSetId, rows }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("RemoteSqliteBean.resultSetSetFetchSize end");
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		

		_logger.fine("end");
	}

	@Override
	public int getFetchSize() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.resultSetGetFetchSize start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getFetchSize = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "resultSetGetFetchSize", new Object[] { _resultSetId }, new String[] {String.class.getName()});
			_logger.fine("getFetchSize="+getFetchSize);
			_logger.fine("RemoteSqliteBean.resultSetGetFetchSize end");
			
			_logger.fine("end with return getFetchSize");
			return getFetchSize;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int getType() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public int getConcurrency() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		 return ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return false;
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void insertRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public void updateRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}

	@Override
	public void deleteRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");	
	}
	
	@Override
	public void refreshRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
	}

	@Override
	public Statement getStatement() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.resultSetGetStatement start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String statementId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "resultSetGetStatement", new Object[] { _resultSetId }, new String[] {String.class.getName()});
			_logger.fine("statementId="+statementId);
			_logger.fine("RemoteSqliteBean.resultSetGetStatement end");
			
			RemoteSqliteStatement remoteSqliteStatement = new RemoteSqliteStatement(_remoteMBeanServerConnection, statementId);
			if (!_logger.getLevel().equals(Level.INFO)) { remoteSqliteStatement.setLoggerLevel(_logger.getLevel()); }
			
			_logger.fine("end with return RemoteSqliteStatement");
			return remoteSqliteStatement;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		throw new SQLFeatureNotSupportedException("not implemented by remote SQLite JDBC driver");
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		throw new SQLFeatureNotSupportedException("not implemented by remote SQLite JDBC driver");
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return _cachedRowSet.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return _cachedRowSet.getDate(columnLabel, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return _cachedRowSet.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return _cachedRowSet.getTime(columnLabel, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return _cachedRowSet.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return _cachedRowSet.getTimestamp(columnLabel, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getHoldability() throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		//cached result is always open
		return false;
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		//org.sqlite.jdbc4.JDBC4ResultSet
		throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new SQLFeatureNotSupportedException("not implemented by remote SQLite JDBC driver");
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw new SQLFeatureNotSupportedException("not implemented by remote SQLite JDBC driver");
	}
	
}
