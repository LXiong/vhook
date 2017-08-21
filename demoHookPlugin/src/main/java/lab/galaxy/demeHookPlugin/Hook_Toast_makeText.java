package lab.galaxy.demeHookPlugin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 717219917@qq.com  2017/8/21 16:33
 */

public class Hook_Toast_makeText {

    public static String className = "android.widget.Toast";
    public static String methodName = "makeText";
    public static String methodSig = "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;";

    public static Toast hook( Context a, CharSequence b, int c) {
        Log.w("YAHFA", "开始注入toast中"+a);


        return origin(a,b,c);
    }
    public static Toast origin(Context a, CharSequence b, int c) {
        Log.w("YAHFA", "注入toast中完毕,执行后续"+a);
        return null;
    }


}
