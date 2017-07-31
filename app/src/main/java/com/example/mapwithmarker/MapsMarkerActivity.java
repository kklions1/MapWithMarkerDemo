package com.example.mapwithmarker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap myMap;
    private Marker currentMarker;
    private EditText markerDescriptionEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        final LatLng seattle = new LatLng(46.6062, -122.3321);

        myMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));
        currentMarker = myMap.addMarker(new MarkerOptions().position(seattle)
            .title("Seattle Marker")
            .draggable(true));

        myMap.setOnMapLongClickListener(this);
        myMap.setOnMarkerClickListener(this);
    }



    @Override
    public void onMapLongClick(LatLng point) {

        currentMarker = myMap.addMarker(new MarkerOptions().position(point)
                    .draggable(true)
                    .title("")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View markerCreateDialog = inflater.inflate(R.layout.marker_create_dialog, null);


        alertDialog.setView(markerCreateDialog);
        markerDescriptionEditor = (EditText) markerCreateDialog.findViewById(R.id.create_marker_description);

        alertDialog.setPositiveButton(R.string.button_positive_okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // FIXME causes crash
                // TODO Make it so the changes from the dialog box are saved to the marker
                currentMarker.setTitle(markerDescriptionEditor.getText().toString());
                dialogInterface.cancel();
            }
        });

        alertDialog.setNegativeButton(R.string.button_negative_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO Delete the selected marker
                currentMarker.remove();
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View textEntryView = inflater.inflate(R.layout.marker_options_dialog, null);

        builder.setView(textEntryView);


        markerDescriptionEditor = (EditText) textEntryView.findViewById(R.id.marker_new_description);

        if(marker.getTitle().isEmpty())
            markerDescriptionEditor.setHint(R.string.marker_set_description_hint);
        else
            markerDescriptionEditor.setHint(marker.getTitle());


        currentMarker = marker;

        builder.setPositiveButton(R.string.button_positive_okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // FIXME causes crash
                // TODO make this edit the text of the title
                currentMarker.setTitle(markerDescriptionEditor.getText().toString());
                dialogInterface.cancel();
            }
        });

        builder.setNegativeButton(R.string.button_negative_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                currentMarker.remove();
                dialogInterface.cancel();
            }
        });

        builder.setNeutralButton(R.string.button_neutral_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });

        builder.show();

        return true;

    }


}
