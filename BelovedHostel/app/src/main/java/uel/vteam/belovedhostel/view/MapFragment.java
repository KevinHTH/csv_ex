package uel.vteam.belovedhostel.view;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import uel.vteam.belovedhostel.R;
import uel.vteam.belovedhostel.adapter.DirectionsJSONParser;
import uel.vteam.belovedhostel.adapter.MyInfoWindowAdapter;
import uel.vteam.belovedhostel.adapter.MyMethods;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends Fragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MapFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meter
    // float action button
    FloatingActionButton fab, fabService, fabMyLocation, fabHome;
    boolean isVisible = false;
    Animation aMoveLeftTop, aMoveRightTop, aMoveDirect;
    Animation aBackLeftTop, aBackRightTop, aBackDirect;
    //  map va tinh khoang cach
    MapView mapView;
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Marker mMakerLocation;
    List<Address> list;

    LinearLayout search;
    EditText editWhere;
    Button btnGo;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    View view;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        // hide action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        dialog=new Dialog(getContext(),R.style.dialogStyle);
        MyMethods.getInstance().displayCustomProgress(getContext(),dialog,getResources().getString(R.string.loading));


        mapView = (MapView) view.findViewById(R.id.myMap);
        mapView.onCreate(savedInstanceState);
        addControls(view);
        mapView.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (checkConditionConnection()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        addEvents();
        MyMethods.getInstance().dismissCustomProgress(getContext(),dialog,1000);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getContext());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Snackbar.make(view, getResources().getString(R.string.map_fragment_error_support),
                    Snackbar.LENGTH_LONG).setDuration(3000).show();
        }
        return false;
    }

    private void createSnackbar(String text, String actionName, int durationTimeout, final String action) {
        Snackbar snackbar = Snackbar
                .make(view, text, Snackbar.LENGTH_LONG)
                .setAction(actionName, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(action);
                        startActivityForResult(intent, RESULT_CANCELED);
                    }
                });
        snackbar.setDuration(durationTimeout);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect(); // co ket noi se goi onConnected
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // get current location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            startLocationUpdates(); // chuyen toi onLocationChange
        } else {
           displayCurrentLocation(location);
           // getHostelLocation();
        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

                /*if (strAdd==null){
                        Toast.makeText(getContext(), getResources().getString(R.string.map_fragment_error_noaddress)
                                , Toast.LENGTH_SHORT).show();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }

    private void getHostelLocation() {

        double latitude = 11.9425105; // vi do
        double longitude = 108.4256703; // kinh do
        int IconId = R.drawable.main_logo;
        String title = "Beloved Hostel";
        creatMakerWindow(title, IconId, latitude, longitude);
    }

    private void displayCurrentLocation(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        int iconId = R.drawable.current_location_icon;
        String title = getResources().getString(R.string.map_fragment_current_location);
        creatMakerWindow(title, iconId, latitude, longitude);
    }

    private void creatMakerWindow(String title, int iconId, double latitude, double longitude) {
        if (mMakerLocation!=null){
            mMakerLocation.remove();
        }
        LatLng toado = new LatLng(latitude, longitude);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(toado);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMakerLocation = map.addMarker(markerOptions);

        String address = getCompleteAddressString(toado.latitude, toado.longitude);
        map.setInfoWindowAdapter(new MyInfoWindowAdapter(getContext(), iconId, address));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(toado, 13));
        mMakerLocation.showInfoWindow();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.map_fragment_cant_get_location),
                    Toast.LENGTH_SHORT).show();
        } else {
            displayCurrentLocation(location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    protected void stopLocationUpdates() {

        if (mGoogleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

    }

    public boolean checkGPS() {
        boolean isConnected = false;
        LocationManager service = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabledGPS) {
            isConnected = false;
        } else {
            isConnected = true;
        }
        return isConnected;
    }

    public boolean checkNetwork() {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        return isConnected;
    }

    private void addEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible == false) {
                    fab.setImageResource(R.drawable.ic_arrow_drop_down_white_48px);
                    Move();
                    isVisible = true;
                } else {
                    fab.setImageResource(R.drawable.ic_arrow_drop_up_white_48px);
                    MoveBack();
                    isVisible = false;
                }
            }
        });
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditionConnection()) {
                    getHostelLocation();
                }
            }
        });

        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditionConnection()) {
                    buildGoogleApiClient();
                }
            }
        });

        fabService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkConditionConnection()) {
                    search.setVisibility(View.VISIBLE);
                    btnGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String somewhere = editWhere.getText().toString();
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mapView.getWindowToken(), 0);
                            findLocationByName(somewhere);
                        }
                    });
                }
            }
        });


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Already two locations

                if (checkNetwork()) {
                    if (markerPoints.size() > 1) {
                        markerPoints.clear();
                        map.clear();
                    }
                    markerPoints.add(point);
                    MarkerOptions options = new MarkerOptions();
                    options.position(point);
                    if (markerPoints.size() == 1) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if (markerPoints.size() == 2) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    }

                    map.addMarker(options);
                    if (markerPoints.size() >= 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask downloadTask = new DownloadTask(view);
                        downloadTask.execute(url);
                    }
                } else {
                    String actionWifi = Settings.ACTION_WIFI_SETTINGS;
                    createSnackbar(getResources().getString(R.string.map_fragment_error_network),
                            getResources().getString(R.string.map_fragment_checkWifi)
                            , 3000, actionWifi);

                }

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng toado=marker.getPosition();

                    String title="this location";

                if (marker.getTitle()!=null) {

                    title = marker.getTitle().toString();
                }


                String address = getCompleteAddressString(toado.latitude,toado.longitude);
                if (marker.getSnippet()!=null){
                    address=marker.getSnippet().toString();
                }


                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(toado);
                markerOptions.title(title);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMakerLocation = map.addMarker(markerOptions);
                map.setInfoWindowAdapter(new MyInfoWindowAdapter(getContext(), R.drawable.current_location_icon, address));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(toado, 13));
                mMakerLocation.showInfoWindow();
               /* LatLng toado = new LatLng();
                marker.getPosition().*/
                return true;
            }
        });

    }

    public boolean checkConditionConnection() {
        boolean isSupport = false;
        if (checkNetwork()) {   // kiem tra internet
            if (checkGPS()) { // kiem tra gps
                if (googleServicesAvailable()) { // kiem tra google service
                    isSupport = true;
                } else {
                    isSupport = false;
                    Snackbar.make(view, getResources().getString(R.string.map_fragment_error_support), Snackbar.LENGTH_LONG).setDuration(3000).show();

                }
            } else {
                isSupport = false;
                String actionGPS = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                createSnackbar(getResources().getString(R.string.map_fragment_error_gps),
                        getResources().getString(R.string.map_fragment_checkGps),3000, actionGPS);
            }

        } else {
            isSupport = false;
            String actionWifi = Settings.ACTION_WIFI_SETTINGS;
            createSnackbar(getResources().getString(R.string.map_fragment_error_network),
                    getResources().getString(R.string.map_fragment_checkWifi), 3000, actionWifi);
        }
        return isSupport;
    }


    public void findLocationByName(String locationText) {
        Geocoder geocoder = new Geocoder(getContext());
        if(mMakerLocation!=null){
            map.clear();
        }
        try {
           list= geocoder.getFromLocationName(locationText, 1);
            if (list.size()>0){
                Address address = list.get(0);
                String locality = address.getLocality();
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                LatLng here = new LatLng(lat, lng);
                Location location = new Location(String.valueOf(here));
                map.addMarker(new MarkerOptions()
                .position(here));
                map.moveCamera(CameraUpdateFactory.newLatLng(here));
               /* CameraUpdate update = CameraUpdateFactory.newLatLngZoom(here, 12);
                map.animateCamera(update);*/

            }else {
                Snackbar.make(view, getResources().getString(R.string.map_fragment_cant_get_location),
                        Snackbar.LENGTH_LONG).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addControls(View v) {
        markerPoints = new ArrayList<LatLng>();
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fabHome = (FloatingActionButton) v.findViewById(R.id.fabHome);
        fabMyLocation = (FloatingActionButton) v.findViewById(R.id.fabMyLocation);
        fabService = (FloatingActionButton) v.findViewById(R.id.fabService);

        aMoveLeftTop = AnimationUtils.loadAnimation(getContext(), R.anim.move_left_top);
        aMoveDirect = AnimationUtils.loadAnimation(getContext(), R.anim.move_direct_top);
        aMoveRightTop = AnimationUtils.loadAnimation(getContext(), R.anim.move_right_top);

        aBackLeftTop = AnimationUtils.loadAnimation(getContext(), R.anim.back_left_top);
        aBackDirect = AnimationUtils.loadAnimation(getContext(), R.anim.back_direct_top);
        aBackRightTop = AnimationUtils.loadAnimation(getContext(), R.anim.back_right_top);

        search = (LinearLayout) v.findViewById(R.id.linearSearchLocation);
        editWhere = (EditText) v.findViewById(R.id.editSearchLocation);
        btnGo = (Button) v.findViewById(R.id.btnGo);

    }

    public void Move() {
        FrameLayout.LayoutParams paramsRight = (FrameLayout.LayoutParams) fabService.getLayoutParams();
        paramsRight.leftMargin = (int) (fabService.getWidth() * 1.2);
        paramsRight.bottomMargin = (int) (fabService.getHeight() * 1.5);
        fabService.setLayoutParams(paramsRight);
        fabService.startAnimation(aMoveRightTop);

        FrameLayout.LayoutParams paramsLeft = (FrameLayout.LayoutParams) fabMyLocation.getLayoutParams();
        paramsLeft.rightMargin = (int) (fabMyLocation.getWidth() * 1.2);
        paramsLeft.bottomMargin = (int) (fabMyLocation.getHeight() * 1.5);
        fabMyLocation.setLayoutParams(paramsLeft);
        fabMyLocation.startAnimation(aMoveLeftTop);


        FrameLayout.LayoutParams paramsCenter = (FrameLayout.LayoutParams) fabHome.getLayoutParams();
        paramsCenter.bottomMargin = (int) (fabHome.getHeight() * 2);
        fabHome.setLayoutParams(paramsCenter);
        fabHome.startAnimation(aMoveDirect);
    }

    public void MoveBack() {

        FrameLayout.LayoutParams paramsRight = (FrameLayout.LayoutParams) fabService.getLayoutParams();
        paramsRight.leftMargin -= (int) (fabService.getWidth() * 1.2);
        paramsRight.bottomMargin -= (int) (fabService.getHeight() * 1.5);
        fabService.setLayoutParams(paramsRight);
        fabService.startAnimation(aBackRightTop);

        FrameLayout.LayoutParams paramsLeft = (FrameLayout.LayoutParams) fabMyLocation.getLayoutParams();
        paramsLeft.rightMargin -= (int) (fabMyLocation.getWidth() * 1.2);
        paramsLeft.bottomMargin -= (int) (fabMyLocation.getHeight() * 1.5);
        fabMyLocation.setLayoutParams(paramsLeft);
        fabMyLocation.startAnimation(aBackLeftTop);

        FrameLayout.LayoutParams paramsCenter = (FrameLayout.LayoutParams) fabHome.getLayoutParams();
        paramsCenter.bottomMargin -= (int) (fabHome.getHeight() * 2);
        fabHome.setLayoutParams(paramsCenter);
        fabHome.startAnimation(aBackDirect);

        search.setVisibility(View.INVISIBLE);
    }
public void searchLocation(String text){
    //http://maps.googleapis.com/maps/api/geocode/json?address="nha hang huong xua"
}

    //============TINH KHOANG CACH ================

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    // Fetches data from url passed
    class DownloadTask extends AsyncTask<String, Void, String> {

        View view;

        public DownloadTask(View view) {
            this.view = view;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                // Connecting to url
                urlConnection.connect();
                // Reading data from url
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (Exception e) {
                // Log.d("Exception while downloading url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(view);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        View view;

        public ParserTask(View view) {
            this.view = view;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Log.d("MAP", "No point");
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);

            }
            Snackbar.make(view, getResources().getString(R.string.map_fragment_distance) + distance
                            + getResources().getString(R.string.map_fragment_duration) + duration,
                    Snackbar.LENGTH_LONG).setDuration(3000).show();

            map.addPolyline(lineOptions);
        }


    }


}
