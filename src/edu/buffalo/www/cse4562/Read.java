package edu.buffalo.www.cse4562;
import java.io.*;

public class Read {
	private File f;
	private BufferedReader br;

	public Read(File f) throws FileNotFoundException {
		this.f = f;
		FileReader fr = new FileReader(f);
		this.br = new BufferedReader(fr);
	}
	

	public String ReadLine() throws IOException {
		return this.br.readLine();
	}
}
