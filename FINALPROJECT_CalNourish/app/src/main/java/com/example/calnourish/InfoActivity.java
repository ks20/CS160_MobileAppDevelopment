package com.example.calnourish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class InfoActivity extends AppCompatActivity {

    private FirebaseDatabase FDB;
    private DatabaseReference DBRef;
    private HashMap<String, Object> infoMap = new HashMap<>();
    private HashMap<String, TextView> textViews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_info);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        final Button updateButton = (Button) findViewById(R.id.updateButton);
        FDB = FirebaseDatabase.getInstance();
        DBRef = FDB.getReference().child("info");

        textViews.put("sunday", (TextView) findViewById(R.id.day0_hours));
        textViews.put("monday", (TextView) findViewById(R.id.day1_hours));
        textViews.put("tuesday", (TextView) findViewById(R.id.day2_hours));
        textViews.put("wednesday", (TextView) findViewById(R.id.day3_hours));
        textViews.put("thursday", (TextView) findViewById(R.id.day4_hours));
        textViews.put("friday", (TextView) findViewById(R.id.day5_hours));
        textViews.put("saturday", (TextView) findViewById(R.id.day6_hours));
        textViews.put("email", (TextView) findViewById(R.id.emailText));
        textViews.put("url", (TextView) findViewById(R.id.urlText));
        textViews.put("location", (TextView) findViewById(R.id.locationText));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        infoMap = (HashMap)dataSnapshot.getValue();

                        textViews.get("sunday").setText((String) ((Map) infoMap.get("-sunday")).get("hours"));
                        textViews.get("monday").setText((String) ((Map) infoMap.get("-monday")).get("hours"));
                        textViews.get("tuesday").setText((String) ((Map) infoMap.get("-tuesday")).get("hours"));
                        textViews.get("wednesday").setText((String) ((Map) infoMap.get("-wednesday")).get("hours"));
                        textViews.get("thursday").setText((String) ((Map) infoMap.get("-thursday")).get("hours"));
                        textViews.get("friday").setText((String) ((Map) infoMap.get("-friday")).get("hours"));
                        textViews.get("saturday").setText((String) ((Map) infoMap.get("-saturday")).get("hours"));
                        textViews.get("email").setText((String) ((Map) infoMap.get("-contact")).get("email"));
                        String url = (String) ((Map) infoMap.get("-contact")).get("url");
                        String text = "<a href='" + url + "'>" + url + "</a>";
                        textViews.get("url").setClickable(true);
                        textViews.get("url").setMovementMethod(LinkMovementMethod.getInstance());
                        textViews.get("url").setText(Html.fromHtml(text));
                        textViews.get("location").setText((String) ((Map) infoMap.get("-location")).get("location"));

                        // TODO: should refactor the switch
                        Calendar calendar = Calendar.getInstance();
                        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                        String now = LocalDateTime.now().format(timeFormat);
                        LocalTime currentTime = LocalTime.parse(now, timeFormat);
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        TextView openOrClose = findViewById(R.id.pantryTime);

                        LocalTime open;
                        LocalTime close;
                        String pantryTime;

                        switch (day) {
                            case Calendar.SUNDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-sunday")).get("24hours"));
                                break;
                            case Calendar.MONDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-monday")).get("24hours"));
                                break;
                            case Calendar.TUESDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-tuesday")).get("24hours"));
                                break;
                            case Calendar.WEDNESDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-wednesday")).get("24hours"));
                                break;
                            case Calendar.THURSDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-thursday")).get("24hours"));
                                break;
                            case Calendar.FRIDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-friday")).get("24hours"));
                                break;
                            case Calendar.SATURDAY:
                                pantryTime = ((String) ((Map) infoMap.get("-saturday")).get("24hours"));
                                break;
                            default:
                                pantryTime = "closed";
                        }

                        if (pantryTime.contains("[a-zA-Z]+")) {
                            openOrClose.setText("closed");
                            openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                        } else {
                            try {
                                open = LocalTime.parse(pantryTime.split(" ")[0], timeFormat);
                                close = LocalTime.parse(pantryTime.split(" ")[2], timeFormat);
                                if (close.minusHours(1).isBefore(currentTime) && close.isAfter(currentTime)) {
                                    openOrClose.setText("closes in " + Duration.between(currentTime, close)
                                            .abs()
                                            .toMinutes()
                                            + " minutes");
                                    openOrClose.setTextSize(18);
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_closing_soon));
                                } else if (currentTime.isAfter(open) && currentTime.isBefore(close)) {
                                    openOrClose.setText("open");
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_open));
                                } else {
                                    openOrClose.setText("closed");
                                    openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                                }
                            } catch (Exception e) {
                                // If invalid date or time, or simply closed? Should check input on web app to prevent error
                                System.out.println("May be closed or wrong input");
                                System.out.println("ACTUAL ERROR: " + e);
                                openOrClose.setText("closed");
                                openOrClose.setBackground(getDrawable(R.drawable.pantry_closed));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateButton.performClick();
            }
        }, 1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.info:
//                        Intent infoIntent = new Intent(InfoActivity.this, InfoActivity.class);
//                        startActivity(infoIntent);
                        break;

                    case R.id.category:
                        Intent categoryIntent = new Intent(InfoActivity.this, MainActivity.class);
                        startActivity(categoryIntent);
                        break;

                    case R.id.search:
                        Intent searchIntent = new Intent(InfoActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;

                    case R.id.foodrecovery:
                        Intent foodrecoveryIntent = new Intent(InfoActivity.this, FoodRecoveryActivity.class);
                        startActivity(foodrecoveryIntent);
                        break;

                    case R.id.menu:
                        Intent menuIntent = new Intent(InfoActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        break;
                }


                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
