package com.hznu.echo.second_handmarket.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.EditPersoninfo;
import com.hznu.echo.second_handmarket.activity.LoginActivity;
import com.hznu.echo.second_handmarket.activity.ResetPasswordActivity;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by beyond on 17/4/11.
 */

public class UserInfoFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.check_number)
    TextView checkNumber;
    @BindView(R.id.check_rl)
    RelativeLayout checkRl;
    @BindView(R.id.published_number)
    TextView publishedNumber;
    @BindView(R.id.published_rl)
    RelativeLayout publishedRl;
    @BindView(R.id.liked_number)
    TextView likedNumber;
    @BindView(R.id.liked_rl)
    RelativeLayout likedRl;
    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.check_pic)
    ImageView checkPic;
    @BindView(R.id.check_arrow)
    TextView checkArrow;
    @BindView(R.id.published_pic)
    ImageView publishedPic;
    @BindView(R.id.published_arrow)
    TextView publishedArrow;
    @BindView(R.id.liked_pic)
    ImageView likedPic;
    @BindView(R.id.liked_arrow)
    TextView likedArrow;
    @BindView(R.id.setting_pic)
    ImageView settingPic;
    @BindView(R.id.setting_arrow)
    TextView settingArrow;
    @BindView(R.id.nickname)
    TextView nickname;


    private String objectId;
    private User currentUser;

    public static UserInfoFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        currentUser = BmobUser.getCurrentUser(User.class);//获取已登录的用户
        objectId = currentUser.getObjectId();
        getLikedNumeber();
        getPublishGoods();
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(objectId, new QueryListener<User>() {

            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    currentUser = object;
                    setInfo(currentUser);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.logout, R.id.check_rl, R.id.published_rl, R.id.liked_rl, R.id.setting, R.id.set_pw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.check_rl:
                break;
            case R.id.published_rl:
                break;
            case R.id.liked_rl:
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(), EditPersoninfo.class);
                startActivity(intent);
                break;
            case R.id.set_pw:
                Intent intentpw = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intentpw);
                break;
        }
    }

    //退出登录
    private void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("登出");
        dialog.setMessage("是否确认注销！");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                //将登录状态退出
                PreferenceUtils.setBoolean(getActivity(),
                        "is_user_logined", false);
                getActivity().finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getLikedNumeber() {
        BmobQuery<Second_Goods> query = new BmobQuery<Second_Goods>();
        query.addWhereRelatedTo("liked_goods", new BmobPointer(currentUser));
        query.findObjects(new FindListener<Second_Goods>() {

            @Override
            public void done(List<Second_Goods> object, BmobException e) {
                if (e == null) {
                    likedNumber.setText(object.size() + "");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }

        });

    }

    private void getPublishGoods() {
        BmobQuery<Second_Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("upload_user", new BmobPointer(currentUser));
        query.include("upload_user");
        query.findObjects(new FindListener<Second_Goods>() {

            @Override
            public void done(List<Second_Goods> objects, BmobException e) {
                if (e == null) {
                    publishedNumber.setText(objects.size() + "");
                } else {
                    Log.e("bmob", "done: " + e.getMessage());
                }
            }
        });

    }

    //设置信息
    private void setInfo(User user) {
        if (user.getHeadPortraitPath() != null) {
            Picasso.with(getActivity())
                    .load(user.getHeadPortraitPath())
                    //设置加载失败的图片显示
                    .error(R.drawable.touxiang)
                    .into(imageView);
            nickname.setText(user.getNickname());
        }
    }


}
