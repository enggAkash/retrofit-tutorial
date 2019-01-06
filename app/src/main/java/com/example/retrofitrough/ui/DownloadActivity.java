package com.example.retrofitrough.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofitrough.BuildConfig;
import com.example.retrofitrough.R;
import com.example.retrofitrough.api.service.FileDownloadClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "DownloadActivity";

    private final int WRITE_PERMISSION_REQUEST_CODE = 1;
    private Button downloadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadBtn = findViewById(R.id.download_btn);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
    }

    private void downloadFile() {

        OkHttpClient.Builder okhBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG)
            okhBuilder.addInterceptor(interceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://futurestud.io/")
                .client(okhBuilder.build());

        Retrofit retrofit = builder.build();

        FileDownloadClient client = retrofit.create(FileDownloadClient.class);

        Call<ResponseBody> call = client.downloadFile("https://futurestud.io/images/futurestudio-university-logo.png");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        boolean isDownloaded = writeReposeBodyToDisk(response.body());
                        return null;
                    }
                }.execute();

                //Toast.makeText(DownloadActivity.this, "FileDownloaded Successfully ? " + isDownloaded, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DownloadActivity.this, "Retrofit Request Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private boolean writeReposeBodyToDisk(ResponseBody body) {

        File futureStudIconFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "futurestud_icon.png"
        );

        InputStream inputStream = null;
        OutputStream outputStream = null;

        byte[] fileReader = new byte[4096];

        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;

        inputStream = body.byteStream();
        try {
            outputStream = new FileOutputStream(futureStudIconFile);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1)
                    break;

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;

                Log.d(TAG, "writeReposeBodyToDisk: download progress " + fileSizeDownloaded + " / " + fileSize);

            }

            outputStream.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case WRITE_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // got it
                } else {
                    Toast.makeText(this, "We need write storage permission to download file", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
