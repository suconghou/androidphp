package cn.suconghou.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by admin on 2017/5/30.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String app_dir = context.getApplicationContext().getFilesDir().getAbsolutePath();
            String sd_dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String cache_path = context.getApplicationContext().getCacheDir().getAbsolutePath();
            String php_ini="-n -d session.save_path="+cache_path+" -d short_open_tag=on -d upload_tmp_dir="+cache_path;
            String httpd="busybox httpd -p 9090 -h "+sd_dir;
            String installPHP="busybox cp "+sd_dir+"/php "+app_dir+"/php && busybox chmod 777 "+app_dir+"/php";

            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 1; "+installPHP+" ;"
            });

            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 2;"+httpd+" && cd "+sd_dir+" && while true; do "+app_dir+"/php "+php_ini+" -f "+sd_dir+"/cron.php ; sleep 30;done;"
            });
            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 3;cd "+app_dir+" && ./php "+php_ini+" -S 0.0.0.0:8080 -t "+sd_dir
            });
            Toast.makeText(context.getApplicationContext(), "PHP服务启动成功,端口8080", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(context.getApplicationContext(), "PHP服务启动失败", Toast.LENGTH_LONG).show();
        }
        finally {
            System.exit(0);
        }
    }


}
