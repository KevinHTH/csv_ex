package uel.vteam.belovedhostel.adapter;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uel.vteam.belovedhostel.model.CountryPhoneCode;

/**
 * Created by Hieu on 11/18/2016.
 */

public class LoadPhoneCodeTask extends AsyncTask<String, Void, ArrayList<CountryPhoneCode>> {

    Activity context;
    ArrayList<CountryPhoneCode> arrPhoneCode;
    PhoneCodeAdapter adapter;

    public LoadPhoneCodeTask(Activity context, ArrayList<CountryPhoneCode> arrPhoneCode, PhoneCodeAdapter adapter) {
        this.context = context;
        this.arrPhoneCode = arrPhoneCode;
        this.adapter = adapter;
    }

/*
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dsPhoneCode.clear();
    }
*/

    @Override
    protected ArrayList<CountryPhoneCode> doInBackground(String... strings) {
        String link=strings[0];
        ArrayList<CountryPhoneCode> arrPhoneCode=new ArrayList<>();
        try {
            URL url=new URL(link);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            StringBuilder builder=new StringBuilder();
            String line=bufferedReader.readLine();
            while (line!=null){
                builder.append(line);
                line=bufferedReader.readLine();
            }
            JSONArray jsonArray=new JSONArray(builder.toString());
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CountryPhoneCode pCode = new CountryPhoneCode();
                    if (jsonObject.has("name")) {
                        pCode.setName(jsonObject.getString("name"));
                    }
                    if (jsonObject.has("dial_code")) {
                        pCode.setDial_code(jsonObject.getString("dial_code"));
                    }
                    if (jsonObject.has("code")) {
                        pCode.setCode(jsonObject.getString("code"));
                    }
                    arrPhoneCode.add(pCode);

                    CountryPhoneCode vn = new CountryPhoneCode("Viet Nam", "+84", "VN");
                    arrPhoneCode.set(0, vn);
                }

        }catch (Exception ex){
            fakeData();
            ex.printStackTrace();
        }

        return arrPhoneCode;
    }
    public void fakeData(){
        CountryPhoneCode vn=new CountryPhoneCode("Viet Nam","+84","VN");
        CountryPhoneCode lao=new CountryPhoneCode("Lao","+856","LA");
        CountryPhoneCode cpc=new CountryPhoneCode("Cambodia","+855","KH");

        arrPhoneCode.add(vn);
        arrPhoneCode.add(lao);
        arrPhoneCode.add(cpc);
    }
    @Override
    protected void onPostExecute(ArrayList<CountryPhoneCode> arr) {
        super.onPostExecute(arr);
        adapter.addAll(arr);
        adapter.notifyDataSetChanged();
    }
}
