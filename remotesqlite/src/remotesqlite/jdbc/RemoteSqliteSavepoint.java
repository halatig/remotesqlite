package remotesqlite.jdbc;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RemoteSqliteSavepoint implements Savepoint {

	private static Logger _logger = Logger.getLogger(RemoteSqliteSavepoint.class.getName());

	protected static MBeanServerConnection _remoteMBeanServerConnection; 
	protected String _savepointId;
	
	public RemoteSqliteSavepoint(MBeanServerConnection remoteMBeanServerConnection, String savepointId) {
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_savepointId = savepointId;
	}

	public void setLoggerLevel(Level level) {
		_logger.setLevel(level);
	}

	public String getId() {
		return _savepointId;
	}
	
	//INTERFACE METHODS	
	@Override
	public int getSavepointId() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.savepointGetSavepointId start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			int getSavepointId = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "savepointGetSavepointId", new Object[] { _savepointId }, new String[] {String.class.getName()});
			_logger.fine("getSavepointId="+getSavepointId);
			_logger.fine("RemoteSqliteBean.savepointGetSavepointId end");
			
			_logger.fine("end with return getSavepointName");
			return getSavepointId;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public String getSavepointName() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.savepointGetSavepointName start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String getSavepointName = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "savepointGetSavepointName", new Object[] { _savepointId }, new String[] {String.class.getName()});
			_logger.fine("getSavepointName="+getSavepointName);
			_logger.fine("RemoteSqliteBean.savepointGetSavepointName end");
			
			_logger.fine("end with return getSavepointName");
			return getSavepointName;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

}
