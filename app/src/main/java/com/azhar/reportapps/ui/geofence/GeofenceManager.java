package com.azhar.reportapps.ui.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

public class GeofenceManager {
    private Context context;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    public GeofenceManager(Context context) {
        this.context = context;
    }





    public void createGeofence(String geofenceId, double latitude, double longitude, float radius) {
        geofencingClient = LocationServices.getGeofencingClient(context);

        Geofence geofence = new Geofence.Builder()
                .setRequestId(geofenceId)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest geofenceRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Geofence added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to add geofence", Toast.LENGTH_SHORT).show());
    }

    public void removeGeofence() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.removeGeofences(geofencePendingIntent)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Geofence removed", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove geofence", Toast.LENGTH_SHORT).show());
    }
}