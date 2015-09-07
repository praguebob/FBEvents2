package com.androiddrupal.fbevents;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.androiddrupal.fbevents.R;

/*
 * Creates an async task that reads XML from a URL in the background,
 * invokes a XML parser to handle the resulting document, and returns a list
 * of the individual "rows" of event data that will be displayed in a "raw"
 * form in the main UI thread.
 * 
 */

public class HttpClientXMLActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new HttpGetTask().execute();
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

		// Initialize the constant for the URL from which to read the XML with Facebook events
		
		private static final String URL = "http://events.devmates.com/mobile-app.xml";

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(Void... params) {

			// Retrieve the XML document from the website
			HttpGet request = new HttpGet(URL);
			
			// Parse the XML document
			XMLResponseHandler responseHandler = new XMLResponseHandler();
			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			if (null != mClient)
				mClient.close();

			// Set up to display the resulting list of parsed XML elements (Facebook events)
			
			setListAdapter(new ArrayAdapter<String>(
					HttpClientXMLActivity.this,
					R.layout.list_item, result));
		}
	}
}