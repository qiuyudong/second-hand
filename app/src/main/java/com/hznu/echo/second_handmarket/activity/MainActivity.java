package com.hznu.echo.second_handmarket.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.fragment.ClassificationFragment;
import com.hznu.echo.second_handmarket.fragment.CollectionFragment;
import com.hznu.echo.second_handmarket.fragment.HomeFragment;
import com.hznu.echo.second_handmarket.fragment.UserInfoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private ArrayList<Fragment> fragmentLists;
    private BottomNavigationItem mHomeBtnItem, mUserMainBtnItem,
            mClassificationBtnItem, mCollectionsBtnItem;
    private BottomNavigationBar mBottomNavigationBar;
    private  Toolbar toolbar;
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        fragments.add(CollectionFragment.newInstance("Collections"));
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
//                switch (position){
//                    case 0 :
//                        toolbar.setTitle(" 地图");
//                        toolbar.setLogo(R.drawable.top_map);
//                        break;
//                    case 1:
//                        toolbar.setTitle(" 问题列表");
//                        toolbar.setLogo(R.drawable.top_Classification);
//                        //  BadgeItem badgeItem = mClassificationNumberBadgeItem.hide().setText("0");
//                        // mClassificationBtnItem.setBadgeItem(badgeItem);
//                        break;
//                    case 2 :
//                        toolbar.setTitle(" 联系人");
//                        toolbar.setLogo(R.drawable.top_contacts);
//                        break;
//                    case 3:
//                        toolbar.setTitle(" 个人信息");
//                        toolbar.setLogo(R.drawable.top_person);
//                        break;
//                    default:
//                        break;
//                }

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
//        Toast.makeText(MainActivity.this, "onTabReselected: " + position, Toast.LENGTH_SHORT).show();
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
}
