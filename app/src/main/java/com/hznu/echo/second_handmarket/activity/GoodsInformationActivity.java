package com.hznu.echo.second_handmarket.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.GoodsInfoAdapter;
import com.hznu.echo.second_handmarket.bean.Goods_Comment;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GoodsInformationActivity extends AppCompatActivity {

    @BindView(R.id.goods_info)
    RecyclerView goodsInfo;
    private Second_Goods msecond_goods;
    private List<Goods_Comment> goods_comments = new ArrayList<>();
    private GoodsInfoAdapter mAdapter;
    boolean isliked;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsinformation);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();
        msecond_goods = (Second_Goods) getIntent().getSerializableExtra("second_goods");
        goodsInfo.setLayoutManager(new LinearLayoutManager(GoodsInformationActivity.this, LinearLayoutManager.VERTICAL, false));
        BmobQuery<Goods_Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("current_goods", msecond_goods);  // 查询当前物品的所有评论
        query.order("updatedAt");
        query.include("create_user");// 希望在查询评论信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Goods_Comment>() {

            @Override
            public void done(List<Goods_Comment> object, BmobException e) {
                if (e == null) {
                    Log.e("bmob", "s");
                    goods_comments = object;
                    getIsliked();
                } else {
                    Log.e("bmob", e.getMessage());
                    getIsliked();
                }
            }

        });
    }

    private void getIsliked() {
        BmobQuery<User> query = new BmobQuery<>();
        final User currentUser = BmobUser.getCurrentUser(User.class);
        query.addWhereRelatedTo("liked_user", new BmobPointer(msecond_goods));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        dialog.dismiss();
                        mAdapter = new GoodsInfoAdapter(goods_comments, msecond_goods, false, GoodsInformationActivity.this);
                        goodsInfo.setAdapter(mAdapter);
                        return;
                    } else {
                        for (User user : list) {
                            if (user.getObjectId().equals(currentUser.getObjectId())) {
                                dialog.dismiss();
                                mAdapter = new GoodsInfoAdapter(goods_comments, msecond_goods, true, GoodsInformationActivity.this);
                                goodsInfo.setAdapter(mAdapter);
                                return;
                            }
                        }
                        dialog.dismiss();
                        mAdapter = new GoodsInfoAdapter(goods_comments, msecond_goods, false, GoodsInformationActivity.this);
                        goodsInfo.setAdapter(mAdapter);
                    }
                } else {
                    dialog.dismiss();
                    mAdapter = new GoodsInfoAdapter(goods_comments, msecond_goods, false, GoodsInformationActivity.this);
                    goodsInfo.setAdapter(mAdapter);
                    return;
                }
            }
        });
    }
}
