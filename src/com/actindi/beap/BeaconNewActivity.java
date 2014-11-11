package com.actindi.beap;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BeaconNewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_new);

		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	private void initialize() {
		Button button = (Button) findViewById(R.id.buttonGo);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				Beacon beacon = (Beacon) intent
						.getSerializableExtra(Beacon.class.getName());
				EditText editText;
				editText = (EditText) findViewById(R.id.editName);
				beacon.name = editText.getText().toString();
				beacon.appName = ((EditText) findViewById(R.id.editAppName))
						.getText().toString();
				beacon.appUrl = ((EditText) findViewById(R.id.editAppUrl))
						.getText().toString();
				String s = ((EditText) findViewById(R.id.editTags)).getText()
						.toString();
				beacon.tags = Arrays.asList(s.split("[, ]+"));

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beacon_new, menu);
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
