package com.yztc.playmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private String defaultPath=Environment.getExternalStorageDirectory() + File.separator + "netease" + File.separator
            + "cloudmusic" + File.separator + "Music" + File.separator + "Fall Out Boy - immortals.mp3";
    @InjectView(R.id.seekBar)
    SeekBar seekBar;
    @InjectView(R.id.show)
    TextView show;
    @InjectView(R.id.showCurrent)
    TextView showCurrent;
    private MediaPlayer player;
    @InjectView(R.id.play)
    ImageButton play;
    @InjectView(R.id.pause)
    ImageButton pause;
    public final int DURATION = 0X1111;
    public final int CURRENT = 0X2222;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DURATION:
                    if (player==null) {
                        return;
                    }
                    show.setText(format.format(new Date(player.getDuration())));
                    seekBar.setMax(player.getDuration() / 1000);
                    break;
                case CURRENT:
                    if (player==null) {
                        return;
                    }
                    seekBar.setProgress(player.getCurrentPosition()/1000);
                    showCurrent.setText(format.format(new Date(player.getCurrentPosition())));
                    handler.sendEmptyMessage(CURRENT);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        String path=getIntent().getStringExtra("path");
        if(path!=null){
            defaultPath=path;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser&&player!=null){
                    player.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @OnClick({R.id.play, R.id.pause,R.id.allMusic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                play();
                break;
            case R.id.pause:
                stop();
                break;
            case R.id.allMusic:
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                 finish();
                break;
        }
    }

    public void play() {
        player = new MediaPlayer();
        player.reset();
        try {
            player.setDataSource(
                    defaultPath
            );
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    handler.sendEmptyMessage(DURATION);
                    handler.sendEmptyMessage(CURRENT);
                }
            });
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        player.stop();
        player.release();
        player=null;
    }

}
