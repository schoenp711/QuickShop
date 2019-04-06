package edu.duq.schoenp.quickshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Displays Google Maps map for user to pick store
 */
public class PickStore extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
    OnMapReadyCallback {

  /**
   * Extra for intent
   */
  public static final String EXTRA_MESSAGE = "edu.duq.schoenp.quickshop.MESSAGE";
  /**
   * if the user selects the Giant Eagle Southside store this extra will be placed in the intent
   */
  private static final String giantEagleSouthsideExtra = "Giant Eagle Southside";
  /**
   * if the user selects the Wegmans Dickson City store this extra will be placed in the intent
   */
  private static final String wegmansDicksonCityExtra = "Wegmans Dickson City";
  /**
   * Lat Lng of Giant Eagle
   */
  private static final LatLng GIANT_EAGLE_SOUTHSIDE = new LatLng(40.431035, -79.9767545);
  /**
   * Lat Lng of Wegmans
   */
  private static final LatLng WEGMANS_DICKSON_CITY = new LatLng(41.4752071, -75.6333655);
  /**
   * Marker placed on map for Giant Eagle
   */
  private Marker mGiantEagleSouthSide;
  /**
   * Marker placed on map for Wegmans
   */
  private Marker mWegmansDicksonCity;

  /**
   * Display map once activity is created
   *
   * @param savedInstanceState current instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pick_store);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    assert mapFragment != null;
    mapFragment.getMapAsync(this);
  }


  /**
   * Manipulates the map once available. This callback is triggered when the map is ready to be
   * used. This is where we can add markers or lines, add listeners or move the camera. If Google
   * Play services is not installed on the device, the user will be prompted to install it inside
   * the SupportMapFragment. This method will only be triggered once the user has installed Google
   * Play services and returned to the app.
   *
   * @param googleMap googleMap
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    // add markers for each store based on lat lng and store name
    mGiantEagleSouthSide = googleMap.addMarker(new MarkerOptions()
        .position(GIANT_EAGLE_SOUTHSIDE)
        .title("Giant Eagle Southside"));
    mGiantEagleSouthSide.setTag(0);

    mWegmansDicksonCity = googleMap.addMarker(new MarkerOptions()
        .position(WEGMANS_DICKSON_CITY)
        .title("Wegmans Dickson City"));
    mWegmansDicksonCity.setTag(0);

    googleMap.setOnMarkerClickListener(this);
    //default zoom level
    float zoomLevel = 18.0f; //This goes up to 21
    //set default location
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GIANT_EAGLE_SOUTHSIDE, zoomLevel));


  }

  /**
   * When a marker is clicked, sent the intent to start the next activity String extra indicating
   * which store is selected is attached to intent
   *
   * @param marker marker that was clicked
   * @return if error starting activity return false
   */
  @Override
  public boolean onMarkerClick(final Marker marker) {
    Intent intent = new Intent(PickStore.this, ItemList.class);

    if (marker.equals(mGiantEagleSouthSide)) {
      intent.putExtra(EXTRA_MESSAGE, giantEagleSouthsideExtra);
      startActivity(intent);

    } else if (marker.equals(mWegmansDicksonCity)) {
      intent.putExtra(EXTRA_MESSAGE, wegmansDicksonCityExtra);
      startActivity(intent);
    }
    return false;
  }

}
