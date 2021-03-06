package com.actindi.beap;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BeaconShowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_show);

		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	private void initialize() {
		Beacon beacon = (Beacon) getIntent().getSerializableExtra(
				Beacon.class.getName());
		TextView textView = (TextView) findViewById(R.id.name);
		textView.setText(beacon.name);
		textView = (TextView) findViewById(R.id.appName);
		textView.setText(beacon.appName);
		textView = (TextView) findViewById(R.id.appUrl);
		textView.setText(beacon.appUrl);
		textView = (TextView) findViewById(R.id.tags);
		textView.setText(TextUtils.join(" ", beacon.tags));

		View button = findViewById(R.id.ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beacon_show, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
