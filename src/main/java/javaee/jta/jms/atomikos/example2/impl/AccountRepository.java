package javaee.jta.jms.atomikos.example2.impl;

import javaee.jta.jms.atomikos.example2.Account;

public class AccountRepository {
	private Account savingAccount = null;
	private Account currentAccount = null;

	public Account getSavingAccount() {
		return savingAccount;
	}
	
	public void setSavingAccount(Account savingAccount) {
		this.savingAccount = savingAccount;
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}
}
