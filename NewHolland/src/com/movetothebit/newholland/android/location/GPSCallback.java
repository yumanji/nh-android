package com.movetothebit.newholland.android.location;

import android.location.Location;

public interface GPSCallback
{
	public abstract void onGPSUpdate(Location location);
	public abstract void onGPSTimeout();
}
