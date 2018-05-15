package adr.ycx.com.drawdemo.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * 创建日期：2018/5/15
 * 创建者ycx
 */

public class PencilView extends android.support.v7.widget.AppCompatImageView {
    Paint paint = new Paint();
    Path path=new Path();
    public PencilView(Context context) {
        super(context);
    }

    public PencilView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PencilView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#FFF5F5F5"));
        canvas.drawLine(10,getHeight()/2,getWidth()-10,getHeight()/2,paint);
    }

    public void updatePencil(int r, int g, int b, int a, float width) {
        paint.setAntiAlias(true);
        paint.setARGB(a,r,g,b);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(width);
        postInvalidateOnAnimation();
    }
}
