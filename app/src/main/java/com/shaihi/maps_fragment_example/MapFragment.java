package com.shaihi.maps_fragment_example;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {


    private static final String LAT_KEY = "LAT";
    private static final String LON_KEY = "LON";

    private GeoPoint location;
    private MapView map;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(Long longtitude, Long latitude) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(LON_KEY, String.valueOf(longtitude));
        args.putString(LAT_KEY, String.valueOf(latitude));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = new GeoPoint(getArguments().getLong(LAT_KEY), getArguments().getLong(LON_KEY));
        }
        Configuration.getInstance().load(getActivity(), getActivity().getPreferences(Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);

        // Setup the map
        map.getZoomController().setVisibility(
                CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);

        // Add a marker at the parsed location
        if (getArguments() != null) {
            Bundle args = getArguments();
            String latitude = args.getString(LAT_KEY);
            String longtitude = args.getString(LON_KEY);
            location = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longtitude));
        }
        addMarker(location);
        map.invalidate();

        return view;
    }

    private void addMarker(GeoPoint point) {
        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.getController().setZoom(17D);
        map.getController().setCenter(point);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); // needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}