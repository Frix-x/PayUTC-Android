package fr.utc.frixx.payutc_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by frixx on 20/06/15.
 */
public class AsyncImgDownload extends AsyncTask<String, Void, Bitmap> {

    ImageView mAvatar;

    public AsyncImgDownload(ImageView mAvatar) {
        this.mAvatar = mAvatar;
    }


    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap img = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            img = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return img;
    }


    protected void onPostExecute(Bitmap result) {
        mAvatar.setImageBitmap(result);
    }
}
