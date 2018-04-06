package edu.buffalo.www.cse4562;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CrossProductOP extends Operator{
	
	private Operator rhS;
	private Operator lhS;
	private Tuple lhTuple;
	private Tuple rhTuple;
	private boolean started;
	private boolean onlylh;
	private boolean onlyrh;
	
	public CrossProductOP(Operator leftson, Operator rightSon) {
		this.rhS = rightSon;
		this.lhS = leftson;
		this.started = false;
		this.onlylh = false;
		this.onlyrh = false;
	}

	@Override
	public Tuple result() {
//		FutureTask[] tasks = new FutureTask[2];
//		Callable callableL = new ProductThread(lhS);
//		Callable callableR = new ProductThread(rhS);
//		tasks[0] = new FutureTask(callableL);
//		tasks[1] = new FutureTask(callableR);
//		try {
//			tasks[0].get();
//			tasks[1].get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		return null;
		if(!started) {
			//the first step
			this.rhTuple = rhS.result();
			this.lhTuple = lhS.result();
			if(this.rhTuple == null && this.lhTuple == null) {
				return null;
			}
			else if(this.rhTuple == null && this.lhTuple != null) {
				//only lhTuple
				this.onlylh = true;
				this.started = true;
				return this.lhTuple;
			}
			else if(this.rhTuple != null && this.lhTuple == null) {
				//only rhTuple
				this.onlyrh = true;
				this.started = true;
				return this.rhTuple;
			}
			else {
				this.started = true;
				this.lhTuple.combineTuples(this.rhTuple);
				return this.lhTuple;
			}
		}
		else {
			if(onlylh && !onlyrh) {
				return lhS.result();
			}
			else if(!onlylh && onlyrh) {
				return rhS.result();
			}
			else {
				this.rhTuple = rhS.result();
				if(rhTuple == null) {
					//one loop over
					this.lhTuple = rhS.result();
					if(this.lhTuple == null) {
						this.started = false;
						return null;//whole loop end
					}
					this.rhTuple = rhS.result();
					this.lhTuple.combineTuples(this.rhTuple);
					return this.lhTuple;
				}
				else {
					this.lhTuple.combineTuples(this.rhTuple);
					return this.lhTuple;
				}
			}
		}
	}


}
