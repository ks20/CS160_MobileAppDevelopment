package com.example.kushal.represent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabFragment2 extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> districtIds = new ArrayList<>();
    private ArrayList<Bitmap> imgs = new ArrayList<>();
    private ArrayList<String> emails = new ArrayList<>();
    private ArrayList<String> parties = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private View view;
    private boolean isCurrLoc, isZipLoc, isRandLoc;
    private static final String API_KEY = "615bc6533dc9ca13f3dfbbda9690539fad00545";
    private double latitude, longitude;
    private int zip;
    private String picUrlStr, name;
    private HashMap<String, String> repNameAndIds = new HashMap<>();
    private HashMap<String, ArrayList<Object>> repImages = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        Bundle args = getArguments();
        isCurrLoc = (boolean) args.get("currLocFlag");
        isZipLoc = (boolean) args.get("zipLocFlag");
        isRandLoc = (boolean) args.get("randLocFlag");

        if (isCurrLoc) {
            latitude = (double) args.get("lat");
            longitude = (double) args.get("long");
        }
        else if (isZipLoc) {
            zip = args.getInt("zip");
        }
        else if (isRandLoc) {
            latitude = (double) args.get("lat");
            longitude = (double) args.get("long");
        }

        startRepAsync();
        return view;
    }

    public void startRepAsync() {
        new TabFragment2.StartAsyncTask().execute();
    }

    private class StartAsyncTask extends AsyncTask<Void, Void, HashMap<String, ArrayList<Object>>> {
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

                    //String state = (String)json.getJSONArray("results").getJSONObject(0).getJSONObject("address_components").get("state");
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
                            ArrayList<Object> repInfo = new ArrayList<>();

                            JSONObject obj2 = congDist.getJSONObject(j);
                            String type = obj2.getString("type");

                            JSONObject bio = obj2.getJSONObject("bio");
                            String firstName = bio.getString("first_name");
                            String lastName = bio.getString("last_name");
                            String fullName = firstName + " " + lastName;
                            String party = bio.getString("party");
                            repInfo.add(party);

                            JSONObject references = obj2.getJSONObject("references");
                            String memId = references.getString("bioguide_id");

                            JSONObject contact = obj2.getJSONObject("contact");
                            String memURL = contact.getString("url");
                            String email = contact.getString("contact_form");
                            if (email == null) {
                                email = "No contact info. available";
                            }
                            char[] emailCharSeq = email.toCharArray();

                            if (type.equals("representative")) {
                                repNameAndIds.put(fullName, memId);
                                repInfo.add(memURL);
                                repInfo.add(emailCharSeq);
                                repImages.put(fullName, repInfo);
                            }
                        }
                    }

                    for (Map.Entry<String, String> entry : repNameAndIds.entrySet()) {
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
                        ArrayList<Object> temp = repImages.get(name);
                        temp.add(myBitmap);
                        repImages.put(name, temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    request.disconnect();
                }


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return repImages;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList<Object>> result) {
            initImageBitmaps();
        }
    }

    private void initImageBitmaps(){
        while (!repImages.isEmpty()) {
            Map.Entry<String, ArrayList<Object>> entry1 = repImages.entrySet().iterator().next();
            String key = entry1.getKey();
            ArrayList<Object> value = entry1.getValue();

            mNames.add(key);

            for (Object x : value) {
                if (x instanceof Bitmap) {
                    imgs.add((Bitmap)x);
                }
                else if (x instanceof char[]) {
                    String str = new String((char[])x);
                    emails.add(str);
                }
                else if (x instanceof String) {
                    if (x.equals("Democrat") || x.equals("Republican")) {
                        parties.add((String)x);
                    }
                    else {
                        urls.add((String)x);
                    }
                }
            }
            repImages.remove(key);
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.rvReps);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, imgs, emails, parties, urls, repNameAndIds);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}