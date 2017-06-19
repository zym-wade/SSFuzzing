package com.imcczy;

import android.graphics.Bitmap;

import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import java.io.ByteArrayOutputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;



/**
 * Created by imcczy on 16/10/11.
 */

public class Main{
    static Looper looper;
    public static int lll = 1;

    public static void main() {
        System.out.println("Andcast Main Entry!");

        /*
        Base base = new Base();
        base.print();
        AsyncHttpServer httpServer = new AsyncHttpServer() {
            protected boolean onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                return super.onRequest(request, response);
            }
        };


        Looper.prepare();
        looper = Looper.myLooper();
        System.out.println("Andcast Main Entry!");
        AsyncServer server = new AsyncServer();
        httpServer.get("/screenshot.jpg", new AnonymousClass5());
        httpServer.get("/test", new AnonymousClass3());
        httpServer.listen(server, 53517);
        //System.out.println("begin!");
        Looper.loop();
*/
    }

    /* renamed from: com.koushikdutta.vysor.Main.5 */
    static class AnonymousClass5 implements HttpServerRequestCallback {

        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            try {
                Class<?> ScreenShotclass = Class.forName("android.view.SurfaceControl");
                Method shot = ScreenShotclass.getDeclaredMethod("screenshot", new Class[]{Integer.TYPE, Integer.TYPE});
                Bitmap bitmap = (Bitmap)shot.invoke(null,1080,1920);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);
                bout.flush();
                response.send("image/jpeg", bout.toByteArray());
                return;
            } catch (Exception e) {
                response.code(500);
                response.send(e.toString());
                return;
            }
        }
    }
    static class AnonymousClass3 implements HttpServerRequestCallback {

        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            response.send("test");
        }
    }
    static class AnonymousClass4 implements HttpServerRequestCallback {

        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            try {
                String name="test:\n";
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                Method method_getService = clazz.getMethod("getService",String.class);
                String s = "phone";

                IBinder bind = (IBinder) method_getService.invoke(clazz, s);
                String ifname = bind.getInterfaceDescriptor();
                Class<?> ifn = Class.forName(ifname + "$Stub");
                if (ifn.equals(null))
                    System.out.println("is null");
                Field fields[] = ifn.getDeclaredFields();
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                System.out.println("\nbegin!");
                for (Field field : fields) {
                    name = field.getName();

                    if (true/*name.startsWith("TRANSACTION") && name != "TRANSACTION_answerRingingCall"
                     && name != "TRANSACTION_answerRingingCallForSubscriber"
                            && name != "TRANSACTION_endCall"&& name != "TRANSACTION_getCellNetworkScanResults"
                            && name != "TRANSACTION_handlePinMmiForSubscriber"*/) {
                        field.setAccessible(true);
                        int ii = (int) field.get(ifn);
                        System.out.println(s+name+"\t"+ii);
                        bind.transact(ii, data, reply, 0);
                    }
                    //sleep(1000);
                }
                return;
            }
            catch (Exception e) {
                response.code(500);
                response.send(e.toString());
                return;
            }
        }
    }
    private static void test(){
        String name="test:\n";
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method_getServicelist = clazz.getMethod("listServices");
            Method method_getService = clazz.getMethod("getService", String.class);
            //String ss[] = (String[])method_getServicelist.invoke(clazz);
            //for (String s:ss){
                String s = "phone";
                IBinder bind = (IBinder) method_getService.invoke(clazz, s);
                //Parcel data =  null,reply=null;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                String ifname = bind.getInterfaceDescriptor();
                data.writeInterfaceToken(ifname);
                //data.writeString("test");
                //data.writeInt(1000000);
                Class<?> ifn = Class.forName(ifname + "$Stub");
                if (ifn.equals(null))
                    System.out.println("is null");
                Field fields[] = ifn.getDeclaredFields();

                //Field i = ifn.getDeclaredField("TRANSACTION_disconnect");
                //i.setAccessible(true);
                //int ii = (int)i.get(ifn);
                //for (Field field : fields)
                    //System.out.println(s+":\t"+field.getName());
                System.out.println("\nbegin!");
                for (Field field : fields) {
                    name = field.getName();

                    if (name.startsWith("TRANSACTION") && name != "TRANSACTION_answerRingingCall" && name != "TRANSACTION_answerRingingCallForSubscriber"
                            && name != "TRANSACTION_endCall"&& name != "TRANSACTION_getCellNetworkScanResults" && name != "TRANSACTION_handlePinMmiForSubscriber") {
                        field.setAccessible(true);
                        int ii = (int) field.get(ifn);
                        System.out.println(s+name+"\t"+ii);
                        bind.transact(ii, data, reply, 0);
                    }
                    //sleep(1000);
                }
                System.out.println("BInder test end");
                //sleep(1000);
            //}

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
