package com.hznu.echo.second_handmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.Goods_Comment;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.message_et)
    EditText messageEt;
    @BindView(R.id.submit_bt)
    Button submitBt;
    private Second_Goods msecond_goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.submit_bt})
    public void onViewClicked(View view) {
        String message = messageEt.getText().toString();
        if (message.length() > 140) {
            ToastUtil.showAndCancel("请不要超出字数限制～");
        } else {
            User user = BmobUser.getCurrentUser(User.class);
            msecond_goods = (Second_Goods) getIntent().getSerializableExtra("second_goods");
            Goods_Comment comment = new Goods_Comment();
            comment.setComment(message);
            //添加一对一关联
            comment.setCreate_user(user);
            comment.setCurrent_goods(msecond_goods);
            Log.e("s", comment.toString());
            comment.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        ToastUtil.showAndCancel("添加成功");
                        Intent intent = new Intent(MessageActivity.this, GoodsInformationActivity.class);
                        intent.putExtra("second_goods", msecond_goods);
                        startActivity(intent);
                        ToastUtil.showAndCancel("点击了");
                        finish();
                    } else {
                        ToastUtil.showAndCancel("保存失败" + e.getMessage());
                    }
                }
            });

        }

    }
}
