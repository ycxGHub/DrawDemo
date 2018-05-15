package adr.ycx.com.drawdemo.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.util.ArrayList;
import java.util.List;

import adr.ycx.com.drawdemo.bitCache.BitmapCacheUtil;

public class DrawCanvas {
    ArrayList<DrawData> mCacheData = new ArrayList<>();
    ArrayList<DrawData> deleteData = new ArrayList<>();
    Bitmap mBitmap;
    Bitmap backgroundBitmap;
    Canvas mCanvas;
    Canvas mHoldCanvas;
    SimpleData mSimpleData;
    Paint paint = new Paint();
    int w, h;
    private int MAX_SIZE = 20;
    PorterDuffXfermode eraserMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public void addData(DrawData path) {
        if (mCacheData.size() >= MAX_SIZE) {
            mCacheData.get(0).drawSelf(mHoldCanvas);
            mCacheData.remove(0);
            mCacheData.add(MAX_SIZE - 1, path);
        } else {
            mCacheData.add(path);
        }
    }

    public DrawCanvas(int w, int h) {
        this.w = w;
        this.h = h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        backgroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mHoldCanvas = new Canvas();
        mHoldCanvas.setBitmap(backgroundBitmap);
        drawAll();
    }

    public void drawAll() {
        mBitmap.eraseColor(Color.WHITE);
        mCanvas.drawColor(Color.WHITE);
        if (backgroundBitmap != null) {
            mCanvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        }
        for (DrawData drawData : mCacheData) {
           drawData.drawSelf(mCanvas);
        }
    }

    public Bitmap drawOnBitmap(DrawData drawData) {
        drawData.drawSelf(mCanvas);
        return mBitmap;
    }

    public void undo() {
        if (mCacheData.size() > 0) {
            addCacheData(deleteData, mCacheData.get(mCacheData.size() - 1));
            removeCacheData(mCacheData);
            drawAll();
        }
    }

    public void undoBack() {
        if (deleteData.size() > 0) {
            addCacheData(mCacheData, deleteData.get(deleteData.size() - 1));
            removeCacheData(deleteData);
            drawAll();
        }
    }


    public Bitmap getCacheBitmap() {
        return mBitmap;
    }

    private void removeCacheData(List<DrawData> datas) {
        if (datas.size() != 0)
            datas.remove(datas.size() - 1);
    }

    private void addCacheData(List<DrawData> datas, DrawData drawData) {
        if (drawData != null) {
            if (datas.size() >= MAX_SIZE) {
                datas.remove(0);
                datas.add(drawData);
            } else
                datas.add(drawData);
        }
    }

    public void setSimpleData(SimpleData simpleData) {
        Bitmap backgroundBitmap = BitmapCacheUtil.getInstance().getBitmap(simpleData.getImageUrl());
        if (backgroundBitmap != null) {
            Matrix matrix = new Matrix();
            int oldW = backgroundBitmap.getWidth();
            int oldH = backgroundBitmap.getHeight();
            float sx = (float) w / oldW;
            float sy = (float) h / oldH;
            matrix.setScale(sx, sy);
            Bitmap temp = Bitmap.createBitmap(backgroundBitmap, 0, 0, oldW, oldH, matrix, true);
            mHoldCanvas.drawBitmap(temp, 0, 0, paint);
        }
        drawAll();
    }


}
