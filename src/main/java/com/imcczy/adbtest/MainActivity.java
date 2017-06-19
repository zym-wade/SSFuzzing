package com.imcczy.adbtest;

import android.app.Activity;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity{
    static String servicename,countint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        EditText service = (EditText) findViewById(R.id.editText);
        EditText count = (EditText) findViewById(R.id.editText2);

        service.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                servicename = s.toString();
            }
        });
        count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                countint = s.toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),servicename+"\t"+countint,Toast.LENGTH_SHORT).show();
                test(servicename,countint);
            }
        });




        


    }

    private void wifi(){

        String name="test";
        try {
            FileOutputStream outputStream = openFileOutput("test",
                    Activity.MODE_APPEND);
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method_getServicelist = clazz.getMethod("listServices");
            Method method_getService = clazz.getMethod("getService", String.class);
            String ss[] = {"sip","carrier_config","phone","isms","iphonesubinfo","simphonebook","telecom","isub","nfc","imms","media_projection",
                    "launcherapps","fingerprint","trust","media_router","media_session","restrictions","print","graphicsstats","assetatlas","dreams",
                    "commontime_management","samplingprofiler","voiceinteraction","appwidget","backup","jobscheduler","serial","usb","midi",
                    "DockObserver","audio","wallpaper","dropbox","search","country_detector","location","devicestoragemonitor","notification","updatelock",
                    "servicediscovery","connectivity","ethernet","rttmanager","wifiscanner","wifi","wifip2p","netpolicy","netstats","network_score",
                    "textservices","network_management","clipboard","statusbar","device_policy","deviceidle","lock_settings","uimode","accessibility",
                    "input_method","bluetooth_manager","input","window","alarm","consumer_ir","vibrator","content","account","media.camera.proxy",
                    "telephony.registry","scheduling_policy","webviewupdate","usagestats","battery","processinfo","permission","procstats","user","package",
                    "media.camera","display","power","appops","batterystats","batteryproperties","android.security.keystore","android.service.gatekeeper.IGateKeeperService"};
            for (String s:ss) {
                name = s;
                IBinder bind = (IBinder) method_getService.invoke(clazz, s);
                if (!(bind == null)) {

                    String ifname = bind.getInterfaceDescriptor();
                    Log.d(name,ifname);
                    Class<?> ifn = Class.forName(ifname + "$Stub");
                    if (ifn.equals(null)) {
                        String cn = ifn.getName()+ "is null\n";
                        //Log.d(ifn.getName(), "is null");
                        outputStream.write(cn.getBytes());
                        continue;
                    }
                    Field fields[] = ifn.getDeclaredFields();
                    for (Field field : fields) {
                        String sss = s+ ":\t" + field.getName()+"\n";
                        //Log.d(s, ":\t" + field.getName());
                        outputStream.write(sss.getBytes());
                    }
                }else {
                    String n = name+"is null\n";
                    //Log.d(name, "is null");
                    outputStream.write(n.getBytes());
                }
            }
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            Log.d(name,e.toString());
        }
    }
    private static void test(String name,String cout){
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method_getService = clazz.getMethod("getService",String.class);
            String s = name;

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
            int ii = Integer.valueOf(cout);
            bind.transact(ii, data, reply, 0);

            /*
            for (Field field : fields) {
                String name = field.getName();
                if (name.startsWith("TRANSACTION")) {
                    field.setAccessible(true);
                    int ii = (int) field.get(ifn);
                    bind.transact(ii, data, reply, 0);
                    Log.d("fuzzing",reply.marshall().toString());
                }

            }
*/
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
