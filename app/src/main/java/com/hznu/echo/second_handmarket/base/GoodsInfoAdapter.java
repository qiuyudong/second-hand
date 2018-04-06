package com.hznu.echo.second_handmarket.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.activity.ChatActivity;
import com.hznu.echo.second_handmarket.activity.MessageActivity;
import com.hznu.echo.second_handmarket.bean.Goods_Comment;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qiuyudong on 2018/4/1.
 */


public class GoodsInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Goods_Comment> commentslist;
    private Second_Goods second_goods;
    private boolean isliked;
    private Context mcontext;
    private BmobIMUserInfo info;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //头布局
    static final int TYPE_HEADER = 1;
    //脚布局
    static final int TYPE_FOOTER = 2;
    //构造方法，初始化列表

    public GoodsInfoAdapter(List<Goods_Comment> prolist, Second_Goods second_goods, boolean isliked,Context context) {
        commentslist = prolist;
        mcontext = context;
        this.second_goods = second_goods;
        this.isliked = isliked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.goods_info_header, parent, false);
            final HeaderHolder holder = new HeaderHolder(view);
            return holder;
        } else if(viewType == TYPE_FOOTER){
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.goods_info_footer, parent, false);
            final FooterHolder holder = new FooterHolder(view);
            return holder;
        }else {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.goods_info_comment, parent, false);
            final CommentsHolder holder = new CommentsHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            CommentsHolder mholder = (CommentsHolder)holder;
            Goods_Comment mcomment = commentslist.get(position - 1);
            User user = mcomment.getCreate_user();
            Picasso.with(mcontext)
                    .load(user.getHeadPortraitPath())
                    .error(R.drawable.logo)
                    .into(mholder.user_head);
            mholder.user_nickname.setText(user.getNickname());
            mholder.comment_content.setText(mcomment.getComment());
            mholder.comment_time.setText(mcomment.getCreatedAt());
            if(user.getSex().equals("male")){
                mholder.male.setVisibility(View.VISIBLE);
                mholder.female.setVisibility(View.GONE);
            }else{
                mholder.female.setVisibility(View.VISIBLE);
                mholder.male.setVisibility(View.GONE);
            }

        }
        else if (getItemViewType(position) == TYPE_HEADER){
            final HeaderHolder mholder = (HeaderHolder) holder;
            User user = second_goods.getUpload_user();
            //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
            info = new BmobIMUserInfo(user.getObjectId(), user.getNickname(), user.getHeadPortraitPath());
            Picasso.with(mcontext)
                    .load(user.getHeadPortraitPath())
                    .error(R.drawable.logo)
                    .into(mholder.user_head);
            Picasso.with(mcontext)
                    .load(second_goods.getImagePath())
                    .error(R.drawable.logo)
                    .into(mholder.goods_pic);
            mholder.user_nickname.setText(user.getNickname());
            if(user.getSex().equals("male")){
                mholder.male.setVisibility(View.VISIBLE);
                mholder.female.setVisibility(View.GONE);
            }else{
                mholder.female.setVisibility(View.VISIBLE);
                mholder.male.setVisibility(View.GONE);
            }
            mholder.user_signature.setText(user.getSignature());
            mholder.goods_price.setText(second_goods.getPrice());
            mholder.goods_discr.setText(second_goods.getDescription());
            mholder.comment_count.setText("留言 - " + commentslist.size());
            if(isliked){
                mholder.goods_like.setVisibility(View.VISIBLE);
                mholder.goods_not_like.setVisibility(View.GONE);
            }else {
                mholder.goods_like.setVisibility(View.GONE);
                mholder.goods_not_like.setVisibility(View.VISIBLE);
            }
            mholder.like_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(isliked){
                       setUnLiked();
                   }else {
                       setLiked();
                   }
                }
            });
        }else {
            FooterHolder mholder = (FooterHolder) holder;
            mholder.message_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, MessageActivity.class);
                    intent.putExtra("second_goods", second_goods);
                    mcontext.startActivity(intent);
                    ((Activity)mcontext).finish();
                }
            });
            mholder.chat_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showAndCancel("交流");
                    chat();
                }
            });
        }
    }


    /**
     * 与陌生人聊天
     */
    private void chat() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            ToastUtil.showAndCancel("尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个常态会话入口，陌生人聊天
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("c", conversationEntrance);
        Intent intent = new Intent(mcontext,ChatActivity.class);
        intent.putExtra("c",conversationEntrance);
//        intent.putExtra(mcontext.getPackageName(),bundle);
        mcontext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return commentslist.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == commentslist.size() + 1){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    //实现adapter和ViewHolder的列表布局
    class CommentsHolder extends RecyclerView.ViewHolder {
        public CircleImageView user_head;
        public TextView user_nickname,comment_content,comment_time;
        public ImageView male,female;
        public CommentsHolder(View itemView) {
            super(itemView);
            user_head = (CircleImageView) itemView.findViewById(R.id.user_head_portrait);
            user_nickname = (TextView) itemView.findViewById(R.id.user_nickname);
            comment_content = (TextView) itemView.findViewById(R.id.comment_content);
            comment_time = (TextView) itemView.findViewById(R.id.comment_time);
            male = (ImageView) itemView.findViewById(R.id.male);
            female = (ImageView) itemView.findViewById(R.id.female);
        }
    }

    //头布局
    class HeaderHolder extends RecyclerView.ViewHolder {
        public CircleImageView user_head;
        public ImageView male,female,goods_like,goods_not_like,goods_pic;
        public TextView user_nickname,user_signature,goods_price,goods_discr,comment_count;
        public LinearLayout like_ll;
        public HeaderHolder(View itemView){
            super(itemView);
            user_head = (CircleImageView) itemView.findViewById(R.id.user_head_portrait);
            user_nickname = (TextView) itemView.findViewById(R.id.user_nickname);
            male = (ImageView) itemView.findViewById(R.id.male);
            female = (ImageView) itemView.findViewById(R.id.female);
            user_signature = (TextView) itemView.findViewById(R.id.user_signature);
            goods_pic = (ImageView) itemView.findViewById(R.id.goods_pic);
            goods_like = (ImageView) itemView.findViewById(R.id.goods_like);
            goods_not_like = (ImageView) itemView.findViewById(R.id.goods_not_like);
            like_ll = (LinearLayout) itemView.findViewById(R.id.goods_liked_ll);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            goods_discr = (TextView) itemView.findViewById(R.id.goods_discri);
            comment_count = (TextView) itemView.findViewById(R.id.comment_count);
        }

    }

    //脚布局
    class FooterHolder extends RecyclerView.ViewHolder {
        TextView message_tv,chat_tv;
        public FooterHolder(View itemView) {
            super(itemView);
            message_tv = (TextView) itemView.findViewById(R.id.message_tv);
            chat_tv = (TextView) itemView.findViewById(R.id.chat_tv);
        }
    }

    public void setLiked(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        //将当前用户添加到多对多关联中
        relation.add(currentUser);
        //多对多关联指向`post`的`likes`字段
        second_goods.setLiked_user(relation);
        second_goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    setUserLike();
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
    public void setUnLiked(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        //将当前用户添加到多对多关联中
        relation.remove(currentUser);
        //多对多关联指向`post`的`likes`字段
        second_goods.setLiked_user(relation);
        second_goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                  setUserUnlike();
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }

    public void setUserLike (){
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        //将当前用户添加到多对多关联中
        relation.add(second_goods);
        //多对多关联指向`post`的`likes`字段
        currentUser.setLiked_goods(relation);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showAndCancel("点赞");
                    isliked = true;
                    notifyDataSetChanged();
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
    public void setUserUnlike(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        //将当前用户添加到多对多关联中
        relation.remove(second_goods);
        //多对多关联指向`post`的`likes`字段
        currentUser.setLiked_goods(relation);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showAndCancel("取消赞");
                    isliked = false;
                    notifyDataSetChanged();
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
}