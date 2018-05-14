package adr.ycx.com.drawdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import adr.ycx.com.drawdemo.bitCache.BitmapCacheUtil;
import adr.ycx.com.drawdemo.data.SimpleData;

public class SimpleDataAdapter extends BaseAdapter {
    Context mContext;
    List<SimpleData> mList;
    BitmapCacheUtil mBitmapCacheUtil;
    Handler mHandler;
    Bitmap cacheBitmap;
    boolean isOnScroll = false;
    ListView mListView;

    public SimpleDataAdapter(Context context, List<SimpleData> list, ListView listView) {
        mContext = context;
        mList = list;
        mHandler = new Handler();
        cacheBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_4444);
        mBitmapCacheUtil = BitmapCacheUtil.getInstance();
        if (mBitmapCacheUtil == null) {
            mBitmapCacheUtil=new BitmapCacheUtil(context.getApplicationContext(),mHandler);
        }
        mListView = listView;
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        //设置为停止滚动
                        setOnScroll(false);
                        //当前屏幕中listview的子项的个数
                        int count = absListView.getChildCount();
                        for (int i = 0; i < count; i++) {
                            //获取到item的图片显示的Imageview控件
                            ImageView iv_show = (ImageView) absListView.getChildAt(i).findViewById(R.id.img);
                            if (!iv_show.getTag().equals("1")) {//如果等于1说明图片资源已加载过，不等于说明没有去getTag()的图片url

                                //直接从Tag中取出我们存储的数据image——url
                                String image_url = iv_show.getTag().toString();
                                if (image_url != null) {//这个判断是防止图片的url是否为空，为空的话给默认图片。
                                    iv_show.setImageBitmap(mBitmapCacheUtil.getBitmap(iv_show, image_url, i));
                                    iv_show.setTag("1");
                                } else {
                                    iv_show.setImageBitmap(cacheBitmap);
                                    iv_show.setTag("1");
                                }

                            }
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        setOnScroll(true);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_data_simple, null);
            viewHolder = new ViewHolder();
            viewHolder.mImage = view.findViewById(R.id.img);
            viewHolder.mCreateTime = view.findViewById(R.id.txt_time);
            viewHolder.mImageName = view.findViewById(R.id.txt_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!isOnScroll) {
            Bitmap bitmap = mBitmapCacheUtil.getBitmap(viewHolder.mImage, mList.get(i).getImageUrl(), i);
            if (bitmap == null) {
                viewHolder.mImage.setImageBitmap(cacheBitmap);
            } else {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        } else {
            viewHolder.mImage.setImageBitmap(cacheBitmap);
        }
        viewHolder.mImage.setTag(mList.get(i).getImageUrl());
        viewHolder.mImageName.setText(mList.get(i).getImageName());
        viewHolder.mCreateTime.setText(mList.get(i).getImageCreateTime());
        return view;
    }

    public void setOnScroll(boolean isOnScroll) {
        this.isOnScroll = isOnScroll;
    }

    class ViewHolder {
        public ImageView mImage;
        public TextView mImageName;
        public TextView mCreateTime;

    }
}
