package com.test.ksi.testmap.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdsmdg.tastytoast.TastyToast;
import com.test.ksi.testmap.R;
import com.test.ksi.testmap.adapter.ViewPagerAdapter;
import com.test.ksi.testmap.interfaces.HandleRetrofitResp;
import com.test.ksi.testmap.models.googlePlacesApis.ModelGooglePlacesApis;
import com.test.ksi.testmap.models.googlePlacesApis.Results;
import com.test.ksi.testmap.retorfitconfig.HandleCalls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, HandleRetrofitResp, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.vpContent)
    ViewPager vpContent;

    private GoogleMap mMap;

    List<Marker> markers;
    ViewPagerAdapter adapter;

    //google maps
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ButterKnife.bind(this);
        initCurrentLocation();
        initViewPager();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        markers = new ArrayList<>();

        for (Results result : modelGooglePlacesApis.getResults()) {

            //  }
            //  for (Data data : Listing.modelProvidersPoints.getData()) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(result.getName()));

            //    .icon(icon));
            //  marker.setTag(data.getUuid());
            marker.setTag(result);
            marker.setSnippet(result.getId());

            markers.add(marker);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (Marker m : markers) {

                    if (m.getSnippet().equals(marker.getSnippet()))
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    else
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                }

                Results data = (Results) marker.getTag();

                TastyToast.makeText(MapsActivity.this, data.getName(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                //  showDialogNewChat(data.getName(), data.getCategory_name(), data.getUuid(), data.getImage_url());
                return true;
            }
        });


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.20); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);
    }

    ModelGooglePlacesApis modelGooglePlacesApis;

    @Override
    public void onResponseSuccess(String flag, Object o) {
        modelGooglePlacesApis = (ModelGooglePlacesApis) o;
        Log.d("statusTest", "here");
        Log.d("statusTest", modelGooglePlacesApis.getStatus());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    private void initLoc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            HandleCalls.getInstance(this).setonRespnseSucess(this);
            HandleCalls.getInstance(this).callGetGooglePlaces("test", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude(), "20");
        }


        // animateMarker(latlang,true);
    }

    private void initCurrentLocation() {
        // needed to get the map to display immediately


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
//============================//
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            //Log.e("expt", e.getMessage());
            e.printStackTrace();
        }
        //=============================//

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLoc();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initViewPager() {

        ArrayList<Results> mItems = new ArrayList<>();
        Results slid;

        slid = new Results();
        slid.setName("dfddsa");
        //   slid.setImg(R.drawable.howitworksstudent);
        mItems.add(slid);

        slid = new Results();
        slid.setName("dfddsa");
        mItems.add(slid);

        slid = new Results();
        slid.setName("dfddsa");
        mItems.add(slid);

        adapter = new ViewPagerAdapter(MapsActivity.this, mItems, vpContent);
        vpContent.setAdapter(adapter);
        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                vpImages.getChildCount();

                Random rnd = new Random();
                // vp_images.setPageTransformer(true, (ViewPager.PageTransformer) new AccordionTransformer());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
