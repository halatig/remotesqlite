package remotesqlite.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.sql.rowset.CachedRowSet;

public class RemoteSqliteDatabaseMetaData implements DatabaseMetaData {

	private static Logger _logger = Logger.getLogger(RemoteSqliteDatabaseMetaData.class.getName());

	protected static MBeanServerConnection _remoteMBeanServerConnection; 
	protected String _databaseMetaDataId;
	
	public RemoteSqliteDatabaseMetaData(MBeanServerConnection remoteMBeanServerConnection, String databaseMetaDataId) {
		_remoteMBeanServerConnection = remoteMBeanServerConnection;
		_databaseMetaDataId = databaseMetaDataId;
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
	public boolean allProceduresAreCallable() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public String getURL() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetURL start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String getURL = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetURL", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("getURL="+getURL);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetURL end");
			
			_logger.fine("end with return getURL");
			return getURL;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public String getUserName() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return null;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataIsReadOnly start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean isReadOnly = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataIsReadOnly", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("isReadOnly="+isReadOnly);
			_logger.fine("RemoteSqliteBean.databaseMetaDataIsReadOnly end");
			
			_logger.fine("end with return isReadOnly");
			return isReadOnly;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public String getDatabaseProductName() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "SQLite";
	}

	@Override
	public String getDatabaseProductVersion() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseProductVersion start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		String getDatabaseProductVersion = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetDatabaseProductVersion", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("getDatabaseProductVersion="+getDatabaseProductVersion);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseProductVersion end");
			
			_logger.fine("end with return getDatabaseProductVersion");
			return getDatabaseProductVersion;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public String getDriverName() throws SQLException {
		return "Remote SQLite JDBC";
	}

	@Override
	public String getDriverVersion() throws SQLException {
		return "1.0";
	}

	@Override
	public int getDriverMajorVersion() {
		return 1;
	}

	@Override
	public int getDriverMinorVersion() {
		return 0;
	}

	@Override
	public boolean usesLocalFiles() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return "\"";
	}

	@Override
	public String getSQLKeywords() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "ABORT,ACTION,AFTER,ANALYZE,ATTACH,AUTOINCREMENT,BEFORE,"
                + "CASCADE,CONFLICT,DATABASE,DEFERRABLE,DEFERRED,DESC,DETACH,"
                + "EXCLUSIVE,EXPLAIN,FAIL,GLOB,IGNORE,INDEX,INDEXED,INITIALLY,INSTEAD,ISNULL,"
                + "KEY,LIMIT,NOTNULL,OFFSET,PLAN,PRAGMA,QUERY,"
                + "RAISE,REGEXP,REINDEX,RENAME,REPLACE,RESTRICT,"
                + "TEMP,TEMPORARY,TRANSACTION,VACUUM,VIEW,VIRTUAL";
	}

	@Override
	public String getNumericFunctions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "";
	}

	@Override
	public String getStringFunctions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "";
	}

	@Override
	public String getSystemFunctions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "";
	}

	@Override
	public String getTimeDateFunctions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "DATE,TIME,DATETIME,JULIANDAY,STRFTIME";
	}

	@Override
	public String getSearchStringEscape() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return "\\";
	}

	@Override
	public String getExtraNameCharacters() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "";
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsConvert() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsGroupBy() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataSupportsFullOuterJoins start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		boolean supportsFullOuterJoins = (boolean)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataSupportsFullOuterJoins", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("supportsFullOuterJoins="+supportsFullOuterJoins);
			_logger.fine("RemoteSqliteBean.databaseMetaDataSupportsFullOuterJoins end");
			
			_logger.fine("end with return supportsFullOuterJoins");
			return supportsFullOuterJoins;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public String getSchemaTerm() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "schema";
	}

	@Override
	public String getProcedureTerm() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "not_implemented";
	}

	@Override
	public String getCatalogTerm() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return "catalog";
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public String getCatalogSeparator() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return ".";
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsUnion() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsUnionAll() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxConnections() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxIndexLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxRowSize() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public int getMaxStatementLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxStatements() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxTableNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getMaxUserNameLength() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 0;
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return Connection.TRANSACTION_SERIALIZABLE;
	}

	@Override
	public boolean supportsTransactions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return level == Connection.TRANSACTION_SERIALIZABLE;
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetProcedures start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetProcedures", new Object[] { _databaseMetaDataId, catalog, schemaPattern, procedureNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetProcedures end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetProcedureColumns start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetProcedureColumns", new Object[] { _databaseMetaDataId, catalog, schemaPattern, procedureNamePattern, columnNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetProcedureColumns end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTables start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetTables", new Object[] { _databaseMetaDataId, catalog, schemaPattern, tableNamePattern, types }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String[].class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTables end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSchemas start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetSchemas", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSchemas end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getCatalogs() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetCatalogs start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetCatalogs", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetCatalogs end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getTableTypes() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTableTypes start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetTableTypes", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTableTypes end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetColumns start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetColumns", new Object[] { _databaseMetaDataId, catalog, schemaPattern, tableNamePattern, columnNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetColumns end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetColumnPrivileges start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetColumnPrivileges", new Object[] { _databaseMetaDataId, catalog, schema, table, columnNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetColumnPrivileges end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTablePrivileges start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetTablePrivileges", new Object[] { _databaseMetaDataId, catalog, schemaPattern, tableNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTablePrivileges end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetBestRowIdentifier start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetBestRowIdentifier", new Object[] { _databaseMetaDataId, catalog, schema, table, scope, nullable }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), int.class.getName(), boolean.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetBestRowIdentifier end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetVersionColumns start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetVersionColumns", new Object[] { _databaseMetaDataId, catalog, schema, table }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetVersionColumns end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetPrimaryKeys start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetPrimaryKeys", new Object[] { _databaseMetaDataId, catalog, schema, table }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetPrimaryKeys end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetImportedKeys start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetImportedKeys", new Object[] { _databaseMetaDataId, catalog, schema, table }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetImportedKeys end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetExportedKeys start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetExportedKeys", new Object[] { _databaseMetaDataId, catalog, schema, table }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetExportedKeys end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetCrossReference start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetCrossReference", new Object[] { _databaseMetaDataId, parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetCrossReference end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getTypeInfo() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTypeInfo start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetTypeInfo", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetTypeInfo end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetIndexInfo start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetIndexInfo", new Object[] { _databaseMetaDataId, catalog, schema, table, unique, approximate }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), boolean.class.getName(), boolean.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetIndexInfo end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean supportsResultSetType(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return type == ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return type == ResultSet.TYPE_FORWARD_ONLY && concurrency == ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean ownDeletesAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean ownInsertsAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean othersDeletesAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean othersInsertsAreVisible(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean updatesAreDetected(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean deletesAreDetected(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean insertsAreDetected(int type) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetUDTs start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetUDTs", new Object[] { _databaseMetaDataId, catalog, schemaPattern, typeNamePattern, types }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), int[].class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetUDTs end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetConnection start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String connectionId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetConnection", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("connectionId="+connectionId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetConnection end");
			
			RemoteSqliteConnection remoteSqliteConnection = new RemoteSqliteConnection(_remoteMBeanServerConnection, connectionId);
			remoteSqliteConnection.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return RemoteSqliteConnection");
			return remoteSqliteConnection;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean supportsSavepoints() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return true;
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return true;
	}

	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSuperTypes start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetSuperTypes", new Object[] { _databaseMetaDataId, catalog, schemaPattern, typeNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSuperTypes end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSuperTables start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetSuperTables", new Object[] { _databaseMetaDataId, catalog, schemaPattern, tableNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetSuperTables end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
		try {
			_logger.fine("start");

			_logger.fine("RemoteSqliteBean.databaseMetaDataGetAttributes start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
			String resultSetId = (String)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetAttributes", new Object[] { _databaseMetaDataId, catalog, schemaPattern, typeNamePattern, attributeNamePattern }, new String[] {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()});
			_logger.fine("resultSetId="+resultSetId);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetAttributes end");
			
			_logger.fine("RemoteSqliteBean.getCachedRowSet start");
    		remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		CachedRowSet cachedRowSet = (CachedRowSet)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "getCachedRowSet", new Object[] { resultSetId }, new String[] {String.class.getName()});
			_logger.fine("RemoteSqliteBean.getCachedRowSet end");

			LocalSqliteResultSet localSqliteResultSet = new LocalSqliteResultSet(cachedRowSet, _remoteMBeanServerConnection, resultSetId);
			localSqliteResultSet.setLoggerLevel(_logger.getLevel());
			
			_logger.fine("end with return LocalSqliteResultSet");
			return localSqliteResultSet;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseMajorVersion start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getDatabaseMajorVersion = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetDatabaseMajorVersion", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("getDatabaseMajorVersion="+getDatabaseMajorVersion);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseMajorVersion end");
			
			_logger.fine("end with return getDatabaseMajorVersion");
			return getDatabaseMajorVersion;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
		try {
			_logger.fine("start");
			
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseMinorVersion start");
    		ObjectName remoteSqliteBeanObj = new ObjectName("Catalina:type=Resource,resourcetype=Context,host=localhost,context=/,class=remotesqlite.server.RemoteSqliteBean,name=\"remotesqlite/RemoteSqliteBean\"");
    		int getDatabaseMinorVersion = (int)_remoteMBeanServerConnection.invoke(remoteSqliteBeanObj, "databaseMetaDataGetDatabaseMinorVersion", new Object[] { _databaseMetaDataId }, new String[] {String.class.getName()});
			_logger.fine("getDatabaseMinorVersion="+getDatabaseMinorVersion);
			_logger.fine("RemoteSqliteBean.databaseMetaDataGetDatabaseMinorVersion end");
			
			_logger.fine("end with return getDatabaseMinorVersion");
			return getDatabaseMinorVersion;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}		
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 4;
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return 2;
	}

	@Override
	public int getSQLStateType() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return DatabaseMetaData.sqlStateSQL99;
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData
		return false;
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
		//org.sqlite.jdbc3.JDBC3DatabaseMetaData		
        throw new SQLFeatureNotSupportedException("Not yet implemented by SQLite JDBC driver");
	}

	@Override
	public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		//org.sqlite.jdbc4.JDBC4DatabaseMetaData		
        throw new SQLFeatureNotSupportedException();
	}

}
