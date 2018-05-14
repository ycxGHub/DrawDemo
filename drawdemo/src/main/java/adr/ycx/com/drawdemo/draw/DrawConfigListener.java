package adr.ycx.com.drawdemo.draw;

public interface DrawConfigListener {
    void changeDrawPaintWidth(float width);
    void changeDrawPaintColor(int Color);
    void changeDrawPaintTransmission();

    void undo();
    void undoBack();


}
