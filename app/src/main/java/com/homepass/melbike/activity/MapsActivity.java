package com.homepass.melbike.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homepass.melbike.BaseActivity;
import com.homepass.melbike.Constants;
import com.homepass.melbike.R;
import com.homepass.melbike.entitiy.BikeSite;
import com.homepass.melbike.utility.Functions;
import com.homepass.melbike.utility.GlobalStatic;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

/**
 * Created by Tony Liu
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final String tag = "MapsActivity";
    private AutoCompleteTextView autoText;
    private String[] bikeSiteArray;
    private Marker marker = null;
    private Button naviBtn;
    private Context context;

    // Internal notification message Handler
    private static class InnerHandler extends Handler{
        WeakReference<MapsActivity> mActivity;
        public InnerHandler(MapsActivity activity){
            mActivity = new WeakReference<MapsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            Log.v(tag, "handleMessage:" + msg.what);
            MapsActivity theActivity = mActivity.get();
            switch(msg.what){
                case Constants.MSG_SITES_READY:
                    theActivity.addCircle();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GlobalStatic.Main_Handler = new InnerHandler(this);
        autoText = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoText.setThreshold(1);

        naviBtn = (Button) findViewById(R.id.naviBtn);
        naviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(marker);
            }
        });
        naviBtn.setEnabled(false);
        context = getApplicationContext();
    }

    @Override
    public boolean onMarkerClick(Marker inmarker) {
        if(inmarker != null){
            inmarker.showInfoWindow();
            this.marker = inmarker;
            autoText.setText(inmarker.getTitle());
            naviBtn.setEnabled(true);
            autoText.clearFocus();
        }
        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Start Navigation when user click the title which is the same with button press
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                navigateTo(marker);
            }
        });

        // Display multi-line information including title
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getApplicationContext();
                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet); // multi-line for displaying more info.
                return info;
            }
        });

        // Add a marker in the center of Melbourne city and move the camera
        LatLng melbourne = new LatLng(-37.813, 144.962);
        marker = mMap.addMarker(new MarkerOptions().position(melbourne).title("City of Melbourne").alpha(0.2f));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(melbourne));
        // start retrieve bike sites list from RESTFul API
        initializeSSLContext(context);
        Functions.retrieveBikeSites();
    }


    /**
     *
     * Initialize SSL
     * @param mContext
     */
    public static void initializeSSLContext(Context mContext){
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add bikesite on the map
     */
    private void addCircle(){
        if((GlobalStatic.G_List_BikeSite != null) && (mMap != null)){
            GlobalStatic.G_List_Circle.clear();
            GlobalStatic.G_Hash_Marker.clear();
            // Temp bike site namelist used only for AutoComplete Text
            List<String> bikelist = new ArrayList<String>();
            for(BikeSite bikeSite: GlobalStatic.G_List_BikeSite){
                // same number same color, different number different color.
                String hexcolor = String.format("#%06X",(0xFFFFFF & (-((bikeSite.getNbemptydoc()*5 + 30) * Constants.COLOR_ADJUDGEMENT))));
                LatLng position = new LatLng(bikeSite.getCoordinates().getCoordinates()[1], bikeSite.getCoordinates().getCoordinates()[0]);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(bikeSite.getFeaturename())
                        .snippet("Bike Number:"+bikeSite.getNbbikes()+"\nEmpty Docker:"+bikeSite.getNbemptydoc() + "\nUpdated Time:"+bikeSite.getUploaddate())
                        .infoWindowAnchor(0.5f, 0.92f)
                        .alpha(0.0f));
                GlobalStatic.G_Hash_Marker.put(bikeSite.getFeaturename(), marker);

                Circle newCircle = mMap.addCircle(new CircleOptions()
                            .center(position)
                            .clickable(true)
                            .radius(bikeSite.getNbemptydoc()*5 + 50)
                            .strokeWidth(Constants.CIRCLE_STROKE_WIDTH)
                            .strokeColor(Color.RED)
                            .fillColor(Color.parseColor(hexcolor)));
                // newCircle.setZIndex();
                GlobalStatic.G_List_Circle.add(newCircle);
                Log.v(tag, "bike circle added:" + bikeSite.getFeaturename());

                bikelist.add(bikeSite.getFeaturename());
            }
            zoomToAllSites();
            initAutoCompleteInput(bikelist.toArray(new String[0]));
        }
    }

    /**
     * initialize auto complete text
     * @param bikeSiteArray
     * @return
     */
    private boolean initAutoCompleteInput(String[] bikeSiteArray){
        if(bikeSiteArray != null){
            try{
                ArrayAdapter adapter = new ArrayAdapter(this,R.layout.search_maps, bikeSiteArray);
                autoText.setAdapter(adapter);
                autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        //here you can save the clicked elements
                        BikeSite selBike = null;
                        for(BikeSite bikeSite: GlobalStatic.G_List_BikeSite){
                            if(bikeSite.getFeaturename().equals(((TextView)arg1).getText().toString())){
                                selBike = bikeSite;
                                break;
                            }
                        }
                        if(selBike != null){
                            hideKeyBoard();
                            LatLng item = new LatLng(selBike.getCoordinates().getCoordinates()[1], selBike.getCoordinates().getCoordinates()[0]);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(item));
                            Marker tmpm = GlobalStatic.G_Hash_Marker.get(selBike.getFeaturename());
                            onMarkerClick(tmpm);
                        }
                    }
                });
                return true;
            }catch(Exception ex){
                return false;                
            }
        }else{
            return false;
        }
    }

    /**
     * zoom to the range of all bike sites
     * @return
     */
    private boolean zoomToAllSites(){
        if((GlobalStatic.G_List_Circle != null) && (mMap != null)){
            try{
                LatLngBounds.Builder mBuilder = new LatLngBounds.Builder();
                for(Circle circle: GlobalStatic.G_List_Circle){
                    mBuilder.include(circle.getCenter());
                }
                LatLngBounds bounds = mBuilder.build();
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
            }catch(Exception ex){
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
