package adr.ycx.com.drawdemo.bitCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import adr.ycx.com.drawdemo.helper.MD5;


public class NetworkCacheUtil {

    /**
     * 成功
     */
    public static final int SUCCESS = 1;

    /**
     * 失败
     */
    public static final int FAIL = 2;
    public static final int DOWNLOADING = 3;
    private Handler mHandler;

    /**
     * 手机缓存工具类
     */
    private MemoryCacheUtil memoryCacheUtils;

    /**
     * 手机本地存储
     */
    private LocalCacheUtil localCacheUtils;

    /**
     * 线程池接口
     */
    private ExecutorService service;

    public NetworkCacheUtil(Handler handler,MemoryCacheUtil memoryCacheUtils, LocalCacheUtil localCacheUtils) {
        mHandler = handler;

        this.memoryCacheUtils = memoryCacheUtils;
        this.localCacheUtils = localCacheUtils;
        service = Executors.newFixedThreadPool(10);
    }

    /**
     * 线程池的使用
     */
    class MyRunnable implements Runnable {
        private String imageUrl;
        private int position;
        private ImageView mImageView;
        public MyRunnable(ImageView imageView,String imageUrl, int position) {
            this.position = position;
            this.imageUrl = imageUrl;
            mImageView=imageView;

        }

        @Override
        public void run() {
            bitmapFileDownload(imageUrl, position);
        }
        public void bitmapFileDownload(final String imageUrl, final int position) {
            String name = MD5.md5(imageUrl);
            AVFile file = new AVFile(name, imageUrl, new HashMap<String, Object>());
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, AVException e) {
                    // bytes 就是文件的数据流
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.arg1 = position;//显示图片的位置
                        //资源获取成功后，加入到一级和二级缓存中
                        //1、缓存到手机缓存中
                        memoryCacheUtils.putBitmap(imageUrl, bitmap);
                        //2、缓存到手机本地存储中
                        localCacheUtils.putBitmap(imageUrl, bitmap);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(memoryCacheUtils.getBitmap(imageUrl));
                            }
                        });
                    }
                }
            });
        }


    }

    /**
     * 根据 图片的网络地址，获取bitmap对象
     *
     * @param imageUrl
     * @param position
     * @return
     */
    public void getBitmapFromNet(ImageView imageView,final String imageUrl, final int position) {
        service.execute(new MyRunnable( imageView,imageUrl, position));
    }
}
