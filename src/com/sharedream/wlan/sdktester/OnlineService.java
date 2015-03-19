package com.sharedream.wlan.sdktester;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;


import com.sharedream.cmccwifi.test.R;
import com.sharedream.wlan.sdk.api.WLANSDKManager;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Result;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Role;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Status;

import android.app.Notification;
import android.app.PendingIntent;

public class OnlineService extends Service {
		private static boolean isRunning = false;
		private static boolean DEVELOPER_MODE = true;
		public static boolean isRunning() {
			return isRunning;
		}
		private final WLANSDKManager.ReachabilityCallback reachabilityCallback = new WLANSDKManager.ReachabilityCallback() {
		public void accessPointAvailableNotification(Status status) {
			showNotification("Status: " + status);
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		WLANSDKManager.registerApp(OnlineService.this, Role.TimePlan, "f3eda650ccf2d14793c688c88836d8c6"/*"16f47e60abb1b6ff4912e6c7bc7dc631"/*"f3eda650ccf2d14793c688c88836d8c6"/*"e5714771dadf4fb60fde66bbe950ba66"/*"e625e5aa9f6b039b2da6fef82e3d0a09"*/, reachabilityCallback, new WLANSDKManager.AsyncActionResult() {
			@Override
			public void handleResult(Result result) {
				showNotification("registerApp Result: " + result);
			}
		});
		isRunning = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		if (DEVELOPER_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		}
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
/*		CurrentStatus status = WLANSDKManager.query();
		if (status.isOnline) {
			WLANSDKManager.offline(new WLANSDKManager.AsyncActionResult() {
				@Override
				public void handleResult(Result result) {
						WLANSDKManager.deregisterApp();
				}
			});
		}*/
		
		WLANSDKManager.deregisterApp();
	}

	@SuppressWarnings("deprecation")
	private void showNotification(String text) {
		Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AuthDialog.class), 0);
		notification.setLatestEventInfo(this, getString(R.string.app_name), text, contentIntent);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.defaults |= Notification.DEFAULT_SOUND;
		startForeground(1, notification);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
