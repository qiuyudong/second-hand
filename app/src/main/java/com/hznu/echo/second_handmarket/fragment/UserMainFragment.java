//package com.hznu.echo.second_handmarket.fragment;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.Toast;
//
//import com.hznu.echo.second_handmarket.R;
//import com.hznu.echo.second_handmarket.activity.LoginActivity;
//import com.hznu.echo.second_handmarket.bean.User;
//import com.hznu.echo.second_handmarket.utils.PreferenceUtils;
//import com.hznu.echo.second_handmarket.utils.ToastUtil;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//import java.io.FileOutputStream;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import cn.bmob.v3.BmobQuery;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.datatype.BmobFile;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.QueryListener;
//import cn.bmob.v3.listener.UpdateListener;
//import cn.bmob.v3.listener.UploadFileListener;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static android.os.Environment.getExternalStorageDirectory;
//
///**
// * Created by beyond on 17/4/11.
// */
//
//public class UserMainFragment extends BaseFragment {
//    @BindView(R.id.nickname)
//    EditText nickname;
//    @BindView(R.id.sex)
//    EditText sex;
//    @BindView(R.id.email)
//    EditText email;
//    @BindView(R.id.school)
//    EditText school;
//    @BindView(R.id.edit)
//    Button edit;
//    @BindView(R.id.logout)
//    Button logout;
//    Unbinder unbinder;
//    @BindView(R.id.save)
//    Button save;
//    @BindView(R.id.imageView)
//    CircleImageView headImage;
//
//    /* 头像文件 */
//    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
//    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory() +"/"+ IMAGE_FILE_NAME;
//
//    /* 请求识别码 */
//    private static final int CODE_GALLERY_REQUEST = 0xa0;
//    private static final int CODE_CAMERA_REQUEST = 0xa1;
//    private static final int CODE_RESULT_REQUEST = 0xa2;
//
//    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
//    private static int output_X = 480;
//    private static int output_Y = 480;
//    private PopupWindow mPopWindow;//弹出窗口
//
//    private String objectId  = null;
//
//    public static UserMainFragment newInstance(String name) {
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        UserMainFragment fragment = new UserMainFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        User user1 = BmobUser.getCurrentUser(User.class);//获取已登录的用户
//        objectId = user1.getObjectId();
//        nickname.setText(PreferenceUtils.getString(getActivity(), "USER_NAME", "null"));
//        sex.setText(PreferenceUtils.getString(getActivity(), "USER_SEX", "null"));
//        email.setText(PreferenceUtils.getString(getActivity(), "USER_EMAIL", "null"));
//        school.setText(PreferenceUtils.getString(getActivity(), "USER_SCHOOL", "null"));
//        headImage.setImageBitmap(getBitmap(IMAGE_PATH));
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick({R.id.imageView, R.id.edit, R.id.logout, R.id.save})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.imageView:
//                showImageSelectPopupWindow();
//                break;
//            case R.id.edit:
//                edit();
//                break;
//            case R.id.logout:
//                logout();
//                break;
//            case R.id.save:
//                save();
//                break;
//        }
//    }
//
//    //编辑相关状态
//    private void edit() {
//        edit.setVisibility(View.GONE);
//        save.setVisibility(View.VISIBLE);
//        nickname.setFocusable(true);
//        nickname.setFocusableInTouchMode(true);
//        nickname.requestFocus();
//        sex.setFocusable(true);
//        sex.setFocusableInTouchMode(true);
//        email.setFocusable(true);
//        email.setFocusableInTouchMode(true);
//        school.setFocusable(true);
//        school.setFocusableInTouchMode(true);
//    }
//
//    //保存
//    private void save() {
//        final String nick_name = nickname.getText().toString();
//        final String sex_ = sex.getText().toString();
//        final String email_ = email.getText().toString();
//        final String school_ = school.getText().toString();
//        if (nick_name.equals("") || sex_.equals("") || email_.equals("") || school_.equals("")) {
//            ToastUtil.showShort("不可为空");
//        } else {
//            User user = new User();
//            user.setNickname(nick_name);
//            user.setSex(sex_);
//            user.setSchool(school_);
//            user.setE_mail(email_);
//            user.update(objectId, new UpdateListener() {
//                @Override
//                public void done(BmobException e) {
//                    if (e == null) {
//                        ToastUtil.showAndCancel("更新用户信息成功");
//                        PreferenceUtils.setString(getContext(), "USER_NAME", nick_name);
//                        PreferenceUtils.setString(getContext(), "USER_SEX", sex_);
//                        PreferenceUtils.setString(getContext(), "USER_EMAIL", email_);
//                        PreferenceUtils.setString(getContext(), "USER_SCHOOL", school_);
//                        initview();
//                    } else {
//                        ToastUtil.showAndCancel("更新用户信息失败:" + e.getMessage());
//                    }
//                }
//            });
//        }
//    }
//
//    //退出登录
//    private void logout() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        dialog.setTitle("登出");
//        dialog.setMessage("是否确认注销！");
//        dialog.setCancelable(false);
//        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                //将登录状态退出
//                PreferenceUtils.setBoolean(getActivity(),
//                        "is_user_logined", false);
//                getActivity().finish();
//            }
//        });
//        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    //储存相关信息
//    private void initview() {
//        nickname.setFocusable(false);
//        sex.setFocusable(false);
//        email.setFocusable(false);
//        school.setFocusable(false);
//        edit.setVisibility(View.VISIBLE);
//        save.setVisibility(View.GONE);
//    }
//
//
//
//
//    // 从本地相册选取图片作为头像
//    private void choseHeadImageFromGallery() {
//        Intent intentFromGallery = new Intent();
//        // 设置文件类型
//        intentFromGallery.setType("image/*");
//        intentFromGallery.setAction(Intent.ACTION_PICK);
//        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
//    }
//
//    // 启动手机相机拍摄照片作为头像
//    private void choseHeadImageFromCameraCapture() {
//        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // 判断存储卡是否可用，存储照片文件
//        if (hasSdcard()) {
//            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
//                    .fromFile(new File(
//                            getExternalStorageDirectory(), IMAGE_FILE_NAME)));
//        }
//
//        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent intent) {
//
//        // 用户没有进行有效的设置操作，返回
//        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        switch (requestCode) {
//            case CODE_GALLERY_REQUEST:
//                cropRawPhoto(intent.getData());
//                break;
//
//            case CODE_CAMERA_REQUEST:
//                if (hasSdcard()) {
//                    File tempFile = new File(
//                            getExternalStorageDirectory(),
//                            IMAGE_FILE_NAME);
//                    cropRawPhoto(Uri.fromFile(tempFile));
//                } else {
//                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
//                            .show();
//                }
//                break;
//
//            case CODE_RESULT_REQUEST:
//                if (intent != null) {
//                    setImageToHeadView(intent);
//
//                }
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, intent);
//    }
//
//    /**
//     * 裁剪原始的图片
//     */
//    public void cropRawPhoto(Uri uri) {
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//
//        // aspectX , aspectY :宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        // outputX , outputY : 裁剪图片宽高
//        intent.putExtra("outputX", output_X);
//        intent.putExtra("outputY", output_Y);
//        intent.putExtra("return-data", true);
//
//        startActivityForResult(intent, CODE_RESULT_REQUEST);
//    }
//
//    /**
//     * 提取保存裁剪之后的图片数据，并设置头像部分的View
//     */
//    private void setImageToHeadView(Intent intent) {
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            headImage.setImageBitmap(photo);
//            saveBitmapToSDCard(photo);
//            uploadImage();
//        }
//    }
//
//    /**
//     * 检查设备是否存在SDCard的工具方法
//     */
//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            // 有存储的SDCard
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//
//    private void showImageSelectPopupWindow() {
//        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_image, null);
//        mPopWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
//        mPopWindow.setContentView(contentView);
//        //外部是否可以点击
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopWindow.setOutsideTouchable(true);
//        Button button_camera = (Button) contentView.findViewById(R.id.camera);
//        Button button_album = (Button) contentView.findViewById(R.id.album);
//        button_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choseHeadImageFromCameraCapture();
//                mPopWindow.dismiss();
//            }
//        });
//
//        button_album.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choseHeadImageFromGallery();
//                mPopWindow.dismiss();
//            }
//        });
//
//        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
//        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
//    }
//
//    private void uploadImage(){
//        final BmobFile bmobFile = new BmobFile(new File(IMAGE_PATH));
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    User user = new User();
//                    user.setHeadPortraitPath( bmobFile.getFileUrl());
//                    user.update(objectId, new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                             if(e == null){
//                                 ToastUtil.showAndCancel("上传成功");
//                             }
//                        }
//                    });
//                }else{
//                    Log.d("sss",e.toString());
//                    ToastUtil.showAndCancel("上传头像失败--" + e.toString());
//                }
//
//            }
//            @Override
//            public void onProgress(Integer value) {
//                // 返回的上传进度（百分比）
//            }
//        });
//    }
//
//    public static void saveBitmapToSDCard(Bitmap bitmap) {
//        File file = new File(IMAGE_PATH);
//        if(file.exists()){
//            file.delete();
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(IMAGE_PATH);
//            if (fos != null) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                fos.close();
//            }
//        } catch (Exception e) {
//            Log.d("ssss",e.toString());
//        }
//    }
//
//
//    private Bitmap getBitmap(String pathString)
//    {
//        Bitmap bitmap = null;
//        try
//        {
//            File file = new File(pathString);
//            if(file.exists())
//            {  //本地读取图片
//                bitmap = BitmapFactory.decodeFile(pathString);
//            }else {
//                //从网络读取图片
//                BmobQuery<User> query = new BmobQuery<User>();
//                query.getObject(objectId, new QueryListener<User>() {
//                    @Override
//                    public void done(User user, BmobException e) {
//                      if(e == null){
//                          String path = user.getHeadPortraitPath();
//                          //加载图片
//                          Picasso.with(getActivity())
//                                  .load(path)
//                                  //加载中的图片
//                                  .placeholder(R.drawable.touxiang)
//                                  //设置加载失败的图片显示
//                                  .error(R.drawable.touxiang)
//                                  .into(headImage);
//                      }
//                    }
//                });
//
//            }
//        } catch (Exception e)
//        {
//            // TODO: handle exception
//        }
//        return bitmap;
//    }
//
//
//}
