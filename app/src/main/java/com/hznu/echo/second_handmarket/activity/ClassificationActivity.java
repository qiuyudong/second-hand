package com.hznu.echo.second_handmarket.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ClassificationActivity extends AppCompatActivity {

    ProgressDialog dialog;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.liked_number)
    LinearLayout likedNumber;
    @BindView(R.id.create_time)
    LinearLayout createTime;
    @BindView(R.id.price)
    LinearLayout price;
    @BindView(R.id.goods_recycler_view)
    RecyclerView goodsRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private List<Second_Goods> mSecond_goodses = new ArrayList<>();
    private TaskAdapter Adapter = new TaskAdapter(mSecond_goodses);
    private String type;
    private boolean priceDesc, timeDesc, likedDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        ButterKnife.bind(this);
        initData();
        initView();
//        设置管理
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

    }

    private void initData() {
        //  进度条
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();
        type = getIntent().getStringExtra("type");
        getData();

    }

    private void initView() {
        //输入框监听
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void refreshList() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshDate();
            }
        });
    }


    // 获取数据
    private void getData() {
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereEqualTo("type", type);
        query.include("upload_user");
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ClassificationActivity.this);
                    goodsRecyclerView.setLayoutManager(layoutManager);
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    initButton();
                    filterState(mSecond_goodses);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void initButton() {
        if (mSecond_goodses.size() == 0) {
            likedNumber.setEnabled(false);
            price.setEnabled(false);
            createTime.setEnabled(false);
        }
    }

    private void refreshDate() {
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereEqualTo("type", type);
        query.include("upload_user");
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    //初始化构造器
                    filterState(mSecond_goodses);
                    swipeRefresh.setRefreshing(false);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @OnClick({R.id.liked_number, R.id.create_time, R.id.price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.liked_number:
                sortByLiked();
                break;
            case R.id.create_time:
                sortByTime();
                break;
            case R.id.price:
                sortByPrice();
                break;
        }
    }


    //实现adapter和ViewHolder
    private class GoodsHolder extends RecyclerView.ViewHolder {
        public TextView goods_name, goods_time, goods_user, goods_price, goods_liked;
        public ImageView goods_photo;

        public GoodsHolder(View itemView) {
            super(itemView);
            goods_photo = (ImageView) itemView.findViewById(R.id.goods_photo);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            goods_time = (TextView) itemView.findViewById(R.id.goods_time);
            goods_user = (TextView) itemView.findViewById(R.id.goods_user);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            goods_liked = (TextView) itemView.findViewById(R.id.goods_like_number);
        }
    }


    private class TaskAdapter extends RecyclerView.Adapter<GoodsHolder> {
        private List<Second_Goods> goodslist;

        //构造方法，初始化列表
        public TaskAdapter(List<Second_Goods> prolist) {
            goodslist = prolist;

        }

        @Override
        public GoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ClassificationActivity.this);
            View view = layoutInflater.inflate(R.layout.goods_item, parent, false);
            final GoodsHolder holder = new GoodsHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Second_Goods second_goods = goodslist.get(position);
                    Intent intent = new Intent(ClassificationActivity.this, GoodsInformationActivity.class);
                    intent.putExtra("second_goods", second_goods);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(GoodsHolder holder, int position) {
            Second_Goods second_goods = goodslist.get(position);
            String pic_uri = second_goods.getImagePath();
            //加载图片
            Picasso.with(ClassificationActivity.this)
                    .load(pic_uri)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(holder.goods_photo);
            holder.goods_name.setText(second_goods.getName());
            holder.goods_price.setText("¥ " + second_goods.getPrice());
            holder.goods_time.setText(second_goods.getCreatedAt());
            holder.goods_user.setText(second_goods.getUpload_user().getNickname());
            holder.goods_liked.setText(second_goods.getLiked_number() + "");
        }

        @Override
        public int getItemCount() {
            return goodslist.size();
        }
    }


    private void sortByLiked() {
        dialog.show();
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereEqualTo("type", type);
        query.include("upload_user");
        if (likedDesc) {
            query.order("-liked_number");
        } else {
            query.order("liked_number");
        }
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    filterState(mSecond_goodses);
                    likedDesc = !likedDesc;
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void sortByTime() {
        dialog.show();
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereEqualTo("type", type);
        query.include("upload_user");
        if (timeDesc) {
            query.order("-createdAt");
        } else {
            query.order("createdAt");
        }
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    //初始化构造器
                    filterState(mSecond_goodses);
                    timeDesc = !timeDesc;
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void sortByPrice() {
        dialog.show();
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereEqualTo("type", type);
        query.include("upload_user");
        if (priceDesc) {
            query.order("-price");
        } else {
            query.order("price");
        }
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    //初始化构造器
                    filterState(mSecond_goodses);
                    priceDesc = !priceDesc;
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Second_Goods>  filtersecond_goods = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filtersecond_goods = mSecond_goodses;
        } else {
            filtersecond_goods.clear();
            for (Second_Goods second_goods : mSecond_goodses) {
                String name = second_goods.getName();
                String discri = second_goods.getDescription();
                if (name.indexOf(filterStr) != -1 || discri.indexOf(filterStr) != -1 ){
                    filtersecond_goods.add(second_goods);
                }
            }
        }
        //初始化构造器
        Adapter = new TaskAdapter(filtersecond_goods);
        goodsRecyclerView.setAdapter(Adapter);
    }

    private void filterState (List<Second_Goods> mSecond_goodses){
        List<Second_Goods>  filtersecond_goods = new ArrayList<>();
        for(Second_Goods second_goods: mSecond_goodses) {
            if(second_goods.getState() == 1){
                filtersecond_goods.add(second_goods);
            }
        }
        //初始化构造器
        Adapter = new TaskAdapter(filtersecond_goods);
        goodsRecyclerView.setAdapter(Adapter);
    }
}
