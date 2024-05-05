package com.shaihi.maps_fragment_example;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {
    static private final String LON_KEY = "LON";
    static private final String LAT_KEY = "LAT";

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance() {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(LON_KEY, "0");
        args.putString(LAT_KEY, "0");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        Button searchBtn = view.findViewById(R.id.searchBtn);
        EditText addressInput = view.findViewById(R.id.etAddress);
        TextView resultText = view.findViewById(R.id.geoResult);
        searchBtn.setOnClickListener(v -> {
            String address = addressInput.getText().toString();
            fetchLocation(address, resultText);
        });
        return view;
    }

    private void fetchLocation(String address, TextView resultText) {
        new Thread(() -> {
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + address.replace(" ", "+") + "&format=json&limit=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    double latitude = jsonObject.getDouble("lat");
                    double longitude = jsonObject.getDouble("lon");

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> resultText.setText("Latitude: " + latitude + ", Longitude: " + longitude));
                        Bundle args = new Bundle();
                        args.putString("LAT", String.valueOf(latitude));
                        args.putString("LON", String.valueOf(longitude));
                        if (getArguments()!= null){getArguments().putAll(args);}
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> resultText.setText("No results found"));
                    }
                }
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> resultText.setText("Error: " + e.getMessage()));
                }
            }
        }).start();
    }
}