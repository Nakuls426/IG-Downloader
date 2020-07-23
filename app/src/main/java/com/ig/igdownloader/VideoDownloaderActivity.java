package com.ig.igdownloader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import com.ig.igdownloader.nakul.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("StaticFieldLeak")
public class VideoDownloaderActivity extends AsyncTask<String, Integer, String> {
    ProgressDialog mProgressDialog;
    final Context context;
    PowerManager.WakeLock mWakeLock;
    String fileN = null;

    public VideoDownloaderActivity(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            int fileLength = connection.getContentLength();
            is = connection.getInputStream();
            fileN = Utils.fileName() + ".mp4";
            File fileName = new File(Environment.getExternalStorageDirectory() + "/" + "IGDownloader", fileN);
//            file = new File(Environment."sample.mp4");
            os = new FileOutputStream(fileName);

            byte[] data = new byte[4096];
            long total = 0;
            int count;
            while ((count = is.read(data)) != -1) {
                if (isCancelled()) {
                    is.close();
                    return null;
                }
                count += total;
                if (fileLength > 0) {
                    publishProgress((int) total * 100 / fileLength);
                    os.write(data, 0, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    if (os != null) {
                        os.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        mProgressDialog = new ProgressDialog(this.context);
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (s != null)
            Toast.makeText(context, "Download error: " + s, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
    }
}
