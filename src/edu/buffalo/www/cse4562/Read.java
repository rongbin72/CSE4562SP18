package edu.buffalo.www.cse4562;
import java.io.*;
import java.util.List;
import net.sf.jsqlparser.expression.*;;

public class Read {
	private BufferedReader br;
	private boolean isFile;
	private List<List<PrimitiveValue>> table;
	private String tableName;
	private int itor;
	private int length;

	public Read(File f, String tablename) throws FileNotFoundException {
		FileReader fr = new FileReader(f);
		this.br = new BufferedReader(fr);
		this.isFile = true;
		this.tableName = tablename;
	}
	
	public Read(String tablename) {
		this.isFile = false;
		this.table = Schema.getTableContent(tablename);
		this.tableName = tablename;
		this.itor = 0;
		this.length = table.size();
	}

	public List<PrimitiveValue> ReadLine() throws IOException {
		List<PrimitiveValue> result;
		if(isFile) {
			String line = this.br.readLine();
			if(line == null) {
				return null;
			}
			return Helper.toPrimitive(this.tableName, line);
		}
		else {
			if(this.itor < this.length) {
				result = this.table.get(this.itor);
				this.itor ++;
				return result;
			}
			else {
				return null;
			}
			
		}
	}
}
