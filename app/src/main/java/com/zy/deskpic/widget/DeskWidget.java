package com.zy.deskpic.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zy.deskpic.R;
import com.zy.deskpic.ZyApplication;
import com.zy.deskpic.util.Constants;
import com.zy.deskpic.util.Pref;
import com.zy.deskpic.util.RealPathFromUriUtils;

import java.io.File;


/**
 * Implementation of App Widget functionality.
 */
public class DeskWidget extends AppWidgetProvider {
    public static final String ACTION_UPDATE_NEXT = "com.zy.deskpic.widget.widget.next";
    public static final String ACTION_UPDATE_REFRESH = "com.zy.deskpic.widget.widget.refresh";
    public static int widgetClickCode = 1001;


    private static final String ACTION_ENABLE = "android.appwidget.action.APPWIDGET_ENABLED";
    private static final String ACTION_DISABLE = "android.appwidget.action.APPWIDGET_DISABLED";
    public static final String ACTION_WIDGET_SYSTEM_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    private static RemoteViews remoteViews;


    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

//        remoteViews.setImageViewUri(R.id.iv_widget_img, Uri.parse(picUri));
        upDataUi(context);
        openNext(context, remoteViews);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    public void upDataUi(Context context) {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.desk_widget);
        }
        int anInt = Pref.getInstance().getInt(Constants.currentNum, 0);
        String picUri = "";
        switch (anInt) {
            case 0:
                picUri = Pref.getInstance().getString(Constants.pic1, "");
                break;
            case 1:
                picUri = Pref.getInstance().getString(Constants.pic2, "");
                break;
            case 2:
                picUri = Pref.getInstance().getString(Constants.pic3, "");
                break;
        }
        String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(ZyApplication.getInstance(), Uri.parse(picUri));
        Log.i("jeff", "---" + realPathFromUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只读取图片尺寸

        Bitmap bitmap = BitmapFactory.decodeFile(realPathFromUri, options);
        // 获取到这个图片的原始宽度和高度
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        // 获取画布中间方框的宽度和高度
        int screenWidth = ZyApplication.getMetrics().widthPixels;
        int screenHeight = ZyApplication.getMetrics().heightPixels;
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                options.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                options.inSampleSize = picHeight / screenHeight;
        }
        options.inSampleSize = 3;
        options.inJustDecodeBounds = false;//读取图片内容
        options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改

        bitmap = BitmapFactory.decodeFile(realPathFromUri, options);
        remoteViews.setImageViewBitmap(R.id.iv_widget_img, bitmap);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }


    /**
     * 下一页
     */
    private static void openNext(Context context, RemoteViews remoteViews) {
        Intent intentClick = new Intent();
        intentClick.setAction(DeskWidget.ACTION_UPDATE_NEXT);
        intentClick.setComponent(new ComponentName(context, DeskWidget.class));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(context
                , widgetClickCode, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.next, broadcastIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (remoteViews == null) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.desk_widget);
        }
        if (intent != null && intent.getAction() != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, DeskWidget.class));
            switch (intent.getAction()) {
                case ACTION_UPDATE_REFRESH:
                    upDataUi(context);
                    appWidgetManager.updateAppWidget(appIds, remoteViews);
                    break;
                case ACTION_UPDATE_NEXT:
                    int lastNum = Pref.getInstance().getInt(Constants.currentNum, 0);
                    int nextNum = lastNum + 1;
                    if (nextNum > 2) {
                        nextNum = 0;
                    }
                    Pref.getInstance().putInt(Constants.currentNum, nextNum);
                    upDataUi(context);
                    appWidgetManager.updateAppWidget(appIds, remoteViews);
                    break;
                case ACTION_ENABLE:
                    appWidgetManager.updateAppWidget(appIds, remoteViews);
                    break;
                case ACTION_DISABLE:
                    appWidgetManager.updateAppWidget(appIds, remoteViews);
                    break;
                case ACTION_WIDGET_SYSTEM_UPDATE:
                    break;
                default:
                    break;
            }
        }

    }


}

