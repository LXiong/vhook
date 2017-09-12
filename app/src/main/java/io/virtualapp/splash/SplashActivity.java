package io.virtualapp.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstallResult;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import io.virtualhook.R;
import io.virtualapp.VCommends;
//import jonathanfinerty.once.Once;

public class SplashActivity extends Activity {
     ServerLastly server;//socket 用
    @Override protected void onCreate(Bundle savedInstanceState) {
//        boolean enterGuide = !Once.beenDone(Once.THIS_APP_INSTALL, VCommends.TAG_NEW_VERSION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showBanner();

        server=new ServerLastly();
        new Thread(server).start();

        doActionInThread();
        insertApk();
    }


    private void insertApk(){
        String pkgName = "com.andlp.browser";
        String path = Environment.getExternalStorageDirectory()+"/"+"demo.apk";
        File file = new File(path);
        if (!file.exists()){
            copyFileFromAssets("demo.apk", Environment.getExternalStorageDirectory()+"/");
        }
        server=new ServerLastly();
        new Thread(server).start();

        if (VirtualCore.get().isAppInstalled(pkgName)){
             int a=-1;
             Intent intent = VirtualCore.get().getLaunchIntent(pkgName, 0);
            a=VActivityManager.get().startActivity(intent, 0);
            Log.i("yahfa","启动结果:"+a);
        }else{
            InstallResult res = VirtualCore.get().installPackage(path, InstallStrategy.UPDATE_IF_EXIST);
            if (res.isSuccess) {
                try { VirtualCore.get().preOpt(res.packageName); } catch (IOException e) { e.printStackTrace(); }
                if (res.isUpdate) {
                    Log.i("yahfa", "Update: " + res.packageName + " success!");
                  } else {
                    Log.i("yahfa", "Install: " + res.packageName + " success!");
                }
                Log.i("yahfa", "launch result:"+launch(pkgName));
            } else {
                Log.i("yahfa", "Install failed: " + res.error);
            }
        }
    }
    private int launch(String pkgName){
        if (VirtualCore.get().isAppInstalled(pkgName)){
            Intent intent = VirtualCore.get().getLaunchIntent(pkgName, 0);
            return VActivityManager.get().startActivity(intent, 0);
        }else{
            return -1;
        }
    }
    class ServerLastly implements Runnable{
        private static final String TAG="Yahfa - socketSer";
        ServerSocket server;
        Socket client;
        PrintWriter os;
        BufferedReader is;
        ServerLastly(){ }

        public void send(String data){
            if (os!=null) {
                os.println(data);
                os.flush();
            }
        }

        @Override public void run() {
            Log.i(TAG, "yahfa Server=======打开服务=========");
            try {
                server=new ServerSocket(4561);
                client=server.accept();
                Log.i(TAG, "yahfa Server=======客户端连接成功=========");
                InetAddress inetAddress=client.getInetAddress();
                String ip=inetAddress.getHostAddress();
                Log.i(TAG, "yahfa ===客户端ID为:"+ip);
                os=new PrintWriter(client.getOutputStream());
                is=new BufferedReader(new InputStreamReader(client.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }


            String result="";
            while(true){
                if (is==null){continue;}
                try { result=is.readLine();
                    Log.i(TAG, "yahfa 服务端接到的数据为："+result);
                    if(result.equals("6")){
                        x.task().autoPost(new Runnable() {
                            @Override public void run() {
                                Intent intent = new Intent(SplashActivity.this,NewActivity.class);
                                startActivity(intent);
                            }
                        });
                    }else if (result.equals("finish")){
                        Log.i(TAG, "yahfa 服务端finish");
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }

            }


        }

        public void close(){
            try {
                if (os!=null) { os.close(); }
                if (is!=null) { is.close(); }
                if(client!=null){ client.close(); }
                if (server!=null) { server.close(); }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }



    //从assets复制文件  assets中文件名,新的路径(带后缀)
    public void copyFileFromAssets(String assetName, String newApkPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = getAssets().open(new File(assetName).getPath()); //读入原文件
            FileOutputStream fs = new FileOutputStream(newApkPath+assetName);       //带文件名
            byte[] buffer = new byte[1444];
            int length;
            while ( (byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }




    private void showBanner() {

    }

    private void doActionInThread() {
        if (!VirtualCore.get().isEngineLaunched()) {
            VirtualCore.get().waitForEngine();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{  server.close();}catch (Throwable t){t.printStackTrace();}
    }
}
