package javaee.jta.jms.atomikos.example2.impl;

import java.util.Properties;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public class SavingAccount extends LocalAccount {	
	public SavingAccount(int id) {
		super(id);
	}

	@Override
	public DataSource getDataSource() {
		/*
		 * if without this checking, when getting balance, it will cause multiple instantiation, where jdbc/mysql is not longer unique
		 */
		if (ds == null) {
			ds = new AtomikosDataSourceBean();   
			// unique resource name for transaction recovery configuration
	        ds.setUniqueResourceName("jdbc/mysql");
	        // driver class file included in mysql-connector-java-5.1.9.jar
	        ds.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
	        Properties p = new Properties();
	        p.setProperty("serverName", "localhost");
	        p.setProperty("port", "3306");
	        p.setProperty("databaseName", "javaee_jta_jms_atomikos_example2");        
	        p.setProperty("user", "root");
	        p.setProperty("password", "");                
	        p.setProperty("url", "jdbc:mysql://localhost:3306/javaee_jta_jms_atomikos_example2?user=root&password=");
	        ds.setXaProperties(p);
		}
        return ds;
	}
}
