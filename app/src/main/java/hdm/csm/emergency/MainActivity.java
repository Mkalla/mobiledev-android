package hdm.csm.emergency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback, View.OnClickListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;

    ListView listMenu;
    TextView textViewCurrentLocation;
    TextView textViewName;
    TextView textViewWeatherType;
    TextView textViewTemperature;
    FloatingActionButton fab;

    User user;
    DataManager dataManager;
    Geocoder geocoder;

    double latitude;
    double longitude;

    String baseUrl;
    String qry;
    String key;
    String reqUrl;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, CreateReportActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = User.getInstance(getApplicationContext());
        dataManager = DataManager.getInstance(getApplicationContext());

        textViewCurrentLocation = (TextView) findViewById(R.id.textView_CurrentLocation);
        textViewName = (TextView) findViewById(R.id.textView_Name);

        textViewWeatherType = (TextView) findViewById(R.id.weatherTypeTextView);
        textViewTemperature = (TextView) findViewById(R.id.weatherTempTextView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        listMenu = (ListView) findViewById(R.id.listMenu);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, CreateReportActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, PastReportsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, FAQActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        //Geocoder
        if(!internetAvailable(this)) {
            Toast.makeText(this, "No internet connection. Some features won't work", Toast.LENGTH_SHORT).show();
            textViewCurrentLocation.setText("No internet connection");
        }
    }

    public static boolean internetAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;
    }
    
    @Override
    protected void onStart() {
        super.onStart();

        //redirect to user registration if no user data
        if (user.getForename() == null || user.getSurname() == null) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }

        if (user != null) {
            textViewName.setText(user.getForename() + " " + user.getSurname());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "registration failed ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        updateGeoLocation(location);

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        getWeatherForLatLng(latLng.latitude, latLng.longitude);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        updateGeoLocation(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.optionsButton_termsAndConditions:
                startActivity(new Intent(MainActivity.this, GTCActivity.class));
                break;
            case R.id.optionsButton_userdata:
                startActivityForResult(new Intent(MainActivity.this, RegisterActivity.class), 1);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateGeoLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latLng = new LatLng(latitude, longitude);

            List<Address> addresses = null;

            if(internetAvailable(this)) {
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException ioException) {
                    // Catch network or other I/O problems.
                    Log.i("Geocoder", "Geocoding failed");
                }

                // Handle case where no address was found.
                if (addresses == null || addresses.size() == 0) {
                    Log.i("Geocoder", "Address not found");
                } else {
                    Address address = addresses.get(0);

                    // Fetch the address lines using getAddressLine,
                    // join them, and send them to the thread.

                    ArrayList<String> addressFragments = new ArrayList<String>();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }

                    textViewCurrentLocation.setText(TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
                }

            }
        }
    }

    public void getWeatherForLatLng(double lat, double lng) {
        // Instantiate the RequestQueue.
        RequestQueue weatherQueue = Volley.newRequestQueue(this);

        baseUrl = "http://api.openweathermap.org";
        qry = "/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&units=metric&APPID=";
        key = "24e194e4e62a3017b24f378910d19827";
        reqUrl = baseUrl + qry + key;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Extracted Json Data", "Trying...");
                        String data = "";

                        try {
                            JSONObject jsonRootObject = new JSONObject(response);

                            String temperature = jsonRootObject.getJSONObject("main").getString("temp") + " °C";
                            textViewTemperature.setText(temperature);
                            dataManager.setTemperature(temperature);


                            JSONArray jsonWeatherArray = jsonRootObject.getJSONArray("weather");

                            String weatherType = jsonWeatherArray.getJSONObject(0).getString("description"); //NOTE: Don't know why it is an array. Getting the first element in array should be ok
                            textViewWeatherType.setText(weatherType);
                            dataManager.setWeatherType(weatherType);

                            Log.i("ExtractedJsonData", data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Extracted Json Data", "Failed!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        weatherQueue.add(stringRequest);
    }
}
