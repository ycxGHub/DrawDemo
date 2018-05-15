package adr.ycx.com.drawdemo.helper;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static double calTwoPointFDistance(PointF start, PointF end) {
        if (start != null && end != null) {
            double dx = start.x - end.x;
            double dy = start.y - end.y;
            double dis = Math.sqrt(dx * dx + dy * dy);
            return dis;
        } else {
            return 0;
        }
    }

    public static void saveBitmapByPath(File file, Bitmap bitmap) {

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveBitmapByName(String name, Bitmap bitmap) {
        String dir = Environment.getExternalStorageDirectory() + "/drawDemo/local/";
        File file=new File(dir,name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getTimeStringByLongMills(long timeMill) {
        Date nowTime = new Date(timeMill);
        SimpleDateFormat time = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String string = time.format(nowTime);
        return string;
    }
}
