package com.ax.axtools;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ax.axtools_library.activitys.DeviceInfoActivity;
import com.ax.axtools_library.utils.AudioTrackManager;
import com.ax.axtools_library.utils.AxActivityTool;
import com.ax.axtools_library.utils.AxFileTool;
import com.ax.axtools_library.utils.AxIntentTool;
import com.ax.axtools_library.utils.AxPerformanceTool;
import com.ax.axtools_library.utils.AxTimeTool;
import com.ax.axtools_library.utils.AxTool;
import com.ax.axtools_library.utils.L;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ax.axtools_library.utils.AxFileTool.getDataColumn;
import static com.ax.axtools_library.utils.AxFileTool.isDownloadsDocument;
import static com.ax.axtools_library.utils.AxFileTool.isExternalStorageDocument;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.play)
    Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        play.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == play) {
            L.e(AxPerformanceTool.start());
            File file = new File(AxFileTool.getSDCardPath() + "/.yqtec/lesson_egret_21");
            File[] files = null;
            if (file.exists() && file.isDirectory() && file.length() > 0) {
                files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory() && pathname.lastModified() > 7 * 24 * 60 * 60 * 1000) {
                            return true;
                        }
                        return false;
                    }
                });
            }
            for (File file1:files){
                L.e(file1.getName());
                AxFileTool.delAllFile(file1.getAbsolutePath());
            }
            L.e(AxPerformanceTool.end());
        }
    }

}
