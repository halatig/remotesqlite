package remotesqlite.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * The listener registers a RemoteSqliteBean to serve Remote SQLite JMX MBean calls handling a local JDBC connection.
 *   
 * @author Attila Halasz
 * 
 * */
@WebListener
public class RemoteSqliteBeanListener implements ServletContextListener {

	private static Logger _logger = Logger.getLogger(RemoteSqliteBeanListener.class.getName());
	
	/**
	 * Creating and registering RemoteSqliteBean JMX MBean object.
	 * 
	 * @param event ServletContextEvent
	 * 
	 */
	@Override
    public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		String level = servletContext.getInitParameter("remotesqlite.level");
		if (level!=null) { try { _logger.setLevel(Level.parse(level)); } catch (Exception e) { } }
		_logger.fine("start");
		
		try {
			MBeanServer currentMBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		if (!currentMBeanServer.isRegistered(remoteSqliteBeanObj)) {
    			RemoteSqliteMBean mbean = new RemoteSqliteBean(event.getServletContext());
    			currentMBeanServer.registerMBean(mbean, remoteSqliteBeanObj);
			}
			_logger.info("RemoteSqliteBean successfully registered");
		} catch (Exception e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_logger.fine("end");
	}

	/**
	 * Removes RemoteSqliteBean JMX MBean registration.
	 * 
	 * @param event ServletContextEvent
	 * 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		_logger.fine("start");

		try {
			MBeanServer currentMBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
			ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			if (currentMBeanServer.isRegistered(remoteSqliteBeanObj)) {
				currentMBeanServer.unregisterMBean(remoteSqliteBeanObj);
			}
			_logger.info("RemoteSqliteBean successfully deregistered");
		} catch (Exception e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}

		_logger.fine("end");
	}
    
}
