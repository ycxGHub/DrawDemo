package adr.ycx.com.drawdemo.draw;


/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public interface DrawEventListener {
    void onPathMoveToNext(float centerX,float centerY,float endX,float endY);
    void onPathSave(float endX,float endY);
    void onPathCreate(float startX,float startY);
    void onCancel();
    void onZoom(float scaleFactor,float centerX,float centerY);
    void onTranslate(float dx, float dy);
}
