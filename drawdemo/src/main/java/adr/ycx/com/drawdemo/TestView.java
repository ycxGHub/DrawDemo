package adr.ycx.com.drawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import adr.ycx.com.drawdemo.data.DrawPath;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class TestView extends View implements DrawStateListener {
    ArrayList<DrawPath> paths = new ArrayList<>();


    public TestView(Context context) {
        super(context);

    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }


    @Override
    public void onDrawPath() {

    }

    @Override
    public void onErase() {

    }

    @Override
    public void onTraslate() {

    }

    @Override
    public void onZoom() {

    }
}
