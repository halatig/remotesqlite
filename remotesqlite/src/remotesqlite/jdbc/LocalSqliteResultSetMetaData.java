package remotesqlite.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.RowSetMetaDataImpl;

public class LocalSqliteResultSetMetaData implements ResultSetMetaData {

	private static Logger _logger = Logger.getLogger(LocalSqliteResultSetMetaData.class.getName());

	private RowSetMetaDataImpl _rowSetMetaDataImpl;

	public LocalSqliteResultSetMetaData(RowSetMetaDataImpl rowSetMetaDataImpl) {
		_rowSetMetaDataImpl = rowSetMetaDataImpl;
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
	public int getColumnCount() throws SQLException {
		return _rowSetMetaDataImpl.getColumnCount();
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		return _rowSetMetaDataImpl.isAutoIncrement(column);
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return true;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return true;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return false;
	}

	@Override
	public int isNullable(int column) throws SQLException {
		return _rowSetMetaDataImpl.isNullable(column);
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        String typeName = getColumnTypeName(column);

        return "NUMERIC".equals(typeName) || "INTEGER".equals(typeName) || "REAL".equals(typeName);
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        return Integer.MAX_VALUE;
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        return getColumnName(column);
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		return _rowSetMetaDataImpl.getColumnName(column);
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        return "";
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		return _rowSetMetaDataImpl.getPrecision(column);
	}

	@Override
	public int getScale(int column) throws SQLException {
		return _rowSetMetaDataImpl.getScale(column);
	}

	@Override
	public String getTableName(int column) throws SQLException {
		return _rowSetMetaDataImpl.getTableName(column);
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
        return "";
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		return _rowSetMetaDataImpl.getColumnType(column);
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		return _rowSetMetaDataImpl.getColumnTypeName(column);
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return false;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return true;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		//org.sqlite.jdbc3.JDBC3ResultSet
		return true;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		return _rowSetMetaDataImpl.getColumnClassName(column);
	}

}
