package com.ig.igdownloader.nakul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.ig.igdownloader.R;

import java.io.File;
import java.util.Objects;

public class Utils extends Activity {
    public static final String TAG= "Utils";
    public static AlertDialog.Builder builder;


    public static String getServiceURL(String str) {
        String y = null;
        if (!str.isEmpty())
            y = Endpoints.INSTAGRAM_HD_PIC_ENDPOINT + Endpoints.INSTAGRAM_USER_ID + str;
        return y;
    }

    public static void settings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri  = Uri.fromParts("package",context.getPackageName(),null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void checkFolder () {
        String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path + "/IGDownloader");
        boolean isDirCreated = dir.exists();
        if (!isDirCreated) {
            isDirCreated = dir.mkdirs();
            Log.d(TAG, "Dir created");
        }
        if (isDirCreated)
            Log.d(TAG, "already created");
    }

    public static String imageDestionation () {

        return Environment.getExternalStorageDirectory().toString() + "/IGDownloader";
    }

    public static void alertDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission_required);
        builder.setMessage(R.string.permission_is_r).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alerT = builder.create();
        alerT.show();
    }

    public static void alertDialog1(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.how_to_use);
        builder.setMessage(R.string.how_to).setNegativeButton("Close", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void displaySize(WindowManager windowManager, AlertDialog alertDialog) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        /*int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(alertDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);

        /*Log.d(TAG, "displaySize: "+displayWidth +"x"+ displayHeight);
        
        if (displayWidth <= 1080 && displayHeight <= 1772) {
            Log.d(TAG, "displaySize: executing first if");
            int dialogWidth = (int) (displayWidth * 0.9F);
            int dialogHeight = (int) (displayHeight * 0.38F);
            lp.width = dialogWidth;
            lp.height = dialogHeight;
            alertDialog.getWindow().setAttributes(lp);
        } else if (displayWidth <=1080 && displayHeight >=2030) {
            Log.d(TAG, "displaySize: executing second if");
            int dialogWidth = (int) (displayWidth * 0.9F); // 972
            int dialogHeight = (int) (displayHeight * 0.29F); // 568
            lp.width = dialogWidth;
            lp.height = dialogHeight;
            alertDialog.getWindow().setAttributes(lp);
            Log.d(TAG,dialogWidth +"x" +dialogHeight);
        }
        // Set the dialog height and width.
        int dialogWidth = (int) (displayWidth * 0.9F);
        int dialogHeight = (int) (displayHeight * 0.28F);*/

    }
}
