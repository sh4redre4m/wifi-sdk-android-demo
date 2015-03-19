package com.sharedream.wlan.sdktester;

import org.json.JSONObject;

import com.sharedream.wlan.sdk.api.WLANSDKManager;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Result;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Role;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Status;
import com.sharedream.wlan.sdk.manager.SDKManager;
import com.sharedream.wlan.sdk.session.SessionManager;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class AppContext extends Application { 
    private static AppContext instance;
    private static final String MESSAGE_TAG = "WLANSDK";
    public static AppContext getInstance() {
    	return instance; 
    }

	final WLANSDKManager.ReachabilityCallback reachabilityCallback = new WLANSDKManager.ReachabilityCallback() {
		public void accessPointAvailableNotification(Status status) {
			Toast.makeText(instance, "Status: " + status, Toast.LENGTH_SHORT).show();
			if (status == Status.Available) {
			} else if (status == Status.NotAvailable){
			}
		}
	};
    
	@Override 
	public void onCreate() {
		super.onCreate();
	    instance = this;
	    initSingletons();
	}
    
	protected void initSingletons() {
		try {
		JSONObject extraData = new JSONObject();
		extraData.put("uid", "1212341234dfad");
		WLANSDKManager.registerApp(instance, Role.TimePlan, "16f47e60abb1b6ff4912e6c7bc7dc631"/*"f3eda650ccf2d14793c688c88836d8c6"/*"e5714771dadf4fb60fde66bbe950ba66"/*"e625e5aa9f6b039b2da6fef82e3d0a09"*/, extraData, reachabilityCallback, new WLANSDKManager.AsyncActionResult() {
			@Override
			public void handleResult(Result result) {
				Toast.makeText(instance, "register result: " + result, Toast.LENGTH_SHORT).show();
			}
		});

//		SDKManager.getInstance();
//		SessionManager.getInstance();
		Log.d(MESSAGE_TAG, "Sigletons have been intialized.");
		} catch (Exception e) {
			
		}
	}
}