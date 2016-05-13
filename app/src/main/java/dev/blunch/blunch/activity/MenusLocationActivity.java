package dev.blunch.blunch.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;

public class MenusLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    Location localizacion;
    MenuService menuService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        localizacion = null;
        menuService = ServiceFactory.getMenuService(getApplicationContext());
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

            //we search for the fib
            LatLng BCN = getLocationFromAddress("Barcelona");
            if(BCN != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(BCN)      // Sets the center to DA FIB
                        .zoom(10)         // we set the zoom
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),5000,null);
            }
            situaMenus();
        }
//    }

    public void situaMenus(){
        List<Menu> menus = menuService.getMenus();
        for(Menu m : menus){
            if(m.getDateEnd().after(new Date())) {
                LatLng posicion = getLocationFromAddress(m.getLocalization());
                if (posicion != null) {
                    mMap.addMarker(new MarkerOptions().position(posicion).title(m.getName()));
                }
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        LatLng coordenades = null;
        try {
            try {
                address = coder.getFromLocationName(strAddress, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            coordenades = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordenades;
    }

    private Boolean gpsEnabled() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                LocationManager.GPS_PROVIDER);
         return gpsStatus;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if(gpsEnabled()) return false;
        Toast.makeText(this, "GPS not enabled",
                Toast.LENGTH_LONG).show();
        return false;
    }
}
