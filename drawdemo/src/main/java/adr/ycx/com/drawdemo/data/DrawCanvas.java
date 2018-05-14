package adr.ycx.com.drawdemo.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import adr.ycx.com.drawdemo.bitCache.BitmapCacheUtil;

public class DrawCanvas {
    ArrayList<DrawData> mDrawData = new ArrayList<>();
    Bitmap mBitmap;
    Bitmap backgroundBitmap;
    Canvas mCanvas;
    SimpleData mSimpleData;
    int w,h;
    public void addData(DrawData path) {
        mDrawData.add(path);

    }

    public DrawCanvas(int w, int h) {
        this.w=w;
        this.h=h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
    }

    public DrawCanvas(int w, int h, SimpleData simpleData) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        Bitmap backgroundBitmap = BitmapCacheUtil.getInstance().getBitmap(simpleData.getImageUrl());
        if (backgroundBitmap != null) {
            this.backgroundBitmap = backgroundBitmap;
            mCanvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        }
    }

    public Bitmap drawOnBitmap(DrawData drawData) {
        mCanvas.drawPath(drawData.path, drawData.mPaint);
        return mBitmap;
    }

    public void undo() {
        mDrawData.remove(mDrawData.size() - 1);
    }

    public Bitmap drawAll() {
        mCanvas.drawColor(Color.WHITE);
        mBitmap.eraseColor(Color.WHITE);
        for (DrawData drawData : mDrawData) {
            mCanvas.drawPath(drawData.path, drawData.mPaint);
        }
        return mBitmap;
    }

    public Bitmap getCacheBitmap() {
        return mBitmap;
    }

    public void setSimpleData(SimpleData simpleData) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        Bitmap backgroundBitmap = BitmapCacheUtil.getInstance().getBitmap(simpleData.getImageUrl());
        if (backgroundBitmap != null) {
            this.backgroundBitmap = backgroundBitmap;
            mCanvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        }
        for (DrawData drawData : mDrawData) {
            mCanvas.drawPath(drawData.path, drawData.mPaint);
        }

    }
}
