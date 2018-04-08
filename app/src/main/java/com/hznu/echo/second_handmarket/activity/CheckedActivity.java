package com.hznu.echo.second_handmarket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class CheckedActivity extends AppCompatActivity {

    @BindView(R.id.user_head_portrait)
    CircleImageView userHeadPortrait;
    @BindView(R.id.user_nickname)
    TextView userNickname;
    @BindView(R.id.male)
    ImageView male;
    @BindView(R.id.female)
    ImageView female;
    @BindView(R.id.user_signature)
    TextView userSignature;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_discri)
    TextView goodsDiscri;
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    private Second_Goods msecond_goods;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_goods);
        ButterKnife.bind(this);
        msecond_goods = (Second_Goods) getIntent().getSerializableExtra("second_goods");
        user = msecond_goods.getUpload_user();
        initView();

    }

    private void initView() {
        Picasso.with(this)
                .load(msecond_goods.getImagePath())
                .error(R.drawable.logo)
                .into(goodsPic);
        Picasso.with(this)
                .load(user.getHeadPortraitPath())
                .error(R.drawable.logo)
                .into(userHeadPortrait);
        userNickname.setText(user.getNickname());
        if(user.getSex().equals("male")){
            male.setVisibility(View.VISIBLE);
            female.setVisibility(View.GONE);
        }else {
            male.setVisibility(View.GONE);
            female.setVisibility(View.VISIBLE);
        }
        userSignature.setText(user.getSignature());
        goodsPrice.setText(msecond_goods.getPrice()+"");
        goodsDiscri.setText(msecond_goods.getDescription());

    }
    @OnClick(R.id.checked)
    public void onViewClicked() {
        Second_Goods check_goods = new Second_Goods();
        check_goods.setState(1);
        check_goods.update(msecond_goods.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showAndCancel("审核通过");
                    finish();
                }else{
                    ToastUtil.showAndCancel(e.getMessage());
                }
            }
        });
    }
}
