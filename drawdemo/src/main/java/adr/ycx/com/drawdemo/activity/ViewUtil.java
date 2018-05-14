package adr.ycx.com.drawdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import adr.ycx.com.drawdemo.R;

public class ViewUtil {


    public static PopupWindow createWaitWindow(Activity activity) {
        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_wait, null);
        popupWindow.setContentView(view);
        return popupWindow;
    }


    public static ProgressDialog createLoadProgress(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setIcon(R.mipmap.ic_launcher);// 设置提示的title的图标，默认是没有的
        progressDialog.setTitle("提示");
        progressDialog.setMax(100);
        return progressDialog;
    }
}
