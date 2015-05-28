package javaee.jta.jms.atomikos.example2.impl;

import javaee.jta.jms.atomikos.example2.Account;
import javaee.jta.jms.atomikos.example2.Client;

public class HSBCBank extends RetailBank {
	public HSBCBank() {
		Client john = new BasicClient(1);
		Account savingAccount = new SavingAccount(1);
		AccountRepository ar = new AccountRepository();
		ar.setSavingAccount(savingAccount);
		
		accounts.put(john.getId(), ar);
	}
}
