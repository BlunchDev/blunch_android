package dev.blunch.blunch.activity;

import android.content.ContentResolver;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.*;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.Barcode.GeoPoint;

import java.io.IOException;
import java.util.List;

import dev.blunch.blunch.R;

public class MenusLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    Location localizacion;
    private GoogleApiClient gapiClient;

    protected synchronized void buildGoogleApiClient(){
        gapiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_menus_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        localizacion = null;
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (gpsEnabled() && localizacion != null){
            LatLng posicion = new LatLng(localizacion.getLatitude(),localizacion.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
        }
        else {
            Toast.makeText(this, "GPS no conectado!",
                    Toast.LENGTH_LONG).show();

            //we search for the fib
            LatLng FIB = getLocationFromAddress("C/Jordi Girona Salgado, 1-3, 08034 Barcelona");
            if(FIB != null) {
                mMap.addMarker(new MarkerOptions().position(FIB).title("DA FIB"));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(FIB)      // Sets the center to DA FIB
                        .zoom(17)         // we set the zoom
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),5000,null);
            }
            else {
                Toast.makeText(this, "DA FIB DOESN'T EXIST ON EARTH!",
                        Toast.LENGTH_LONG).show();
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
        public void onLocationChanged(Location loc) {
           if(loc != null) {
               localizacion = loc;
               LatLng posicion = new LatLng(localizacion.getLatitude(), localizacion.getLongitude());
               mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
           }
        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
