package com.example.kushal.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Kushal on 9/27/18.
 */

public class TabFragment1 extends Fragment {
    private TextView senator1Name, senator1Party, senator2Party, senator2Name, senator1URL, senator2URL, senator1Email, senator2Email, latView, longView;
    private static final String API_KEY = "615bc6533dc9ca13f3dfbbda9690539fad00545";
    private int zip;
    private HashSet<String> memberIds = new HashSet<>();
    private HashMap<String, String> senNameAndIds = new HashMap<>();
    private ArrayList<Integer> districtIds = new ArrayList<>();
    private HashMap<String, ArrayList<Object>> senatorImages = new HashMap<>();

    private View view;
    private String picUrlStr, name;
    private ImageView senator1Image, senator2Image;
    private Button s1Details, s2Details;
    private boolean isCurrLoc, isZipLoc, isRandLoc;
    private double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        senator1Name = view.findViewById(R.id.senator1);
        senator1Image = view.findViewById(R.id.senator1Image);

        senator1Party = (TextView) view.findViewById(R.id.senator1Party);
        senator2Party = (TextView) view.findViewById(R.id.senator2Party);

        latView = view.findViewById(R.id.textView2);
        longView = view.findViewById(R.id.textView3);

        senator2Name = view.findViewById(R.id.senator2);
        senator2Image = (ImageView) view.findViewById(R.id.senator2Image);

        senator1URL = view.findViewById(R.id.senator1Web);
        senator2URL = view.findViewById(R.id.senator2Web);

        senator1Email = view.findViewById(R.id.senator1Email);
        senator2Email = view.findViewById(R.id.senator2Email);

        s1Details = view.findViewById(R.id.moreDetails);
        s2Details = view.findViewById(R.id.moreDetails2);

        Bundle args = getArguments();
        isCurrLoc = (boolean) args.get("currLocFlag");
        isZipLoc = (boolean) args.get("zipLocFlag");
        isRandLoc = (boolean) args.get("randLocFlag");

        if (isCurrLoc) {
            latitude = (double) args.get("lat");
            longitude = (double) args.get("long");
            latView.setText("Latitude: " + latitude);
            longView.setText("Longitude: " + longitude);
        }
        else if (isZipLoc) {
            zip = args.getInt("zip");
        }
        else if (isRandLoc) {
            latitude = (double) args.get("lat");
            longitude = (double) args.get("long");
            latView.setText("Latitude: " + latitude);
            longView.setText("Longitude: " + longitude);
        }

        startASync();
        return view;
    }

    public void startASync() {
        new StartAsyncTask(getContext()).execute();
    }

    private class StartAsyncTask extends AsyncTask<Void, Void, HashMap<String, ArrayList<Object>>> {
        Context context;

        private StartAsyncTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected HashMap<String, ArrayList<Object>> doInBackground(Void... params) {
            try {
                URL reqUrl = null;
                if (isCurrLoc || isRandLoc) {
                    reqUrl = new URL("https://api.geocod.io/v1.3/reverse?q=" + latitude + "," + longitude + "&fields=cd,stateleg&api_key=" + API_KEY);
                }
                else if (isZipLoc) {
                    reqUrl = new URL("https://api.geocod.io/v1.3/geocode?q=" + zip + "&fields=cd,stateleg&api_key=" + API_KEY);
                }

                HttpURLConnection request = (HttpURLConnection) reqUrl.openConnection();
                request.setRequestMethod("GET");

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

                    String state = (String)json.getJSONArray("results").getJSONObject(0).getJSONObject("address_components").get("state");
                    JSONArray distArray = json.getJSONArray("results").getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts");

                    for (int i = 0; i < distArray.length(); i++) {
                        JSONObject obj = distArray.getJSONObject(i);
                        int districtNumber = obj.getInt("district_number");
                        districtIds.add(districtNumber);
                    }

                    for (int i = 0; i < distArray.length(); i++) {
                        JSONObject obj = distArray.getJSONObject(i);
                        JSONArray congDist = obj.getJSONArray("current_legislators");

                        for (int j = 0; j < congDist.length(); j++) {
                            ArrayList<Object> senatorInfo = new ArrayList<>();

                            JSONObject obj2 = congDist.getJSONObject(j);
                            String type = obj2.getString("type");

                            JSONObject bio = obj2.getJSONObject("bio");
                            String firstName = bio.getString("first_name");
                            String lastName = bio.getString("last_name");
                            String fullName = firstName + " " + lastName;
                            String party = bio.getString("party");
                            senatorInfo.add(party);

                            JSONObject references = obj2.getJSONObject("references");
                            String memId = references.getString("bioguide_id");

                            JSONObject contact = obj2.getJSONObject("contact");
                            String memURL = contact.getString("url");
                            String email = contact.getString("contact_form");
                            if (email == null) {
                                email = "No contact info.";
                            }
                            char[] emailCharSeq = email.toCharArray();

                            if (type.equals("senator")) {
                                senNameAndIds.put(fullName, memId);
                                senatorInfo.add(memURL);
                                senatorInfo.add(emailCharSeq);
                                senatorImages.put(fullName, senatorInfo);
                            }
                        }
                    }

                    for (Map.Entry<String, String> entry : senNameAndIds.entrySet()) {
                        name = entry.getKey();
                        String mId = entry.getValue();
                        char firstLetter = mId.charAt(0);
                        picUrlStr = "http://bioguide.congress.gov/bioguide/photo/" + firstLetter + "/" + mId + ".jpg";
                        URL url = new URL(picUrlStr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        ArrayList<Object> temp = senatorImages.get(name);
                        temp.add(myBitmap);
                        senatorImages.put(name, temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    request.disconnect();
                }


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return senatorImages;
        }

        @Override
        protected void onPostExecute(final HashMap<String, ArrayList<Object>> result) {
            final Map.Entry<String, ArrayList<Object>> entry1 = result.entrySet().iterator().next();
            final HashMap<String, ArrayList<Object>> ttt = new HashMap<String, ArrayList<Object>>();
            final HashMap<String, ArrayList<Object>> ppp = new HashMap<String, ArrayList<Object>>();

            String key = entry1.getKey();
            //String memberId = senNameAndIds.get(key);
            ArrayList<Object> value = entry1.getValue();
            ttt.put(key, value);

            for (Object x : value) {
                if (x instanceof Bitmap) {
                    senator1Image.setImageBitmap((Bitmap)x);
                }
                else if (x instanceof char[]) {
                    String str = new String((char[])x);
                    String codify = " <a href=" + str + " >Email</a>";
                    senator1Email.setMovementMethod(LinkMovementMethod.getInstance());
                    senator1Email.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));
                }
                else if (x instanceof String) {
                    if (x.equals("Democrat") || x.equals("Republican")) {
                        senator1Party.setText((String)x);
                        if (x.equals("Republican")) {
                            senator1Party.setTextColor(Color.parseColor("#B6151B"));
                        }
                        else if (x.equals("Democrat")) {
                            senator1Party.setTextColor(Color.parseColor("#0169E3"));
                        }
                    }
                    else {
                        String codify = " <a href=" + (String)x + " >Website</a>";
                        senator1URL.setMovementMethod(LinkMovementMethod.getInstance());
                        senator1URL.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));
                    }
                }
            }

            if (senator1Party.getText().equals("Democrat")) {
                senator1Email.setLinkTextColor(Color.parseColor("#0169E3"));
                senator1URL.setLinkTextColor(Color.parseColor("#0169E3"));
            }
            else {
                senator1Email.setLinkTextColor(Color.parseColor("#B6151B"));
                senator1URL.setLinkTextColor(Color.parseColor("#B6151B"));
            }
            senator1Name.setText("Senator " + key);

            result.remove(key);

            Map.Entry<String, ArrayList<Object>> entry2 = result.entrySet().iterator().next();

            if (entry2 != null) {
                String k = entry2.getKey();
                ArrayList<Object> v = entry2.getValue();
                ppp.put(k, v);

                for (Object x : v) {
                    if (x instanceof Bitmap) {
                        senator2Image.setImageBitmap((Bitmap)x);
                    }
                    else if (x instanceof char[]) {
                        String str = new String((char[])x);
                        String codify = " <a href=" + str + " >Email</a>";
                        senator2Email.setMovementMethod(LinkMovementMethod.getInstance());
                        senator2Email.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));
                    }
                    else if (x instanceof String) {
                        if (x.equals("Democrat") || x.equals("Republican")) {
                            senator2Party.setText((String)x);
                            if (x.equals("Republican")) {
                                senator2Party.setTextColor(Color.parseColor("#B6151B"));
                            }
                            else if (x.equals("Democrat")) {
                                senator2Party.setTextColor(Color.parseColor("#0169E3"));
                            }
                        }
                        else {
                            String codify = " <a href=" + (String)x + " >Website</a>";
                            senator2URL.setMovementMethod(LinkMovementMethod.getInstance());
                            senator2URL.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));
                        }
                    }
                }

                if (senator2Party.getText().equals("Democrat")) {
                    senator2Email.setLinkTextColor(Color.parseColor("#0169E3"));
                    senator2URL.setLinkTextColor(Color.parseColor("#0169E3"));
                }
                else {
                    senator2Email.setLinkTextColor(Color.parseColor("#B6151B"));
                    senator2URL.setLinkTextColor(Color.parseColor("#B6151B"));
                }


                senator2Name.setText("Senator " + k);
            }

            s1Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MoreDetailsActivity.class);
                    intent.putExtra("data", (Serializable)ttt);
                    intent.putExtra("ids", (Serializable)senNameAndIds);
                    startActivity(intent);
                }
            });

            s2Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MoreDetailsActivity.class);
                    intent.putExtra("data", (Serializable)ppp);
                    intent.putExtra("ids", (Serializable)senNameAndIds);
                    startActivity(intent);
                }
            });

        }
    }
}

