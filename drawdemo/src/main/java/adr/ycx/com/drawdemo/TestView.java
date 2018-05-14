package adr.ycx.com.drawdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import adr.ycx.com.drawdemo.data.DrawCanvas;
import adr.ycx.com.drawdemo.data.DrawData;
import adr.ycx.com.drawdemo.data.SimpleData;
import adr.ycx.com.drawdemo.draw.DrawConfigListener;
import adr.ycx.com.drawdemo.draw.DrawEventListener;
import adr.ycx.com.drawdemo.draw.DrawEventManager;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class TestView extends View implements DrawEventListener, DrawConfigListener {
    DrawCanvas mDrawCanvans;
    DrawConfig mDrawConfig = new DrawConfig();
    Path mPath;
    DrawData drawData;
    DrawEventManager mDrawStateManager;
    ArrayList<DrawCanvas> mDrawCanvasArrayList = new ArrayList<>();
    Matrix mMatrix = new Matrix();

    float xCen = 0;
    float yCen = 0;
    float scale = 1.0f;
    float scaleFactorCurrent = 1;
    float cX;
    float cY;

    public RectF originCanvasRect = new RectF();

    public RectF currentCanvasRect = new RectF();
    public SimpleData mSimpleData;

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

        mMatrix.reset();

        mMatrix.preTranslate(xCen, yCen);
        float tempScale = scaleFactorCurrent * scale;
        canvas.drawColor(Color.parseColor("#FFF5F5F5"));
        if (tempScale < DrawConfig.MIN_SCALE) {
            tempScale = DrawConfig.MIN_SCALE;
        }
        if (tempScale > DrawConfig.MAX_SCALE) {
            tempScale = DrawConfig.MAX_SCALE;
        }
        mMatrix.preScale(tempScale, tempScale, getWidth() / 2, getHeight() / 2);

//        canvas.drawColor(Color.WHITE);

        mMatrix.mapRect(currentCanvasRect, originCanvasRect);

        canvas.drawBitmap(mDrawCanvans.getCacheBitmap(), mMatrix, mDrawConfig.drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("TestView", "onSizeChanged: ");
        mDrawStateManager = new DrawEventManager(new Rect(0, 0, w, h), this);
        mDrawCanvans = new DrawCanvas(w, h);
        if (mSimpleData != null)
            mDrawCanvans.setSimpleData(mSimpleData);
        originCanvasRect = new RectF(0, 0, w, h);
        this.setTouchDelegate(mDrawStateManager);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    public void onPathMoveToNext(float pX, float pY, float cX, float cY) {
        float[] values = {pX,pY,cX,cY};
        float[] pathMatrix = new float[9];
        mMatrix.getValues(pathMatrix);
        pathMatrix[0] = 1/pathMatrix[0];
        pathMatrix[2] = -pathMatrix[2];
        pathMatrix[5] = -pathMatrix[5];
        Matrix matrix=new Matrix();
        matrix.postTranslate(pathMatrix[2],pathMatrix[5]);
        matrix.postScale(pathMatrix[0],pathMatrix[0]);

        matrix.mapPoints(values);
        drawData.setPaint(mDrawConfig.drawPaint);
        mPath.quadTo(values[0], values[1], values[2], values[3]);
        drawData.setPath(mPath);

        mDrawCanvans.drawOnBitmap(drawData, mMatrix);
        invalidate();
    }

    @Override
    public void onPathSave(float endX, float endY) {
        drawData.setPath(mPath);
        mDrawCanvans.addData(drawData);
        mDrawCanvans.drawOnBitmap(drawData, mMatrix);
        mPath = null;
        invalidate();
    }

    @Override
    public void onPathCreate(float startX, float startY) {
        mPath = new Path();
        drawData = new DrawData();
        float[] values = {startX, startY};
        float[] pathMatrix = new float[9];
        mMatrix.getValues(pathMatrix);
        pathMatrix[0] =1/ pathMatrix[0];
        pathMatrix[2] = -pathMatrix[2];
        pathMatrix[5] = -pathMatrix[5];
        Matrix matrix=new Matrix();
        matrix.postTranslate(pathMatrix[2],pathMatrix[5]);
        matrix.postScale(pathMatrix[0],pathMatrix[0]);
        matrix.mapPoints(values);
        drawData.setPaint(mDrawConfig.drawPaint);
        mPath.moveTo(values[0], values[1]);
    }

    @Override
    public void onCancel() {
        scale = scaleFactorCurrent * scale;
        if (scale < DrawConfig.MIN_SCALE) {
            scale = DrawConfig.MIN_SCALE;
        }
        if (scale > DrawConfig.MAX_SCALE) {
            scale = DrawConfig.MAX_SCALE;
        }
        scaleFactorCurrent = 1;
    }

    @Override
    public void onZoom(float scaleFactor, float cX, float cY) {
        this.scaleFactorCurrent = scaleFactor;
        this.cX = cX;
        this.cY = cY;

//        checkBorderAndCenterWhenScale();
        invalidate();
    }

    @Override
    public void onTranslate(float dx, float dy) {
        xCen = xCen + dx;
        yCen = yCen + dy;
//        mMatrix.postTranslate(xCen,yCen);
        invalidate();
    }


    @Override
    public void changeDrawPaintWidth(float x) {
        mDrawConfig.drawPaint.setStrokeWidth(x);
    }

    @Override
    public void changeDrawPaintColor(int color) {

    }

    @Override
    public void changeDrawPaintTransmission() {

    }

    @Override
    public void undo() {
        mDrawCanvans.undo();
        invalidate();
    }

    @Override
    public void undoBack() {
        mDrawCanvans.undoBack();
        invalidate();
    }

    public void setSimpleData(SimpleData simpleData) {

        mSimpleData = simpleData;

    }

    public Bitmap getBitmap() {
        return mDrawCanvans.getCacheBitmap();
    }
}
