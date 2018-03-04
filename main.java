package com.example.xunistockcsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.grapecity.xuni.core.*;
import com.grapecity.xuni.flexgrid.*;

public class MainActivity extends Activity {
	private FlexGrid grid;
	private Button saveButton;
	private ObservableList<StockData> stockList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		com.grapecity.xuni.core.LicenseManager.KEY = License.KEY;
		setContentView(R.layout.activity_main);
        grid = (FlexGrid)findViewById(R.id.flexgrid);
        saveButton = (Button)findViewById(R.id.button1);
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
		final StockData stock = new StockData();
        File file = new File(path, "stock.csv");
		if(file.exists()){
    		InputStream input;
			try {
				input = new FileInputStream(file);
	    		ReadCSV csv = new ReadCSV(input);
	    		stockList = stock.getObservableList(csv.read());
	    		grid.setItemsSource(stockList);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("File not found" + e);
			}
		}
		else{
			InputStream in = getResources().openRawResource(R.raw.stock);
			ReadCSV csv = new ReadCSV(in);
			stockList = stock.getObservableList(csv.read());
			grid.setItemsSource(stockList);
		}
		grid.setAutoGenerateColumns(true); 
	    grid.setReadOnly(false);
		GridColumn dateCol = grid.getColumns().getColumn("date");
        dateCol.setFormat("M-dd-yyyy");
		grid.autoSizeColumns(0, grid.getColumns().size() - 1);
	    saveButton.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v){	
	    		path.mkdirs();
	    		File file = new File(path, "stock.csv");
	    		SaveCSV sCSV = new SaveCSV(file);
	    		sCSV.save(stock.getStringList(stockList));
	    	}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}