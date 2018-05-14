package adr.ycx.com.drawdemo.draw;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TouchDelegate;
import android.view.View;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DrawEventManager extends TouchDelegate {
    DrawEventListener lis;
    DrawState preDrawSate;
    PointF lastMovePoint = new PointF();
    int fingerNum = 0;
    PointF firstPoint = new PointF();
    //    PointF secondPoint = new PointF();
    float centerX = 0.0f;
    float centerY = 0.0f;
    float scale = 1.0f;


    PointF beisalControlPoint = new PointF();
    PointF beisalEndPoint = new PointF();


    public boolean beforeUpEventHadTwoFingerEvent = false;

    //    long twoFingerEventPreStartTime = 0;
//
//    double twoFingerStartDistance = 0;
//    public final static long TIME_DIFF_THRESHOLD = 5;
//    public final static int VALID_TRIGGER_MAX_TIMES = 50;
//    public final static int VALID_TRIGGER_MIN_TIMES = 2;
    public final static int ADVANTAGE_TRIGGER_START_TIMES = 5;

    public Matrix mMatrix = new Matrix();
    public float xCen = 0;
    public float yCen = 0;
    //    public boolean isFirstTwoFingerMatrix = false;
//    public float lastCenterX, lastCenterY = 0;
//    public boolean isOnScale = false;
//    public int triggerScaleTimes = 0;
//    public int triggerTransTimes = 0;
//    public int sumTriggerTimes = 0;
    ScaleGestureDetector mScaleGestureDetector;
    SaleListener mSaleListener;
    public int mActivePointerId = -1;

    /**
     * Constructor
     *
     * @param bounds       Bounds in local coordinates of the containing view that should be mapped to
     *                     the delegate view
     * @param delegateView The view that should receive motion events
     */
    public DrawEventManager(Rect bounds, View delegateView) {
        super(bounds, delegateView);
        if (delegateView instanceof DrawEventListener) {
            lis = (DrawEventListener) delegateView;
            mSaleListener = new SaleListener();
            mScaleGestureDetector = new ScaleGestureDetector(delegateView.getContext(), mSaleListener);
        } else {
            try {
                throw new Exception("No imp listener! ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        judgeCurrentState(event);
        notifyListener();
        return true;
    }


    public boolean isMove(MotionEvent event) {
        boolean res = Math.abs(lastMovePoint.x - event.getX()) > 10;
        res = res || (Math.abs(lastMovePoint.y - event.getY()) > 10);
        return res;
    }

    public void pathMoveToNextPoint(float x, float y) {
        float cX = (x + firstPoint.x) / 2;
        float cY = (y + firstPoint.y) / 2;


        beisalControlPoint.x = firstPoint.x;
        beisalControlPoint.y = firstPoint.y;
        beisalEndPoint.x = cX;
        beisalEndPoint.y = cY;


        firstPoint.x = x;
        firstPoint.y = y;
    }

    public void judgeCurrentState(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        if (event.getPointerCount() == 2) {
            handleTwoFingerEvent(event);
        } else {
            handlerSingleFinger(event);
        }


    }

    public void notifyListener() {
        switch (preDrawSate) {
            case CANCEL:
                lis.onCancel();
                break;
            case ONSAVEPATH:
                lis.onPathSave(0, 0);
                break;
            case ONCREATEPATH:
                lis.onPathCreate(firstPoint.x, firstPoint.y);
                break;
            case ONDRAW:
                lis.onPathMoveToNext(beisalControlPoint.x, beisalControlPoint.y, beisalEndPoint.x, beisalEndPoint.y);
                break;
            case ONZOOM:
                lis.onZoom(scale,centerX,centerY);
                break;
            case ONTRANSLATE:
                 lis.onTranslate(xCen, yCen);
                break;
            default:
                break;
        }
    }

    public void handleTwoFingerEvent(MotionEvent ev) {
        if (ev.getPointerCount() == 2) {
            beforeUpEventHadTwoFingerEvent = true;
            final int action = ev.getAction();
            float x,y;
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    x = ev.getX(0);
                    y = ev.getY(0);
                    lastMovePoint.x = x;
                    lastMovePoint.y = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                  x = ev.getX(0);
                  y = ev.getY(0);
                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!mScaleGestureDetector.isInProgress()) {
                        float dx = x - lastMovePoint.x;
                        float dy = y - lastMovePoint.y;
                        xCen = dx;
                        yCen = dy;
                        preDrawSate=DrawState.ONTRANSLATE;
                    }else {
                        preDrawSate=DrawState.ONZOOM;
                    }
                    lastMovePoint.x = x;
                    lastMovePoint.y = y;
                    break;

                case MotionEvent.ACTION_UP:
                    mActivePointerId = INVALID_POINTER_ID;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    mActivePointerId = INVALID_POINTER_ID;
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    // Extract the index of the pointer that left the touch sensor
                    final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        lastMovePoint.x = ev.getX(newPointerIndex);
                        lastMovePoint.y = ev.getY(newPointerIndex);
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    break;

            }
        }
    }

    public void handlerSingleFinger(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                fingerNum = 1;
                firstPoint.x = event.getX();
                firstPoint.y = event.getY();
                preDrawSate = DrawState.ONCREATEPATH;
                beforeUpEventHadTwoFingerEvent = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!beforeUpEventHadTwoFingerEvent)
                    preDrawSate = DrawState.ONSAVEPATH;
                else
                    preDrawSate=DrawState.CANCEL;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove(event)&&!beforeUpEventHadTwoFingerEvent) {
                    preDrawSate = DrawState.ONDRAW;
                    pathMoveToNextPoint(event.getX(), event.getY());
                }
                break;

        }
    }

    public void calMatrix(PointF curFirstPoint, PointF curSecondPoint) {
//        float curMinusPreFirstDx = curFirstPoint.x - firstPoint.x;
//        float curMinusPreFirstDy = curFirstPoint.y - firstPoint.y;
//
//        float curMinusPreSecondDx = curSecondPoint.x - secondPoint.x;
//        float curMinusPreSecondDy = curSecondPoint.y - secondPoint.y;
//        float dx = curMinusPreFirstDx;
//        float dy = curMinusPreFirstDy;
//        double scaleFactor = 1;
//        double firstTransDistance = Util.calTwoPointFDistance(firstPoint, curFirstPoint);
//        double secondTransDistance = Util.calTwoPointFDistance(secondPoint, curSecondPoint);
//        double firstAndSecondPreDistance = Util.calTwoPointFDistance(firstPoint, secondPoint);
//        double firstAndSeoncdCurDistance = Util.calTwoPointFDistance(curFirstPoint, curSecondPoint);
//        scaleFactor = (firstAndSeoncdCurDistance + 200) / (firstAndSecondPreDistance + 200);
//        if (firstTransDistance < 20 && secondTransDistance < 20) {//两指都未移动
//            preDrawSate = DrawState.NONE;
//            return;
//        }
//        if (curMinusPreSecondDx * curMinusPreFirstDx * curMinusPreSecondDy * curMinusPreFirstDy == 0) {
//            preDrawSate = DrawState.NONE;
//            return;
//        }
//        mMatrix.reset();
//        boolean isXOrientationIsSame = curMinusPreSecondDx * curMinusPreFirstDx > 0;
//        boolean isYOrientationIsSame = curMinusPreSecondDy * curMinusPreFirstDy > 0;
//        if (isXOrientationIsSame || isYOrientationIsSame) {
////            triggerScaleTimes = --triggerScaleTimes >= 0 ? triggerScaleTimes : 0;
//            triggerTransTimes++;
//        } else {
//            triggerScaleTimes++;
////            triggerTransTimes = --triggerTransTimes >= 0 ? triggerTransTimes : 0;
//        }
//        sumTriggerTimes++;
//        if (triggerScaleTimes / sumTriggerTimes > 0.5)//在触发的所有次数中的event中有一般是认为是sacle
//        {
//            scale = (float) (scaleFactor * scale);
//        } else {
//            xCen = xCen + dx;
//            yCen = yCen + dy;
//        }
//
//        if (sumTriggerTimes > VALID_TRIGGER_MAX_TIMES) {
//            sumTriggerTimes = 0;
//            if (triggerScaleTimes > triggerTransTimes) {
//                triggerTransTimes = 0;
//                triggerScaleTimes = ADVANTAGE_TRIGGER_START_TIMES;
//            } else {
//                triggerTransTimes = ADVANTAGE_TRIGGER_START_TIMES;
//                triggerScaleTimes = 0;
//            }
//        }
//        mMatrix.preTranslate(xCen, yCen);
//
//        mMatrix.preScale(scale, scale);
//        preDrawSate = DrawState.ONMATRIX;
//        firstPoint = curFirstPoint;
//        secondPoint = curSecondPoint;
    }

    enum DrawState {
        ONCREATEPATH,
        ONDRAW,
        ONTRANSLATE,
        ONZOOM,
        ONSAVEPATH,
        CANCEL,
        DRAWNONE
    }

    public class SaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale=detector.getScaleFactor();
            centerX=detector.getFocusX();
            centerY=detector.getFocusY();
            return super.onScale(detector);
        }
    }
}
