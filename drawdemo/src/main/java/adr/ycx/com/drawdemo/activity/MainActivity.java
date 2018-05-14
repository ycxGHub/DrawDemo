package adr.ycx.com.drawdemo.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;

import java.io.File;

import adr.ycx.com.drawdemo.R;
import adr.ycx.com.drawdemo.TestView;
import adr.ycx.com.drawdemo.data.SimpleData;
import adr.ycx.com.drawdemo.draw.DrawConfigListener;
import adr.ycx.com.drawdemo.helper.AvHelper;
import adr.ycx.com.drawdemo.helper.Util;

public class MainActivity extends AppCompatActivity implements AvHelper.AvDataPushListener {
    TestView mTestView;
    AvHelper mAvHelper;
    ProgressDialog mProgressDialog;
    DrawConfigListener mDrawConfigListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAvHelper = new AvHelper();
        mProgressDialog = ViewUtil.createLoadProgress(this);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 第一种方法
        setContentView(R.layout.activity_main);
        mTestView = (TestView) findViewById(R.id.testView);
        mDrawConfigListener=mTestView;
        ImageView save = (ImageView) findViewById(R.id.btn_save_data);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                mProgressDialog.setProgress(0);
                saveData();
            }
        });

        ImageView close = (ImageView) findViewById(R.id.btn_cancel);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
        ImageView btn_undo= (ImageView) findViewById(R.id.btn_revertback);
        ImageView btn_revert= (ImageView) findViewById(R.id.btn_revertforward);
        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawConfigListener.undo();
            }
        });
        btn_revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawConfigListener.undoBack();
            }
        });
    }


    @Override
    public void progress(Integer integer) {
        Log.d("MainActivity", "progress: " + integer);
        mProgressDialog.incrementProgressBy(integer);
        if (integer == 100) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            SimpleData data = (SimpleData) bundle.getSerializable("SimpleData");
            mTestView.setSimpleData(data);
        }
    }

    @Override
    public void success() {
        Log.d("MainActivity", "success: ");
    }

    @Override
    public void erro(AVException e) {

        mProgressDialog.dismiss();
        Toast.makeText(this, "erro :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "erro: " + e.getMessage());

    }

    private void saveData() {
        SimpleData simpleData = new SimpleData();
        Bitmap bitmap = mTestView.getBitmap();
        long time = System.currentTimeMillis();
        String dir = Environment.getExternalStorageDirectory() + "/drawDemo/local/";
        String name = Long.toString(time) + ".png";
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(dir, name);
        simpleData.setImageName(name);
        simpleData.setImageCreateTime(Util.getTimeStringByLongMills(time));
        Util.saveBitmapByPath(file, bitmap);
        mAvHelper.pushSimpleData(file, simpleData, MainActivity.this);
    }

}
