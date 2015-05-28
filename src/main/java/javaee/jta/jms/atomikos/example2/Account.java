package javaee.jta.jms.atomikos.example2;

public interface Account {
	public void withdraw(int amount) throws Exception;
	public void deposit(int amount) throws Exception;
	public int getBalance() throws Exception;
}
