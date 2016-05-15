package dev.blunch.blunch.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.IntentUtils;

public class MenusLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener {

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
        //mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

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
                    MarkerOptions marcador = new MarkerOptions()
                            .position(posicion)
                            .title(m.getId())
                            .snippet("Finalización: " + m.getDateEnd());
                    if (m instanceof PaymentMenu) marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mMap.addMarker(marcador);
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
        Toast.makeText(this, "GPS no conectado",
                Toast.LENGTH_LONG).show();
        return false;
    }

    //TO delete if not needed
    @Override
    public void onInfoWindowClick(Marker marker) {
        Menu m = menuService.getMenu(marker.getTitle());
        Intent intent = IntentUtils.getMenuDetailIntent(m,getApplicationContext());
        intent.putExtra("menuId", m.getId());
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Menu m = menuService.getMenu(marker.getTitle());
        Intent intent = IntentUtils.getMenuDetailIntent(m,getApplicationContext());
        intent.putExtra("menuId", m.getId());
        startActivity(intent);
        return true;
    }
}
