package vn.com.barcode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jacky Hua on 27/02/2018.
 */

public class WriteCsvFileAcitivy extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyDir");
        if (!fileDir.exists()) {
            try {
                fileDir.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BlogData" + File.separator + "MyText.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeCSV(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void writeCSV(File file) {
        if(file.exists()){
            try {
                FileWriter fileWriter  = new FileWriter(file);
                BufferedWriter bfWriter = new BufferedWriter(fileWriter);
                bfWriter.write("Text Data");
                bfWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
