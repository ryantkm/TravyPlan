package com.eventdee.travyplan.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;

import com.eventdee.travyplan.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

public class General {

    private static final String RESTAURANT_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";
    private static final int MAX_IMAGE_NUM = 22;

    public static SimpleDateFormat dateFormat  = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    /**
     * Get a random image.
     */
    public static String getRandomImageUrl(Random random) {
        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        int id = random.nextInt(MAX_IMAGE_NUM) + 1;

        return String.format(Locale.getDefault(), RESTAURANT_URL_FMT, id);
    }

    /**
     * Get a random image from drawables.
     */
    public static Uri getRandomDrawableUrl(Context context) {

        final TypedArray imgs = context.getResources().obtainTypedArray(R.array.random_images_array);
        final Random random = new Random();
        final int rndInt = random.nextInt(imgs.length());
        final int resID = imgs.getResourceId(rndInt, 0);
        Uri imageUri = null;

        try {
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + context.getResources().getResourcePackageName(resID)
                    + '/' + context.getResources().getResourceTypeName(resID) + '/' + context.getResources().getResourceEntryName(resID) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUri;
    }
}
