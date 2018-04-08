package com.hznu.echo.second_handmarket.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.GoodsInformationActivity;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wengqian on 2018/4/1.
 */


public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Second_Goods> goodslist;
    private Context mcontext;
    ProgressDialog dialog;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_HEADER = 1;

    //构造方法，初始化列表
    public HomeAdapter(List<Second_Goods> prolist, Context context) {
        goodslist = prolist;
        mcontext = context;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.fragment_home_header, parent, false);
            final HeaderHolder holder = new HeaderHolder(view);
            return holder;
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.home_goods_item, parent, false);
            final GoodsHolder holder = new GoodsHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Second_Goods second_goods = goodslist.get(position - 1);
                    Intent intent = new Intent(mcontext, GoodsInformationActivity.class);
                    intent.putExtra("second_goods", second_goods);
                    mcontext.startActivity(intent);
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final GoodsHolder goodsholder = (GoodsHolder)holder;
            Second_Goods second_goods = goodslist.get(position-1);
            String pic_uri = second_goods.getImagePath();
            String user_head = second_goods.getUpload_user().getHeadPortraitPath();
            //加载图片
            Picasso.with(mcontext)
                    .load(pic_uri)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(goodsholder.goods_photo);
            //加载头像
            Picasso.with(mcontext)
                    .load(user_head)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(goodsholder.user_head);
            goodsholder.goods_name.setText(second_goods.getName());
            goodsholder.goods_user.setText(second_goods.getUpload_user().getNickname());
            goodsholder.goods_time.setText(second_goods.getCreatedAt());
            String usersex = second_goods.getUpload_user().getSex();
            if(usersex.equals("male")){
                goodsholder.male.setVisibility(View.VISIBLE);
                goodsholder.female.setVisibility(View.GONE);
            }else {
                goodsholder.female.setVisibility(View.VISIBLE);
                goodsholder.male.setVisibility(View.GONE);
            }
            goodsholder.goods_name.setText(second_goods.getName());
            goodsholder.goods_price.setText("¥ " + second_goods.getPrice());
            goodsholder.liked_number.setText(second_goods.getLiked_number()+"");
        } else {
            Banner banner = ((HeaderHolder)holder).banner;
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            List<Integer> list = new ArrayList<>();
            list.add(R.drawable.c_beauty);
            list.add(R.drawable.c_home);
            list.add(R.drawable.c_digital);
            ((HeaderHolder)holder).hotGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getHotData();
                    ((HeaderHolder) holder).new_line.setVisibility(View.INVISIBLE);
                    ((HeaderHolder) holder).hot_line.setVisibility(View.VISIBLE);
                }
            });
            ((HeaderHolder)holder).newGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getNewData();
                    ((HeaderHolder) holder).new_line.setVisibility(View.VISIBLE);
                    ((HeaderHolder) holder).hot_line.setVisibility(View.INVISIBLE);
                }
            });
            //设置图片集合
            banner.setImages(list);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

    }

    @Override
    public int getItemCount() {
        return goodslist.size() == 0 ? 0 : goodslist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_HEADER){
                        return 2;
                    }else{
                        return 1;
                    }
                }
            });
        }

    }

    //实现adapter和ViewHolder的列表布局
    class GoodsHolder extends RecyclerView.ViewHolder {
        public TextView goods_name, goods_time, goods_price, goods_user, liked_number;
        public ImageView goods_photo, male, female;
        public CircleImageView user_head;

        public GoodsHolder(View itemView) {
            super(itemView);
            goods_user = (TextView) itemView.findViewById(R.id.user_nickname);
            user_head = (CircleImageView) itemView.findViewById(R.id.user_head_portrait);
            male = (ImageView) itemView.findViewById(R.id.male);
            female = (ImageView) itemView.findViewById(R.id.female);
            goods_time = (TextView) itemView.findViewById(R.id.goods_time);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            liked_number = (TextView) itemView.findViewById(R.id.liked_number);
            goods_photo = (ImageView) itemView.findViewById(R.id.goods_photo);
        }
    }

    //头布局
    class HeaderHolder extends RecyclerView.ViewHolder {
        public Banner banner;
        public LinearLayout hearder_container;
        public LinearLayout newGoods, hotGoods;
        public View new_line, hot_line;

        public HeaderHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner);
            hearder_container = (LinearLayout) itemView.findViewById(R.id.hearder_container);
            newGoods = (LinearLayout) itemView.findViewById(R.id.new_ll);
            hotGoods = (LinearLayout) itemView.findViewById(R.id.hot_ll);
            new_line = itemView.findViewById(R.id.new_line);
            hot_line = itemView.findViewById(R.id.hot_line);
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            Picasso.with(context).load((Integer) path).into(imageView);
        }
    }

    private void getNewData(){
        dialog.show();
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.include("upload_user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    goodslist.clear();
                    goodslist = new ArrayList<>(list);
                    Log.e("time", goodslist.size()+"" );
                    for(Second_Goods second_goods: goodslist){
                        Log.e("time", second_goods.getCreatedAt());
                    }
                    notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Log.e("times", goodslist.size()+"" );
                    ToastUtil.showAndCancel(e.getMessage());
                    dialog.dismiss();
                }
            }
        });
    }
    private void getHotData(){
        dialog.show();
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.include("upload_user");
        query.order("-liked_number");
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    goodslist.clear();
                    goodslist = new ArrayList<>(list);
                    notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    ToastUtil.showAndCancel(e.getMessage());
                    dialog.dismiss();
                }
            }
        });
    }
}