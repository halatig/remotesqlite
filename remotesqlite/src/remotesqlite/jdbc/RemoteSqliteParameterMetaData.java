package remotesqlite.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RemoteSqliteParameterMetaData implements ParameterMetaData {

	private static Logger _logger = Logger.getLogger(RemoteSqliteParameterMetaData.class.getName());

	private static MBeanServerConnection _remoteMBeanServerConnection; 
	private String _parameterMetaDataId;

	public RemoteSqliteParameterMetaData(MBeanServerConnection remoteMBeanServerConnection, String parameterMetaDataId) {
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_parameterMetaDataId = parameterMetaDataId;
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
	public int getParameterCount() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterCount start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getParameterCount = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "parameterMetaDataGetParameterCount", new Object[] { _parameterMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("getParameterCount="+getParameterCount);
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterCount end");
			
			_logger.fine("end with return getParameterCount");
			return getParameterCount;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int isNullable(int param) throws SQLException {
		//org.sqlite.jdbc3.JDBC4PreparedStatement
		return ParameterMetaData.parameterNullable;
	}

	@Override
	public boolean isSigned(int param) throws SQLException {
		//org.sqlite.jdbc3.JDBC4PreparedStatement
		return true;
	}

	@Override
	public int getPrecision(int param) throws SQLException {
		//org.sqlite.jdbc3.JDBC4PreparedStatement
		return 0;
	}

	@Override
	public int getScale(int param) throws SQLException {
		//org.sqlite.jdbc3.JDBC4PreparedStatement
		return 0;
	}

	@Override
	public int getParameterType(int param) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterType start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getParameterType = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "parameterMetaDataGetParameterType", new Object[] { _parameterMetaDataId, param }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("getParameterType="+getParameterType);
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterType end");
			
			_logger.fine("end with return getParameterType");
			return getParameterType;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public String getParameterTypeName(int param) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterTypeName start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String getParameterTypeName = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "parameterMetaDataGetParameterTypeName", new Object[] { _parameterMetaDataId, param }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("getParameterTypeName="+getParameterTypeName);
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterTypeName end");
			
			_logger.fine("end with return getParameterTypeName");
			return getParameterTypeName;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public String getParameterClassName(int param) throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterClassName start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String getParameterClassName = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "parameterMetaDataGetParameterClassName", new Object[] { _parameterMetaDataId, param }, new String[] {String.class.getName(), int.class.getName()});
			_logger.fine("getParameterClassName="+getParameterClassName);
			_logger.fine("RemoteSqliteBean.parameterMetaDataGetParameterClassName end");
			
			_logger.fine("end with return getParameterClassName");
			return getParameterClassName;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int getParameterMode(int param) throws SQLException {
		//org.sqlite.jdbc3.JDBC4PreparedStatement
        return ParameterMetaData.parameterModeIn;
	}

}
