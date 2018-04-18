package edu.buffalo.www.cse4562;

import java.util.ArrayList;
import java.util.List;

public class CrossProductOP extends Operator {

    private Operator rhS;
    private Operator lhS;
    private Tuple lhTuple;
    private Tuple rhTuple;
    private boolean started;
    private List<String> relatedTables;

    public CrossProductOP(Operator leftSon, Operator rightSon) {
        this.rhS = rightSon;
        this.lhS = leftSon;
        this.started = false;
        this.relatedTables = new ArrayList<>();
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
        if (!started) {
            //the first step
            this.rhTuple = rhS.result();
            this.lhTuple = lhS.result();
            if (this.rhTuple == null || this.lhTuple == null) {
                return null;
            } else {
                this.started = true;
                this.rhTuple.addTable(this.lhTuple);
                return this.rhTuple;
            }
        } else {
            this.rhTuple = rhS.result();
            if (rhTuple == null) {
                //one loop over
                this.lhTuple = lhS.result();
                if (this.lhTuple == null) {
                    this.started = false;
                    return null;//whole loop end
                }
                this.rhTuple = rhS.result();
                this.rhTuple.addTable(this.lhTuple);
                return this.rhTuple;
            } else {
                this.rhTuple.addTable(this.lhTuple);
                return this.rhTuple;
            }
        }
    }

    public List<String> relatedGetter() {
        return this.relatedTables;
    }

    public void relatedSetter(List<String> table) {
        this.relatedTables = table;
    }

    public void relatedT() {
        if (this.lhS instanceof Read) {
            Read r = (Read) this.lhS;
            this.relatedTables.add(r.getTableName());
        }
        if (this.rhS instanceof Read) {
            Read r = (Read) this.rhS;
            this.relatedTables.add(r.getTableName());
        }
        if (this.lhS instanceof CrossProductOP) {
            CrossProductOP c = (CrossProductOP) this.lhS;
            c.relatedT();
            this.relatedTables.addAll(c.relatedGetter());
        }

        if (this.rhS instanceof CrossProductOP) {
            CrossProductOP c = (CrossProductOP) this.rhS;
            c.relatedT();
            this.relatedTables.addAll(c.relatedGetter());
        }

        if (this.rhS instanceof RenameOperator) {
            RenameOperator rename = (RenameOperator) this.rhS;
            this.relatedTables.add(rename.nameGetter());
        }

        if (this.lhS instanceof RenameOperator) {
            RenameOperator rename = (RenameOperator) this.lhS;
            this.relatedTables.add(rename.nameGetter());
        }
    }

    @Override
    public Operator getSon() {
        return this.lhS;
    }

    @Override
    public void setSon(Operator son) {
        this.lhS = son;

    }

    public Operator getRhson() {
        return this.rhS;
    }

    public void setRhS(Operator son) {
        this.rhS = son;
    }

}