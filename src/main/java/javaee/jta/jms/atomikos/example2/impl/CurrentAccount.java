package javaee.jta.jms.atomikos.example2.impl;

import java.util.Properties;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public class CurrentAccount extends LocalAccount {
	public CurrentAccount(int id) {
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
	        ds.setUniqueResourceName("jdbc/postgresql");
	        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
	        Properties p = new Properties();
	        p.setProperty("serverName", "localhost");
	        /*
	         	comment this because it hit    
	         	com.atomikos.beans.PropertyException: no writeable property 'port' in class 'org.postgresql.xa.PGXADataSource'     		
	         */
	        //p.setProperty("port", "5432");
	        p.setProperty("databaseName", "javaee_jta_jms_atomikos_example2");        
	        p.setProperty("user", "postgres");
	        p.setProperty("password", "");    
	        /*
	         	comment this because it hit  
	         	com.atomikos.beans.PropertyException: no writeable property 'url' in class 'org.postgresql.xa.PGXADataSource'
	         */
	        //p.setProperty("url", "jdbc:postgresql://localhost:5432/javaee_jta_jms_atomikos_example2?user=root&password=");
	        ds.setXaProperties(p); 
		}
        return ds;
	}
}
