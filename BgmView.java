package com.zzw.cdd;

import android.app.Service;
//import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
//import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.media.AudioManager;

public class BgmView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, OnSeekBarChangeListener{

    boolean threadFlag = true;
    Context context;
    SurfaceHolder holder;
    Canvas canvas;
    Bitmap backgroundBitmap;
    private SeekBar seekbar;
    private AudioManager am;

    Thread bgmThread = new Thread() {
        @SuppressLint("WrongCall")
        @Override
        public void run() {
            holder = getHolder();
            while (threadFlag) {
//                desk.gameLogic();
                try{
                    canvas = holder.lockCanvas();
                    onDraw(canvas);
                }finally {
                    holder.unlockCanvasAndPost(canvas);
                }
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    protected void onDraw(Canvas canvas){
        Rect src = new Rect();
        Rect des = new Rect();
        src.set(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        des.set(0, 0, MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT);
        canvas.drawBitmap(backgroundBitmap, src, des, null);

    }
    public BgmView(Context context) {
        super( context );
        this.context = context;
        am=(AudioManager)context.getSystemService(Service.AUDIO_SERVICE);
        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgm_bg);
        seekbar=(SeekBar)findViewById(R.id.seekbar);
        this.getHolder().addCallback(this);
        seekbar.setOnSeekBarChangeListener(this);
        this.setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        threadFlag = true;
        bgmThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        threadFlag = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        threadFlag = false;
        boolean retry = true;
        while (retry) {// 循环
            try {
                bgmThread.join();// 等待线程结束
                retry = false;// 停止循环
            } catch (InterruptedException e) {
            }// 不断地循环，直到刷帧线程结束
        }
    }
    @Override
    public void onProgressChanged(SeekBar sb, int progress, boolean fromUser){
        am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
    }
        @Override
        public void onStartTrackingTouch(SeekBar sb){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        }
        @Override
        public void onStopTrackingTouch(SeekBar sb){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
        }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
