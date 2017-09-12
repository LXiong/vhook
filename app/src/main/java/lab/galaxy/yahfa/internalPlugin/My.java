package lab.galaxy.yahfa.internalPlugin;

/**
 * 717219917@qq.com  2017/9/12 14:40
 */
        import android.app.Application;
        import android.content.Context;
        import android.util.Log;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;


public class My {

//    private static volatile   My my;
    private My(){}
//    public   My getInstance(){
//        if (my == null) {
//            synchronized (My.class) {
//                if (my == null) {
//                    my = new My();
//                }
//            }
//        }
//        return my;
//    }

    public static  boolean toast=true;
    public static  boolean eventbus=false;
    private  static boolean isfirst=true;

    public static Application application;//hook applicaion的onCreate赋值到这里
    public Context context;

    public  static ClientLastly clientLastly;
    public  static StringBuffer receiveData=new StringBuffer();

    public static void init(){
        isfirst=false;
        clientLastly=new ClientLastly();
        new Thread(clientLastly).start();
    }

   public static boolean isfirst(){
       return isfirst;
   }


    static  class ClientLastly implements Runnable{
        private static final String TAG="ClientLastly";
        private int timeout=30000;
        private static  Socket myclient;
        private static PrintWriter os;
        private  static  BufferedReader is;
        ClientLastly(){ }

        public   void send(String data){
            Log.i(TAG, "yahfa   Client发送=======data========="+data);
            if (os!=null) {
                os.println(data);
                os.flush();
            }
        }


        //接收据
        @Override public void run() {
            try {
                myclient=new Socket("localhost", 4561);
                Log.i(TAG, "yahfa Client=======连接服务器成功=========");
                myclient.setSoTimeout(timeout);
                os=new PrintWriter(myclient.getOutputStream());
                is=new BufferedReader(new InputStreamReader(myclient.getInputStream()));
            } catch (IOException e) {
                Log.i(TAG, "yahfa Client=======连接服务器失败=========");
                e.printStackTrace();
            }


            String result="";
//            while(true){
//                try {
//                    result=is.readLine();
//                    Log.i(TAG, "yahfa  客户端接到的数据为："+result);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        public void close(){
            try {
                if (os!=null) {
                    os.close();
                }
                if (is!=null) {
                    is.close();
                }
                if(myclient!=null){
                    myclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public static void  send(String str){
        clientLastly.send(str);
    }



}
