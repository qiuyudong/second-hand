package com.hznu.echo.second_handmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.EditTextLayout;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    @BindView(R.id.rgroup)
    RadioGroup rgroup;
    private String sex = "male";
    @BindView(R.id.studoentId)
    EditTextLayout studoentId;
    @BindView(R.id.username)
    EditTextLayout username;
    @BindView(R.id.eamil)
    EditTextLayout eamil;
    @BindView(R.id.password)
    EditTextLayout password;
    @BindView(R.id.repassword)
    EditTextLayout repassword;
    private String  user_name, password1 ,password2,student_id,user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initView();

    }

    @OnClick(R.id.signup)
    public void onViewClicked() {
        initData();
        isEmpty();
    }

    private void initView() {
        studoentId.setHint("请输入学号");
        studoentId.setInputType(InputType.TYPE_CLASS_NUMBER);
        username.setHint("请输入用户名");
        username.setInputType(InputType.TYPE_CLASS_TEXT);
        eamil.setHint("请输入邮箱");
        eamil.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        password.setHint("请输入密码");
        repassword.setHint("请重复密码");
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radbtn = (RadioButton) findViewById(i);
                if(radbtn.getText().equals("男")){
                    sex = "male";
                }else {
                    sex = "female";
                }
                Log.e(TAG, sex );
            }
        });
    }

    //从view获取数据
    private void initData() {
        user_name = username.getTextView();
        student_id = studoentId.getTextView();
        user_email = eamil.getTextView();
        password1 = password.getTextView();
        password2 = repassword.getTextView();
    }

    //非空判断
    private void isEmpty() {
        if (user_name.equals("") || password.equals("") || password2.equals("")
                || student_id.equals("") || user_email.equals("")) {
            ToastUtil.showShort("不能为空");
        } else if (!password1.equals(password2)) {
            ToastUtil.showShort("两次输入的密码不一致");
        } else if (student_id.length() != 10) {
            ToastUtil.showShort("学号格式不对");
        } else if(password1.length() != 8) {
            ToastUtil.showShort("密码长度错误，8位");
        }else {
                User user = new User();
                user.setUsername(student_id);
                user.setPassword(password1);
                user.setEmail(user_email);
                user.setRole(1);
                user.setSex(sex);
                user.setNickname(user_name);
                user.setSignature("这家伙很懒噢～什么都没有留下～");
                user.setHeadPortraitPath("http://bmob-cdn-15170.b0.upaiyun.com/2018/04/07/10a9e50edf204503b1946fcc3ddc6d29.jpg");
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            ToastUtil.showShort("注册成功，请去邮箱激活!");
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtil.showShort(e.getMessage());
                            Log.d(TAG, "done: " + e.getErrorCode());
                            if (e.getErrorCode() == 202) {
                                ToastUtil.showShort("用户名已经存在");
                                return;
                            }

                        }
                    }
                });
            }
        }
    }

