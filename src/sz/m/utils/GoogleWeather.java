package sz.m.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml.Encoding;

public class GoogleWeather {
	private Integer Latitude;// 纬度
	private Integer Longitude;// 经度
	private String url = null;
	private String sTemp;// 温度

	public GoogleWeather(Integer longi, Integer lati) {
		Latitude = lati;
		Longitude = longi;

		// http://www.google.com/ig/api?hl=zh-cn&weather=,,,30670000,104019996
		url = "http://www.google.com/ig/api?hl=en&weather=,,," + Longitude
				+ "," + Latitude;
		Log.d("log", "url=" + url);
	}

	public void getWeatherData() throws ClientProtocolException, IOException,
			ParserConfigurationException, FactoryConfigurationError,
			SAXException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);

		HttpResponse Response = client.execute(Request);
		HttpEntity Entity = Response.getEntity();

		InputStream stream = Entity.getContent();
		
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        // xpp.setInput( new StringReader ( "<foo>Hello World!</foo>" ) );
	        String temperature;
	        xpp.setInput(stream,null);
	        
	        int event = xpp.getEventType();
	        while (event != XmlPullParser.END_DOCUMENT) 
	        {
	           String name=xpp.getName();
	           switch (event){
	              case XmlPullParser.START_TAG:
	              break;
	              case XmlPullParser.END_TAG:
	              if(name.equals("temperature")){
	                 temperature = xpp.getAttributeValue(null,"value");
	              }
	              break;
	           }		 
	           event = xpp.next(); 					
	        }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		
		
		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();

		Document doc = Builder.parse(new InputSource(stream));
		NodeList n = doc.getElementsByTagName("current_conditions");

		Log.d("log", "Node Length=" + n.getLength());
		for (int i = 0; i < n.getLength(); i++)// 遍列current_condition所有节点
		{
			// 获取节点的天气数据
			sTemp = n.item(i).getChildNodes().item(2).getAttributes().item(0)
					.getNodeValue();
		}
		Log.d("log", "sTemp=" + sTemp);
	}
}
