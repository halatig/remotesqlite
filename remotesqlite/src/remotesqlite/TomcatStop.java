package remotesqlite;

import java.io.File;

public class TomcatStop {
	
	public static void main(String[] args) {

		try {
	        new File("tomcat.pid").delete();
		} catch (Exception e) {
			return;
		}
	}

}
