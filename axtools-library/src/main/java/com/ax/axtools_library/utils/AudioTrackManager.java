package com.ax.axtools_library.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AudioTrackManager {
    public static final int PLAY_DECODE_WAV = 1;
    public static final int PLAY_PCM = 2;

    private HandlerThread mHandlerThread;
    private Handler mAsynHandler;
    private int duration;
    private int bufferSize = 0;
    private AudioTrack audioTrack;
    DataInputStream dis;
    private boolean isStart;
    final int sampleRate = 16000;//16000;
    private AudioTrackManager.OnAudioTrackComplete audioTrackComplete;

    private PriorityQueue<Integer> queue;
    private AtomicBoolean isWait = new AtomicBoolean(false);
    private Object mLock = new Object();

    public AudioTrackManager() {
        AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        initAudioTrack();
        initAsynHandle();
    }

    private void initAudioTrack() {
        bufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);
        queue = new PriorityQueue<Integer>(2);
    }

    private void initAsynHandle() {
        mHandlerThread = new HandlerThread("audio_track_msg");
        mHandlerThread.start();
        mAsynHandler = new AudioTrackManager.AsynHandle(mHandlerThread.getLooper());
    }


    private class AsynHandle extends Handler {

        public AsynHandle(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_PCM:
                    playPcm((String) msg.obj);
                    break;
            }
        }
    }

    public void startPlay(String path, int method) {
        mAsynHandler.removeCallbacksAndMessages(null);
        stopPlay();
        Message msg = Message.obtain();
        msg.obj = path;
        msg.what = method;
        mAsynHandler.sendMessage(msg);
    }


    private synchronized void playPcm(String path) {
        try {
            byte[] tempBuffer = new byte[bufferSize];
            int readCount = 0;
            dis = setPath(path);
            if (dis == null) {
                return;
            }
            isStart = true;
            audioTrack.play();
            while (dis != null && dis.available() > 0) {
                if (isWait.get()) {
                    synchronized (queue) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!isStart) return;
                readCount = dis.read(tempBuffer);
                if (readCount != 0 && readCount != -1) {
                    if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                        audioTrack.write(tempBuffer, 0, readCount);
                    }
                }
            }
            if (dis != null) {
                dis.close();
            }
            if (audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PAUSED) {
                audioTrack.pause();
                audioTrack.flush();
            }
            if (audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                audioTrack.stop();
            }
            isStart = false;
            if (audioTrackComplete != null) {
                audioTrackComplete.complete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DataInputStream setPath(String path) throws IOException {
        File file = new File(path);
        if (dis != null) {
            dis.close();
        }
        dis = null;
        if (file.exists()) {
            dis = new DataInputStream(new FileInputStream(file));
        } else {
            dis = null;
        }
        return dis;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isPlaying() {
        return audioTrack != null && audioTrack.getPlayState() == 3 && !isWait.get();
    }

    public boolean isWait() {
        return audioTrack != null && isWait.get();
    }

    public void pause() {
        isWait.set(true);
    }

    public void start() {
        if (isWait.get()) {
            synchronized (queue) {
                isWait.set(false);
                queue.notify();
            }
        }
    }

    public void stopPlay() {
        try {
            start();
            synchronized (mLock) {
                isStart = false;
            }
            if (audioTrack != null) {
                if (audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PAUSED) {
                    audioTrack.pause();
                    audioTrack.flush();
                }
                if (audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                    audioTrack.stop();
                }

            }
            mAsynHandler.removeCallbacksAndMessages(null);
            if (dis != null) {
                dis.close();
                dis = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isPlay() {
        return isStart;
    }

    public void release() {
        audioTrack.release();
        mHandlerThread.quit();
    }


    public void setAudioTrackComplete(OnAudioTrackComplete audioTrackComplete) {
        this.audioTrackComplete = audioTrackComplete;
    }

    public interface OnAudioTrackComplete {
        void complete();
    }

}