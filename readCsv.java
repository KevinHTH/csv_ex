package com.example.xunistockcsv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class ReadCSV {
	InputStream in;
	
	public ReadCSV(InputStream in){
		this.in = in;
	}
	public List<String[]> read(){
		List<String[]> results = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			String line;
			while((line = reader.readLine()) != null){
				String[] row = line.split(",");
				results.add(row);
			}
		}
		catch (IOException e){
			throw new RuntimeException("Error reading CSV File " + e);
		}
		finally{
			try {
				in.close();
			}
			catch (IOException e){
				throw new RuntimeException("Error reading CSV File " + e);
			}
		}
	
	return results;
	}
}