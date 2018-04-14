package edu.buffalo.www.cse4562;

public abstract class Operator {
	public abstract Tuple result();
	public abstract Operator getSon();
	public abstract void setSon(Operator son);
	public Operator() {
		
	}
}
