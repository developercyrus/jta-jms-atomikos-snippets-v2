package javaee.jta.jms.atomikos.example2.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javaee.jta.jms.atomikos.example2.Account;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public abstract class LocalAccount implements Account {
	private int id = 0;
	/*
		AtomikosDataSourceBean takes care of resource enlistment and delistment for you. 
		You do not have to call Transaction methods like enlistResource and delistResource. 
		You also don't have to care about recovery as this is also handled fully transparently.
		
		http://www.atomikos.com/Documentation/ConfiguringJdbc
	 */	
	protected AtomikosDataSourceBean ds;
	
	public abstract DataSource getDataSource();

	public LocalAccount(int id) {
		this.id = id;
	}
	
	@Override
	public void withdraw(int amount) throws SQLException {
		DataSource ds = getDataSource();
		Connection conn = ds.getConnection();
		Statement s = conn.createStatement();
		s.executeUpdate("update account set balance = balance - " + amount + " where id = " + id);
		s.close();
		conn.close();
	}
	
	@Override
	public void deposit(int amount) throws SQLException {
		this.withdraw(-amount);
	}

	@Override
	public int getBalance() throws SQLException {
		int balance = -1;
		DataSource ds = getDataSource();
		Connection conn = ds.getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select balance from account where id = " + id);
		if (rs == null || !rs.next()) {
			throw new SQLException("Account not found: " + id);
		}
		balance = rs.getInt("balance");
		s.close();
		conn.close();
		return balance;
	}
}
