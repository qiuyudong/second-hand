package com.hznu.echo.second_handmarket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.MyAdapter;
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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsInformationActivity extends AppCompatActivity {

    @BindView(R.id.goods_photo)
    ImageView goodsPhoto;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_desc)
    TextView goodsDesc;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_user)
    TextView goodsUser;
    @BindView(R.id.goods_comment)
    EditText goodsComment;
    @BindView(R.id.goods_comment_upload)
    Button goodsCommentUpload;
    @BindView(R.id.comments)
    RecyclerView comments;

    private List<String> mComments = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsinformation);
        ButterKnife.bind(this);
        goodsComment.clearFocus();
        initData();
        // 设置布局管理器
        comments.setLayoutManager(new LinearLayoutManager(this));

    }


    private void initData() {
         id = getIntent().getStringExtra("goods_id");
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.getObject(id, new QueryListener<Second_Goods>() {
            @Override
            public void done(Second_Goods second_goods, BmobException e) {
                if (e == null) {
                    goodsName.setText(second_goods.getName());
                    goodsDesc.setText(second_goods.getDescription());
                    goodsPrice.setText(second_goods.getPrice());
                    goodsUser.setText(second_goods.getUpload_user().getNickname());
//                    if (second_goods.getGoods_comment() != null) {
//                        mComments = second_goods.getGoods_comment();
//                    }
                    mAdapter = new MyAdapter(mComments);
                    // 设置adapter
                    comments.setAdapter(mAdapter);
                    //加载图片
                    Picasso.with(getBaseContext())
                            .load(second_goods.getImagePath())
                            //加载中的图片
                            .placeholder(R.drawable.logo)
                            //设置加载失败的图片显示
                            .error(R.drawable.logo)
                            .into(goodsPhoto);
                } else {
                    ToastUtil.showAndCancel("出错" + e.toString());
                }
            }
        });
    }

    @OnClick(R.id.goods_comment_upload)
    public void onViewClicked() {
        saveComment();
    }

    private void saveComment(){
        if(goodsComment.getText().toString()!=null){
            mComments.add(goodsComment.getText().toString());
            Second_Goods second_goods  = new Second_Goods();
            second_goods.setValue("goods_comment",mComments);
            second_goods.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                        ToastUtil.showAndCancel("成功" );
                        goodsComment.setText("");
                        mAdapter.notifyDataSetChanged();
                    Log.d("upload", "done: " + e.toString() );

                }
            });
        }

    }
}
