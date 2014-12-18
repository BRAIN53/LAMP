package sz.lamp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetMessages {

	private String email;
	private String pass;
	private Map<String, Object> map;
	private Context mContext;
	private String result = "";
	HttpPost httpPost = null;
	List<NameValuePair> params = null;
	HttpResponse httpResponse;

	public GetMessages(Context mContext) {
		this.mContext = mContext;
	}

	public JSONObject getAllLights(String username) {
		String h = "api/<username>/lights";
		h = h.replace("<username>", username);
		String url = "http://shuncom.3322.org:12350/yii/shuncom/index.php/" + h;
		JSONObject json = null;
		try {
			URI uri = new URI(url);
			HttpGet httpGet = new HttpGet(uri);

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				int bufferSize = 1024 * 10;
				byte[] buffer = new byte[bufferSize];
				int len;
				InputStream input = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						input, "UTF-8"));
				String line = "";
				while ((line = br.readLine()) != null) {
					result = result + line;
				}
				System.out.println(result);
				// result=EntityUtils.toString(response.getEntity());
				json = new JSONObject(result);

			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
