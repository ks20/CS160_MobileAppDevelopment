package com.example.kushal.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int zipCode;
    private double latitude, longitude;
    private HashMap<String, ArrayList<Object>> repImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        Bundle getBund = getIntent.getExtras();
        boolean isCurrLoc = (boolean) getBund.get("currLocationFlag");
        boolean isZipLoc = (boolean) getBund.get("zipLocationFlag");
        boolean isRandLoc = (boolean) getBund.get("randLocationFlag");

        Bundle bundle = new Bundle();

        if (isCurrLoc) {
            latitude = (double) getBund.get("Latitude");
            longitude = (double) getBund.get("Longitude");
            bundle.putDouble("lat", latitude);
            bundle.putDouble("long", longitude);
            bundle.putBoolean("currLocFlag", true);
            bundle.putBoolean("zipLocFlag", false);
            bundle.putBoolean("randLocFlag", false);
        }
        else if (isZipLoc) {
            String zipCodeVal = (String) getBund.get("ZipCode");
            zipCode = Integer.parseInt(zipCodeVal);
            bundle.putInt("zip", zipCode);
            bundle.putBoolean("currLocFlag", false);
            bundle.putBoolean("zipLocFlag", true);
            bundle.putBoolean("randLocFlag", false);
        }
        else if (isRandLoc) {
            latitude = (double) getBund.get("Latitude");
            longitude = (double) getBund.get("Longitude");
            bundle.putDouble("lat", latitude);
            bundle.putDouble("long", longitude);
            bundle.putBoolean("currLocFlag", false);
            bundle.putBoolean("zipLocFlag", false);
            bundle.putBoolean("randLocFlag", true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Senators"));
        tabLayout.addTab(tabLayout.newTab().setText("Representatives"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount(), bundle);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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
