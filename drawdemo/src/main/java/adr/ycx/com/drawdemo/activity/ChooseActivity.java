package adr.ycx.com.drawdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;

import java.util.ArrayList;
import java.util.List;

import adr.ycx.com.drawdemo.helper.AvHelper;
import adr.ycx.com.drawdemo.R;
import adr.ycx.com.drawdemo.SimpleDataAdapter;
import adr.ycx.com.drawdemo.bitCache.BitmapCacheUtil;
import adr.ycx.com.drawdemo.data.SimpleData;

public class ChooseActivity extends AppCompatActivity implements AvHelper.AvDataQuryListener<SimpleData> {
    ListView mListView;
    Button btn_enter;
    Button btn_update;

    AvHelper mAvHelper;
    List<SimpleData> mSimpleDataList;
    SimpleDataAdapter mSimpleDataAdapter;

    BitmapCacheUtil mBitmapCacheUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAvHelper = new AvHelper();
        setContentView(R.layout.activity_choose);
        mListView = (ListView) findViewById(R.id.list_simple_data);
        btn_enter = (Button) findViewById(R.id.enter);
        btn_update = (Button) findViewById(R.id.update);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAvHelper.getAll(ChooseActivity.this);
            }
        });
        mSimpleDataList=new ArrayList<>();
        mSimpleDataAdapter = new SimpleDataAdapter(this, mSimpleDataList,mListView);
        mListView.setAdapter(mSimpleDataAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ChooseActivity.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("SimpleData",mSimpleDataList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void notifyDataUpdate() {
        mSimpleDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void addAll(List<SimpleData> list) {
        mSimpleDataList.clear();
        mSimpleDataList.addAll(list);
    }

    @Override
    public void erro(AVException e) {
        Toast.makeText(this, "更新失败!", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    /**
     * 当有多个权限需要申请的时候
     * 这里以打电话和SD卡读写权限为例
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_CONTACTS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissionList.isEmpty()) {  //申请的集合不为空时，表示有需要申请的权限
                ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
            } else { //所有的权限都已经授权过了

            }
        }
    }

    /**
     * 权限申请返回结果
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED) { //这个是权限拒绝
                            String s = permissions[i];
                            Toast.makeText(this, s + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                            this.finish();
                        } else { //授权成功了
                            //do Something

                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    class LoadDataThread extends Thread {
        @Override
        public void run() {
            super.run();

        }
    }

}
