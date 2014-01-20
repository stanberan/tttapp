package com.example.trustedtinythings;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class NFCActivity extends FragmentActivity {
	static ScanATagFragment scan = null;
	static String info = "noinfo";
	static String MD5 = null;
	boolean first=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!NfcAdapter.getDefaultAdapter(this).isEnabled()) {
			Toast.makeText(getApplicationContext(),
					"Please Enable NFC before using this app",
					Toast.LENGTH_LONG).show();
			finish();
		}

		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			scan = new ScanATagFragment();
			first=true;
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in,
							android.R.anim.fade_out)
					.add(android.R.id.content, scan).commit();
		}

		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	public void onResume() {
		super.onResume();
	}

	@SuppressLint("NewApi")
	public void handleIntent(Intent i) {
		String action = i.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			Log.e("NFC", "Discovered NFC with NDEF");
			String scheme = i.getScheme();
			if (scheme.equals("http")) {
				Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Ndef ndef = Ndef.get(tag);
				if (ndef == null) {
					// not supported;
					Log.d("Tag:Ndef", "Not supported");
				}
				NdefMessage ndefMessage = ndef.getCachedNdefMessage();
				byte[] rawMessage = ndefMessage.toByteArray();
				byte[] idTag = tag.getId();
				NdefRecord ndefrecord = ndefMessage.getRecords()[0];
				
				String type = new String(ndefrecord.getType());
				byte[] payloadurl = ndefMessage.getRecords()[0].getPayload();
				String urltemp = "http://";
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					try {
						NdefMessage nestedMessage = new NdefMessage(payloadurl);
						byte[] nestedPayload = nestedMessage.getRecords()[0]
								.getPayload();
						for (int s = 1; s < nestedPayload.length; s++) {
							urltemp += (char) payloadurl[s];
						}
					} catch (FormatException e) {
						Toast.makeText(getApplicationContext(),
								"It seems this is not nested",
								Toast.LENGTH_LONG).show();
					}

				}

				String u = ndefrecord.toUri().toString();
				if (u.contains("deps.at")) {
					MD5 = "MD5Hash";
				} else {
					MD5 = Helpers.getMD5(rawMessage, idTag);

				}
				info = "URI JELY:" + u + "\nURI From " + urltemp + "Size:"
						+ ndefMessage.getRecords().length + "\nTYPE:" + type;
				MD5 = Helpers.getMD5(rawMessage, idTag);

			}
		}
	}

}
