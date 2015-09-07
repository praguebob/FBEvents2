package com.androiddrupal.fbevents;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* 
 * This is a simple but effective pull parser for the XML document containing Facebook events 
 * and will be modified to add parsing of event tags when they are later added to the input stream
 */

class XMLResponseHandler implements ResponseHandler<List<String>> {

	// Initialize constants for the various tags that we expect to find in the XML document
	
	private static final String ID_TAG = "ID";
	private static final String TITLE_TAG = "TITLE";
	private static final String ORGANIZER_TAG = "ORGANIZER";
	private static final String CITY_TAG = "CITY";
	private static final String START_TIME_TAG = "START_TIME";
	private static final String END_TIME_TAG = "END_TIME";
	private static final String CATEGORYTEXT_TAG = "CATEGORYTEXT";
	private static final String IMGURL_TAG = "IMGURL";
	private static final String DESCRIPTION_TAG = "DESCRIPTION";
	private String mDescription, mIMGURL, mCategoryText, mEndTime, 
	mStartTime, mCity, mOrganizer, mTitle, mID;
	private boolean mIsParsingDescription, mIsParsingIMGURL, mIsParsingCategoryText, 
	mIsParsingEndTime, mIsParsingStartTime, mIsParsingCity, mIsParsingOrganizer, mIsParsingTitle, mIsParsingID;
	private final List<String> mResults = new ArrayList<String>();

	@Override
	public List<String> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		try {

			// Create the pull parser
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			// Set the parser's input to be the XML document (in the HTTP Response)
			xpp.setInput(new InputStreamReader(response.getEntity()
					.getContent()));
			
			// Get the first parser event and start iterating over the XML document 
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {
					startTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					endTag(xpp.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					text(xpp.getText());
				}
				eventType = xpp.next();
			}

			// We're done parsing the XML and we've created the results list
			return mResults;

		} catch (XmlPullParserException e) {
		}
		return null;
	}

	// Determine where we are in parsing the XML by examining the start tag
	
	public void startTag(String localName) {
		if (localName.equals(ID_TAG)) {
			mIsParsingID = true;
		} else if (localName.equals(TITLE_TAG)) {
			mIsParsingTitle = true;
		} else if (localName.equals(ORGANIZER_TAG)) {
			mIsParsingOrganizer = true;
		} else if (localName.equals(CITY_TAG)) {
			mIsParsingCity = true;
		} else if (localName.equals(START_TIME_TAG)) {
			mIsParsingStartTime = true;
		} else if (localName.equals(END_TIME_TAG)) {
			mIsParsingEndTime = true;
		} else if (localName.equals(CATEGORYTEXT_TAG)) {
			mIsParsingCategoryText = true;
		} else if (localName.equals(IMGURL_TAG)) {
			mIsParsingIMGURL = true;
		} else if (localName.equals(DESCRIPTION_TAG)) {
			mIsParsingDescription = true;
		}
	}

	// Pull out the content for the current XML tag we have parsed
	
	public void text(String text) {
		if (mIsParsingDescription) {
			mDescription = text.trim();
		} else if (mIsParsingIMGURL) {	
			mIMGURL = text.trim();
		} else if (mIsParsingCategoryText) {
			mCategoryText = text.trim();
		} else if (mIsParsingEndTime) {
			mEndTime = text.trim();
		} else if (mIsParsingStartTime) {
			mStartTime = text.trim();
		} else if (mIsParsingCity) {
			mCity = text.trim();
		} else if (mIsParsingOrganizer) {
			mOrganizer = text.trim();
		} else if (mIsParsingTitle) {
			mTitle = text.trim();
		} else if (mIsParsingID) {
			mID = text.trim();
		}
	}

	// Determine where we are in parsing the XML by examining the end tag
	
	public void endTag(String localName) {
		if (localName.equals(ID_TAG)) {
			mIsParsingID = false;
		} else if (localName.equals(TITLE_TAG)) {
			mIsParsingTitle = false;
		} else if (localName.equals(ORGANIZER_TAG)) {
			mIsParsingOrganizer = false;
		} else if (localName.equals(CITY_TAG)) {
			mIsParsingCity = false;
		} else if (localName.equals(START_TIME_TAG)) {
			mIsParsingStartTime = false;
		} else if (localName.equals(END_TIME_TAG)) {
			mIsParsingEndTime = false;
		} else if (localName.equals(CATEGORYTEXT_TAG)) {
			mIsParsingCategoryText = false;
		} else if (localName.equals(IMGURL_TAG)) {
			mIsParsingIMGURL = false;
		} else if (localName.equals(DESCRIPTION_TAG)) {
			mIsParsingDescription = false;
		} else if (localName.equals("EVENT")) {

// Add a single "row" of parsed XML to the results
			
			mResults.add(ID_TAG + ":" + mID + "," +
					TITLE_TAG + ":" + mTitle + "," + 
					ORGANIZER_TAG + ":" + mOrganizer + "," +
					CITY_TAG + ":" + mCity + "," +
					START_TIME_TAG + ":" + mStartTime + "," +
					END_TIME_TAG + ":" + mEndTime + "," +
					CATEGORYTEXT_TAG + ":" + mCategoryText + "," +
					IMGURL_TAG + ":" + mIMGURL + "," + 
					DESCRIPTION_TAG + ":" + mDescription);
			
			// Clean up for parsing another tag
			
			mDescription = null;
			mIMGURL = null;
			mCategoryText = null;
			mEndTime = null;
			mStartTime = null;
			mCity = null;
			mOrganizer = null;
			mTitle = null;
			mID = null;
		}
	}
}