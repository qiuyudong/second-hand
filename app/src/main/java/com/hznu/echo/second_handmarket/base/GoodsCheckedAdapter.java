package com.hznu.echo.second_handmarket.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.CheckedActivity;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wengqian on 2018/4/1.
 */


public class GoodsCheckedAdapter extends RecyclerView.Adapter<GoodsCheckedAdapter.CheckedGoods> {
    private List<Second_Goods> second_goods;
    private Context mcontext;

    public GoodsCheckedAdapter(List<Second_Goods> prolist,  Context context) {
        second_goods = prolist;
        mcontext = context;
    }

    @Override
    public CheckedGoods onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.check_goods_item, parent, false);
        final CheckedGoods holder = new CheckedGoods(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Second_Goods msecond_goods = second_goods.get(position);
                Intent intent = new Intent(mcontext, CheckedActivity.class);
                intent.putExtra("second_goods", msecond_goods);
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(CheckedGoods holder, int position) {
        Second_Goods msecond_goods = second_goods.get(position);
        Picasso.with(mcontext)
                .load(msecond_goods.getImagePath())
                .error(R.drawable.logo)
                .into(holder.goods_iv);
        holder.goods_name.setText(msecond_goods.getName());

    }

    @Override
    public int getItemCount() {
        return second_goods.size();
    }

    //实现adapter和ViewHolder的列表布局
    class CheckedGoods extends RecyclerView.ViewHolder {
        public TextView goods_name;
        public ImageView goods_iv;

        public CheckedGoods(View itemView) {
            super(itemView);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            goods_iv = (ImageView) itemView.findViewById(R.id.goods_iv);
        }
    }

}