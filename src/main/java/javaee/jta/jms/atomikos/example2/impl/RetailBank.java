package javaee.jta.jms.atomikos.example2.impl;

import java.util.HashMap;
import java.util.Map;

import javaee.jta.jms.atomikos.example2.Bank;
import javaee.jta.jms.atomikos.example2.Client;

public abstract class RetailBank implements Bank {
	protected Map<Integer, AccountRepository> accounts = new HashMap<Integer, AccountRepository>();
	protected String bankURL = null;

	@Override
	public AccountRepository getAccounts(Client client) {
		return accounts.get(client.getId());
	}
	
	public String getBankURL() {
		return bankURL;
	}

	public void setBankURL(String bankURL) {
		this.bankURL = bankURL;
	}
}
