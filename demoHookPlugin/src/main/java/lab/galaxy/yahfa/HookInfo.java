package lab.galaxy.yahfa;

import android.app.Application;
import android.util.Log;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by liuruikai756 on 31/03/2017.
 */

public class HookInfo {
    static {
        System.loadLibrary("helloJni");
    }
    public static Application application;

    public static void init(Application app){
        application=app;
//        HermesEventBus.getDefault().register(application);
//        HermesEventBus.getDefault().connectApp(application, "io.virtualhook");
//        HermesEventBus.getDefault().post("这a是从plugin发送的消息000");
        Log.i("yahfa","eventbus--"+HermesEventBus.getDefault());
    }


    public static String[] hookItemNames = {
        "lab.galaxy.demeHookPlugin.Hook_AssetManager_open",
        "lab.galaxy.demeHookPlugin.Hook_URL_openConnection",
        "lab.galaxy.demeHookPlugin.Hook_File_init",
        "lab.galaxy.demeHookPlugin.Hook_Toast_makeText",
        "lab.galaxy.demeHookPlugin.Hook_Toast_show",
        "lab.galaxy.demeHookPlugin.Hook_TelephonyManager_getDeviceId",
        "lab.galaxy.demeHookPlugin.Hook_Application_onCreate",
    };
}
