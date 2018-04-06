package com.hznu.echo.second_handmarket.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.IM.event.RefreshEvent;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.fragment.ClassificationFragment;
import com.hznu.echo.second_handmarket.fragment.ConversationFragment;
import com.hznu.echo.second_handmarket.fragment.HomeFragment;
import com.hznu.echo.second_handmarket.fragment.UserInfoFragment;
import com.hznu.echo.second_handmarket.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private ArrayList<Fragment> fragmentLists;
    private BottomNavigationItem mHomeBtnItem, mUserMainBtnItem,
            mClassificationBtnItem, mCollectionsBtnItem;
    private BottomNavigationBar mBottomNavigationBar;
    private  Toolbar toolbar;
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getNickname(), user.getHeadPortraitPath()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        ToastUtil.showAndCancel(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    ToastUtil.showAndCancel(status.getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
//        IMMLeaks.fixFocusedViewLeak(getApplication());


        fManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            fragmentLists = setFragments();
        }else {
            fragmentLists = getFragments();
        }
        super.onCreate(savedInstanceState);
        //保持竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();



    }

    private void initView() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        //设置导航栏模式
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        //设置导航栏背景模式
        mBottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );

        mHomeBtnItem = new BottomNavigationItem(
                R.drawable.home, "首页").setActiveColorResource(R.color.blue);
        mClassificationBtnItem = new BottomNavigationItem(
                R.drawable.fenlei, "分类").setActiveColorResource(R.color.blue);
        mCollectionsBtnItem = new BottomNavigationItem(
                R.drawable.collection, "收藏").setActiveColorResource(R.color.blue);
        mUserMainBtnItem = new BottomNavigationItem(
                R.drawable.usermain, "我的").setActiveColorResource(R.color.blue);
        mBottomNavigationBar.addItem(mHomeBtnItem)
                .addItem(mClassificationBtnItem)
                .addItem(mCollectionsBtnItem)
                .addItem(mUserMainBtnItem)
                .setFirstSelectedPosition(1)//默认
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);

    }

    private void initData() {
        setDefaultFragment();
        toolbar.setTitle("二手");
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = fManager.beginTransaction();
        transaction.add(R.id.layFrame, fragmentLists.get(1));//默认
        transaction.commit();
    }

    private ArrayList<Fragment> setFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fManager.findFragmentByTag("Home"));
        fragments.add(fManager.findFragmentByTag("Classification"));
        fragments.add(fManager.findFragmentByTag("Collections"));
        fragments.add(fManager.findFragmentByTag("UserMain"));
        return fragments;
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(ClassificationFragment.newInstance("Classification"));
        fragments.add(new ConversationFragment());
        fragments.add(UserInfoFragment.newInstance("UserMain"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentTransaction ft = fManager.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                if (fragment.isAdded()) {
                    //   ft.replace(R.id.layFrame, fragment);
                    ft.show(fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();

            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentTransaction ft = fManager.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();

//                Toast.makeText(MainActivity.this, "onTabUnselected: " + position, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.add_newgoods:
                    startActivity(new Intent(MainActivity.this,AddGoodsActivity.class));
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
           ToastUtil.showAndCancel("youxiaoxi");
        } else {
//            ToastUtil.showAndCancel("youxiaoxi");
        }
    }
}
