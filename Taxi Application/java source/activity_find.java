package com.example.uitest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;

import java.util.ArrayList;

public class activity_find extends AppCompatActivity implements View.OnClickListener {

    private int button_Id = 0x9000;
    private int num_button = 0;
    private TMapData tmapData = null;
    private EditText editText = null;
    private Handler handler = null;
    private LinearLayout line = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        editText = findViewById(R.id.find_Edit);
        tmapData = new TMapData();
        Button button = (Button)findViewById(R.id.enter);

        line = (LinearLayout) findViewById(R.id.find_array);

        //고객이 검색한 값에 따라 각 주소를 버튼의 형태로 출력해줌
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String str = msg.getData().getString("data");
                Button customer_info = new Button(activity_find.this);
                customer_info.setOnClickListener(activity_find.this);
                customer_info.setId(button_Id + num_button++);
                //버튼의 아이디를 설정하고 주소의 값을 넣어줌
                customer_info.setText(str);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //버튼의 크기를 조절
                param.height = 250;
                param.gravity = Gravity.CENTER;
                customer_info.setLayoutParams(param);
                line.addView(customer_info);
                //linearRayout에 추가
            }
        };
        //주소를 검색하기 위한 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_Id = 0x9000;
                num_button = 0;
                String strData = editText.getText().toString();
                line.removeAllViews();
                //승객이 입력한 주소를 검색하여 해당하는 주소를 배열로 획득
                tmapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            //각 획득된 주소를 버튼의 형태로 만듦
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);

                            String str = item.getPOIName() + "\n" + item.getPOIAddress()+ "\n"+item.getPOIPoint().toString();
                            data.putString("data", str);
                            msg.setData(data);

                            handler.sendMessage(msg);
                        }

                    }
                });
            }
        });
    }
    //주소를 정할때 버튼을 누를경우 그 해당하는 버튼의 주소값이 전달됨
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Button button = (Button)v;
        String str[] = button.getText().toString().trim().split("\n");
        //버튼의 값 중 좌표값만 뽑아 전달하기위해 intent에 값을 넣어줌
        intent.putExtra("point",str[2]);
        Log.d("chop",str[2]);
        setResult(RESULT_OK,intent);
        //버튼의 값을 전달
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        //버튼의 값 중 좌표값만 뽑아 전달하기위해 intent에 값을 넣어줌
        intent.putExtra("point","0");
        setResult(RESULT_OK,intent);
        finish();
    }
}
