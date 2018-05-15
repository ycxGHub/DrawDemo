package adr.ycx.com.drawdemo.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DrawData {
    Path path;
    Paint mPaint = new Paint();

    public boolean isPen = true;

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {

        mPaint.set(paint);
    }

    public Path getPath() {

        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void drawSelf(Canvas canvas) {
        if (!isPen) {
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(20);
            canvas.drawPath(path, mPaint);
        } else {
            canvas.drawPath(path, mPaint);
        }
    }

}
