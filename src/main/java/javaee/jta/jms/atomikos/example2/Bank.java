package javaee.jta.jms.atomikos.example2;

import javaee.jta.jms.atomikos.example2.impl.AccountRepository;

public interface Bank {
	public AccountRepository getAccounts(Client client);
}
