package com.ax.axtools;

import android.app.Application;

import com.ax.axtools_library.utils.AxTool;

public class MyAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AxTool.init(this);
    }
}
