package com.hznu.echo.second_handmarket.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qiuyudong on 2018/4/1.
 */


public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Second_Goods> goodslist;
    private Context mcontext;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_HEADER = 1;

    //构造方法，初始化列表
    public HomeAdapter(List<Second_Goods> prolist, Context context) {
        goodslist = prolist;
        mcontext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.fragment_home_header, parent, false);
            final HeaderHolder holder = new HeaderHolder(view);
            return holder;
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            View view = layoutInflater.inflate(R.layout.home_goods_item, parent, false);
            final GoodsHolder holder = new GoodsHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int position = holder.getAdapterPosition();
//                    Second_Goods second_goods = goodslist.get(position);
//                    Intent intent = new Intent(mcontext, GoodsInformationActivity.class);
//                    intent.putExtra("goods_id", second_goods.getObjectId());
//                    mcontext.startActivity(intent);
                    ToastUtil.showAndCancel("点击了");
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final GoodsHolder goodsholder = (GoodsHolder)holder;
            Second_Goods second_goods = goodslist.get(position-1);
            String pic_uri = second_goods.getImagePath();
            String user_head = second_goods.getUpload_user().getHeadPortraitPath();
            //加载图片
            Picasso.with(mcontext)
                    .load(pic_uri)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(goodsholder.goods_photo);
            //加载头像
            Picasso.with(mcontext)
                    .load(user_head)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(goodsholder.user_head);
            goodsholder.goods_name.setText(second_goods.getName());
            goodsholder.goods_user.setText(second_goods.getUpload_user().getNickname());
            goodsholder.goods_time.setText(second_goods.getUpdatedAt());
            String usersex = second_goods.getUpload_user().getSex();
            Log.d("bmob ", usersex);
            if(usersex.equals("male")){
                goodsholder.male.setVisibility(View.VISIBLE);
                goodsholder.female.setVisibility(View.GONE);
            }else {
                goodsholder.female.setVisibility(View.VISIBLE);
                goodsholder.male.setVisibility(View.GONE);
            }
            goodsholder.goods_name.setText(second_goods.getName());
            goodsholder.goods_price.setText("¥ " + second_goods.getPrice());
            BmobQuery<User>  query  = new BmobQuery<>();
            query.addWhereRelatedTo("liked_user",new BmobPointer(second_goods));
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        goodsholder.liked_number.setText(list.size());
                    } else {
                        goodsholder.liked_number.setText("0");
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
//            // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
//            StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) ((HeaderHolder)holder).hearder_container.getLayoutParams();
//            // 最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
//            clp.setFullSpan(true);
            Banner banner = ((HeaderHolder)holder).banner;
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            List<Integer> list = new ArrayList<>();
            list.add(R.mipmap.test2);
            list.add(R.mipmap.test2);
            list.add(R.mipmap.test2);
            ((HeaderHolder)holder).hotGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Second_Goods x = new Second_Goods();
                    x.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    x.setName("asdasd");
                    Second_Goods y = new Second_Goods();
                    y.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    y.setName("asdasd");
                    Second_Goods z = new Second_Goods();
                    z.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    z.setName("asdasd");
                    goodslist.add(x);
                    goodslist.add(y);
                    goodslist.add(z);
                    notifyDataSetChanged();
                }
            });
            ((HeaderHolder)holder).newGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Second_Goods x = new Second_Goods();
                    x.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    x.setName("wawawa");
                    Second_Goods y = new Second_Goods();
                    y.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    y.setName("waawawa");
                    Second_Goods z = new Second_Goods();
                    z.setImagePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522594157933&di=97996eb1a521fd1b6e1fc3169cb485fd&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b55b319ebc4b745b19f82c1c4fc1e178b8215d9.jpg");
                    z.setName("wawawa");
                    goodslist.add(x);
                    goodslist.add(y);
                    goodslist.add(z);
                    notifyDataSetChanged();
                }
            });
            //设置图片集合
            banner.setImages(list);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

    }

    @Override
    public int getItemCount() {
        return goodslist.size() == 0 ? 0 : goodslist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_HEADER){
                        return 2;
                    }else{
                        return 1;
                    }
                }
            });
        }

    }

    //实现adapter和ViewHolder的列表布局
    class GoodsHolder extends RecyclerView.ViewHolder {
        public TextView goods_name, goods_time, goods_price, goods_user, liked_number;
        public ImageView goods_photo, male, female;
        public CircleImageView user_head;

        public GoodsHolder(View itemView) {
            super(itemView);
            goods_user = (TextView) itemView.findViewById(R.id.user_nickname);
            user_head = (CircleImageView) itemView.findViewById(R.id.user_head_portrait);
            male = (ImageView) itemView.findViewById(R.id.male);
            female = (ImageView) itemView.findViewById(R.id.female);
            goods_time = (TextView) itemView.findViewById(R.id.goods_time);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            liked_number = (TextView) itemView.findViewById(R.id.liked_number);
            goods_photo = (ImageView) itemView.findViewById(R.id.goods_photo);
        }
    }

    //头布局
    class HeaderHolder extends RecyclerView.ViewHolder {
        public Banner banner;
        public LinearLayout hearder_container;
        public Button newGoods, hotGoods;

        public HeaderHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner);
            hearder_container = (LinearLayout) itemView.findViewById(R.id.hearder_container);
            newGoods = (Button) itemView.findViewById(R.id.newGoods);
            hotGoods = (Button) itemView.findViewById(R.id.hotGoods);
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            Picasso.with(context).load((Integer) path).into(imageView);
        }
    }
}