package adr.ycx.com.drawdemo;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DrawConfig {
    Paint drawPaint =new Paint();
    Matrix mMatrix=new Matrix();
    public static final  float MAX_SCALE=4f;
    public static final  float MIN_SCALE=0.5f;
    public static final  String DEFAULT_COLOR="#FFF5F5F5";
    Paint erasePaint=new Paint();

    public DrawConfig() {
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStrokeWidth(5);
    }





}