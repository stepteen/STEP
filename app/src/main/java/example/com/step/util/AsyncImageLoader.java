package example.com.step.util;

/**
 * Created by qinghua on 2016/12/1.
 */
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
public class AsyncImageLoader {
    private HashMap<String, SoftReference<Bitmap>> imageCache;

    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }

    public Bitmap loadDrawable(final String imageUrl,
                                 final ImageCallback imageCallback) {

        if (imageCache.containsKey(imageUrl)) {

            SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
            Bitmap bitmap = softReference.get();

            if (bitmap != null) {

                return bitmap;

            }
        }

        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);

            }
        };

        new Thread() {

            @Override
            public void run() {

                Bitmap bitmap = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                Message message = handler.obtainMessage(0, bitmap);
                handler.sendMessage(message);

            }

        }.start();

        return null;
    }

    public static Bitmap loadImageFromUrl(String url) {
        URL m;
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Bitmap btp =BitmapFactory.decodeStream(i,null,options);
        //Drawable d = Drawable.createFromStream(i, "src");
        return btp;
    }

    public interface ImageCallback {
        public void imageLoaded(Bitmap imageDrawable, String imageUrl);
    }

}
