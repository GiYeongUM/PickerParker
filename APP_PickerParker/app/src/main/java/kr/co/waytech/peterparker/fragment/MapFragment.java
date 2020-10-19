package kr.co.waytech.peterparker.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import kr.co.waytech.peterparker.activity.PostClass;
import kr.co.waytech.peterparker.activity.MainActivity;
import kr.co.waytech.peterparker.model.MyItem;
import kr.co.waytech.peterparker.model.OwnIconRendered;
import kr.co.waytech.peterparker.R;
import kr.co.waytech.peterparker.adapter.RecyclerAdapter;
import kr.co.waytech.peterparker.adapter.ListAdapter;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static kr.co.waytech.peterparker.activity.MainActivity.location;
import static kr.co.waytech.peterparker.activity.MainActivity.mlat;
import static kr.co.waytech.peterparker.activity.MainActivity.mlon;
import static kr.co.waytech.peterparker.activity.PostClass.All_Parkinglot;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static final LatLng ABC = null;
    public static Float ZoomLevel;
    public static int Connect_Flag = 0;
    public static int count_ranged;
    public static String[][] Selected_Parking;
    public static MainActivity mainActivity;
    public static final LatLng target = null;
    private GoogleApiClient mGoogleApiClient = null;
    private Marker currentMarker = null;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int REQUEST_LOCATION = 1;

    public static double mlatitude, mlongitude;
    public static String ParkingID;
    public static LinearLayout tab1_layout;
    public static LinearLayout tab2_layout;
    public static TextView tab1_text;
    private ListView mListView, mPickedView;
    ImageButton search_btn;
    EditText search_edt;
    public static double lat, lng;
    public static int Plus_array;
    public static double x1, y1, x2, y2;
    final PostClass Postc = new PostClass();
    public static SlidingUpPanelLayout slidingUpPanelLayout;
    TabLayout tabLayout;
    public static List<Address> AddressList;
    public static InputMethodManager imm = null;
    public static GoogleMap mMap;
    private Context mapFragment;
    private MapView mapView = null;
    FrameLayout frameLayout;
    private ClusterManager<MyItem> clusterManager;
    public static Geocoder geocoder;
    private RecyclerAdapter recyclerAdapter;
    private ListAdapter listadapter;


    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    public MapFragment() {

        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        imm = (InputMethodManager) mapFragment.getSystemService(INPUT_METHOD_SERVICE);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.getMapAsync(this);
        search_edt = (EditText) view.findViewById(R.id.search_edt);
        search_btn = (ImageButton) view.findViewById(R.id.search_btn);
        tab1_text = (TextView) view.findViewById(R.id.tab1_text);
        geocoder = new Geocoder(mapFragment);
        search_edt.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                tab1_layout.setVisibility(View.INVISIBLE);
                tab2_layout.setVisibility(View.INVISIBLE);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = search_edt.getText().toString();
                AddressList = null;
                TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                if(tab3 != null) {
                    tabLayout.removeTabAt(3);
                }
                tabLayout.addTab(tabLayout.newTab().setText("검색 위치"));
                try {
                    AddressList = geocoder.getFromLocationName(str, 10); // 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (AddressList != null) {
                    if (AddressList.size() == 0) {
                        System.out.println("주소 오류");
                    } else {
                        System.out.println(AddressList.get(0).toString());
                        String[] splitStr = AddressList.get(0).toString().split(",");
                        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                        System.out.println(address);
                        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                        System.out.println(latitude);
                        System.out.println(longitude);
                        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        Address addr = AddressList.get(0);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

                    }
                }
                imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
                //search_result = search_edt.getText().toString();
                TabLayout.Tab tab = tabLayout.getTabAt(3);
                tab.select();
                tab1_layout.setVisibility(View.GONE);
                tab2_layout.setVisibility(View.GONE);
                tab2_layout.setVisibility(View.VISIBLE);
                ZoomLevel = mMap.getCameraPosition().zoom;
                lat = mMap.getCameraPosition().target.latitude;
                lng = mMap.getCameraPosition().target.longitude;
                x1 = (lat + 1 * (0.012 * (2 ^ (int) (15.0 - ZoomLevel))));
                y1 = (lng - 1 * (0.012 * (2 ^ (int) (15.0 - ZoomLevel))));
                x2 = (lat - 1 * (0.012 * (2 ^ (int) (15.0 - ZoomLevel))));
                y2 = (lng + 1 * (0.012 * (2 ^ (int) (15.0 - ZoomLevel))));
                getData_onCameradistance(lat, lng);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tab1_layout = (LinearLayout) view.findViewById(R.id.tab1_layout);
        tab2_layout = (LinearLayout) view.findViewById(R.id.tab2_layout);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_view);
        tabLayout.addTab(tabLayout.newTab().setText("현재 Pick"));
        tabLayout.addTab(tabLayout.newTab().setText("거리순"));
        tabLayout.addTab(tabLayout.newTab().setText("가격순"));
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_main);
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    //mainActivity.GetMyLocation();
                    TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                    if(tab3 != null) {
                        tabLayout.removeTabAt(3);
                    }

                    imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
                    mlatitude = MainActivity.mlat;
                    mlongitude = MainActivity.mlon;
                    System.out.println("핀마커---------------------------------------------------------");
                    ZoomLevel = mMap.getCameraPosition().zoom;
                    lat = mMap.getCameraPosition().target.latitude;
                    lng = mMap.getCameraPosition().target.longitude;
                    tab1_layout.setVisibility(View.VISIBLE);
                    tab2_layout.setVisibility(View.GONE);
                } else if (pos == 1) {
                    TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                    if(tab3 != null) {
                        tabLayout.removeTabAt(3);
                    }

                    imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
                    // mainActivity.GetMyLocation();
                    mlatitude = MainActivity.mlat;
                    mlongitude = MainActivity.mlon;
                    System.out.println("내 위치 :" + mlatitude + ", " + mlongitude);
                    System.out.println("거리순---------------------------------------------------------");
                    ZoomLevel = mMap.getCameraPosition().zoom;
                    lat = mlatitude;
                    lng = mlongitude;
                    tab1_layout.setVisibility(View.GONE);
                    tab2_layout.setVisibility(View.GONE);
                    tab2_layout.setVisibility(View.VISIBLE);
                    getData_distance();
                }
                else if (pos == 2){
                    TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                    if(tab3 != null) {
                        tabLayout.removeTabAt(3);
                    }

                    imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
                    //mainActivity.GetMyLocation();
                    mlatitude = MainActivity.mlat;
                    mlongitude = MainActivity.mlon;
                    System.out.println("내 위치 :" + mlatitude + ", " + mlongitude);
                    System.out.println("가격순---------------------------------------------------------");
                    ZoomLevel = mMap.getCameraPosition().zoom;
                    lat = mlatitude;
                    lng = mlongitude;
                    tab1_layout.setVisibility(View.GONE);
                    tab2_layout.setVisibility(View.GONE);
                    tab2_layout.setVisibility(View.VISIBLE);
                    getData_Price();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    /*  주소변환 코드
        for(int b = 0; b < Postc.count; b++) {
            List<Address> Adlist = null;
            try {

                double d1 = Double.parseDouble(All_Parkinglot[b][2]);
                double d2 = Double.parseDouble(All_Parkinglot[b][3]);
                Adlist = geocoder.getFromLocation(
                        d1,
                        d2,
                        5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Adlist != null) {
                if (Adlist.size()==0) {

                } else {
                    String[] splitStr = Adlist.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                    System.out.println("주소 : "+ address);
                }
            }
                    }


         */
    private void getData_pick(String ID, String Address, int Price, double distance){
        mPickedView = (ListView) getView().findViewById(R.id.picked_parkinglot);
        recyclerAdapter = new RecyclerAdapter();
        mPickedView.setAdapter(recyclerAdapter);
        recyclerAdapter.addItem(Address, Integer.toString(Price), (int)distance + "m", ID);

    }

    private void getData_distance() {
        mListView = (ListView) getView().findViewById(R.id.list_parkinglot);
        listadapter = new ListAdapter();
        mListView.setAdapter(listadapter);
        count_ranged = 0;
        Plus_array = 0;
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if ((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
            }
        }
        Selected_Parking = new String[count_ranged][5];
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
                Selected_Parking[Plus_array][0] = getAddress(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]));
                Selected_Parking[Plus_array][1] = All_Parkinglot[i][1];
                Selected_Parking[Plus_array][2] = All_Parkinglot[i][2];
                Selected_Parking[Plus_array][3] = All_Parkinglot[i][3];
                Selected_Parking[Plus_array][4] = All_Parkinglot[i][0];
                Plus_array++;
            }
        }
        for (int i = 0; i < Selected_Parking.length; i++) {
            for (int j = i + 1; j < Selected_Parking.length; j++) {
                if ((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3])))
                        > (int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[j][2]), Double.parseDouble(Selected_Parking[j][3])))) {
                    String tempdis = Selected_Parking[i][0];
                    String tempprice = Selected_Parking[i][1];
                    String templat = Selected_Parking[i][2];
                    String templng = Selected_Parking[i][3];
                    String tempid = Selected_Parking[i][4];
                    Selected_Parking[i][0] = Selected_Parking[j][0];
                    Selected_Parking[i][1] = Selected_Parking[j][1];
                    Selected_Parking[i][2] = Selected_Parking[j][2];
                    Selected_Parking[i][3] = Selected_Parking[j][3];
                    Selected_Parking[i][4] = Selected_Parking[j][4];
                    Selected_Parking[j][0] = tempdis;
                    Selected_Parking[j][1] = tempprice;
                    Selected_Parking[j][2] = templat;
                    Selected_Parking[j][3] = templng;
                    Selected_Parking[j][4] = tempid;
                }
            }
        }

        for(int i = 0; i < Selected_Parking.length; i++) {
            if((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) < 4000) {
                listadapter.addItem(Selected_Parking[i][0], Selected_Parking[i][1], (int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) + "m", Selected_Parking[i][4]);

                System.out.println(Selected_Parking[i][0] + ", " + Selected_Parking[i][1] + ", " + Selected_Parking[i][2]+ ", " +Selected_Parking[i][3]);
            }
        }
    }

    private void getData_onCameradistance(double clat, double clon) {
        mListView = (ListView) getView().findViewById(R.id.list_parkinglot);
        listadapter = new ListAdapter();
        mListView.setAdapter(listadapter);
        count_ranged = 0;
        Plus_array = 0;
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if ((int) getDistance(clat, clon, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
            }
        }
        Selected_Parking = new String[count_ranged][5];
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if((int) getDistance(clat, clon, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
                Selected_Parking[Plus_array][0] = getAddress(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]));
                Selected_Parking[Plus_array][1] = All_Parkinglot[i][1];
                Selected_Parking[Plus_array][2] = All_Parkinglot[i][2];
                Selected_Parking[Plus_array][3] = All_Parkinglot[i][3];
                Selected_Parking[Plus_array][4] = All_Parkinglot[i][0];
                Plus_array++;
            }
        }
        for (int i = 0; i < Selected_Parking.length; i++) {
            for (int j = i + 1; j < Selected_Parking.length; j++) {
                if ((int) getDistance(clat, clon, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3])))
                        > (int) getDistance(clat, clon, latlngConv(Double.parseDouble(Selected_Parking[j][2]), Double.parseDouble(Selected_Parking[j][3])))) {
                    String tempdis = Selected_Parking[i][0];
                    String tempprice = Selected_Parking[i][1];
                    String templat = Selected_Parking[i][2];
                    String templng = Selected_Parking[i][3];
                    String tempid = Selected_Parking[i][4];
                    Selected_Parking[i][0] = Selected_Parking[j][0];
                    Selected_Parking[i][1] = Selected_Parking[j][1];
                    Selected_Parking[i][2] = Selected_Parking[j][2];
                    Selected_Parking[i][3] = Selected_Parking[j][3];
                    Selected_Parking[i][4] = Selected_Parking[j][4];
                    Selected_Parking[j][0] = tempdis;
                    Selected_Parking[j][1] = tempprice;
                    Selected_Parking[j][2] = templat;
                    Selected_Parking[j][3] = templng;
                    Selected_Parking[j][4] = tempid;
                }
            }
        }

        for(int i = 0; i < Selected_Parking.length; i++) {
            if((int) getDistance(clat, clon, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) < 4000) {
                listadapter.addItem(Selected_Parking[i][0], Selected_Parking[i][1], (int) getDistance(clat, clon, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) + "m", Selected_Parking[i][4]);

                System.out.println(Selected_Parking[i][0] + ", " + Selected_Parking[i][1] + " 원"+ ", " + Selected_Parking[i][2]+ ", " +Selected_Parking[i][3]);
            }
        }
    }


    private void getData_Price() {
        mListView = (ListView) getView().findViewById(R.id.list_parkinglot);
        listadapter = new ListAdapter();
        mListView.setAdapter(listadapter);
        count_ranged = 0;
        Plus_array = 0;
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if ((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
            }
        }
        Selected_Parking = new String[count_ranged][5];
        for(int i = 0; i < All_Parkinglot.length; i++) {
            if((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]))) < 4000) {
                count_ranged++;
                Selected_Parking[Plus_array][0] = getAddress(Double.parseDouble(All_Parkinglot[i][2]), Double.parseDouble(All_Parkinglot[i][3]));
                Selected_Parking[Plus_array][1] = All_Parkinglot[i][1];
                Selected_Parking[Plus_array][2] = All_Parkinglot[i][2];
                Selected_Parking[Plus_array][3] = All_Parkinglot[i][3];
                Selected_Parking[Plus_array][4] = All_Parkinglot[i][0];
                Plus_array++;
            }
        }
        for (int i = 0; i < Selected_Parking.length; i++) {
            for (int j = i + 1; j < Selected_Parking.length; j++) {
                if (Integer.parseInt(Selected_Parking[i][1]) > Integer.parseInt(Selected_Parking[j][1])) {
                    String tempdis = Selected_Parking[i][0];
                    String tempprice = Selected_Parking[i][1];
                    String templat = Selected_Parking[i][2];
                    String templng = Selected_Parking[i][3];
                    String tempid = Selected_Parking[i][4];
                    Selected_Parking[i][0] = Selected_Parking[j][0];
                    Selected_Parking[i][1] = Selected_Parking[j][1];
                    Selected_Parking[i][2] = Selected_Parking[j][2];
                    Selected_Parking[i][3] = Selected_Parking[j][3];
                    Selected_Parking[i][4] = Selected_Parking[j][4];
                    Selected_Parking[j][0] = tempdis;
                    Selected_Parking[j][1] = tempprice;
                    Selected_Parking[j][2] = templat;
                    Selected_Parking[j][3] = templng;
                    Selected_Parking[j][4] = tempid;
                }
            }
        }

        for(int i = 0; i < Selected_Parking.length; i++) {
            if((int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) < 4000) {
                listadapter.addItem(Selected_Parking[i][0], Selected_Parking[i][1], (int) getDistance(mlatitude, mlongitude, latlngConv(Double.parseDouble(Selected_Parking[i][2]), Double.parseDouble(Selected_Parking[i][3]))) + "m", Selected_Parking[i][4]);

                System.out.println(Selected_Parking[i][0] + ", " + Selected_Parking[i][1] + " 원"+ ", " + Selected_Parking[i][2]+ ", " +Selected_Parking[i][3]);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    public String getAddress(double lat, double lng){
        List<Address> Adlist = null;
        try {
            Adlist = geocoder.getFromLocation(
                    lat,
                    lng,
                    1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Adlist != null) {
            if (Adlist.size()==0) {
                return null;
            } else {
                String[] splitStr = Adlist.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                return address;
            }
        }
        return null;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setMinZoomPreference((float) 7.5);
        enableMyLocationBtn();
        checkp();

        //mainActivity.GetMyLocation();
        mlatitude = MainActivity.mlat;
        mlongitude = MainActivity.mlon;
        LatLng ABC = new LatLng(mlatitude, mlongitude);
        System.out.println("내 위치 :" + mlatitude + ", " + mlongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ABC, 14.0f));
        clusterManager = new ClusterManager<>(mapFragment, mMap);

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {
                checkp();
                TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                if(tab3 != null) {
                    tabLayout.removeTabAt(3);
                }
                LatLng point = new LatLng(mlatitude, mlongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
                return true;
            }
        });
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng latLng = new LatLng(mlatitude, mlongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }
        });
        clusterManager.setRenderer(new OwnIconRendered(mapFragment.getApplicationContext(), mMap, clusterManager));
        clusterManager.setAnimation(true);
        mMap.setOnCameraIdleListener(clusterManager);
        for (int i = 0; i < Postc.count; i++) {
            MyItem ParkingItem = new MyItem(All_Parkinglot[i][2], All_Parkinglot[i][3], All_Parkinglot[i][0], All_Parkinglot[i][1]);
            clusterManager.addItem(ParkingItem);
        }
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {TabLayout.Tab tab3 = tabLayout.getTabAt(3);
                if(tab3 != null) {
                    tabLayout.removeTabAt(3);
                }

                imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
                LatLng latLng = new LatLng(item.getPosition().latitude, item.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                //mainActivity.GetMyLocation();
                mlatitude = MainActivity.mlat;
                mlongitude = MainActivity.mlon;
                System.out.println("내 위치 :" + mlatitude + ", " + mlongitude);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
                tab1_layout.setVisibility(View.VISIBLE);
                tab1_text.setVisibility(View.GONE);
                ParkingID = MyItem.returnID();
                final String ssid = item.getTitle();
                System.out.println(ssid);
                System.out.println("주소 : "+ getAddress(item.getPosition().latitude, item.getPosition().longitude));
                getData_pick(ssid, getAddress(item.getPosition().latitude, item.getPosition().longitude),
                        item.getPrice(), getDistance(mlatitude, mlongitude, item.getPosition()));
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            }
        });

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                LatLngBounds.Builder builder_c = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder_c.include(item.getPosition());
                }
                LatLngBounds bounds_c = builder_c.build();
                float zoom = mMap.getCameraPosition().zoom - 0.5f;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
                LatLng latLng = new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                return false;
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });
    }

    public boolean setConnectBool() {
        if ((ZoomLevel >= 10.8000 && ZoomLevel <= 11.2000) || (ZoomLevel >= 12.8000 && ZoomLevel <= 13.2000) || (ZoomLevel >= 14.8000 && ZoomLevel <= 15.2000)) {
            Connect_Flag++;
            return true;

        } else {
            Connect_Flag = 0;
            return false;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        imm.hideSoftInputFromWindow(search_edt.getWindowToken(), 0);
        tab1_text.setVisibility(View.VISIBLE);
    }
    public double getDistance(double mlat, double mlng , LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("User");
        locationA.setLatitude(mlat);
        locationA.setLongitude(mlng);
        Location locationB = new Location("Parkinglot");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public LatLng latlngConv(double a, double b){
        LatLng newLatlng = new LatLng(a, b);
        return newLatlng;
    }
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Toast.makeText(mapFragment, "Latitude: " + latitude + "\nLongitude: " + longitude,Toast.LENGTH_LONG ).show();
    }
    public void checkp(){
        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
        if (permissionAccessCoarseLocationApproved) {
            boolean backgroundLocationPermissionApproved =
                    ActivityCompat.checkSelfPermission(MainActivity.getAppContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (backgroundLocationPermissionApproved) {
                // App can access location both in the foreground and in the background.
                // Start your service that doesn't have a foreground service type
                // defined.
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                LocationManager locationManager = (LocationManager) MainActivity.getAppContext().getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                location = locationManager.getLastKnownLocation(locationProvider);
                mlat = location.getLatitude();
                mlon = location.getLongitude();
                System.out.println("내 위치 mlat, mlon :" + mlat + ", " + mlon);

            } else {
                // App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to
                // location in order to function properly. Then, request background
                // location.
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                LocationManager locationManager = (LocationManager) MainActivity.getAppContext().getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                mlat = location.getLatitude();
                mlon = location.getLongitude();
                System.out.println("내 위치 mlat, mlon :" + mlat + ", " + mlon);

            }
        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    },
                    REQUEST_LOCATION);
        }
    }
    public void enableMyLocationBtn(){
        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
        if (permissionAccessCoarseLocationApproved) {
            boolean backgroundLocationPermissionApproved =
                    ActivityCompat.checkSelfPermission(MainActivity.getAppContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (backgroundLocationPermissionApproved) {
                // App can access location both in the foreground and in the background.
                // Start your service that doesn't have a foreground service type
                // defined.
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                mMap.setMyLocationEnabled(true);
                System.out.println("로케이션 버튼");

            } else {
                // App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to
                // location in order to function properly. Then, request background
                // location.
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_LOCATION);
                mMap.setMyLocationEnabled(true);
                System.out.println("로케이션 버튼");

            }
        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    },
                    REQUEST_LOCATION);
        }
    }
}
