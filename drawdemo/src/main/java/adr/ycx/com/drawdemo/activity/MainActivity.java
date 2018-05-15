package adr.ycx.com.drawdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;

import java.io.File;

import adr.ycx.com.drawdemo.R;
import adr.ycx.com.drawdemo.TestView;
import adr.ycx.com.drawdemo.data.PencilView;
import adr.ycx.com.drawdemo.data.SimpleData;
import adr.ycx.com.drawdemo.draw.DrawConfigListener;
import adr.ycx.com.drawdemo.helper.AvHelper;
import adr.ycx.com.drawdemo.helper.Util;

public class MainActivity extends AppCompatActivity implements AvHelper.AvDataPushListener {
    TestView mTestView;
    AvHelper mAvHelper;
    ProgressDialog mProgressDialog;
    PencilView mPencilView;
    DrawConfigListener mDrawConfigListener;
    boolean isPencil=true;
    ImageView pencil;
    ImageView erase;
    Paint paint;
    int w=5;
    int a=255;
    int r=255;
    int g=255;
    int b=255;
    View rootView;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView=LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        popupWindow=createPencilWindow(this);
        mAvHelper = new AvHelper();
        mProgressDialog = ViewUtil.createLoadProgress(this);
        mTestView = (TestView) findViewById(R.id.testView);
        mDrawConfigListener = mTestView;
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
        pencil = (ImageView) findViewById(R.id.btn_pencil);
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPencil = true;
                mDrawConfigListener.erase(isPencil);
                updateImageView();
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,-100);

            }
        });
        erase = (ImageView) findViewById(R.id.btn_erase);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPencil = false;
                updateImageView();
                mDrawConfigListener.erase(isPencil);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
        ImageView btn_undo = (ImageView) findViewById(R.id.btn_revertback);
        ImageView btn_revert = (ImageView) findViewById(R.id.btn_revertforward);
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
        updateImageView();
    }

    public void updateImageView() {
        if (isPencil) {
            pencil.setImageResource(R.mipmap.pencil_click);
            erase.setImageResource(R.mipmap.erase);
        } else {
            pencil.setImageResource(R.mipmap.pencil);
            erase.setImageResource(R.mipmap.erase_click);
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

    public PopupWindow createPencilWindow(Activity activity) {
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_pencil_set, null);
        mPencilView=view.findViewById(R.id.img_pencil);
        SeekBar seekBar_width = view.findViewById(R.id.seek_solidwidth);
        SeekBar seekBar_trans = view.findViewById(R.id.seek_transimisson);
        SeekBar seekBar_r = view.findViewById(R.id.seek_color_r);
        SeekBar seekBar_g = view.findViewById(R.id.seek_color_g);
        SeekBar seekBar_b = view.findViewById(R.id.seek_color_b);
        seekBar_width.setProgress(w);
        seekBar_trans.setProgress(a-100);
        seekBar_r.setProgress(r);
        seekBar_b.setProgress(b);
        seekBar_g.setProgress(g);
        seekBar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                w=progress;
                mPencilView.updatePencil(r,g,b,a,w);
                mDrawConfigListener.changeDrawPaintWidth(w);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_trans.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                a =255-progress;
                mPencilView.updatePencil(r,g,b,a,w);
                mDrawConfigListener.changeDrawPaintColor(Color.argb(a,r,g,b));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                mPencilView.updatePencil(r,g,b,a,w);
                mDrawConfigListener.changeDrawPaintColor(Color.argb(a,r,g,b));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                mPencilView.updatePencil(r,g,b,a,w);
                mDrawConfigListener.changeDrawPaintColor(Color.argb(a,r,g,b));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                mPencilView.updatePencil(r,g,b,a,w);
                mDrawConfigListener.changeDrawPaintColor(Color.argb(a,r,g,b));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        popupWindow.setContentView(view);
        return popupWindow;
    }


}
