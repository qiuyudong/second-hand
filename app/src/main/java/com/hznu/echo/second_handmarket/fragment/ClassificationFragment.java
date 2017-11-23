package com.hznu.echo.second_handmarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.ClassificationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by beyond on 17/4/11.
 */

public class ClassificationFragment extends BaseFragment1 {


    @BindView(R.id.car_goods)
    ImageView carGoods;
    @BindView(R.id.study_goods)
    ImageView studyGoods;
    @BindView(R.id.life_goods)
    ImageView lifeGoods;
    @BindView(R.id.myScrollView)
    ScrollView myScrollView;
    Unbinder unbinder;

    public static ClassificationFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        ClassificationFragment fragment = new ClassificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classfication, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.car_goods, R.id.study_goods, R.id.life_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.car_goods:
                Intent intent = new Intent(getActivity(),ClassificationActivity.class);
                intent.putExtra("type","出行");
                startActivity(intent);
                break;
            case R.id.study_goods:
                Intent intent1 = new Intent(getActivity(),ClassificationActivity.class);
                intent1.putExtra("type","学习");
                startActivity(intent1);
                break;
            case R.id.life_goods:
                Intent intent2 = new Intent(getActivity(),ClassificationActivity.class);
                intent2.putExtra("type","生活");
                startActivity(intent2);
                break;
        }
    }
}