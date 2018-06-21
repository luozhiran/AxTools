package com.ax.axtools_library.utils;

/**
 * 测试性能工具
 */
public class AxPerformanceTool {


    private static long time;

    public static void initpointTime() {
        time = System.currentTimeMillis();
    }


    public static String intervalTo() {
        return (System.currentTimeMillis() - time)+"毫秒";
    }

    public static String intervalBy() {
        if (time == 0){
            time = System.currentTimeMillis();
        }
        long temp =  System.currentTimeMillis()-time;
        time = System.currentTimeMillis();
        return temp+"毫秒";
    }
}
