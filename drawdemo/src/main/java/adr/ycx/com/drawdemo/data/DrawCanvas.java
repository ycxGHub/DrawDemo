package adr.ycx.com.drawdemo.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.util.ArrayList;

import adr.ycx.com.drawdemo.bitCache.BitmapCacheUtil;

public class DrawCanvas {
    ArrayList<DrawData> mCacheData = new ArrayList<>();
    ArrayList<DrawData> deleteData = new ArrayList<>();
    Bitmap mBitmap;
    Bitmap mCacheBitmap;
    Bitmap backgroundBitmap;
    Canvas mCacheCanvas;
    Canvas mCanvas;
    SimpleData mSimpleData;
    Paint paint = new Paint();
    int w, h;

    public void addData(DrawData path) {
        mCacheData.add(path);
        drawAtBackGroundBitmap();
    }

    public DrawCanvas(int w, int h) {
        this.w = w;
        this.h = h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        mCacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas();
        mCacheCanvas.setBitmap(mCacheBitmap);
        mCacheCanvas.drawColor(Color.WHITE);
        drawAll();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    public void drawAll() {
        mBitmap.eraseColor(Color.WHITE);
        mCanvas.drawColor(Color.WHITE);
        if (backgroundBitmap != null) {
            mCanvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        }

        drawAllCache();
        mCanvas.drawBitmap(mCacheBitmap, 0, 0, paint);


    }

    public Bitmap drawOnBitmap(DrawData drawData, Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        mCacheCanvas.drawPath(drawData.path, drawData.mPaint);
        return mCacheBitmap;
    }

    public void undo() {
        deleteData.add(mCacheData.get(mCacheData.size() - 1));
        mCacheData.remove(mCacheData.size() - 1);
        drawAllCache();


    }

    public void undoBack() {
        mCacheData.add(deleteData.get(deleteData.size() - 1));
        deleteData.remove(deleteData.size() - 1);
        drawAllCache();
    }

    public void drawAllCache() {
        mCacheBitmap.eraseColor(Color.WHITE);
        mCacheCanvas.drawColor(Color.WHITE);
        for (DrawData drawData : mCacheData) {
            mCacheCanvas.drawPath(drawData.path, drawData.mPaint);
        }

    }

    public Bitmap getCacheBitmap() {
        return mBitmap;
    }

    public void drawAtBackGroundBitmap() {
        if (mCacheData.size() > 20) {
//        drawData.path.offset(-values[2],-values[5]);
            mCanvas.drawPath(mCacheData.get(0).path, mCacheData.get(0).getPaint());
            mCacheData.remove(0);
            drawAllCache();
        }


    }

    public void setSimpleData(SimpleData simpleData) {

        Bitmap backgroundBitmap = BitmapCacheUtil.getInstance().getBitmap(simpleData.getImageUrl());
        if (backgroundBitmap != null) {
            this.backgroundBitmap = backgroundBitmap;
        }
        drawAll();

    }


}
