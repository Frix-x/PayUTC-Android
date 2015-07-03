package fr.utc.frixx.payutc_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequests {

    static HashMap<String, String> cookies = new HashMap<String, String>();

    public String casAuth1(String u, String p){
        HashMap<String, String> data = new HashMap<String, String>(2);
        data.put("username", u);
        data.put("password", p);
        String dataToPost = mkDataToPost(data);

        String rep = null;
        try {
            rep = httpsRequest("https://cas.utc.fr/cas/v1/tickets", dataToPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tgt = null;
        if (rep != null) {
            int index = rep.indexOf("/cas/v1/tickets/");
            tgt = rep.substring(index + "/cas/v1/tickets/".length());
        }
        if (tgt != null) {
            tgt = tgt.split("\"")[0];
        }

        return tgt;
    }

    public String casAuth2(String tgt, String ser){
        HashMap<String, String> data = new HashMap<String, String>(2);
        data.put("service", ser);
        String dataToPost = mkDataToPost(data);

        String rep = null;
        try {
            rep = httpsRequest("https://cas.utc.fr/cas/v1/tickets/" + tgt, dataToPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rep;
    }

    public String casAuth3(String ser, String tick, String sysid, String appkey){
        HashMap<String, String> data = new HashMap<String, String>(2);
        data.put("service", ser);
        data.put("ticket", tick);
        data.put("system_id", sysid);
        data.put("app_key", appkey);
        String dataToPost = mkDataToPost(data);

        String rep = null;
        try {
            rep = httpsRequest("https://api.nemopay.net/services/KEY/loginCas2", dataToPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rep;
    }

    protected String mkDataToPost(HashMap<String, String> data) {
        StringBuilder builder = new StringBuilder();

        for(HashMap.Entry<String, String> entry : data.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();

            try {
                builder.append(URLEncoder.encode(cle, "UTF-8")+"="+ URLEncoder.encode(valeur, "UTF-8")+"&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    protected String httpsRequest(String uri, String data) throws IOException {

        URL url = new URL(uri);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
        conn.setRequestProperty("Cookie", getCookiesHeader());
        conn.setUseCaches(false);
        conn.setDoInput(true);

        OutputStream output = conn.getOutputStream();
        output.write(data.getBytes("UTF-8"));

        StringBuilder builder = new StringBuilder();
        InputStreamReader is = null;
        int status = conn.getResponseCode();
        if (status >= 200 && status <= 226)
            is = new InputStreamReader(conn.getInputStream(), "UTF-8");

        BufferedReader in = new BufferedReader(is);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        in.close();
        updateCookies(conn.getHeaderFields().get("Set-Cookie"));
        return builder.toString();
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

}
