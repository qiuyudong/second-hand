package com.hznu.echo.second_handmarket.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.os.Environment.getExternalStorageDirectory;
import static com.hznu.echo.second_handmarket.R.id.iv_photo_show;

public class AddGoodsActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(iv_photo_show)
    ImageView ivPhotoShow;
    @BindView(R.id.take_photo)
    TextView takePhoto;
    @BindView(R.id.upload_button)
    Button uploadButton;
    @BindView(R.id.tv_price)
    EditText tvPrice;


//    //照片路径
//    private String PHOTO_PIC_PATH = Environment
//            .getExternalStorageDirectory().getAbsolutePath() + "/second-goods.jpg";
//    private File outputImage;
//    public static final int TAKE_PHOTO = 1;
//    public static final int CROP_PHOTO = 2;
//    private Uri imageUri;
    private ListView popListOne;
    private List<String> currentTypeLists;
    private AlertDialog dialog;
    private PopupWindow popupWindow;
    private Second_Goods mSecond_goods;
    private ProgressDialog progressdialog;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_publish_image.jpg";
    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory() +"/"+ IMAGE_FILE_NAME;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;
    private PopupWindow mPopWindow;//弹出窗口
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
                popupWindow.showAsDropDown(view,200,80);
                popListOne.setAdapter(new PopAdapter(TypeData.typeLists));
                break;
            case R.id.take_photo:
                showImageSelectPopupWindow();
                break;
            case R.id.upload_button:
                uploadImage();
                break;
        }
    }

//    //拍照
//    private void takePhoto() {
//
//        outputImage = new File(PHOTO_PIC_PATH);
//        // 创建File对象，储存拍照后的照片，存在根目录下
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (Exception e) {
//            e.printStackTrace();// TODO: handle exception
//        }
//        imageUri = Uri.fromFile(outputImage);
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, TAKE_PHOTO);// 启动相机程序
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    intent.setDataAndType(imageUri, "image/*");
//                    intent.putExtra("scale", true);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent, CROP_PHOTO);
//                }
//                break;
//            case CROP_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        ivPhotoShow.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();// TODO: handle exception
//                    }
//                }
//                break;
//            default:
//                break;
//
//
//        }
//    }


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
        final BmobFile bmobFile = new BmobFile(new File(IMAGE_PATH));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //--返回的上传文件的完整地址
                    getInfo(bmobFile.getFileUrl());
                    Log.d("bmob11", "上传文件成功:" + bmobFile.getFileUrl());
                } else {
                    ToastUtil.showAndCancel("上传文件失败：" + e.getMessage());
                    progressdialog.dismiss();
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
            mSecond_goods.setPrice(Float.parseFloat(price));
            User user = BmobUser.getCurrentUser(User.class);//获取已登录的用户
            mSecond_goods.setUpload_user(user);
            mSecond_goods.setImagePath(imagepath);
            mSecond_goods.setLiked_number(0);
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


    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(
                            getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(this, "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);

                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            ivPhotoShow.setImageBitmap(photo);
            saveBitmapToSDCard(photo);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    private void showImageSelectPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_image, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        //外部是否可以点击
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        Button button_camera = (Button) contentView.findViewById(R.id.camera);
        Button button_album = (Button) contentView.findViewById(R.id.album);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseHeadImageFromCameraCapture();
                mPopWindow.dismiss();
            }
        });

        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseHeadImageFromGallery();
                mPopWindow.dismiss();
            }
        });

        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }

    public static void saveBitmapToSDCard(Bitmap bitmap) {
        File file = new File(IMAGE_PATH);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(IMAGE_PATH);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }
        } catch (Exception e) {
            Log.d("ssss",e.toString());
        }
    }
}
