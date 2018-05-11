package adr.ycx.com.drawdemo;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public interface DrawStateListener {
    void onDrawPath();
    void onErase();
    void onTraslate();
    void onZoom();
}
