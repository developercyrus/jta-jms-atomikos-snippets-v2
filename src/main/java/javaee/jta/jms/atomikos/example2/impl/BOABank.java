package javaee.jta.jms.atomikos.example2.impl;

import javaee.jta.jms.atomikos.example2.Account;
import javaee.jta.jms.atomikos.example2.Client;

public class BOABank extends RetailBank {
	public BOABank() {
		Client john = new BasicClient(1);
		Account currentAccount = new CurrentAccount(1);
		AccountRepository ar = new AccountRepository();
		ar.setCurrentAccount(currentAccount);
		
		accounts.put(john.getId(), ar);
		super.setBankURL("tcp://localhost:61616");
	}
}
