package javaee.jta.jms.atomikos.example2.impl;

import java.util.ArrayList;

import javaee.jta.jms.atomikos.example2.Account;
import javaee.jta.jms.atomikos.example2.Transaction;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.XAConnectionFactory;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQXAConnectionFactory;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jms.AtomikosConnectionFactoryBean;

public class RemoteTransaction implements Transaction {
	private Account withdrawalAccount = null;
	private Account despoitAccount = null;
	private String destinationURL = null;
	private AtomikosConnectionFactoryBean cf = null;
	
	public RemoteTransaction(RetailBank bank) {
		this.destinationURL = bank.getBankURL();
	}
	
	public ConnectionFactory getConnectionFactory() throws JMSException {
		/*
			reference http://www.debugease.com/j2ee/3422394.html
		*/
		if (cf == null) {
			XAConnectionFactory xacf = new ActiveMQXAConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, this.destinationURL);  
			cf = new AtomikosConnectionFactoryBean(); 
			cf.setUniqueResourceName("jms/activemq"); 
			cf.setXaConnectionFactory(xacf); 
			cf.setPoolSize(2);
		}
		return cf;			
	}
	
	public void wire(int amount) throws Exception {
		Connection conn = this.getConnectionFactory().createConnection();
		Session session = conn.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);
		Destination destination = session.createQueue("queue");
		MessageProducer producer = session.createProducer(destination);
		ObjectMessage message = session.createObjectMessage();
		/*
			disallowed message.setObject("despoitAccount", despoitAccount); 
			javax.jms.MessageFormatException: Only objectified primitive objects, String, Map and List types are allowed

		*/
		message.setObjectProperty("despoitAccount", (new ArrayList<Account>()).add(despoitAccount));
		message.setIntProperty("amount", amount);
        producer.send(message);         
        session.close();  
        conn.close();
	}

	
	@Override
	public void setWithdrawal(Account account) {
		this.withdrawalAccount = account;		
	}

	@Override
	public void setDeposit(Account account) {
		this.despoitAccount = account;		
	}
	
	@Override
	public void transfer(int amount) {		
		UserTransaction utx = new UserTransactionImp();
		try {
			utx.begin();			
			withdrawalAccount.withdraw(amount);		
			// before commit, if it tries to get Account .getBalance(), it will throw error
			this.wire(amount);
			utx.commit();
			System.out.println("commit success");
		} catch (Exception e) {			
			try {
				utx.rollback();
				System.out.println("rollbak success");
				e.printStackTrace();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}          
		} 
	}
	
	@Override
	public void transferWithError(int amount) {		
		UserTransaction utx = new UserTransactionImp();
		try {
			utx.begin();	
			// to test if message sent can be rollback
			this.wire(amount);
			//deliberately make error
			withdrawalAccount.withdraw(Integer.parseInt("amount"));		
			utx.commit();
			System.out.println("commit success");
		} catch (Exception e) {			
			try {
				utx.rollback();
				System.out.println("rollbak success");
				e.printStackTrace();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}          
		} 
	}

}
