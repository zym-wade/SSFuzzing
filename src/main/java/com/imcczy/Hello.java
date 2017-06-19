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
 * Created by imcczy on 2017/6/19.
 */

public class Hello {
    static Looper looper;

    public static void main(String[] args) {

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
        httpServer.get("/test/.*", new AnonymousClass3());
        httpServer.listen(server, 53516);

        Looper.loop();

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
            try {

                Class<?> clazz = Class.forName("android.os.ServiceManager");
                Method method_getService = clazz.getMethod("getService",String.class);
                String s = request.getPath().replace("/test/","");

                IBinder bind = (IBinder) method_getService.invoke(clazz, s);
                String ifname = bind.getInterfaceDescriptor();
                Class<?> ifn = Class.forName(ifname + "$Stub");
                if (ifn.equals(null))
                    System.out.println("is null");
                Field fields[] = ifn.getDeclaredFields();
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken(ifname);
                Parcel reply = Parcel.obtain();
                System.out.println("\nbegin!");
                //data.writeString("com.imcczy.adbtest");
                //data.writeString("android.permission.CAPTURE_AUDIO_OUTPUT");
                //bind.transact(39, data, reply, 0);
                StringBuffer sb = new StringBuffer();
                for (Field field : fields) {
                    String name = field.getName();
                    if (name.startsWith("TRANSACTION")) {
                        field.setAccessible(true);
                        int ii = (int) field.get(ifn);
                        sb.append(s+name+"\t"+ii+"\n");
                        bind.transact(ii, data, reply, 0);
                    }

                }

                response.code(200);
                response.send(sb.toString());

                return;
            }
            catch (Exception e) {
                response.code(500);
                response.send(e.toString());
                return;
            }
        }
    }
}
