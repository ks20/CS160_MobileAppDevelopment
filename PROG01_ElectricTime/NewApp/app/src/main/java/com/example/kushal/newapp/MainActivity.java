package com.example.kushal.newapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText travelDistance;
    private TextView maxRange, walkingManTime, evolveBoardTime, boostedBoardTime, oneWheelTime, motoTecTime, nineBotTime, i2_seTime, razorScooterTime, geoBladeTime, hoverTraxTime;
    private ImageButton walkingMan, boostedBoard, evolveBoard, oneWheel, mototecBoard, nineBot,
    i2_se, razorScooter, geoBlade, hoverTrax;
    private Button submitButton, clearButton;
    private String travelDistString;
    private double dist;
    private int walkingRange, evolveBoardRange, boostedBoardRange, oneWheelRange, motoTecRange, nineBotRange, i2_seRange, razorScooterRange, geoBladeRange, hoverTraxRange;
    boolean evolveIndicator = false, walkingIndicator = false, boostedIndicator = false, oneWheelIndicator = false, mototecIndicator = false,
            nineBotIndicator = false, i2_seIndicator = false, razorIndicator = false, geoIndicator = false, hoverIndicator= false;

    Map<String, Double> map = new HashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        travelDistance = (EditText) findViewById(R.id.travel_distance);
        maxRange = (TextView) findViewById(R.id.range);

        walkingMan = (ImageButton) findViewById(R.id.walkingMan);
        walkingManTime = (TextView) findViewById(R.id.walkingManTime);

        boostedBoard = (ImageButton) findViewById(R.id.boostedBoard);
        boostedBoardTime = (TextView) findViewById(R.id.boostedBoardTime);

        evolveBoard = (ImageButton) findViewById(R.id.evolveBoard);
        evolveBoardTime = (TextView) findViewById(R.id.evolveBoardTime);

        oneWheel = (ImageButton) findViewById(R.id.oneWheel);
        oneWheelTime = (TextView) findViewById(R.id.oneWheelTime);

        mototecBoard = (ImageButton) findViewById(R.id.motoTec);
        motoTecTime = (TextView) findViewById(R.id.motoTecTime);

        nineBot = (ImageButton) findViewById(R.id.nineBot);
        nineBotTime = (TextView) findViewById(R.id.nineBotTime);

        i2_se = (ImageButton) findViewById(R.id.i2se);
        i2_seTime = (TextView) findViewById(R.id.i2seTime);

        razorScooter = (ImageButton) findViewById(R.id.razor);
        razorScooterTime = (TextView) findViewById(R.id.razorTime);

        geoBlade = (ImageButton) findViewById(R.id.geoBlade);
        geoBladeTime = (TextView) findViewById(R.id.geoBladeTime);

        hoverTrax = (ImageButton) findViewById(R.id.hoverTrax);
        hoverTraxTime = (TextView) findViewById(R.id.hoverTraxTime);

        map.put("walkingManSpeed", 3.1);
        map.put("boostedBoardSpeed", 18.0);
        map.put("evolveSpeed", 26.0);
        map.put("oneWheelSpeed", 19.0);
        map.put("motoTecSpeed", 22.0);
        map.put("nineBotSpeed", 12.5);
        map.put("i2seSpeed", 12.5);
        map.put("razorSpeed", 10.0);
        map.put("geoSpeed", 15.0);
        map.put("hoverSpeed", 8.0);

        walkingRange = 30;
        boostedBoardRange = 7;
        evolveBoardRange = 31;
        oneWheelRange = 7;
        motoTecRange = 10;
        nineBotRange = 15;
        i2_seRange = 24;
        razorScooterRange = 7;
        geoBladeRange = 8;
        hoverTraxRange = 8;

        walkingMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = true;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose Walking!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Walking is 30 miles.");
            }
        });

        boostedBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = true;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose Boosted Board!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Boosted Mini S Board is 7 miles.");
            }
        });

        evolveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = true;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose Evolve Board!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Evolve Skateboard is 31 miles.");
            }
        });

        oneWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = true;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose One Wheel!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for OneWheel is 7 miles.");
            }
        });

        mototecBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = true;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose MotoTec!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for MotoTec is 10 miles.");
            }
        });

        nineBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = true;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose NineBot!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Segway Ninebot One S1 is 15 miles.");
            }
        });

        i2_se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = true;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose i2_se!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Segway i2 SE is 24 miles.");
            }
        });

        razorScooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = true;
                geoIndicator = false;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose Razor Scooter!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for Razor Scooter is " + razorScooterRange + " miles.");
            }
        });

        geoBlade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = true;
                hoverIndicator= false;
                Toast.makeText(getBaseContext(), "You chose GeoBlade 500!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for GeoBlade 500 is " + geoBladeRange + " miles.");
            }
        });

        hoverTrax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator = true;
                Toast.makeText(getBaseContext(), "You chose HoverTrax!", Toast.LENGTH_SHORT).show();
                maxRange.setText("Max Range for HoverTrax Hoverboard is " + hoverTraxRange + " miles.");
            }
        });

        submitButton = (Button) findViewById(R.id.submit_button);
        clearButton = (Button) findViewById(R.id.clear_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                travelDistString = travelDistance.getText().toString();

                if (travelDistString.isEmpty()) {
                    Toast.makeText(getBaseContext(), "You need to enter a distance!", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    dist = Double.parseDouble(travelDistString);
                }

                if (!evolveIndicator && !walkingIndicator && !boostedIndicator && !oneWheelIndicator && !mototecIndicator && !nineBotIndicator && !i2_seIndicator && !razorIndicator && !geoIndicator && !hoverIndicator) {
                    Toast.makeText(getBaseContext(), "You need to choose a method of transportation!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (evolveIndicator && dist > evolveBoardRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + evolveBoardRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (walkingIndicator && dist > walkingRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + walkingRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (boostedIndicator && dist > boostedBoardRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + boostedBoardRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (oneWheelIndicator && dist > oneWheelRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + oneWheelRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (mototecIndicator && dist > motoTecRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + motoTecRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (nineBotIndicator && dist > nineBotRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + nineBotRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (i2_seIndicator && dist > i2_seRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + i2_seRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (razorIndicator && dist > razorScooterRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + razorScooterRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (geoIndicator && dist > geoBladeRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + geoBladeRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (hoverIndicator && dist > hoverTraxRange) {
                    Toast.makeText(getBaseContext(), "Please enter a distance less than " + hoverTraxRange + " miles!", Toast.LENGTH_LONG).show();
                    return;
                }

                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    String transportSpeed = entry.getKey();
                    double speed = entry.getValue();
                    double timeTaken = (dist / speed) * 60;
                    DecimalFormat df = new DecimalFormat("#.#");
                    double roundedTime = Double.parseDouble(df.format(timeTaken));

                    if (transportSpeed.equals("walkingManSpeed")) {
                        walkingManTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("boostedBoardSpeed")) {
                        boostedBoardTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("evolveSpeed")) {
                        evolveBoardTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("oneWheelSpeed")) {
                        oneWheelTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("motoTecSpeed")) {
                        motoTecTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("nineBotSpeed")) {
                        nineBotTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("i2seSpeed")) {
                        i2_seTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("razorSpeed")) {
                        razorScooterTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("geoSpeed")) {
                        geoBladeTime.setText("Time: " + roundedTime + " min");
                    }
                    else if (transportSpeed.equals("hoverSpeed")) {
                        hoverTraxTime.setText("Time: " + roundedTime + " min");
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evolveIndicator = false;
                walkingIndicator = false;
                boostedIndicator = false;
                oneWheelIndicator = false;
                mototecIndicator = false;
                nineBotIndicator = false;
                i2_seIndicator = false;
                razorIndicator = false;
                geoIndicator = false;
                hoverIndicator= false;

                evolveBoardTime.setText("");
                walkingManTime.setText("");
                boostedBoardTime.setText("");
                oneWheelTime.setText("");
                motoTecTime.setText("");
                nineBotTime.setText("");
                i2_seTime.setText("");
                razorScooterTime.setText("");
                geoBladeTime.setText("");
                hoverTraxTime.setText("");

                maxRange.setText("");
                travelDistance.setText("");
                dist = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
