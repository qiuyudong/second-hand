package com.hznu.echo.second_handmarket.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.GoodsInformationActivity;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by beyond on 17/4/11.
 */

public class CollectionFragment extends BaseFragment {

    @BindView(R.id.goods_recycler_view)
    RecyclerView goodsRecyclerView;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;
    ProgressDialog dialog;
    private List<Second_Goods> mSecond_goodses = new ArrayList<>();
    private TaskAdapter Adapter = new TaskAdapter(mSecond_goodses);

    public static CollectionFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        Log.d("sss","sss");
        initData();
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
//      设置管理
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    private void initData() {
        //  进度条
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();
        getData();

    }

    private void refreshList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshDate();
            }
        });
    }


    // 获取数据
    private void getData() {
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        User user = BmobUser.getCurrentUser(User.class);
        String nickname = user.getNickname();
        query.addWhereEqualTo("upload_user_nickname", nickname);
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    //初始化构造器
                    Adapter = new TaskAdapter(mSecond_goodses);
                    goodsRecyclerView.setAdapter(Adapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    goodsRecyclerView.setLayoutManager(layoutManager);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void refreshDate() {
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        User user = BmobUser.getCurrentUser(User.class);
        String nickname = user.getNickname();
        query.addWhereEqualTo("upload_user_nickname", nickname);
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<Second_Goods>(list);
                    //初始化构造器
                    Adapter = new TaskAdapter(mSecond_goodses);
                    goodsRecyclerView.setAdapter(Adapter);
                    Adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //实现adapter和ViewHolder
    private class GoodsHolder extends RecyclerView.ViewHolder {
        public TextView goods_name, goods_desc, goods_time, goods_user;
        public ImageView goods_photo;

        public GoodsHolder(View itemView) {
            super(itemView);
            goods_photo = (ImageView) itemView.findViewById(R.id.goods_photo);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
//            goods_desc = (TextView) itemView.findViewById(R.id.goods_desc);
            goods_time = (TextView) itemView.findViewById(R.id.goods_time);
            goods_user = (TextView) itemView.findViewById(R.id.goods_user);
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
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.goods_item, parent, false);
            final GoodsHolder holder = new GoodsHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Second_Goods second_goods = goodslist.get(position);
                    Intent intent = new Intent(getActivity(), GoodsInformationActivity.class);
                    intent.putExtra("goods_id", second_goods.getObjectId());
                    startActivity(intent);
                    ToastUtil.showAndCancel("点击了");
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(GoodsHolder holder, int position) {
            Second_Goods second_goods = goodslist.get(position);
            String pic_uri = second_goods.getImagePath();
            //加载图片
            Picasso.with(getActivity())
                    .load(pic_uri)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(holder.goods_photo);
            holder.goods_name.setText(second_goods.getName());
            holder.goods_desc.setText(second_goods.getDescription());
            holder.goods_time.setText(second_goods.getCreatedAt());
            holder.goods_user.setText(second_goods.getUpload_user().getNickname());
        }

        @Override
        public int getItemCount() {
            return goodslist.size();
        }
    }


}
