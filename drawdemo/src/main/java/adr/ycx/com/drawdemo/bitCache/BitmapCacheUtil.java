package adr.ycx.com.drawdemo.bitCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

public class BitmapCacheUtil {


    public static final String CACHE_DIR = "/drawDemo/cache/";
    public final Context context;
    public static BitmapCacheUtil sBitmapCacheUtil;
    /**
     * 手机缓存工具类
     */
    public MemoryCacheUtil memoryCacheUtils;

    /**
     * 网络获取资源工具类
     */
    public NetworkCacheUtil netCacheUtils;

    public LocalCacheUtil localCacheUtils;

    public BitmapCacheUtil(Context context, Handler handler) {
        this.context = context;
        sBitmapCacheUtil = this;
        memoryCacheUtils = new MemoryCacheUtil(context);
        localCacheUtils = new LocalCacheUtil(memoryCacheUtils);
        netCacheUtils = new NetworkCacheUtil(handler, memoryCacheUtils, localCacheUtils);
    }

    /**
     * 根据图片的网络地址获取为内存的bitmap对象
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(ImageView imageView, String imageUrl, int position) {
        //1、内存中获取，最快
        Bitmap bitmap = memoryCacheUtils.getBitmap(imageUrl);
        if (bitmap != null) {
            return bitmap;
        }
        //2、本地存储中，次之
        bitmap = localCacheUtils.getBitmap(imageUrl);

        if (bitmap != null) {
            return bitmap;
        }
        //3、联网获取，最后
        netCacheUtils.getBitmapFromNet(imageView, imageUrl, position);
        return null;
    }

    public static BitmapCacheUtil getInstance() {
        return sBitmapCacheUtil;
    }


    public Bitmap getBitmap(String imageUrl) {
        //1、内存中获取，最快
        Bitmap bitmap = memoryCacheUtils.getBitmap(imageUrl);
        if (bitmap != null) {
            return bitmap;
        }
        //2、本地存储中，次之
        bitmap = localCacheUtils.getBitmap(imageUrl);
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }
}
