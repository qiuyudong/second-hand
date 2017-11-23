package com.hznu.echo.second_handmarket.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.base.BasePopWindow;
import com.hznu.echo.second_handmarket.bean.Second_Goods;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.hznu.echo.second_handmarket.utils.TypeData;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.hznu.echo.second_handmarket.R.id.iv_photo_show;

public class AddGoodsActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_description)
    EditText tvDescription;
    @BindView(iv_photo_show)
    ImageView ivPhotoShow;
    @BindView(R.id.take_photo)
    TextView takePhoto;
    @BindView(R.id.upload_button)
    Button uploadButton;
    @BindView(R.id.tv_price)
    EditText tvPrice;


    //照片路径
    private String PHOTO_PIC_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/second-goods.jpg";
    private File outputImage;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri;
    private ListView popListOne;
    private List<String> currentTypeLists;
    private AlertDialog dialog;
    private PopupWindow popupWindow;
    private Second_Goods mSecond_goods;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        ButterKnife.bind(this);
        tvPrice.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);//设置只能输入数字
        initPopView();

    }

    @OnClick({R.id.tv_type, R.id.take_photo, R.id.upload_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                // popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                popupWindow.showAsDropDown(view, 80, 20);
                popListOne.setAdapter(new PopAdapter(TypeData.typeLists));
                break;
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.upload_button:
                uploadImage();
                break;
        }
    }

    //拍照
    private void takePhoto() {

        outputImage = new File(PHOTO_PIC_PATH);
        // 创建File对象，储存拍照后的照片，存在根目录下
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);// 启动相机程序
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivPhotoShow.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();// TODO: handle exception
                    }
                }
                break;
            default:
                break;


        }
    }


    //类型选择
    private void initPopView() {

        popupWindow = new BasePopWindow();
        int width = getResources().getDisplayMetrics().widthPixels * 2 / 3;
        int height = getResources().getDisplayMetrics().heightPixels * 1 / 2;
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);

        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_type, null);
        popupWindow.setContentView(view);
        popListOne = (ListView) view.findViewById(R.id.lv_one);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//点击外侧是否关闭
        currentTypeLists = TypeData.typeLists;
        popListOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvType.setText(currentTypeLists.get(position));
                popupWindow.dismiss();
            }
        });

    }

    class PopAdapter extends BaseAdapter {

        private List<String> lists;

        public PopAdapter(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddGoodsActivity.this)
                        .inflate(R.layout.pop_list_item, null);
                holder.nameView = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (getItem(position) != null) {
                holder.nameView.setText(getItem(position).toString());
            }
            return convertView;

        }
    }

    private static class ViewHolder {
        TextView nameView;
    }

    private void uploadImage() {
        showProgressDialog();
        final BmobFile bmobFile = new BmobFile(new File(PHOTO_PIC_PATH));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //--返回的上传文件的完整地址
                    getInfo(bmobFile.getFileUrl());
                    Log.d("bmob11", "上传文件成功:" + bmobFile.getFileUrl());
                } else {
                    ToastUtil.showAndCancel("上传文件失败：" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });

    }

    private void getInfo(String imagepath) {
        String name = tvName.getText().toString();
        String type = tvType.getText().toString();
        String desc = tvDescription.getText().toString();
        String price = tvPrice.getText().toString();
        if (name.equals("") || type.equals("") || desc.equals("")) {
            ToastUtil.showAndCancel("不可为空");
        } else {
            mSecond_goods = new Second_Goods();
            mSecond_goods.setName(name);
            mSecond_goods.setType(type);
            mSecond_goods.setDescription(desc);
            mSecond_goods.setPrice(price);
            mSecond_goods.setUpload_time(System.currentTimeMillis() + "");
            User user1 = BmobUser.getCurrentUser(User.class);//获取已登录的用户
            mSecond_goods.setUpload_user(user1.getObjectId());
            mSecond_goods.setUpload_user_nickname(user1.getNickname());
            mSecond_goods.setImagePath(imagepath);
            mSecond_goods.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        progressdialog.dismiss();
                        ToastUtil.showShort("数据上传成功");
                        finish();
                    } else {
                        progressdialog.dismiss();
                        ToastUtil.showShort("数据上传失败" + e.getMessage() + "," + e.getErrorCode());
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    //返回
    @Override
    public void onBackPressed() {
        hesitate();
    }

    private void hesitate() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否放弃保存的信息")
                    .setNegativeButton(
                            "取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(AddGoodsActivity.this,
                                            "暂不放弃", Toast.LENGTH_SHORT).show();
                                }

                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AddGoodsActivity.this,
                                            "确定放弃", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                    .setCancelable(false)
                    .show();
        } else {
            dialog.show();
        }
    }

    private void showProgressDialog() {
        progressdialog = new ProgressDialog(this);
        progressdialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressdialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        progressdialog.setTitle("提示");
        progressdialog.setMessage("正在上传");
        progressdialog.show();
    }

}
