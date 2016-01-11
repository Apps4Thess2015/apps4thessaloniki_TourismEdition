package com.rakir.stavgeo.thesstour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TYPE = "type";
    private GoogleMap mMap;

    private String type;
    private FloatingActionButton fab;
    private HashMap<Marker,String> markers= new HashMap<Marker,String>();
    private String curID="";

    public static MapFragment newInstance(String type) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);


        return fragment;
    }

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View res= inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab=(FloatingActionButton) res.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info=new Intent(getActivity(),Info.class);
                info.putExtra("id",curID);
                startActivity(info);
            }
        });

        return res;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                fab.setVisibility(View.GONE);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                curID=markers.get(marker);
                fab.setVisibility(View.VISIBLE);
                return false;
            }
        });
        LatLng center = new LatLng(40.6264774, 22.9482365);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        String jMarkers[] = getResources().getStringArray(R.array.markers_array);
        for(String json : jMarkers){
            JSONObject jObj = null;
            try {
                jObj = new JSONObject(json);

                if (!type.equals("map")) {
                    if (type.equals(jObj.getString("type"))) {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(
                                getResources().getIdentifier(jObj.getString("icon"), "drawable",
                                        getActivity().getPackageName())
                        );

                        Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(jObj.getDouble("lat"), jObj.getDouble("lon")))
                                .title(jObj.getString("name"))) ;
                        marker.setIcon(icon);
                        markers.put(marker,jObj.getString("id"));
                    }
                } else {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(
                            getResources().getIdentifier(jObj.getString("icon"), "drawable",
                                    getActivity().getPackageName())
                    );

                    Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(jObj.getDouble("lat"), jObj.getDouble("lon")))
                            .title(jObj.getString("name")));
                    marker.setIcon(icon);
                    markers.put(marker,jObj.getString("id"));
                }
            } catch (JSONException e) {
                Log.e("JSE MF L77", e.getMessage() + "!");
            } catch (NullPointerException e) {
                Log.e("NullE MF L79", "!");
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(zoom);
    }

}
