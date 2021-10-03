package com.zy.deskpic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zy.deskpic.util.Constants;
import com.zy.deskpic.util.ImageHelper;
import com.zy.deskpic.util.Pref;
import com.zy.deskpic.widget.DeskWidget;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String pic1Uri = "";
    String pic2Uri = "";
    String pic3Uri = "";
    private View tv_set;
    private int whoClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    ImageView iv_1;
    ImageView iv_2;
    ImageView iv_3;

    private void initView() {
        iv_1 = findViewById(R.id.iv_1);
        iv_2 = findViewById(R.id.iv_2);
        iv_3 = findViewById(R.id.iv_3);
        tv_set = findViewById(R.id.tv_set);
        iv_1.setOnClickListener(this);
        iv_2.setOnClickListener(this);
        iv_3.setOnClickListener(this);
        tv_set.setOnClickListener(this);

    }

    private void initData() {
        //TODO 检测机型是否支持
        pic1Uri = Pref.getInstance().getString(Constants.pic1, "");
        pic2Uri = Pref.getInstance().getString(Constants.pic2, "");
        pic3Uri = Pref.getInstance().getString(Constants.pic3, "");
        if (!TextUtils.isEmpty(pic1Uri)) {
            ImageHelper.showPic(iv_1, Uri.parse(pic1Uri));
        }

        if (!TextUtils.isEmpty(pic2Uri)) {
            ImageHelper.showPic(iv_2, Uri.parse(pic2Uri));
        }

        if (!TextUtils.isEmpty(pic3Uri)) {
            ImageHelper.showPic(iv_3, Uri.parse(pic3Uri));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_1:
                whoClick = 0;
                jump2SystemDcim();
                break;
            case R.id.iv_2:
                whoClick = 1;
                jump2SystemDcim();
                break;
            case R.id.iv_3:
                whoClick = 2;
                jump2SystemDcim();
                break;
            case R.id.tv_set:
                Toast.makeText(ZyApplication.getInstance(), "设置成功", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(pic1Uri)) {
                    Pref.getInstance().putString(Constants.pic1, pic1Uri);
                }

                if (!TextUtils.isEmpty(pic2Uri)) {
                    Pref.getInstance().putString(Constants.pic2, pic2Uri);
                }

                if (!TextUtils.isEmpty(pic3Uri)) {
                    Pref.getInstance().putString(Constants.pic3, pic3Uri);
                }
                Pref.getInstance().putInt(Constants.currentNum, 0);
                Intent intentClick = new Intent();
                intentClick.setAction(DeskWidget.ACTION_UPDATE_REFRESH);
                intentClick.setComponent(new ComponentName(this, DeskWidget.class));
                sendBroadcast(intentClick);
                break;
            default:
                break;
        }
    }

    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;

    public void jump2SystemDcim() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri originalUri;

            if (Build.VERSION.SDK_INT < 19) {
                originalUri = data.getData();
            } else {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            Log.i("jeff", "--" + originalUri.toString());
            Toast.makeText(ZyApplication.getInstance(), "选择成功", Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(originalUri.toString())) {
                switch (whoClick) {
                    case 0:
                        pic1Uri = originalUri.toString();
                        Pref.getInstance().putString(Constants.pic1, originalUri.toString());
                        ImageHelper.showPic(iv_1, originalUri);
                        break;
                    case 1:
                        pic2Uri = originalUri.toString();
                        Pref.getInstance().putString(Constants.pic2, originalUri.toString());
                        ImageHelper.showPic(iv_2, originalUri);
                        break;
                    case 2:
                        pic3Uri = originalUri.toString();
                        Pref.getInstance().putString(Constants.pic3, originalUri.toString());
                        ImageHelper.showPic(iv_3, originalUri);
                        break;
                    default:
                        break;
                }
            }
        }
    }


}