package adr.ycx.com.drawdemo.data;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DrawData {
    Path path;
    Paint mPaint=new Paint();

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public Path getPath() {

        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }


}
