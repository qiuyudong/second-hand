package com.hznu.echo.second_handmarket.activity;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hznu.echo.second_handmarket.R;
import com.hznu.echo.second_handmarket.bean.User;
import com.hznu.echo.second_handmarket.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.getExternalStorageDirectory;

public class EditPersoninfo extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.nickname_et)
    EditText nicknameEt;
    @BindView(R.id.sex_et)
    TextView sexEt;
    @BindView(R.id.studentid)
    TextView studentid;
    @BindView(R.id.email_et)
    TextView emailEt;
    @BindView(R.id.sign_et)
    EditText signEt;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory() +"/"+ IMAGE_FILE_NAME;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;
    private PopupWindow mPopWindow;//弹出窗口
    private User currentUser;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_edit_info);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser(User.class).getObjectId(), new QueryListener<User>() {

            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                   currentUser = object;
                    if(currentUser.getHeadPortraitPath() != null){
                        Picasso.with(EditPersoninfo.this)
                                .load(currentUser.getHeadPortraitPath())
                                .into(imageView);
                    }
                    nicknameEt.setText(currentUser.getNickname());
                    sexEt.setText(currentUser.getSex());
                    studentid.setText(currentUser.getUsername());
                    emailEt.setText(currentUser.getEmail());
                    signEt.setText(currentUser.getSignature());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }
    @OnClick({R.id.back, R.id.save, R.id.imageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                hesitate();
                break;
            case R.id.save:
                saveInfo();
                break;
            case R.id.imageView:
                showImageSelectPopupWindow();
                break;
        }
    }

    private void saveInfo(){
        String nickname = nicknameEt.getText().toString();
        String sign = signEt.getText().toString();
        if(nickname == "" || sign == ""){
            ToastUtil.showAndCancel("不可为空");
        }else {
            User user = new User();
            user.setNickname(nickname);
            user.setSignature(sign);
            user.update(currentUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ToastUtil.showAndCancel("更新用户信息成功");
                        finish();
                    } else {
                        ToastUtil.showAndCancel("更新用户信息失败:" + e.getMessage());
                    }
                }
            });
        }
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
                                    Toast.makeText(EditPersoninfo.this,
                                            "暂不放弃", Toast.LENGTH_SHORT).show();
                                }

                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(EditPersoninfo.this,
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
            imageView.setImageBitmap(photo);
            saveBitmapToSDCard(photo);
            uploadImage();
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

    private void uploadImage(){
        final BmobFile bmobFile = new BmobFile(new File(IMAGE_PATH));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    User user = new User();
                    user.setHeadPortraitPath( bmobFile.getFileUrl());
                    user.update(currentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                ToastUtil.showAndCancel("上传成功");
                            }
                        }
                    });
                }else{
                    Log.d("sss",e.toString());
                    ToastUtil.showAndCancel("上传头像失败--" + e.toString());
                }

            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
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
