package com.example.kushal.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoreDetailsActivity extends AppCompatActivity {
    private static final String PROPUBLICA_API_KEY = "dN9snAtTr8lJGhaLe6ztke6BOvW8YQsQlqPLA6i3";
    private Serializable resultTemp;
    private HashMap<String, ArrayList<Object>> result;
    private HashMap<String, String> senIds;
    private ImageView pImage;
    private TextView pName, pParty, pEmail, pWeb, pComm1, pComm2, pComm3, pBill1, pBill2;
    private String sId;
    private ArrayList<String> committees = new ArrayList<>();
    private ArrayList<String> bills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        pImage = (ImageView) findViewById(R.id.personImage);
        pName = (TextView) findViewById(R.id.personName) ;
        pParty = (TextView) findViewById(R.id.personParty);
        pEmail = (TextView) findViewById(R.id.personEmail);
        pWeb = (TextView) findViewById(R.id.personWeb);
        pComm1 = (TextView) findViewById(R.id.comm1);
        pComm2 = (TextView) findViewById(R.id.comm2);
        pComm3 = (TextView) findViewById(R.id.comm3);
        pBill1 = (TextView) findViewById(R.id.bill1);
        pBill2 = (TextView) findViewById(R.id.bill2);

        Intent getIntent = getIntent();
        Bundle getBund = getIntent.getExtras();
        result = (HashMap<String, ArrayList<Object>>)getBund.getSerializable("data");
        senIds = (HashMap<String, String>)getBund.get("ids");

        Map.Entry<String, ArrayList<Object>> entry1 = result.entrySet().iterator().next();
        String key = entry1.getKey();
        ArrayList<Object> value = entry1.getValue();
        pName.setText(key);
        sId = senIds.get(key);

        for (Object x : value) {
            if (x instanceof Bitmap) {
                pImage.setImageBitmap((Bitmap)x);
            }
            else if (x instanceof char[]) {
                String str = new String((char[])x);
                String webCodify = " <a href=" + str + " >Email</a>";
                pEmail.setMovementMethod(LinkMovementMethod.getInstance());
                pEmail.setText(Html.fromHtml(webCodify, Html.FROM_HTML_MODE_LEGACY));
            }
            else if (x instanceof String) {
                if (x.equals("Democrat") || x.equals("Republican")) {
                    pParty.setText((String)x);
                    if (x.equals("Republican")) {
                        pParty.setTextColor(Color.parseColor("#B6151B"));
                    }
                    else if (x.equals("Democrat")) {
                        pParty.setTextColor(Color.parseColor("#0169E3"));
                    }
                }
                else {
                    String codify = " <a href=" + (String)x + " >Website</a>";
                    pWeb.setMovementMethod(LinkMovementMethod.getInstance());
                    pWeb.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));
                }
            }
        }

        if (pParty.getText().toString().equals("Democrat")) {
            pEmail.setLinkTextColor(Color.parseColor("#0169E3"));
            pWeb.setLinkTextColor(Color.parseColor("#0169E3"));
        }
        else {
            pEmail.setLinkTextColor(Color.parseColor("#B6151B"));
            pWeb.setLinkTextColor(Color.parseColor("#B6151B"));
        }

        startDetailsAsync();
        startBillsAsync();
    }

    public void startDetailsAsync() {
        new MoreDetailsActivity.StartDetailsAsyncTask().execute();
    }
    public void startBillsAsync() {new MoreDetailsActivity.StartBillsAsyncTask().execute(); }



    private class StartDetailsAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                URL reqUrl = null;
                reqUrl = new URL("https://api.propublica.org/congress/v1/members/" + sId + ".json");

                HttpURLConnection request = (HttpURLConnection) reqUrl.openConnection();
                request.setRequestMethod("GET");
                request.setRequestProperty("X-Api-Key", PROPUBLICA_API_KEY);
                request.connect();

                try {
                    InputStream in = new BufferedInputStream(request.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }

                    JSONObject json = new JSONObject(out.toString());
                    JSONArray committeesArray = json.getJSONArray("results").getJSONObject(0).getJSONArray("roles").getJSONObject(0).getJSONArray("committees");

                    for (int i = 0; i < committeesArray.length(); i++) {
                        JSONObject obj = committeesArray.getJSONObject(i);
                        String committeeName = obj.getString("name");
                        committees.add(committeeName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    request.disconnect();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return committees;
        }


        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            String x = result.remove(0);
            pComm1.setText("1. " + x);

            if (!result.isEmpty()) {
                String y = result.remove(0);
                pComm2.setText("2. " + y);
            }

            if (!result.isEmpty()) {
                String z = result.remove(0);
                pComm3.setText("3. " + z);
            }
        }
    }


    private class StartBillsAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                URL billsUrl = null;
                billsUrl = new URL("https://api.propublica.org/congress/v1/members/" + sId + "/bills/introduced.json");
                HttpURLConnection bRequest = (HttpURLConnection) billsUrl.openConnection();
                bRequest.setRequestMethod("GET");
                bRequest.setRequestProperty("X-Api-Key", PROPUBLICA_API_KEY);
                bRequest.connect();

                try {
                    InputStream in = new BufferedInputStream(bRequest.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }

                    JSONObject json = new JSONObject(out.toString());
                    JSONArray billsArray = json.getJSONArray("results").getJSONObject(0).getJSONArray("bills");

                    for (int i = 0; i < billsArray.length(); i++) {
                        JSONObject obj = billsArray.getJSONObject(i);
                        String committeeName = obj.getString("title");
                        bills.add(committeeName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    bRequest.disconnect();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return bills;
        }


        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            String x = result.remove(0);
            pBill1.setText("1. " + x);

            if (!result.isEmpty()) {
                String y = result.remove(0);
                pBill2.setText("2. " + y);
            }
        }
    }
}
