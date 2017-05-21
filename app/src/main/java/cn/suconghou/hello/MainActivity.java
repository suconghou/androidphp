package cn.suconghou.hello;

import android.app.Activity;
import android.os.Environment;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startPHP();
    }

    protected void startPHP(){
        try{

            TextView text = (TextView) findViewById(R.id.output);
            TextView errText = (TextView) findViewById(R.id.erroutput);

            String app_dir = getApplicationContext().getFilesDir().getAbsolutePath();

            String sd_dir = Environment.getExternalStorageDirectory().getAbsolutePath();

            String cache_path = getApplicationContext().getCacheDir().getAbsolutePath();

            String php_ini="-n -d session.save_path="+cache_path;

            Process proc = Runtime.getRuntime().exec(new String[]{
                    "/system/bin/sh", "-c",
                    "cd "+app_dir+" && ./php "+php_ini+" -S 0.0.0.0:8080 -t "+sd_dir
            });

            String s = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
            text.setText("out:" +s);

            String err="";
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            while ( (line = br.readLine()) != null){
                err+=line+"\n";
            }
            errText.setText("error:" +err);

            int exitVal = proc.waitFor();

            Toast toast = Toast.makeText(getApplicationContext(), app_dir+" PHP服务开启成功,端口8080,ret: "+exitVal, Toast.LENGTH_LONG);
            toast.show();

        }
        catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "PHP服务开启失败", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    public void tip(String s){
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        toast.show();
    }

    // 更新PHP可执行文件
    public void copyFile(){
        InputStream in = null;
        OutputStream out = null;
        String app_dir = getApplicationContext().getFilesDir().getAbsolutePath();;
        String outFile=app_dir+"/php";
        String sd_dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String inFile = sd_dir+"/php";
        try{
            in= new FileInputStream(inFile);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            tip("更新文件成功");
        }catch (Exception e){
            tip(e.toString());
        }finally {
            try{
                in.close();
                out.close();
            }catch (Exception e){
                tip("close error : "+ e.toString());
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
