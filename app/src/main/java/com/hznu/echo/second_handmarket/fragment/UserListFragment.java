package com.hznu.echo.second_handmarket.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.UserInfoAdapter;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wengqian on 2018/4/7.
 */

public class UserListFragment extends Fragment {
    @BindView(R.id.goods_recycler_view)
    RecyclerView goodsRecyclerView;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;
    ProgressDialog dialog;
    private UserInfoAdapter homeAdapter;
    private List<User> users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_checked, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDate();
        initView();
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//        设置管理
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        return view;
    }
    private void initDate() {
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

    private void initView () {
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
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
        BmobQuery<User> query = new BmobQuery<User>();
        query.setLimit(100);
        query.addWhereEqualTo("role",1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    dialog.dismiss();
                    users = new ArrayList<>(list);
                    homeAdapter = new UserInfoAdapter(users, getActivity());
                    goodsRecyclerView.setAdapter(homeAdapter);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void refreshDate() {
        BmobQuery<User> query = new BmobQuery<>();
        query.setLimit(100);
        query.addWhereEqualTo("role",1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    dialog.dismiss();
                    users.clear();
                    users.addAll(list);
                    homeAdapter.notifyDataSetChanged();
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

}
