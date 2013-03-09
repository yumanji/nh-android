package com.movetothebit.newholland.android.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GPSManager
{
	private static final int gpsMinTime = 500;
	private static final int gpsMinDistance = 0;
	
	private LocationManager locationManager = null;
	private LocationListener locationListenerGPS = null;
	private LocationListener locationListenerNW = null;
	
	private Handler handler = new Handler();
	
	private GPSCallback gpsCallback = null;
	
	private boolean flagGPSEnable = true;
	private boolean flagNetworkEnable = true;

	private boolean locationObtained;
	
	public GPSManager() {
		locationListenerGPS = new LocationListener() {
			public void onProviderDisabled(final String provider) {
			}
			
			public void onProviderEnabled(final String provider) {
			}
			
			public void onStatusChanged(final String provider, final int status, final Bundle extras) {
			}
			
			public void onLocationChanged(final Location location) {
				if (location != null && gpsCallback != null) {
					locationObtained = true;
					
					gpsCallback.onGPSUpdate(location);
				}
			}
		};
		locationListenerNW = new LocationListener() {
			public void onProviderDisabled(final String provider) {
			}
			
			public void onProviderEnabled(final String provider) {
			}
			
			public void onStatusChanged(final String provider, final int status, final Bundle extras) {
			}
			
			public void onLocationChanged(final Location location){
				if (location != null && gpsCallback != null) {
					locationObtained = true;					
					gpsCallback.onGPSUpdate(location);
				}
			}
		};
	}
	
	public void startListening(final Activity activity) {
		startListening( activity, 0 );
	}
	
	public void startListening(final Activity activity, long timeOut) {
		locationObtained = false;
		
		if ( timeOut > 0 ) handler.postDelayed( timeOutController, timeOut);
		
		if (locationManager == null)
			locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		flagGPSEnable = locationManager.isProviderEnabled("gps");
		flagNetworkEnable = locationManager.isProviderEnabled("network");

		if ( flagGPSEnable )
			locationManager.requestLocationUpdates("gps", GPSManager.gpsMinTime, GPSManager.gpsMinDistance, locationListenerGPS);
		
		if ( flagNetworkEnable )
			locationManager.requestLocationUpdates("network", GPSManager.gpsMinTime, GPSManager.gpsMinDistance,	locationListenerNW);
	}
	
	public void stopListening() {
		try {
			if (locationManager != null && locationListenerGPS != null)
				locationManager.removeUpdates(locationListenerGPS);
			
			if (locationManager != null && locationListenerNW != null)
				locationManager.removeUpdates(locationListenerNW);

			locationManager = null;
		} catch (final Exception ex) {
			
		}
	}
	
	public void setGPSCallback(final GPSCallback gpsCallback){
		this.gpsCallback = gpsCallback;
	}
	
	public GPSCallback getGPSCallback(){
		return gpsCallback;
	}
	
	public boolean isGPSenabled( final Activity activity ) {	
    	final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);	
    	return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

	private Runnable timeOutController = new Runnable() {
		public void run() {
			if (!locationObtained ) {
				stopListening();
				gpsCallback.onGPSTimeout( );
			}
		}
	};
	
	public static Location geopoint2Location( GeoPoint geo ) {
		Location loc = new Location("");
		loc.setLatitude( geo.getLatitudeE6() / 1E6 );
		loc.setLongitude(geo.getLongitudeE6() / 1E6);
		
		return loc;
	}
	public static Double coordinateE6toNormal(Double coordinate) {		
		return coordinate/1E6;
	}
	
	public static Double coordinateToE6(Double coordinate) {		
		return coordinate*1E6;
	}
	public static Location geopoint2Location( Integer latitudeE6, Integer longitudeE6 ) {
		Location loc = new Location("");
		loc.setLatitude( latitudeE6 / 1E6 );
		loc.setLongitude( longitudeE6 / 1E6);
		
		return loc;
	}

	public static GeoPoint location2GeoPoint( Location loc ) {
		GeoPoint geoPoint = new GeoPoint( new Double(loc.getLatitude() * 1E6).intValue(), new Double( loc.getLongitude() * 1E6 ).intValue() );
		
		return geoPoint;
	}

	public static GeoPoint location2GeoPoint( Double latitude, Double longitude ) {
		GeoPoint geoPoint = new GeoPoint( new Double(latitude * 1E6).intValue(), new Double(longitude * 1E6).intValue() );
		
		return geoPoint;
	}

	public static Location coordinates2Location( Double latitude, Double longitude ) {
		Location loc = new Location("");
		loc.setLatitude(latitude);
		loc.setLongitude( longitude );
		
		return loc;
	}
	/**
	 * @param address
	 * @return
	 */
	public static Address getLocationFromAddress(Context ctx, String address){
		
		Location loc = null;		
		Geocoder  geocoder =null;
		List<Address> addresses = null;
		Address returnedAddress=null;
		
		try {
			
			geocoder = new Geocoder(ctx, new Locale("es", "ES"));
			addresses = geocoder.getFromLocationName(address, 1);
			
			if(addresses != null) {
				   returnedAddress = addresses.get(0);
//				   StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
//				   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
//				    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//				   }
			}else{
				Toast.makeText(ctx	, "no ha devuelto ninguna localizacion para esa direccion", Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return returnedAddress; 
	}
	public static void zoomToEnglobePoints( MapView map, List<GeoPoint> listPoints, boolean centerInMiddle ) {
		if ( listPoints != null ) { 
			int minLatitude = Integer.MAX_VALUE;
			int maxLatitude = Integer.MIN_VALUE;
			int minLongitude = Integer.MAX_VALUE;
			int maxLongitude = Integer.MIN_VALUE;
	
			for (GeoPoint item : listPoints ) { 
				int lat = item.getLatitudeE6();
				int lon = item.getLongitudeE6();
	
				maxLatitude = Math.max(lat, maxLatitude);
				minLatitude = Math.min(lat, minLatitude);
				maxLongitude = Math.max(lon, maxLongitude);
				minLongitude = Math.min(lon, minLongitude);
			}
			
			if ( centerInMiddle ) {
				GeoPoint centerGeoPoint = new GeoPoint(( maxLatitude + minLatitude ) / 2, (minLongitude + maxLongitude) / 2 );
				map.getController().animateTo( centerGeoPoint );
			}
			map.getController().zoomToSpan(Math.abs(maxLatitude - minLatitude), Math.abs(maxLongitude - minLongitude));
			map.getController().setZoom( map.getZoomLevel() - 1);
			map.invalidate();		
		}
	}
}