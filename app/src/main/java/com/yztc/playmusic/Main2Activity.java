package com.yztc.playmusic;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yztc.adapter.MusicAdapter;
import com.yztc.bean.MusicBean;
import com.yztc.call_back.OnClickCallBack;
import com.yztc.customView.RecycleViewDivider;
import com.yztc.services.MyServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {
    @InjectView(R.id.recycler)
    RecyclerView recycler;
    @InjectView(R.id.show)
    TextView show;
    @InjectView(R.id.showCurrent)
    TextView showCurrent;
    @InjectView(R.id.seekBar)
    SeekBar seekBar;
    @InjectView(R.id.previous)
    ImageButton previous;
    @InjectView(R.id.play)
    ImageButton play;
    @InjectView(R.id.pause)
    ImageButton pause;
    @InjectView(R.id.next)
    ImageButton next;
    @InjectView(R.id.myImport)
    LinearLayout myImport;
    @InjectView(R.id.allMusic)
    Button allMusic;
    public static final int DURATION = 0X1111;
    public static final int CURRENT = 0X2222;
    private static SeekBar seekBar1;
    private ContentResolver resolver;
    private static List<MusicBean> datas;
    private MusicAdapter adapter;
    private MyServices.MyIBinder iBinder;
    private static SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private static Date date=new Date();
    private static String path;
    private static TextView show1,showCurrent1;
    //是否显示
    private Boolean flage=false;
    private static int    lastClick=-1;
    private static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.inject(this);
        seekBar1=seekBar;
        show1=show;
        showCurrent1=showCurrent;
        resolver = getContentResolver();
        datas = new ArrayList<>();
        adapter = new MusicAdapter(datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(new RecycleViewDivider(20));
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        adapter.setEntity(new OnClickCallBack<TextView>() {
            @Override
            public void click(TextView view, int position) {
//                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
//                intent.putExtra("path", datas.get(position).getPath());
//                startActivity(intent);
               //play.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.myanimation));
                //ObjectAnimator.ofFloat(view, "rotationX", 0F, 360F).setDuration(1000).start();
                Animator anim = AnimatorInflater.loadAnimator(Main2Activity.this, R.animator.myanimator);
                anim.setTarget(view.getParent().getParent());
                anim.start();
                path = datas.get(position).getPath();
                if (!flage) {
                    show();
                }
                if (lastClick != position) {
                    Message play = Message.obtain();
                    play.obj = path;
                    play.what = MyServices.START;
                    MyServices.handler.sendMessage(play);
                    Toast.makeText(Main2Activity.this, "被点击了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main2Activity.this, "正在播放请勿重复点击", Toast.LENGTH_SHORT).show();
                }

                //finish();
                lastClick = position;
            }
        });
        getData();
        Intent musicServices=new Intent(Main2Activity.this, MyServices.class);
        startService(musicServices);
        seek();

    }

    /**
     * 初始化数据
     */

    public void getData() {
        List<MusicBean> musicBeans = new ArrayList<>();
        String[] tDatas = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION};
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, tDatas, null, null, null);
        while (cursor.moveToNext()) {
            MusicBean musicBean = new MusicBean(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            musicBeans.add(musicBean);

        }
        datas.clear();
        datas.addAll(musicBeans);
        adapter.notifyDataSetChanged();

    }

    /**
     * 点击出来
     */
    public void show() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(myImport, "translationY", 0, -650).setDuration(5000);
        objectAnimator.start();
        flage=true;
    }

    /**
     * 点击进去
     */
    @OnClick(R.id.allMusic)
    public void onClick() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(myImport, "translationY", -650, 0).setDuration(5000);
        objectAnimator.start();
        flage=false;

    }

    /**
     * 播放的按钮点击事件
     * @param view
     */
    @OnClick({R.id.previous, R.id.play, R.id.pause, R.id.next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                Message previous=Message.obtain();
                previous.what=MyServices.PREVIOUS;
                lastClick--;
                if(lastClick==-1){
                    lastClick=datas.size()-1;
                }
                previous.obj= datas.get(lastClick).getPath();
                MyServices.handler.sendMessage(previous);
                break;
            case R.id.play:
                Message play=Message.obtain();
                play.obj=path;
                play.what=MyServices.START;
                MyServices.handler.sendMessage(play);
                break;
            case R.id.pause:
                Message pause=Message.obtain();
                pause.what=MyServices.PAUSE;
                MyServices.handler.sendMessage(pause);
                break;
            case R.id.next:
                Message next=Message.obtain();
                next.what=MyServices.NEXT;
                lastClick++;
                if(lastClick==datas.size()){
                    lastClick=0;
                }
               View view1= recycler.getChildAt(lastClick);
                Animator anim = AnimatorInflater.loadAnimator(Main2Activity.this, R.animator.myanimator);
                anim.setTarget(view1);
                anim.start();
                next.obj=datas.get(lastClick).getPath();
                MyServices.handler.sendMessage(next);
                break;
        }
    }

    /**
     * 当前进度与时间
     */
     public static Handler handler=new Handler(){

         @Override
         public void handleMessage(Message msg) {
              switch (msg.what){
                  case CURRENT:
                      if(MyServices.player==null){
                          return;
                      }
                      seekBar1.setProgress(MyServices.player.getCurrentPosition()/1000);
                      handler.sendEmptyMessageDelayed(CURRENT, 1000);
                      date.setTime(MyServices.player.getCurrentPosition());
                      showCurrent1.setText(format.format(date));
                      break;
                  case DURATION:
                      seekBar1.setMax(MyServices.player.getDuration()/1000);
                      date.setTime(MyServices.player.getDuration());
                      show1.setText(format.format(date));
                      if(count==0){
                          MyServices.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                              @Override
                              public void onCompletion(MediaPlayer mp) {
                                  lastClick++;
                                  if(lastClick==datas.size()){
                                      lastClick=0;
                                  }
                                  MyServices.play(datas.get(lastClick).getPath());



                              }
                          });
                      }

                  break;
              }
         }
     };

    /**
     * 进度条拖动
     */
    public void seek(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    MyServices.player.seekTo(progress*1000);

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
}
