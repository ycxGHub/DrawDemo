package adr.ycx.com.drawdemo.bitCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import adr.ycx.com.drawdemo.helper.MD5;

public class LocalCacheUtil {
    /**
     * Created by Star-梦回 on 2016/3/30.
     * 本地存储工具类
     */

    /**
     * 手机缓存工具类
     */
    private MemoryCacheUtil memoryCacheUtils;

    public LocalCacheUtil(MemoryCacheUtil memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 获取bitmap文件
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(String imageUrl) {
        String fileName = MD5.md5(imageUrl);
        File file = new File(Environment.getExternalStorageDirectory() + BitmapCacheUtil.CACHE_DIR, fileName);
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                fis.close();
                //添加到手机缓存中
                memoryCacheUtils.putBitmap(imageUrl, bitmap);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存一个bitmap对象到手机本地存储中
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        //对图片路径进行md5加密，作为文件名，因为这样，长度一样切唯一
        String fileName = MD5.md5(imageUrl);
        File file = new File(Environment.getExternalStorageDirectory() + BitmapCacheUtil.CACHE_DIR, fileName);
        try {
            File parentFile = file.getParentFile();//获取图片路径
            if (!parentFile.exists()) {//如果该文件路径不存在
                parentFile.mkdirs();//创建
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();//刷新
            fos.close();//关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


