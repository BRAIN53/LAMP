package sz.lamp.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;

public class SendMessage {
	private String email;
	private String pass;
	private Map<String, Object> map;
	JSONArray jarray;
	private Context mContext;
	private String result = "";
	HttpPost httpPost = null;
	List<NameValuePair> params = null;
	HttpResponse httpResponse;

	public SendMessage(Context mContext) {
		this.mContext = mContext;
	}

	public JSONArray setLightState(String username, String light_id, State s) {
		String h = "api/<username>/lights/<id>/state";
		h = h.replace("<username>", username).replace("<id>", light_id);

		String url = "http://shuncom.3322.org:12350/yii/shuncom/index.php/" + h;

		httpPost = new HttpPost(url);
		params = new ArrayList<NameValuePair>();
		if (s != null) {
			params.add(new BasicNameValuePair("on", s.on ? "true" : "false"));
			params.add(new BasicNameValuePair("hue", s.hue + ""));
			params.add(new BasicNameValuePair("bri", s.bri + ""));
			params.add(new BasicNameValuePair("sat", s.sat + ""));
		}

		try {

			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = new DefaultHttpClient().execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity());
				jarray = new JSONArray(result);

			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jarray;
	}
}
