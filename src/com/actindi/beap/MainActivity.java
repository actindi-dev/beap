package com.actindi.beap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	// private final BluetoothAdapter.LeScanCallback mLeScanCallback = new
	// BluetoothAdapter.LeScanCallback() {
	// @Override
	// public void onLeScan(final BluetoothDevice device, int rssi,
	// byte[] scanRecord) {
	// // デバイスが検出される度に呼び出されます。
	//
	// // Byte 数 説明
	// // 1 1 ブロック目のバイト数
	// // 2,3 flag
	// // 4 2 ブロック目のバイト数
	// // 5 メーカー固有の AD type データ
	// // 6,7 会社コード(0x004C が Apple の会社コード)
	// // 8 データのタイプ(0×02 が iBeacon)
	// // 9 連なる iBeacon データのバイト数
	// // 10~25 UUID
	// // 26,27 major
	// // 28,29 minor
	// // 30 校正された電波強度(距離を求めるときの基準値、2 の補数
	//
	// // iBeacon の場合 6 byte 目から、 9 byte 目はこの値に固定されている。
	// if (scanRecord.length > 30 && (scanRecord[5] == (byte) 0x4c)
	// && (scanRecord[6] == (byte) 0x00)
	// && (scanRecord[7] == (byte) 0x02)
	// && (scanRecord[8] == (byte) 0x15)) {
	// String uuid = IntToHex2(scanRecord[9] & 0xff)
	// + IntToHex2(scanRecord[10] & 0xff)
	// + IntToHex2(scanRecord[11] & 0xff)
	// + IntToHex2(scanRecord[12] & 0xff) + "-"
	// + IntToHex2(scanRecord[13] & 0xff)
	// + IntToHex2(scanRecord[14] & 0xff) + "-"
	// + IntToHex2(scanRecord[15] & 0xff)
	// + IntToHex2(scanRecord[16] & 0xff) + "-"
	// + IntToHex2(scanRecord[17] & 0xff)
	// + IntToHex2(scanRecord[18] & 0xff) + "-"
	// + IntToHex2(scanRecord[19] & 0xff)
	// + IntToHex2(scanRecord[20] & 0xff)
	// + IntToHex2(scanRecord[21] & 0xff)
	// + IntToHex2(scanRecord[22] & 0xff)
	// + IntToHex2(scanRecord[23] & 0xff)
	// + IntToHex2(scanRecord[24] & 0xff);
	//
	// String major = IntToHex2(scanRecord[25] & 0xff)
	// + IntToHex2(scanRecord[26] & 0xff);
	// String minor = IntToHex2(scanRecord[27] & 0xff)
	// + IntToHex2(scanRecord[28] & 0xff);
	//
	// System.out.println(uuid);
	// System.out.println(major);
	// System.out.println(minor);
	// } else {
	// System.out.println("not iBeacon");
	// }
	//
	// }
	// };

	private static final int BEACON_NEW_RESULT_CODE = 1;

	private final Map<Marker, Beacon> beaconMap = new HashMap<Marker, Beacon>();

	private GoogleMap map;

	private void addBeaconToMap(Beacon beacon) {
		LatLng latLng = new LatLng(beacon.lat, beacon.lng);
		Marker marker = map.addMarker(new MarkerOptions().position(latLng)
				.title(beacon.name));
		beaconMap.put(marker, beacon);
	}

	private float defaultTilt() {
		return 70;
	}

	private float defaultZoom() {
		return 18;
	}

	private void initGps() {
		// /////////////////////////////////
		// GPS
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 位置情報の要求条件を指定する
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 精度を指定する
		criteria.setAltitudeRequired(true); // 標高を取得するかどうか
		criteria.setBearingRequired(true); // 進行方向を取得するかどうか
		criteria.setCostAllowed(false); // 取得費用がかかることを許可するかどうか
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 消費電力量を指定する
		criteria.setSpeedRequired(true); // 速度を取得するかどうか

		// 指定する取得条件でプロバイダーを取得
		String provider = locationManager.getBestProvider(criteria, true);

		// ロケーションリスナーを設定
		locationManager.requestLocationUpdates(provider, // プロバイダー
				1000, // リスナーに通知する最小時間間隔 ミリ秒
				1, // リスナーに通知する最小距離間隔 メートル
				new LocationListener() { // リスナー

					// 位置情報が変更されたときに呼び出される
					@Override
					public void onLocationChanged(Location location) {
						if (location == null || map == null) {
							return;
						}
						StringBuffer sb = new StringBuffer();

						if (location != null) {
							sb.append("緯度：").append(location.getLatitude());
							sb.append("緯度：").append(location.getLongitude());
							sb.append("精度：").append(location.getAccuracy());
							sb.append("標高：").append(location.getAltitude());
							sb.append("時間：").append(location.getTime());
							sb.append("速度：").append(location.getSpeed());
							sb.append("進行方向：").append(location.getBearing());
							sb.append("プロバイダ：").append(location.getProvider());
							Log.d("beap", sb.toString());
						}

						LatLng latLng = new LatLng(location.getLatitude(),
								location.getLongitude());
						CameraPosition.Builder builder = new CameraPosition.Builder();
						builder.target(latLng);
						builder.zoom(defaultZoom());
						builder.bearing(location.getBearing());
						builder.tilt(defaultTilt());
						map.moveCamera(CameraUpdateFactory
								.newCameraPosition(builder.build()));

						// デモデモ
						if (beaconMap.size() > 2) {
							return;
						}
						BitmapDescriptor icon = BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
						map.addMarker(new MarkerOptions().icon(icon).position(
								new LatLng(location.getLatitude(), location
										.getLongitude())));
					}

					// プロバイダが利用不可になった時に呼び出される
					@Override
					public void onProviderDisabled(String provider) {
					}

					// プロバイダが利用可能になった時に呼び出される
					@Override
					public void onProviderEnabled(String provider) {
					}

					// プロバイダ・ステータスが変更された時に呼び出される
					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}
				});
	}

	private void initialize() {
		initMap();
		initGps();
		// initBluetooth();
	}

	// @SuppressWarnings("deprecation")
	// private void initBluetooth() {
	// BluetoothManager bluetoothManager = (BluetoothManager)
	// getSystemService(Context.BLUETOOTH_SERVICE);
	// BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
	// if (bluetoothAdapter != null) {
	// bluetoothAdapter.startLeScan(mLeScanCallback);
	// }
	// }

	private void initMap() {
		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		map = fragment.getMap();
		if (map == null) {
			return;
		}

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation("gps");
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraPosition.Builder builder = new CameraPosition.Builder();
		builder.target(latLng);
		builder.zoom(defaultZoom());
		builder.bearing(location.getBearing());
		builder.tilt(defaultTilt());
		map.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

		if (beaconMap.isEmpty()) {
			Beacon beacon = new Beacon("コーヒークーポン", "いこーよクーポン",
					"http://iko-yo.net/apps/coupon", Arrays.asList("コーヒー",
							"クーポン"), latLng.latitude + 0.0002,
					latLng.longitude - 0.0002);

			addBeaconToMap(beacon);

			beacon = new Beacon("スーパー特売情報", "スパー情報",
					"http://super.example.com", Arrays.asList("スーパー", "特売"),
					latLng.latitude - 0.0008, latLng.longitude + 0.0003);
			addBeaconToMap(beacon);
		}

		// // 未登録ビーコン
		// BitmapDescriptor icon = BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
		// map.addMarker(new MarkerOptions().icon(icon).position(
		// new LatLng(latLng.latitude + 0.001, latLng.longitude - 0.0004)));

		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latLng) {
				Intent intent = new Intent(getApplicationContext(),
						BeaconNewActivity.class);
				Beacon beacon = new Beacon();
				beacon.lat = latLng.latitude;
				beacon.lng = latLng.longitude;
				intent.putExtra(Beacon.class.getName(), beacon);
				startActivityForResult(intent, BEACON_NEW_RESULT_CODE);
			}
		});

		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Beacon beacon = beaconMap.get(marker);
				if (beacon == null) {
					Intent intent = new Intent(getApplicationContext(),
							BeaconNewActivity.class);
					beacon = new Beacon();
					beacon.lat = marker.getPosition().latitude;
					beacon.lng = marker.getPosition().longitude;
					intent.putExtra(Beacon.class.getName(), beacon);
					startActivityForResult(intent, BEACON_NEW_RESULT_CODE);

					marker.remove();

					return false;
				}

				Intent intent = new Intent(getApplicationContext(),
						BeaconShowActivity.class);
				intent.putExtra(Beacon.class.getName(), beacon);
				startActivity(intent);

				return false;
			}
		});
	}

	// int データを 2桁16進数に変換するメソッド
	@SuppressLint("DefaultLocale")
	public String IntToHex2(int i) {
		char hex_2[] = { Character.forDigit((i >> 4) & 0x0f, 16),
				Character.forDigit(i & 0x0f, 16) };
		String hex_2_str = new String(hex_2);
		return hex_2_str.toUpperCase();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BEACON_NEW_RESULT_CODE:
			Beacon beacon = (Beacon) data.getSerializableExtra(Beacon.class
					.getName());
			addBeaconToMap(beacon);
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}
}
