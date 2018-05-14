package adr.ycx.com.drawdemo.helper;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import adr.ycx.com.drawdemo.data.SimpleData;

public class AvHelper {

    public void querySimpleData(String name, final AvDataQuryListener<SimpleData> avDataQuryListener) {
        AVQuery<AVObject> avQuery = new AVQuery<>(SimpleData.class.getSimpleName());
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("imageName", name);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    List<SimpleData> simpleDataList = new ArrayList<>();
                    if (list != null) {
                        for (AVObject avObject : list) {
                            SimpleData simpleData = new SimpleData();
                            simpleData.setImageName(avObject.getString("imageName"));
                            simpleData.setImageCreateTime(avObject.getString("createTime"));
                            simpleData.setImageUrl(avObject.getString("imageUrl"));
                            simpleDataList.add(simpleData);
                        }
                    }
                    avDataQuryListener.addAll(simpleDataList);
                    avDataQuryListener.notifyDataUpdate();
                } else {
                    avDataQuryListener.erro(e);
                }
            }
        });

    }

    public void getAll(final AvDataQuryListener<SimpleData> avDataQuryListener) {
        AVQuery<AVObject> avQuery = new AVQuery<>(SimpleData.class.getSimpleName());
        avQuery.orderByDescending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    List<SimpleData> simpleDataList = new ArrayList<>();
                    if (list != null) {
                        for (AVObject avObject : list) {
                            SimpleData simpleData = new SimpleData();
                            simpleData.setImageName(avObject.getString("imageName"));
                            simpleData.setImageCreateTime(avObject.getString("createTime"));
                            simpleData.setImageUrl(avObject.getString("imageUrl"));
                            simpleDataList.add(simpleData);
                        }
                    }
                    avDataQuryListener.addAll(simpleDataList);
                    avDataQuryListener.notifyDataUpdate();
                } else {
                    avDataQuryListener.erro(e);
                }
            }
        });
    }


    public void pushSimpleData(File file, final SimpleData simpleData, final AvDataPushListener avDataPushListener) {
        try {
            final AVFile avFile = AVFile.withFile(simpleData.getImageName(), file);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e==null) {
                        AVObject avObject = new AVObject(SimpleData.class.getSimpleName());
                        avObject.put("imageName", simpleData.getImageName());
                        avObject.put("createTime", simpleData.getImageCreateTime());
                        avObject.put("imageUrl",avFile.getUrl());
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e==null) {
                                    avDataPushListener.success();
                                }else {
                                    avDataPushListener.erro(e);
                                }
                            }
                        });
                    }else {
                       avDataPushListener.erro(e);
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    avDataPushListener.progress(integer);
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public interface AvDataQuryListener<T> {
        void notifyDataUpdate();

        void addAll(List<T> list);

        void erro(AVException e);

    }

    public interface AvDataPushListener{
        void progress(Integer integer);
        void success();
        void erro(AVException e);


    }
}
