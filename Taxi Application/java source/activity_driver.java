package com.example.uitest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.Timer;
import java.util.TimerTask;

public class activity_driver extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    public static final int REQUEST_CODE = 1001;
    private TMapGpsManager gps = null;
    private TMapPoint startPoint =  null;
    private TMapPoint destinationPoint =  null;
    private EditText edit  = null;
    private EditText destination = null;
    private boolean m_bTrackingmode = true;
    private TMapView tmapview = null;
    private TMapMarkerItem tItem = null;
    private TMapMarkerItem tItem2 = null;
    private Handler handler = null;
    private Handler handler2 = null;
    private Thread t = null;
    private String address = null;
    private String id = null;
    private String customer_id = null;
    private TMapData tMapData = null;
    private Button wait_button = null;
    private Button ok_button = null;
    private Button cancel_button = null;
    private Button reset_button = null;
    private double now_latitude;
    private double now_longitude;
    private Timer timer = null;
    private Intent intent = null;
    private TimerTask addTask;
    private int button_switch;
    private Bitmap marker_start;
    private Bitmap marker_end;
    private double destination_longitude;
    private double destination_latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        //Button 설정
        wait_button = (Button)findViewById(R.id.wait_button);
        ok_button = (Button)findViewById(R.id.ok_button);
        cancel_button = (Button)findViewById(R.id.cancel_button);
        reset_button = (Button)findViewById(R.id.reset_driver_button);
        //button visible 설정
        ok_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);
        reset_button.setVisibility(View.GONE);
        //스위치 및 목적지 설정
        button_switch = 0;
        destination_longitude = 0;
        destination_latitude = 0;

        RelativeLayout rt = (RelativeLayout)findViewById(R.id.Relative_map2);

        //로그인시 아이디정보 획득
        intent = getIntent();
        id = intent.getStringExtra("id");

        tmapview  = new TMapView(activity_driver.this);
        tmapview.setSKTMapApiKey(getString(R.string.tmap_code));
        //T-Map 인증 코드
        rt.addView(tmapview);
        //T-Map 지도 설정
        tMapData = new TMapData();
        tmapview.setCompassMode(false);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        //gps 설정
        gps = new TMapGpsManager(activity_driver.this);

        gps.setMinTime(3000);
        gps.setMinDistance(30);
        //gps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        gps.setProvider(TMapGpsManager.GPS_PROVIDER);
        //app player일 경우 network_provider일 경우 위치를 잡지 못함
        //권한 얻기
        get_permission();
        gps.OpenGps();
        tItem = new TMapMarkerItem();
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        //마커에 출력할 비트맵 설정
        Bitmap bitmap_start = BitmapFactory.decodeResource(activity_driver.this.getResources(),R.drawable.map_marker_start);
        Bitmap bitmap_end = BitmapFactory.decodeResource(activity_driver.this.getResources(),R.drawable.map_marker_end);
        marker_start=Bitmap.createScaledBitmap(bitmap_start, bitmap_start.getWidth()/8,bitmap_start.getHeight()/8, false);
        marker_end=Bitmap.createScaledBitmap(bitmap_end, bitmap_end.getWidth()/8,bitmap_end.getHeight()/8, false);

        tItem = new TMapMarkerItem();//출발지마커
        tItem2 = new TMapMarkerItem();//도착지마커
        //마커의 크기를 조절
        tItem.setIcon(marker_start);
        tItem.setPosition(0.5f,1.0f);
        tItem2.setIcon(marker_end);
        tItem2.setPosition(0.5f,1.0f);

        //timer 설정
        timer = new Timer();

        wait_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_switch == 0){
                    m_bTrackingmode = false;
                    gps.CloseGps();
                    addTask = createTimerTask();
                    new JsonTask().execute(getString(R.string.address),"isDrive_true",id);
                    timer.schedule(addTask,0,3000);
                    button_switch = 1;
                    wait_button.setText("운행중");
                }
                else{
                    m_bTrackingmode = true;
                    gps.OpenGps();
                    tmapview.removeAllMarkerItem();
                    new JsonTask().execute(getString(R.string.address),"isDrive_false",id);
                    button_switch = 0;
                    wait_button.setText("운행정지");
                    if(addTask != null){
                        addTask.cancel();
                    }
                }
            }
        });
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_bTrackingmode = true;
                gps.OpenGps();
                new JsonTask().execute(getString(R.string.address3),"ok",id,customer_id);
                wait_button.setVisibility(View.GONE);
                ok_button.setVisibility(View.GONE);
                cancel_button.setVisibility(View.GONE);
                reset_button.setVisibility(View.VISIBLE);
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_bTrackingmode = true;
                gps.OpenGps();
                new JsonTask().execute(getString(R.string.address3),"cancel",id,customer_id);
                wait_button.setVisibility(View.VISIBLE);
                ok_button.setVisibility(View.GONE);
                cancel_button.setVisibility(View.GONE);
            }
        });
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapview.removeAllMarkerItem();
                new JsonTask().execute(getString(R.string.address3),"reset_driver",id);
                destination_longitude = 0;
                destination_latitude = 0;
                Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
                m_bTrackingmode = true;
                gps.OpenGps();
                addTask = createTimerTask();
                timer.schedule(addTask,0,3000);
                button_switch = 1;
                wait_button.setText("운행중");
                reset_button.setVisibility(View.GONE);
                wait_button.setVisibility(View.VISIBLE);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                wait_button.setVisibility(View.GONE);
                ok_button.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
            }
        };
        handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
            }
        };
    }
    private void get_permission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            return;
        }
    }

    public TimerTask createTimerTask(){
        TimerTask newTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    String result = new JsonTask().execute(getString(R.string.address2),"get_information",id,getString(R.string.type_driver)).get();
                    if(!result.equals("0")){
                        m_bTrackingmode = false;
                        gps.CloseGps();
                        Message msg2 = new Message();
                        Bundle data2 = new Bundle();
                        handler.sendMessage(msg2);
                        tmapview.removeAllMarkerItem();
                        String [] results = result.split(" ");
                        //정보획득
                        customer_id = results[2];
                        double longitude = Double.parseDouble(results[5]);
                        double latitude = Double.parseDouble(results[8]);
                        destination_longitude = Double.parseDouble(results[11]);
                        destination_latitude = Double.parseDouble(results[14]);
                        TMapPoint tmapPoint = new TMapPoint(latitude,longitude);
                        TMapPoint tmapPoint2 = new TMapPoint(destination_latitude,destination_longitude);
                        setCeneterPoint(latitude,longitude,destination_latitude,destination_longitude);
                        //출발지점 설정
                        tItem.setTMapPoint(tmapPoint);
                        tItem2.setTMapPoint(tmapPoint2);

                        tmapview.addMarkerItem("tItem",tItem);
                        tmapview.addMarkerItem("tItem2",tItem2);
                        //마커의 출력
                        //줌레벨 수정
                        addTask.cancel();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        return newTask;
    }
    private void setCeneterPoint(Double latitude, Double longitude, Double destination_latitude, Double destination_longitude){
        TMapPoint lefttop;
        TMapPoint rightbottom;
        if(latitude > destination_latitude){

            if(longitude > destination_longitude){
                lefttop = new TMapPoint(latitude-0.001,longitude+0.001);
                rightbottom = new TMapPoint(destination_latitude+0.001,destination_longitude-0.001);
            }else
            {
                lefttop = new TMapPoint(latitude-0.001,destination_longitude+0.001);
                rightbottom = new TMapPoint(destination_latitude+0.001,longitude-0.001);
            }
        }else{
            if(longitude > destination_longitude){
                lefttop = new TMapPoint(destination_latitude-0.001,longitude+0.001);
                rightbottom = new TMapPoint(latitude+0.001,destination_longitude-0.001);
            }else
            {
                lefttop = new TMapPoint(destination_latitude-0.001,destination_longitude+0.001);
                rightbottom = new TMapPoint(latitude+0.001,longitude-0.001);
            }
        }
        tmapview.setCenterPoint( (longitude + destination_longitude) / 2.0,(latitude + destination_latitude) / 2.0,true);
        tmapview.zoomToTMapPoint(lefttop,rightbottom);
    }
    @Override
    protected void onStart() {
        super.onStart();
        m_bTrackingmode = true;
        gps.OpenGps();
    }

    @Override
    protected void onStop() {
        super.onStop();
        m_bTrackingmode = false;
        gps.CloseGps();
        tmapview.removeAllMarkerItem();
        if(addTask != null){
            addTask.cancel();
        }
        button_switch = 0;
        wait_button.setText("운행정지");
        try{
            new JsonTask().execute(getString(R.string.address),"logout",id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_bTrackingmode = false;
        gps.CloseGps();
        if(addTask != null){
            addTask.cancel();
        }
        try{
            new JsonTask().execute(getString(R.string.address),"logout",id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingmode){
            tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
            tmapview.setCenterPoint(location.getLongitude(), location.getLatitude());
            startPoint = tmapview.getLocationPoint();
            now_latitude = location.getLatitude();
            now_longitude = location.getLongitude();
            if(destination_latitude != 0 && destination_longitude != 0){
                int now_la, now_lo, des_la,des_lo;
                now_la = (int)Math.floor(now_latitude * 1000);
                now_lo = (int)Math.floor(now_longitude * 1000);
                des_la = (int)Math.floor(destination_latitude * 1000);
                des_lo = (int)Math.floor(destination_longitude * 1000);
                if(now_la == des_la && now_lo == des_lo){
                    new JsonTask().execute(getString(R.string.address3),"reset_driver",id);
                    destination_longitude = 0;
                    destination_latitude = 0;
                    Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
                    addTask = createTimerTask();
                    timer.schedule(addTask,0,3000);
                    button_switch = 1;
                    wait_button.setText("운행중");
                    wait_button.setVisibility(View.VISIBLE);
                }
            }
            try{
                String result = new JsonTask().execute(getString(R.string.address2),"location_set",id,getString(R.string.type_driver),Double.toString(location.getLatitude()),Double.toString(location.getLongitude())).get();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "서버와 연결이 불가능합니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
