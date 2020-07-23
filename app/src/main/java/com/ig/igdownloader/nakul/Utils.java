package com.ig.igdownloader.nakul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.ig.igdownloader.R;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils extends Activity {
    public static final String TAG= "Utils";
    public static AlertDialog.Builder builder;


    public static String fileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0,6);
    }



    public static String parseProfilePic(String str) {
        Matcher matcher = Pattern.compile("pictureLink: (.*?)<br>").matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getServiceURL(String str) {
        String y = null;
        if (!str.isEmpty())
            y = "https://yazilimsalcozumler.com/api/hdppfi/hd.php?user_id=" + str;
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
        builder.setTitle(R.string.about);
        builder.setMessage(R.string.about_description).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alerT = builder.create();
        alerT.show();
    }

    public static void alertDialog1 (Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.how_to_use);
        builder.setMessage(R.string.how_to).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
