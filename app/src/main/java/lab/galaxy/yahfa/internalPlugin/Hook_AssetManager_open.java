package lab.galaxy.yahfa.internalPlugin;

/**
 * 717219917@qq.com  2017/9/12 15:31
 */
   import android.util.Log;
   import java.io.InputStream;

public class Hook_AssetManager_open {
    public static String className = "android.content.res.AssetManager";
    public static String methodName = "open";
    public static String methodSig = "(Ljava/lang/String;)Ljava/io/InputStream;";//参数为string 包名java.io.inputstream
    public static InputStream hook(Object thiz, String fileName) {
        Log.w("YAHFA", "注入到open asset的文件名"+fileName);
        Log.w("YAHFA", "注入到open asset的"+thiz);
        return null;
//        return origin(thiz, fileName);
    }

    public static InputStream origin(Object thiz, String msg) {
        Log.w("YAHFA", "注入 正常should not be here");
        return null;
    }
}