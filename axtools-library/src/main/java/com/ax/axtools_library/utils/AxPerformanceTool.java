package com.ax.axtools_library.utils;

/**
 * 测试性能工具
 */
public class AxPerformanceTool {


    private static long time;


    public static String start(){
        time = System.currentTimeMillis();
        return "计时："+time;
    }

    public static String end(){
        long diff = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        return diff+"毫秒";
    }

}
