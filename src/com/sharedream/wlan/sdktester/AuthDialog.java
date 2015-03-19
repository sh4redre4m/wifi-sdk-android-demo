package com.sharedream.wlan.sdktester;

//import com.hicapt.wlan.sdktester.R;
import com.sharedream.cmccwifi.test.R;
import com.sharedream.wlan.sdk.api.WLANSDKManager;
import com.sharedream.wlan.sdk.api.WLANSDKManager.CurrentStatus;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Result;
import com.sharedream.wlan.sdk.api.WLANSDKManager.Status;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AuthDialog extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_dialog);
		TextView text_view = (TextView)findViewById(R.id.text);
		Button buttonOnline = (Button) findViewById(R.id.buttonOnline);
		Button buttonOffline = (Button) findViewById(R.id.buttonOffline);
		
		CurrentStatus status = WLANSDKManager.query();
		if (status == null) {
			return;
		}
		
		if (status.isOnline) {
			//buttonOnline.setEnabled(false);
			buttonOnline.setEnabled(true);
			buttonOffline.setEnabled(true);
			text_view.setText("timeConsumed: " + status.timeConsumed + ", flowConsumed: " + status.flowConsumed + ", versison: " + status.version);
		} else {
			buttonOffline.setEnabled(false);
			if (status.status == Status.Available) {
				buttonOnline.setEnabled(true);
			} else {
				buttonOnline.setEnabled(false);
			}
			text_view.setText("ssid: " + status.ssid + ", status: " + status.status + ", version: " + status.version);
		}
		
		buttonOnline.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				WLANSDKManager.online(4 * 60, 0, new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(AuthDialog.this, "online result: " + result, Toast.LENGTH_SHORT).show();
					}
				});
				finish();
			}
		});
		buttonOffline.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				WLANSDKManager.offline(new WLANSDKManager.AsyncActionResult() {
					@Override
					public void handleResult(Result result) {
						Toast.makeText(AuthDialog.this, "offline result: " + result, Toast.LENGTH_SHORT).show();
					}
				});
				finish();
			}
		});
	}
}