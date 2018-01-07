package com.eventdee.travyplan.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;

import com.eventdee.travyplan.R;
import com.google.android.gms.location.places.Place;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class General {

    private static final String RESTAURANT_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";
    private static final int MAX_IMAGE_NUM = 22;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    public static SimpleDateFormat dateFormatPlace = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.US);

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
                    + '/' + context.getResources().getResourceTypeName(resID) + '/' + context.getResources().getResourceEntryName(resID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUri;
    }

    public static String setAndroidType(List<Integer> placeTypes) {

        for (Integer placeType : placeTypes) {
            switch (placeType) {
                case Place.TYPE_AIRPORT:
                    return "Airport";
                case Place.TYPE_TRAIN_STATION:
                    return "Train Station";
                case Place.TYPE_SUBWAY_STATION:
                    return "Subway Station";
                case Place.TYPE_TRANSIT_STATION:
                    return "Subway Station";
                case Place.TYPE_BUS_STATION:
                    return "Bus Station";
                case Place.TYPE_CAR_RENTAL:
                    return "Car Rental";
                case Place.TYPE_TAXI_STAND:
                    return "Taxi Stand";
                case Place.TYPE_RV_PARK:
                    return "Recreational Vehicle Park";
                case Place.TYPE_GAS_STATION:
                    return "Gas Station";
                case Place.TYPE_BICYCLE_STORE:
                    return "Bicycle Store";

                case Place.TYPE_LODGING:
                    return "Accommodation";
                case Place.TYPE_CAMPGROUND:
                    return "Camp Ground";

                case Place.TYPE_CAFE:
                    return "Cafe";
                case Place.TYPE_BAR:
                    return "Bar";
                case Place.TYPE_BAKERY:
                    return "Bakery";
                case Place.TYPE_RESTAURANT:
                    return "Restaurant";
                case Place.TYPE_FOOD:
                    return "Eatery";

                case Place.TYPE_MUSEUM:
                    return "Museum";
                case Place.TYPE_STADIUM:
                    return "Stadium";
                case Place.TYPE_AMUSEMENT_PARK:
                    return "Amusement Park";
                case Place.TYPE_BOWLING_ALLEY:
                    return "Bowling Alley";
                case Place.TYPE_PARK:
                    return "Park";
                case Place.TYPE_SHOPPING_MALL:
                    return "Shopping Mall";
                case Place.TYPE_NIGHT_CLUB:
                    return "Night Club";
                case Place.TYPE_MOVIE_THEATER:
                    return "Theater";
                case Place.TYPE_ART_GALLERY:
                    return "Art Gallery";
                case Place.TYPE_AQUARIUM:
                    return "Aquarium";
                case Place.TYPE_CASINO:
                    return "Casino";

                case Place.TYPE_CHURCH:
                    return "Church";
                case Place.TYPE_HINDU_TEMPLE:
                    return "Hindu Temple";

                case Place.TYPE_HOSPITAL:
                    return "Hospital";
                case Place.TYPE_BANK:
                    return "Bank";

                case Place.TYPE_GYM:
                    return "Gym";
                case Place.TYPE_UNIVERSITY:
                    return "University";
                case Place.TYPE_SCHOOL:
                    return "School";
                case Place.TYPE_LIBRARY:
                    return "Library";
                case Place.TYPE_FIRE_STATION:
                    return "Fire Station";
                case Place.TYPE_CITY_HALL:
                    return "City Hall";
                case Place.TYPE_COURTHOUSE:
                    return "Courthouse";
                case Place.TYPE_EMBASSY:
                    return "Embassy";
                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE:
                    return "Local Government Office";

                case Place.TYPE_TRAVEL_AGENCY:
                    return "Travel Agency";
                case Place.TYPE_DEPARTMENT_STORE:
                    return "Department Store";
                case Place.TYPE_CLOTHING_STORE:
                    return "Clothing Store";
                case Place.TYPE_BOOK_STORE:
                    return "Book Store";
                case Place.TYPE_FLORIST:
                    return "Florist";
                case Place.TYPE_JEWELRY_STORE:
                    return "Jewelry Store";
                case Place.TYPE_LIQUOR_STORE:
                    return "Liquor Store";
                case Place.TYPE_SPA:
                    return "Spa";
                case Place.TYPE_PHARMACY:
                    return "Pharmacy";
                case Place.TYPE_HOME_GOODS_STORE:
                    return "Home Goods Store";
                case Place.TYPE_PET_STORE:
                    return "Pet Store";
                case Place.TYPE_STORE:
                    return "Store";

                case Place.TYPE_POINT_OF_INTEREST:
                    return "Point of Interest";
            }
        }
        return "General";
    }

    public static String getPriceString(int priceInt) {
        switch (priceInt) {
            case 1:
                return "$";
            case 2:
                return "$$";
            case 3:
            default:
                return "$$$";
        }
    }
}

