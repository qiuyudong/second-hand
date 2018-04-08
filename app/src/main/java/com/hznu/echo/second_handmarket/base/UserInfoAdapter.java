package com.hznu.echo.second_handmarket.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wengqian on 2018/4/1.
 */


public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.CheckedGoods> {
    private List<User> users;
    private Context mcontext;
    private AlertDialog dialog;

    public UserInfoAdapter(List<User> users, Context context) {
        this.users = users;
        mcontext = context;
    }

    @Override
    public CheckedGoods onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.user_item, parent, false);
        final CheckedGoods holder = new CheckedGoods(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(CheckedGoods holder, int position) {
        final User user = users.get(position);
        Picasso.with(mcontext)
                .load(user.getHeadPortraitPath())
                .error(R.drawable.logo)
                .into(holder.goods_iv);
        holder.goods_name.setText("昵称： " + user.getNickname());
        holder.student_id.setText("学号： " + user.getUsername());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(mcontext)
                            .setTitle("提示")
                            .setMessage("是否重置密码")
                            .setNegativeButton(
                                    "取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            ToastUtil.showAndCancel("取消重置");
                                        }

                                    })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.showAndCancel("重置成功");
                                        }
                                    })
                            .setCancelable(false)
                            .show();
                } else {
                    dialog.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //实现adapter和ViewHolder的列表布局
    class CheckedGoods extends RecyclerView.ViewHolder {
        public TextView goods_name,student_id;
        public CircleImageView goods_iv;
        public Button button;

        public CheckedGoods(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.reset);
            student_id = (TextView) itemView.findViewById(R.id.student_id);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            goods_iv = (CircleImageView) itemView.findViewById(R.id.goods_iv);
        }
    }

}