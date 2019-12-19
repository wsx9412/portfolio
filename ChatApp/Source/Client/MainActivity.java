package com.example.chatapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private int text_id = 0x9000;
    private int num_text = 0;
    Button enterText;
    LinearLayout linearLayout;
    EditText chatText;
    DataInputStream input;
    DataOutputStream out;
    Socket socket;
    InetAddress address;
    String data;
    String data2;
    String name;
    Handler handler;
    Handler handler2;
    ScrollView chatScroll;
    ImageButton nameButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                name = data.getStringExtra("result");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout)findViewById(R.id.linear);
        nameButton = (ImageButton)findViewById(R.id.nameButton);
        enterText = (Button)findViewById(R.id.enterText);
        chatText = (EditText)findViewById(R.id.chatText);
        chatScroll = (ScrollView)findViewById(R.id.chatScroll);
        name = "이름";

        enterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = chatText.getText().toString();
                if(!data.equals("") && out != null){
                    new SendThread(socket,data,name).start();
                }
            }
        });
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PopupActivity.class);
                intent.putExtra("data",name);
                startActivityForResult(intent,1);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1111){
                    data = msg.obj.toString();
                    TextView textView2 = new TextView(getApplicationContext());
                    textView2.setText(data);
                    textView2.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    linearLayout.addView(textView2);

                }
                if(msg.what == 2222){
                    chatText.setText("");
                    TextView textView = new TextView(getApplicationContext());
                    data = msg.obj.toString();
                    textView.setText(data);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    textView.setId(text_id + num_text++);
                    textView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_custom2));
                    linearLayout.addView(textView);
                    handler2.sendEmptyMessage(1);
                }
            }
        };
        handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                chatScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    address = InetAddress.getByName(getString(R.string.ip_address));
                    socket = new Socket(address, 9999);
                    out = new DataOutputStream(socket.getOutputStream());
                    input = new DataInputStream(socket.getInputStream());
                    new ReceiveThread(socket,handler).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
class SendThread extends Thread{
    Socket socket;
    String sendMsg;
    String name;
    DataOutputStream output;
    public SendThread(Socket socket, String sendMsg, String name) {
        this.socket = socket;
        this.sendMsg = sendMsg;
        this.name = name;
        try{
            output = new DataOutputStream(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            if(output != null){
                if(sendMsg != null){
                    output.writeUTF(name);
                    output.writeUTF(sendMsg);
                }
            }
        }catch (Exception e){

        }
    }
}
class ReceiveThread extends Thread{
    Socket socket;
    DataInputStream in;
    Handler handler;
    public ReceiveThread(Socket socket,Handler handler) {
        this.socket = socket;
        this.handler = handler;
        try{
            in = new DataInputStream(socket.getInputStream());
        }catch (Exception e){

        }
    }

    @Override
    public void run() {
        try{
            while (in != null){
                String msg = in.readUTF();
                String msg2 = in.readUTF();
                if(msg != null){
                    // 핸들러에게 전달할 메세지 객체
                    Message hdmg = handler.obtainMessage();
                    Message hdmg2 = handler.obtainMessage();

                    // 핸들러에게 전달할 메세지의 식별자
                    hdmg.what = 1111;
                    hdmg2.what = 2222;

                    // 메세지의 본문
                    hdmg.obj = msg;
                    hdmg2.obj = msg2;

                    // 핸들러에게 메세지 전달 ( 화면 처리 )
                    handler.sendMessage(hdmg);
                    handler.sendMessage(hdmg2);
                }
            }
        }catch (Exception e){

        }
    }
}

