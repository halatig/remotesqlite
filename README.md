# remotesqlite
JDBC driver and Tomcat server component for remote SQLite database access over JMX

![Concept art](https://halatig.github.io/remotesqlite/remotesqlite.jpg)

## Usage
Embedded SQLite databases on a Tomcat server are not suitable for remote read and write without a file access connection, especially in containers. This solution is developed for transfer JDBC calls from a remotesqlite driver equipped client (for eg DBeaver) to a Tomcat JMX Bean server component that has access to the SQLite database on the server. The network communication is over JMX technology, that can be protected by authentication and transfer security.

To get it work the remotesqlite/src/remotesqlite/server classes have to be added to a Tomcat application. The RemoteSQLiteBeanListener registers a RemoteSqliteMBean implementation (RemoteSQLiteBean) on the server, which is a managed bean to receive JMX calls from the client JDBC driver. The JDBC driver implementation is in the remotesqlite/src/remotesqlite/client folder, that is released in a remote-sqlite-jdbc-1.0.jar file. This jar file can be imported as a Generic driver in DBeaver.

![DBeaver settings](https://halatig.github.io/remotesqlite/dbeaver-driver-settings.jpg)
![DBeaver libraries](https://halatig.github.io/remotesqlite/dbeaver-driver-libraries.jpg)
![DBeaver libraries](https://halatig.github.io/remotesqlite/dbeaver-driver-properties.jpg)

The result sets are cached on the client for better performance. FINE Logging can be configured with remotesqlite.level parameter (both client and server side).

The remotesqlite/src/remotesqlite/TomcatStart is an example initialization presentation with an embedded Tomcat. This can be started with an ant script "start" target (see ant.xml). The code looks for tomcat.pid, the file deletion stops the embedded Tomcat, the TomcatStop (triggered by ant "stop" target) does this deletion.

The available databases are predefined "environments", and only they can be accessed from the client for security reasons. Such an environment variable in context.xml:
\<Environment name="sqlite_chinook" value="jdbc:sqlite:chinook.db" type="java.lang.String"/>
The program looks for "sqlite_" beginning environment parameters, and in the example the chinook.db database can be accessed from the client through "chinook" environment (database).

jmxremote.config file controls the served JMX port. For the embedded Tomcat example there is no authentication and transfer security configured.

The neccessary libraries are in the lib folder. The developed solution uses https://github.com/xerial/sqlite-jdbc driver on the server side to access SQLite database.

The result looks like this:
![DBeaver chinook](https://halatig.github.io/remotesqlite/dbeaver-chinook.jpg)
