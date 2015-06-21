package fr.utc.frixx.payutc_android;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by frixx on 20/06/15.
 */

// TODO : Ebauche à mettre en forme et tester (voir réimplémenter les méthodes d'une meilleure manière)
public class PostRQ /*extends AsyncTask<String, Void, String>*/ {

    /*
    public String uri;
    public String dataToPost;
    static HashMap<String, String> cookies = new HashMap<String, String>();


    public void params(String adresse, String service, HashMap<String, String> data) throws UnsupportedEncodingException {

        uri = adresse + service;
        StringBuilder builder = new StringBuilder();

        for(HashMap.Entry<String, String> entry : data.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();

            builder.append(URLEncoder.encode(cle, "UTF-8")+"="+ URLEncoder.encode(valeur, "UTF-8")+"&");
        }
        dataToPost = builder.toString();
    }

    synchronized String getCookiesHeader() {
        StringBuilder sb = new StringBuilder();
        for (String cookie : cookies.keySet()) {
            sb.append(cookie + "=" + cookies.get(cookie) + "; ");
        }
        return sb.toString();
    }

    synchronized void updateCookies(List<String> cookiesHeader) {
        if (cookiesHeader!=null) {
            for (String cookie : cookiesHeader) {
                try {
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    String cookieName = cookie.substring(0, cookie.indexOf("="));
                    String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    cookies.put(cookieName, cookieValue);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected String doInBackground() throws IOException {

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", "" + Integer.toString(dataToPost.getBytes().length));
        conn.setRequestProperty("Cookie", getCookiesHeader());
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(dataToPost);
        wr.flush();

        StringBuilder builder = new StringBuilder();
        InputStreamReader is;
        if (conn.getResponseCode() == 200) {
            is = new InputStreamReader(conn.getInputStream(), "UTF-8");
        }
        else {
            is = new InputStreamReader(conn.getErrorStream(), "UTF-8");
        }

        BufferedReader in = new BufferedReader(is);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        in.close();
        updateCookies(conn.getHeaderFields().get("Set-Cookie"));
        return builder.toString();
    }


    protected String onPostExecute(String result) {
        // TODO : A voir
    }
    */
}
