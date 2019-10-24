package com.example.uitest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapAddressInfo;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapGpsManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class activity_map extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback,TMapView.OnClickListenerCallback {
    public static final int REQUEST_CODE = 1001;
    private TMapGpsManager gps = null;
    private TMapPoint startPoint =  null;
    private TMapPoint destinationPoint =  null;
    private EditText edit  = null;
    private EditText destination_edit = null;
    private boolean m_bTrackingmode = true;
    private TMapView tmapview = null;
    private TMapMarkerItem tItem = null;
    private Handler handler = null;
    private Handler handler2 = null;
    private Handler handler3 = null;
    private Handler handler4 = null;
    private Thread t = null;
    private String address = null;
    private TMapData tMapData = null;
    private String id = null;
    private boolean found = true;
    private Timer timer;
    private double now_latitude;
    private double now_longitude;
    private Bitmap marker_taxi;
    private Bitmap marker_end;
    private TimerTask addTask = null;

    //택시 검색용 분기제어
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
            if(data.hasExtra("point")){

                address = data.getExtras().getString("point");
                if(!address.equals("0")){
                    //find Activity에서 데이터를 받지않고 종료했을때 문제가 생기지 않게 하기위함
                    String[] point = address.split(" ");
                    //받아온 주소의 데이터중 좌표값을 얻어냄
                    double latitude = Double.parseDouble(point[1]);
                    double longitude = Double.parseDouble(point[3]);

                    m_bTrackingmode = false;
                    gps.CloseGps();

                    TMapPoint tmapPoint = new TMapPoint(latitude,longitude);
                    TMapMarkerItem tItem = new TMapMarkerItem();
                    //마커의 크기를 조절
                    tItem.setIcon(marker_end);
                    tItem.setPosition(0.5f,1.0f);

                    tmapview.removeMarkerItem("tItem");
                    tItem.setTMapPoint(tmapPoint);
                    destinationPoint = tmapPoint;
                    //도착지점 설정
                    tmapview.addMarkerItem("tItem",tItem);
                    //마커의 출력
                    tmapview.setCenterPoint(tmapPoint.getLatitude(), tmapPoint.getLongitude(),true);
                    //지도의 중앙부분에 위치
                    //받아온 좌표값을 리버스지오코딩하여 정확한 주소를 획득 및 출력
                    tMapData.reverseGeocoding(tmapPoint.getLatitude(), tmapPoint.getLongitude(), "A04", new TMapData.reverseGeocodingListenerCallback() {
                        @Override
                        public void onReverseGeocoding(TMapAddressInfo tMapAddressInfo) {
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("data", tMapAddressInfo.strFullAddress);
                            msg.setData(data);
                            handler2.sendMessage(msg);
                        }
                    });
                }

            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        RelativeLayout rt = (RelativeLayout)findViewById(R.id.Relative_map);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        tmapview  = new TMapView(activity_map.this);
        tmapview.setSKTMapApiKey(getString(R.string.tmap_code));
        //T-Map 인증 코드
        rt.addView(tmapview);
        ImageButton find_button = (ImageButton)findViewById(R.id.find_button);
        ImageButton enter_button = (ImageButton)findViewById(R.id.enter_button);
        ImageButton call_button = (ImageButton)findViewById(R.id.call_button);
        Button reset_button = (Button)findViewById(R.id.reset_customer_button);
        //동기식 통신을 위해 타이머를 이용
        timer = new Timer();
        //T-Map 지도 설정
        tMapData = new TMapData();

        edit = (EditText)findViewById(R.id.nowPosition_Edit);
        destination_edit = (EditText)findViewById(R.id.find_Edit);

        tmapview.setCompassMode(false);
        tmapview.setIconVisibility(true);

        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        gps = new TMapGpsManager(activity_map.this);

        gps.setMinTime(20000);
        gps.setMinDistance(30);
        //gps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        gps.setProvider(TMapGpsManager.GPS_PROVIDER);
        //app player일 경우 network_provider일 경우 위치를 잡지 못함
        //
        //권한 얻기
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
        gps.OpenGps();
        //현재위치 텍스트 변경 핸들러
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                edit.setText(str);
            }
        };
        //도착지점 텍스트 변경 핸들러
        handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                destination_edit.setText(str);
            }
        };
        //토스트 메세지 출력 핸들러
        handler3 = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                Toast.makeText(activity_map.this, str, Toast.LENGTH_LONG).show();
            }
        };

        handler4 = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                Toast.makeText(activity_map.this, str, Toast.LENGTH_LONG).show();
            }
        };
        tItem = new TMapMarkerItem();
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        Bitmap bitmap_end = BitmapFactory.decodeResource(activity_map.this.getResources(),R.drawable.map_marker_end);
        Bitmap bitmap_taxi = BitmapFactory.decodeResource(activity_map.this.getResources(),R.drawable.map_marker_taxi);
        marker_end=Bitmap.createScaledBitmap(bitmap_end, bitmap_end.getWidth()/8,bitmap_end.getHeight()/8, false);
        marker_taxi=Bitmap.createScaledBitmap(bitmap_taxi, bitmap_taxi.getWidth()/8,bitmap_taxi.getHeight()/8, false);

        //timer.schedule(addTask,1000,3000);

        //버튼 리스너
        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startPoint != null){
                    tmapview.setCenterPoint(startPoint.getLongitude(),startPoint.getLatitude());
                }
            }
        });

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(destinationPoint != null){
                        new JsonTask().execute(getString(R.string.address2),"set_destination",id,Double.toString(destinationPoint.getLatitude()),Double.toString(destinationPoint.getLongitude()));
                        new JsonTask().execute(getString(R.string.address3),"get_nearestTaxi",id,Double.toString(now_latitude),Double.toString(now_longitude));
                        addTask = createTimerTask();

                        m_bTrackingmode = false;
                        gps.CloseGps();
                        //Toast.makeText(activity_map.this,result,Toast.LENGTH_LONG);
                        timer.schedule(addTask,0,3000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_map.this,activity_find.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapview.removeAllMarkerItem();
                new JsonTask().execute(getString(R.string.address3),"reset_customer",id);
                tmapview.setCenterPoint(tmapview.getLongitude(),tmapview.getLatitude());
                destinationPoint = null;
                destination_edit.setText(" ");
                Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
            }
        });
    }

    public TimerTask createTimerTask(){
        TimerTask newTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                try{
                    String result = new JsonTask().execute(getString(R.string.address2),"get_information",id,getString(R.string.type_customer)).get();
                    if(!result.equals("0")){
                        String [] results = result.split(" ");

                        TMapMarkerItem tItem2 = new TMapMarkerItem();

                        tItem2.setIcon(marker_taxi);
                        tItem2.setPosition(0.5f,1.0f);

                        Double longitude = Double.parseDouble(results[2]);
                        Double latitude = Double.parseDouble(results[5]);
                        String taxi_id = results[8];
                        //도착지점 설정
                        TMapPoint tmapPoint = new TMapPoint(latitude,longitude);
                        tmapview.setCenterPoint(latitude,longitude,true);
                        tmapview.removeMarkerItem("tItem2");
                        tItem2.setTMapPoint(tmapPoint);
                        tmapview.addMarkerItem("tItem2",tItem2);
                        //마커의 출력

                        double taxi_latitude = tmapPoint.getLatitude();
                        double taxi_longitude = tmapPoint.getLongitude();
                        int now_la, now_lo, des_la,des_lo;
                        now_la = (int)Math.floor(now_latitude * 1000);
                        now_lo = (int)Math.floor(now_longitude * 1000);
                        des_la = (int)Math.floor(taxi_latitude * 1000);
                        des_lo = (int)Math.floor(taxi_longitude * 1000);
                        if(now_la == des_la && now_lo == des_lo){
                            tmapview.removeMarkerItem("tItem2");
                            gps.OpenGps();
                            addTask.cancel();
                            Toast.makeText(getApplicationContext(), "택시 탑승", Toast.LENGTH_LONG).show();
                            }
                    }
                }catch (Exception e){
                    data.putString("data", "서버에 연결할 수 없습니다.");
                    msg.setData(data);
                    handler3.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        };
        return newTask;
    }

    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, final TMapPoint tMapPoint, PointF pointF) {

        return false;
    }

    @Override//어플리케이션이 시작했을때
    protected void onStart() {
        super.onStart();
        gps.OpenGps();
    }

    @Override//어플리케이션을 종료했을때
    protected void onStop() {
        super.onStop();
        gps.CloseGps();
        if(addTask != null){
            addTask.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
        return false;
    }


    public void reverseGeocoding(Location location){
        //획득한 좌표를 리버스지오코딩하여 정확한 주소를 획득
        tMapData.reverseGeocoding(location.getLatitude(), location.getLongitude(), "A04", new TMapData.reverseGeocodingListenerCallback() {
            @Override
            public void onReverseGeocoding(TMapAddressInfo tMapAddressInfo) {
                Message msg = new Message();
                Bundle data = new Bundle();
                //Log.d("chop","선택한 위치의 주소는 " + tMapAddressInfo.strFullAddress);
                //획득한 주소를 핸들러로 보내서 editText의 값을 수정
                address = tMapAddressInfo.strFullAddress;
                data.putString("data", address);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        });
    }
    @Override
    public void onLocationChange(Location location) {// GPS 프로바이더 사용가능여부
        if(m_bTrackingmode){
            tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
            tmapview.setCenterPoint(location.getLongitude(), location.getLatitude());
            startPoint = tmapview.getLocationPoint();
            reverseGeocoding(location);
            try{
                String result = new JsonTask().execute(getString(R.string.address2),"location_set",id,getString(R.string.type_customer),Double.toString(location.getLatitude()),Double.toString(location.getLongitude())).get();
                now_latitude = location.getLatitude();
                now_longitude = location.getLongitude();
                if(destinationPoint != null){
                    double destination_latitude = destinationPoint.getLatitude();
                    double destination_longitude = destinationPoint.getLongitude();
                    int now_la, now_lo, des_la,des_lo;
                    now_la = (int)Math.floor(now_latitude * 1000);
                    now_lo = (int)Math.floor(now_longitude * 1000);
                    des_la = (int)Math.floor(destination_latitude * 1000);
                    des_lo = (int)Math.floor(destination_longitude * 1000);
                    if(now_la == des_la && now_lo == des_lo){
                        new JsonTask().execute(getString(R.string.address3),"reset_customer",id);
                        destinationPoint = null;
                        Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(activity_map.this, "서버와 연결이 불가능합니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}