package javaee.jta.jms.atkmikos.example2;

import javaee.jta.jms.atomikos.example2.Account;
import javaee.jta.jms.atomikos.example2.Client;
import javaee.jta.jms.atomikos.example2.Transaction;
import javaee.jta.jms.atomikos.example2.impl.AccountRepository;
import javaee.jta.jms.atomikos.example2.impl.BOABank;
import javaee.jta.jms.atomikos.example2.impl.BasicClient;
import javaee.jta.jms.atomikos.example2.impl.HSBCBank;
import javaee.jta.jms.atomikos.example2.impl.RemoteTransaction;
import javaee.jta.jms.atomikos.example2.impl.RetailBank;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppIT {		
	@Test
    public void test1()  {
		Client john = new BasicClient(1);
		
		RetailBank hsbc = new HSBCBank();
		RetailBank boa = new BOABank();
		
		AccountRepository hsbcAr = hsbc.getAccounts(john);
		AccountRepository boaAr = boa.getAccounts(john);
		
		Transaction t = new RemoteTransaction(boa);
		
		Account savingAccountFrom = hsbcAr.getSavingAccount();
		Account currentAccountTo = boaAr.getCurrentAccount();
		
		t.setWithdrawal(savingAccountFrom);
		t.setDeposit(currentAccountTo);
		
		try {
			int savingBalance = savingAccountFrom.getBalance();
			int currentBalance = currentAccountTo.getBalance();
			
			t.transferWithError(500);
			System.out.println("saving: " + savingAccountFrom.getBalance());
			System.out.println("current: " + currentAccountTo.getBalance());
			assertEquals(currentBalance-savingBalance, currentAccountTo.getBalance()-savingAccountFrom.getBalance());
			
			t.transfer(500);
			System.out.println("saving: " + savingAccountFrom.getBalance());
			System.out.println("current: " + currentAccountTo.getBalance());
			assertEquals(savingBalance-500, savingAccountFrom.getBalance());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
}
