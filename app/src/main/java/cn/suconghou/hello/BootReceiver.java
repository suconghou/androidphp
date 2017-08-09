package cn.suconghou.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

/**
 * Created by admin on 2017/5/30.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String app_dir = context.getApplicationContext().getFilesDir().getAbsolutePath();
            String sd_dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String cache_path = context.getApplicationContext().getCacheDir().getAbsolutePath();
            String httpd = "busybox httpd -p 7070 -h " + sd_dir;
            String installPHP = "busybox cp " + sd_dir + "/php " + app_dir + "/php && busybox chmod 777 " + app_dir + "/php";
            String installHTTP = "busybox cp " + sd_dir + "/microhttp " + app_dir + "/microhttp && busybox chmod 777 " + app_dir + "/microhttp";

            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 2; " + installPHP + " ; " + installHTTP
            });

            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 8;" + httpd + " ; cd " + sd_dir + " ; while true; do " + app_dir + "/php " + " -f " + sd_dir + "/cron.php ; sleep 30;done;"
            });
            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 8;cd " + app_dir + " && ./php " + " -S 0.0.0.0:8080 -t " + sd_dir + " > " + cache_path + "/php.log 2>&1"
            });
            Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "sleep 12;cd " + app_dir + " && ./microhttp > " + cache_path + "/http.log 2>&1"
            });

        } catch (Exception e) {

        } finally {
            System.exit(0);
        }
    }


}
