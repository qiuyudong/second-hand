package com.hznu.echo.second_handmarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ClassificationFragment extends BaseFragment {


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


    @OnClick({R.id.life_goods, R.id.study_goods, R.id.iv_digital, R.id.iv_beauty, R.id.iv_cloth, R.id.iv_sport, R.id.iv_trans})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.life_goods:
                Intent liftIntent = new Intent(getActivity(),ClassificationActivity.class);
                liftIntent.putExtra("type","生活用品");
                startActivity(liftIntent);
                break;
            case R.id.study_goods:
                Intent studyIntent = new Intent(getActivity(),ClassificationActivity.class);
                studyIntent.putExtra("type","书籍资料");
                startActivity(studyIntent);
                break;
            case R.id.iv_digital:
                Intent digitalIntent = new Intent(getActivity(),ClassificationActivity.class);
                digitalIntent.putExtra("type","手机数码");
                startActivity(digitalIntent);
                break;
            case R.id.iv_beauty:
                Intent beautyIntent = new Intent(getActivity(),ClassificationActivity.class);
                beautyIntent.putExtra("type","美妆护肤");
                startActivity(beautyIntent);
                break;
            case R.id.iv_cloth:
                Intent clothIntent = new Intent(getActivity(),ClassificationActivity.class);
                clothIntent.putExtra("type","服饰配件");
                startActivity(clothIntent);
                break;
            case R.id.iv_sport:
                Intent sportIntent = new Intent(getActivity(),ClassificationActivity.class);
                sportIntent.putExtra("type","运动户外");
                startActivity(sportIntent);
                break;
            case R.id.iv_trans:
                Intent transIntent = new Intent(getActivity(),ClassificationActivity.class);
                transIntent.putExtra("type","出行必备");
                startActivity(transIntent);
                break;
        }
    }
}