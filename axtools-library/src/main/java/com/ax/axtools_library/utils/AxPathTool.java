package com.ax.axtools_library.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import com.ax.axtools_library.module.StorageModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AxPathTool {
    private static final String STORAGE_VOLUME_CLASS = "android.os.storage.StorageVolume";

    private static final String TAG = "AxFileTool";
    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (AxStatusTool.sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }
    /**
     * 获取的目录默认没有最后的”/”,需要自己加上
     * 获取本应用图片缓存目录
     *
     * @return
     */
    public static File getCecheFolder(Context context) {
        File folder = new File(context.getCacheDir(), "IMAGECACHE");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }


    /**
     * 获取SD卡路径
     * <p>一般是/storage/emulated/0/</p>
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        if (!AxStatusTool.isSDCardEnable()) return "sdcard unable!";
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }


    /**
     * 获取SD卡Data路径
     *
     * @return SD卡Data路径
     */
    public static String getDataPath() {
        if (!AxStatusTool.isSDCardEnable()) return "sdcard unable!";
        return Environment.getDataDirectory().getPath();
    }


    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return AxDataTool.isNullString(filePath) ? null : new File(filePath);
    }
    /**
     * 获取扩展sd卡路径
     * @param context
     * @return
     */
    public static List<String> getExternalStorageVolume(Context context) {
        List<StorageModel> volumeLists = getStorageList(context);
        List<String> list = new ArrayList<>();
        for (StorageModel volume : volumeLists) {
            if (volume.isRemovable() && !volume.getPath().equals("/storage/usbotg")) {
                list.add(volume.getPath());
            }
        }
        return list;
    }

    /**
     * 获取设备存储的挂载点列表
     *
     * @param context
     * @return
     */
    public static List<StorageModel> getStorageList(Context context) {
        List<StorageModel> volumeLists = new ArrayList<StorageModel>();
        StorageManager mStorageManager = getStorageManager(context);
        if (mStorageManager == null)
            return volumeLists;
        try {
            Class<?> mStorageVolume = Class.forName(STORAGE_VOLUME_CLASS);
            Class<StorageManager> clazz = StorageManager.class;
            Method getPath = mStorageVolume.getMethod("getPath");
            Method isRemovable = mStorageVolume.getMethod("isRemovable");
            Method getState = mStorageVolume.getMethod("getState");
            Method getDescription = mStorageVolume.getMethod("getDescription", Context.class);
            Method method = clazz.getMethod("getVolumeList");
            method.setAccessible(true);
            Object[] invokes = (Object[]) method.invoke(mStorageManager);
            if (invokes != null) {
                for (int i = 0; i < invokes.length; i++) {
                    String path = (String) getPath.invoke(invokes[i]);
                    String state = (String) getState.invoke(invokes[i]);
                    boolean removable = (Boolean) isRemovable.invoke(invokes[i]);
                    String description = (String) getDescription.invoke(invokes[i], context);
                    StorageModel model = new StorageModel();
                    model.setPath(path);
                    model.setState(state);
                    model.setRemovable(removable);
                    model.setDescription(description);
                    volumeLists.add(model);
                }
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "[getStorageVolumeList]NoSuchMethod!");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "[getStorageVolumeList]IllegalAccess!");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "[getStorageVolumeList]IllegalArgument!");
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "[getStorageVolumeList]InvocationTarget!");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "[getStorageVolumeList]ClassNotFound");
        }
        for (StorageModel storageVolumeModel : volumeLists) {
            Log.d(TAG, "[getStorageVolumeList]volume = " + storageVolumeModel);
        }
        return volumeLists;
    }

    /**
     * 获取StorageManager
     */
    public static StorageManager getStorageManager(Context context) {
        if (context == null)
            return null;
        return (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取缓存视频文件目录
     *
     * @param context
     * @return
     */
    public static String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

}
