package remotesqlite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * Embedded Tomcat test.
 *   
 * @author Attila Halasz
 * 
 * */
public class TomcatStart {
	
	public static void main(String[] args) throws LifecycleException  {

    	Tomcat tomcat = new Tomcat();
	    tomcat.setPort(8080);
	    tomcat.getConnector();
	    tomcat.enableNaming();
	        
	    String docBase = new File(".").getAbsolutePath();
	    Context context = tomcat.addContext("", docBase);

	    //META-INF/config.xml
	    //<Parameter name="remotesqlite.level" value="FINE" />
	    //
	    //or
	    //
	    //WEB-INF/web.xml
	    //<context-param>
    	//	<param-name>remotesqlite.level</param-name>
	    //	<param-value>FINE</param-value>
	    //</context-param>
	    //
	    //context.addParameter("remotesqlite.level", "FINE");
	        
	    context.addApplicationListener(remotesqlite.server.RemoteSqliteBeanListener.class.getName());

    	tomcat.start();

    	System.out.println("Tomcat started on localhost:8080");
    	
    	try (FileWriter writer = new FileWriter("tomcat.pid")) {
			writer.write(String.valueOf(ProcessHandle.current().pid()));
    	} catch (IOException e) {
	        e.printStackTrace();
            tomcat.stop();
            System.exit(1); 
		}

    	File pidFile = new File("tomcat.pid");
        Thread watcherThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000); 
                    
        			if (!pidFile.exists()) {
                        tomcat.stop();
                        System.exit(0); 
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.exit(1);
            }
        });
        
        watcherThread.setDaemon(true);
        watcherThread.start();

        tomcat.getServer().await();
	}

}
