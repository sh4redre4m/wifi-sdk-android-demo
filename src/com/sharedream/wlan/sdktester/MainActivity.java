package com.sharedream.wlan.sdktester;

//import com.hicapt.wlan.sdktester.R;
import org.json.JSONObject;

import com.sharedream.cmccwifi.test.R;
import com.sharedream.wlan.sdk.api.WLANSDKManager;
import com.sharedream.wlan.sdk.api.WLANSDKManager.CurrentStatus;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Result;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Role;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Status;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button buttonRegister = null;
	private Button buttonOnline = null;
	private Button buttonOffline = null;
	private Button buttonQuery = null;
	private Button buttonStart = null;
	private Button buttonStop = null;
	private boolean serviceMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonRegister = (Button)findViewById(R.id.buttonRegister);
		buttonOnline = (Button)findViewById(R.id.buttonOnline);
		buttonOffline = (Button)findViewById(R.id.buttonOffline);
		buttonQuery = (Button)findViewById(R.id.buttonQuery);
		buttonStart = (Button)findViewById(R.id.buttonStart);
		buttonStop = (Button)findViewById(R.id.buttonStop);
		buttonOnline.setEnabled(true);
		buttonOffline.setEnabled(false);
		buttonQuery.setEnabled(false);
		serviceMode = false;
		
		if (OnlineService.isRunning()) {
			serviceMode = true;
				buttonRegister.setEnabled(false);
			} else {
				serviceMode = false;
		}
		
		final WLANSDKManager.ReachabilityCallback reachabilityCallback = new WLANSDKManager.ReachabilityCallback() {
			public void accessPointAvailableNotification(Status status) {
				Toast.makeText(MainActivity.this, "Status: " + status, Toast.LENGTH_SHORT).show();
				if (status == Status.Available) {
					buttonOnline.setEnabled(true);
				} else if (status == Status.NotAvailable){
					buttonOnline.setEnabled(false);
				}
			}
		};
		
		buttonRegister.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				JSONObject extraData = new JSONObject();
				try {
					extraData.put("uid", "s09df82h3lgs");
				} catch (Exception e) {
				}
				WLANSDKManager.registerApp(MainActivity.this, Role.TimePlan, "f3eda650ccf2d14793c688c88836d8c6"/*"16f47e60abb1b6ff4912e6c7bc7dc631"/*"f3eda650ccf2d14793c688c88836d8c6"/*"e5714771dadf4fb60fde66bbe950ba66"/*"e625e5aa9f6b039b2da6fef82e3d0a09"*/, extraData, reachabilityCallback, new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						serviceMode = false;
						buttonStart.setEnabled(false);
						buttonStop.setEnabled(false);
						Toast.makeText(MainActivity.this, "register result: " + result, Toast.LENGTH_SHORT).show();
						if (result == Result.Success) {
							buttonRegister.setEnabled(false);
							buttonQuery.setEnabled(true);
						}
					}
				});
			}
		});

		buttonOnline.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				buttonOnline.setEnabled(false);
				WLANSDKManager.online(new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(MainActivity.this, "online result: " + result, Toast.LENGTH_SHORT).show();
						if (result == Result.Success) {
							buttonOffline.setEnabled(true);
						} else {
							buttonOnline.setEnabled(true);
						}
					}
				});
			}
		});

		buttonOffline.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				buttonOffline.setEnabled(false);
				WLANSDKManager.offline(new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(MainActivity.this, "offline result: " + result, Toast.LENGTH_SHORT).show();
						if (result == Result.Success) {
							buttonOnline.setEnabled(true);
						} else {
							buttonOnline.setEnabled(true);
							buttonOffline.setEnabled(true);
						}
					}
				});
			}
		});

		buttonQuery.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
            	CurrentStatus status = WLANSDKManager.query();
            	if (status == null) {
            		Toast.makeText(MainActivity.this, "Object has been recycled", Toast.LENGTH_SHORT).show();            		
            	}
            	
            	if (status.isOnline) {
            		Toast.makeText(MainActivity.this, "Online: Yes " + "TimeConsumed: " + status.timeConsumed + ", FlowConsumed: " + status.flowConsumed + ", Version: " + status.version, Toast.LENGTH_SHORT).show();
            	} else {
            		Toast.makeText(MainActivity.this, "Online: No " + "SSid: " + status.ssid + ", Status: " + status.status + ", Version: " + status.version, Toast.LENGTH_SHORT).show();
            	}
			}
		});
		
		buttonStart.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				serviceMode = true;
				buttonRegister.setEnabled(false);
				startService(new Intent(MainActivity.this, OnlineService.class));
				finish();
			}
		});
		
		buttonStop.setOnClickListener(new Button.OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				stopService(new Intent(MainActivity.this, OnlineService.class));
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!serviceMode) {
			CurrentStatus status = WLANSDKManager.query();
			if (status == null) {
				return;
			}
			if (status.isOnline) {
/*				WLANSDKManager.offline(new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(MainActivity.this, "auto offline result: " + result, Toast.LENGTH_SHORT).show();
					}
				});*/
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!serviceMode) {
			CurrentStatus status = WLANSDKManager.query();
			if (status == null) {
				return;
			}
			buttonQuery.setEnabled(true);

			if (status.isOnline) {
				buttonOnline.setEnabled(false);
				buttonOffline.setEnabled(true);
			} else {
				buttonOffline.setEnabled(false);
				if (status.status == Status.Available) {
					buttonOnline.setEnabled(true);
				} else {
					buttonOnline.setEnabled(false);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CurrentStatus status = WLANSDKManager.query();
		if (status == null) {
			return;
		}
		
		if (!serviceMode) {
			/*if (status.isOnline) {
				WLANSDKManager.offline(new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(MainActivity.this, "auto offline result: " + result, Toast.LENGTH_SHORT).show();
							WLANSDKManager.deregisterApp();
					}
				});
			}*/
			
			WLANSDKManager.deregisterApp();
		}
	}
}