package edu.buffalo.www.cse4562;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CrossProductOP extends Operator{
	
	private Operator rhS;
	private Operator lhS;
	
	public CrossProductOP(Operator leftson, Operator rightSon) {
		this.rhS = rightSon;
		this.lhS = leftson;
	}

	@Override
	public Tuple result() {
		FutureTask[] tasks = new FutureTask[2];
		Callable callableL = new ProductThread(lhS);
		Callable callableR = new ProductThread(rhS);
		tasks[0] = new FutureTask(callableL);
		tasks[1] = new FutureTask(callableR);
		try {
			tasks[0].get();
			tasks[1].get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}


}
