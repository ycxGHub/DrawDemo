package adr.ycx.com.drawdemo;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DrawStateManager extends TouchDelegate implements View.OnTouchListener {
    DrawStateListener lis;
    DrawState drawState;

    /**
     * Constructor
     *
     * @param bounds       Bounds in local coordinates of the containing view that should be mapped to
     *                     the delegate view
     * @param delegateView The view that should receive motion events
     */
    public DrawStateManager(Rect bounds, View delegateView) {
        super(bounds, delegateView);
        if (delegateView instanceof DrawStateListener) {
            lis= (DrawStateListener) delegateView;
        } else {
            try {
                throw new Exception("No imp listener! ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("TestView", "onTouch: Down");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TestView", "onTouch: Up");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("TestView", "onTouch: Another Down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("TestView", "onTouch: move");
                break;
        }
        return false;
    }

    enum DrawState {
        ONDRAW,
        ONERASE,
        ONTRANSLATE,
        ONZOOM
    }
}
