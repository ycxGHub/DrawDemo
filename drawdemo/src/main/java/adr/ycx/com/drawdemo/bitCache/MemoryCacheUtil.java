package adr.ycx.com.drawdemo.bitCache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCacheUtil {

    /**
     * 手机缓存的容器，最近最少算法集合，底部是LinkedHashMap形式存储
     */
    private LruCache<String, Bitmap> lruCache;
    private Context context;

    public MemoryCacheUtil(Context context) {
        this.context = context;
        //获取ActivityManager对象
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取手机当前可用内存的大小，单位是Mb
        int memoryClass = manager.getMemoryClass();
        //换算成byte，获取可用空间的1/8作为图片的缓存，在空间不足时，系统会自动回收
        int size = memoryClass * 1024 * 1024 / 8;
        lruCache = new LruCache<String, Bitmap>(size) {
            //之所以说需要换算成byte，是因为，该方法的返回值是byte，两者需要统一
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 保存bitmap对象到手机缓存中
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl, bitmap);
    }

    /**
     * 获取bitmap对象
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(String imageUrl) {
        return lruCache.get(imageUrl);

    }
}