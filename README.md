# remotesqlite
JDBC driver and Tomcat server component for remote SQLite database access over JMX

![Concept art](https://halatig.github.io/remotesqlite/remotesqlite.jpg)

## Usage
Embedded SQLite databases in a Tomcat web application are not suitable for remote read and write without a file access server connection. This can be sometimes complicated, especially when using containers.

This remotesqlite solution is developed for transfer JDBC calls from a remotesqlite driver equipped client (for eg DBeaver) to a JMX Bean server component which JVM has access to the SQLite database on the Tomcat server. The network communication is over JMX technology, that can be protected by authentication and transfer security.

To get it work the remotesqlite-server-1.0.jar has to be added to a Tomcat application lib. The RemoteSQLiteBeanListener registers a RemoteSqliteMBean implementation (RemoteSQLiteBean) on the server, which is a managed bean to receive JMX calls from the client JDBC driver.

![Remotesqlite bean](https://halatig.github.io/remotesqlite/remotesqlite-bean.jpg)

The accessible databases can be configured with a remotesqlite.properties file in WEB-INF. It has a structure:

remotesqlite.&lt;database name&gt;.path=&lt;sqlite database path on the server&gt;

remotesqlite.&lt;database name&gt;.authenticate=true|false

remotesqlite.&lt;database name&gt;.user=&lt;user&gt;

remotesqlite.&lt;database name&gt;.password=&lt;password&gt;

These are predefined database names, and only they can be accessed from the client for security reasons. In the example the chinook.db database can be accessed from the client through "chinook" database name, and because of db level authentication is set, the database name/url must contain a user and a password property.

The JDBC driver implementation is in remotesqlite-jdbc-1.0.jar file. This jar file can be imported as a Generic driver in DBeaver.
![DBeaver driver settings](https://halatig.github.io/remotesqlite/dbeaver-driver-settings.jpg)
![DBeaver driver libraries](https://halatig.github.io/remotesqlite/dbeaver-driver-libraries.jpg)

The result sets are cached on the client for better performance. FINE logging can be configured with 'remotesqlite.level' parameter (both client and server side), server side in web.xml/config.xml/remotesqlite.properties file, client side as driver property.
![DBeaver driver properties](https://halatig.github.io/remotesqlite/dbeaver-driver-properties.jpg)

An authentication protected connection example looks like this:
![DBeaver connection settings](https://halatig.github.io/remotesqlite/dbeaver-connection-settings.jpg)

The remotesqlite/TomcatStart is a test application with an embedded Tomcat. This can be started with an ant script "start" target. The code looks for tomcat.pid, the file deletion stops the embedded Tomcat, it can triggered by ant "stop" target. The jmxremote.config file controls the served JMX port. For the embedded Tomcat example there is no authentication and transfer security configured. The neccessary libraries are in the lib folder. The developed solution uses https://github.com/xerial/sqlite-jdbc driver on the server side to access SQLite database.

The result looks like this in function:
![DBeaver chinook](https://halatig.github.io/remotesqlite/dbeaver-chinook.jpg)
