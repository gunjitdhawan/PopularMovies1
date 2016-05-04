package com.nanodegreeandroid.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by gunjit on 04/05/16.
 */
public class AppUtils {

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
