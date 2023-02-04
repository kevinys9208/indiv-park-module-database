package indiv.park.data;

import indiv.park.data.exception.TypeNotFoundException;

public enum DataSource {
	
	ORACLE() {
		@Override
		public String getUrl(String ip, int port, String sid) {
			return "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
		}

		@Override
		public String getDataSourceClassName() {
			return "oracle.jdbc.pool.OracleDataSource";
		}

		@Override
		public String getDiarect() {
			return "org.hibernate.dialect.OracleDialect";
		}
	},
	
	TIBERO() {
		@Override
		public String getUrl(String ip, int port, String sid) {
			return "jdbc:tibero:thin:@" + ip + ":" + port + ":" + sid;
		}

		@Override
		public String getDataSourceClassName() {
			return "com.tmax.tibero.jdbc.ext.TbConnectionPoolDataSource";
		}

		@Override
		public String getDiarect() {
			return "org.hibernate.dialect.Oracle9iDialect";
		}
	},
	
	SQLITE() {

		@Override
		public String getUrl(String ip, int port, String sid) {
			return "jdbc:sqlite:" + sid + ".sqlite";
		}

		@Override
		public String getDataSourceClassName() {
			return "org.sqlite.javax.SQLiteConnectionPoolDataSource";
		}

		@Override
		public String getDiarect() {
			return "org.sqlite.hibernate.dialect.SQLiteDialect";
		}
	};
	
	public abstract String getUrl(String ip, int port, String sid);
	public abstract String getDataSourceClassName();
	public abstract String getDiarect();

	public static DataSource getDataSourceByName(String name) {
		try {
			return DataSource.valueOf(name.toUpperCase());

		} catch (Exception e) {
			throw new TypeNotFoundException(name);
		}
	}
}
