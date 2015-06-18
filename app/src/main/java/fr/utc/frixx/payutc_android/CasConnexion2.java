package fr.utc.frixx.payutc_android;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CasConnexion2 {

    public String mkRequest(String... params){
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        HttpPost httppost = new HttpPost("https://cas.utc.fr/cas/v1/tickets/"+params[0]);
        nameValuePairs.add(new BasicNameValuePair("service", params[1]));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response;
            response = httpclient.execute(httppost);

            String rep = EntityUtils.toString(response.getEntity());
            return rep;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
