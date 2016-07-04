package com.yztc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.yztc.playmusic.Main2Activity;
import com.yztc.playmusic.MyApplication;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/2.
 */
public class MyServices extends Service{
    private static boolean isToast=true;
    private static  String prePath;
    public  static MediaPlayer player;
    public static final int START=0x1,PAUSE=0x2,NEXT=0x3,PREVIOUS=0x4;
    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case START:
                     play(msg.obj.toString());
                     break;
                 case PAUSE:
                    stop();
                     break;
                 case NEXT:
                     Toast.makeText(MyApplication.getAppContext(),"下一首",Toast.LENGTH_SHORT).show();
                     play(msg.obj.toString());
                     break;
                 case PREVIOUS:
                     Toast.makeText(MyApplication.getAppContext(),"上一首",Toast.LENGTH_SHORT).show();
                     play(msg.obj.toString());
                     break;


             }
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyIBinder();
    }

    public class MyIBinder extends Binder {
        public void play(){
            Toast.makeText(MyServices.this,"开始播放",Toast.LENGTH_SHORT).show();
        };
        public void pause(){
            Toast.makeText(MyServices.this,"暂停播放",Toast.LENGTH_SHORT).show();
        };
        public void next(){
            Toast.makeText(MyServices.this,"下一首",Toast.LENGTH_SHORT).show();
        };
        public void  previous(){
            Toast.makeText(MyServices.this,"上一首",Toast.LENGTH_SHORT).show();
        };

    }

    /**
     * 播放初始化
     */
    public static void play(String path) {
        //点击开始或暂停
        if(path.equals(prePath)){

            if(player!=null&&player.isPlaying()){
                Toast.makeText(MyApplication.getAppContext(),"暂停播放",Toast.LENGTH_SHORT).show();
                player.pause();
                return ;

            }else if(player!=null&&!player.isPlaying()){
                player.start();
                Toast.makeText(MyApplication.getAppContext(),"开始播放",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        prePath=path;
        if(player!=null){
            isToast=false;
           stop();
            isToast=true;
        }

        Toast.makeText(MyApplication.getAppContext(),"开始播放",Toast.LENGTH_SHORT).show();
        player = new MediaPlayer();
        player.reset();

        try {
            player.setDataSource(
              path
            );
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    Main2Activity.handler.sendEmptyMessage(Main2Activity.DURATION);
                    Main2Activity.handler.sendEmptyMessage(Main2Activity.CURRENT);
                }
            });
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止歌曲
     */
    public static void stop() {
        if(player==null){
            return;
        }
      if(isToast){
          Toast.makeText(MyApplication.getAppContext(),"停止播放",Toast.LENGTH_SHORT).show();
      }

        player.stop();
        player.release();
        player=null;

    }


}
