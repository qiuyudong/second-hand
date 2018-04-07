package com.hznu.echo.second_handmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.EditTextLayout;
import com.hznu.echo.second_handmarket.utils.PreferenceUtils;
import com.hznu.echo.second_handmarket.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.password)
    EditTextLayout password;
    @BindView(R.id.repassword)
    EditTextLayout repassword;
    @BindView(R.id.oldpassword)
    EditTextLayout oldpassword;
    private String pw, repw, oldpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        oldpassword.setHint("请输入旧密码");
        password.setHint("请输入新密码");
        repassword.setHint("请重复密码");
    }

    @OnClick(R.id.signup)
    public void onViewClicked() {
        updatePw();
    }

    private void updatePw() {
        oldpw = oldpassword.getTextView();
        Log.e("op", oldpw);
        pw = password.getTextView();
        repw = repassword.getTextView();
        if (pw.equals("") || repw.equals("") || oldpw.equals("")) {
            ToastUtil.showAndCancel("不可为空");
        } else if (pw.length() != 8) {
            ToastUtil.showShort("密码长度错误，8位");
        } else if (!pw.equals(repw)) {
            ToastUtil.showAndCancel("两次输入的密码不一致");
        } else {
            BmobUser.updateCurrentUserPassword(oldpw, pw, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        PreferenceUtils.setBoolean(ResetPasswordActivity.this, "is_user_logined", false);
                        ToastUtil.showAndCancel("密码修改成功，可以用新密码进行登录啦");
                        finish();
                        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                    } else {
                        ToastUtil.showAndCancel("失败:" + e.getMessage());
                    }
                }

            });
        }
    }
}
