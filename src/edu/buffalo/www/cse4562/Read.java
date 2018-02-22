package edu.buffalo.www.cse4562;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Read {
	private File f;
	private DataInputStream dis;

	public Read(File f) throws FileNotFoundException {
		this.f = f;
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);
	}
	

	public String ReadLine() throws IOException {
		return dis.readLine();
	}
}
