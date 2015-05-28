package javaee.jta.jms.atomikos.example2.impl;

import javaee.jta.jms.atomikos.example2.Client;

public class BasicClient implements Client {
	private int id = 0;

	public BasicClient(int id) {
		this.id = id;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;		
	}

}
