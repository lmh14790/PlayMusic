package com.yztc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yztc.bean.MusicBean;
import com.yztc.call_back.OnClickCallBack;
import com.yztc.playmusic.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/1.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private List<MusicBean> datas;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    private Date date = new Date();
    private OnClickCallBack<TextView> entity;
    public MusicAdapter(List<MusicBean> datas) {
        this.datas = datas;
    }

    public void setEntity(OnClickCallBack<TextView> entity) {
        this.entity = entity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, null);
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MusicBean musicBean = datas.get(position);
        holder.name.setText(musicBean.getFileName());
        date.setTime(musicBean.getTime());
        holder.time.setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.time)
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            name.setOnClickListener(this);
            time.setOnClickListener(this);
        }

        /**
         *  重写的onclick方法
         * @param v
         */
        @Override
        public void onClick(View v) {
              switch (v.getId()){
                  case R.id.name :
                      entity.click((TextView)v,getLayoutPosition());
                       break;
                  case R.id.time:
                     entity.click((TextView)v,getLayoutPosition());
                   break;
              }
        }
    }
}
