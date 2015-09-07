package com.androiddrupal.fbevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.androiddrupal.fbevents.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

/*
 * This is a simple proof of concept experiment to read XML from a website as a background activity
 * and parse it, then return the result in a list for display in the main UI thread. 
 * 
 * The results are "raw", so next experiments will properly format any resulting HTML and images
 * will also be retrieved and displayed. Later experiments will compare performance of various ORMs 
 * (Sugar, GreenDAO, etc) against  "native" Android SQLite approaches to persistent data storage.
 * 
 * The first, real release version of the app will store the resulting data about Czech Facebook events 
 * in a database that will then be updated periodically and that will be searchable according to criteria
 * set by the user in preferences. Future versions will likely include options to register and pay for events.
 * 
 */
		final Button loadButton = (Button) findViewById(R.id.button1);
		loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						HttpClientXMLActivity.class));
			}
		});
		
		
	}
}
