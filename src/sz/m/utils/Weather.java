package sz.m.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Weather {

	private String country = "country";
	private String temperature = "temperature";
	private String humidity = "humidity";
	private String pressure = "pressure";
	private String city = "city";
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;

	public Weather(String url) {
		this.urlString = url;
	}

	public Weather(String city, int mode) {
		String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
		String url2 = "&mode=xml";
		String url = "lat=31&lon=121";
		String finalUrl = url1 + city + url2;
		this.urlString = finalUrl;
	}

	public Weather(double Longitude, double Latitude) {
		String url1 = "http://api.openweathermap.org/data/2.5/weather?";
		String url2 = "&mode=xml";
		String url = "lat=" + Latitude + "&lon=" + Longitude;
		String finalUrl = url1 + url;
		this.urlString = finalUrl;
	}

	public String getCountry() {
		return country;
	}

	public String getTemperature() {
		return temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public String getPressure() {
		return pressure;
	}

	public String getCity() {
		return city;
	}

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		int event;
		String text = null;
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("country")) {
						country = text;
					} else if (name.equals("humidity")) {
						humidity = myParser.getAttributeValue(null, "value");
					} else if (name.equals("pressure")) {
						pressure = myParser.getAttributeValue(null, "value");
					} else if (name.equals("temperature")) {
						temperature = myParser.getAttributeValue(null, "value");
					} else if (name.equals("name")) {
						city = myParser.getAttributeValue(null, "value");
					}
					break;
				}
				event = myParser.next();

			}
			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					InputStream stream = conn.getInputStream();

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(
							XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);
					parseXMLAndStoreIt(myparser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

	public void fetchJSON() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					InputStream stream = conn.getInputStream();

					byte[] buffer = Tools.readResource(stream);
					String jsonString = new String(buffer);
					JSONObject json = new JSONObject(jsonString);
					JSONArray weather = json.getJSONArray("weather");
					JSONObject jo = weather.getJSONObject(0);
					city = json.getString("name");
					JSONObject jomain = json.getJSONObject("main");
					temperature = jomain.getString("temp");
					humidity = jomain.getString("humidity");
					pressure = jomain.getString("pressure");
					JSONObject josys = json.getJSONObject("sys");
					country = josys.getString("country");
					parsingComplete = false;
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

}