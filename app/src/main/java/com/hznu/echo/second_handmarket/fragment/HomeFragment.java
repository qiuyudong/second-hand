package com.hznu.echo.second_handmarket.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.HomeAdapter;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
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
 * Created by beyond on 17/4/11.
 */

public class HomeFragment extends BaseFragment1 {


    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.goods_recycler_view)
    RecyclerView goodsRecyclerView;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;
    ProgressDialog dialog;
    private HomeAdapter homeAdapter;
    private List<Second_Goods> mSecond_goodses = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
//        goodsRecyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
//                10,
//                10));
//        goodsRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
//                10, getResources().getColor(R.color.itemDivider)));
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
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses = new ArrayList<>(list);
                    homeAdapter = new HomeAdapter(mSecond_goodses, getActivity());
                    goodsRecyclerView.setAdapter(homeAdapter);
                } else {
                    ToastUtil.showAndCancel(e.toString());
                }
            }
        });
    }

    private void refreshDate() {
        BmobQuery<Second_Goods> query = new BmobQuery<>();
        query.findObjects(new FindListener<Second_Goods>() {
            @Override
            public void done(List<Second_Goods> list, BmobException e) {
                if (e == null) {
                    ToastUtil.showAndCancel("查询成功");
                    dialog.dismiss();
                    mSecond_goodses.clear();
                    mSecond_goodses.addAll(list);
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
