package com.ax.axtools_library.utils;

/**
 * 黑夜模式
 */
public class AxNightTool {
    public boolean isNight(){
        int k  = Integer.parseInt(AxTimeTool.getCurrentDateTime("HH"));
        if ((k>=0 && k<6) ||(k >=18 && k<24)){
            return true;
        } else {
            return false;
        }
    }

}
