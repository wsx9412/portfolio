package com.example.uitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button)findViewById(R.id.LoginButton);
        Button signButton = (Button)findViewById(R.id.SignButton);
        signButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                intent.putExtra("test",0);
                startActivity(intent);
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText loginText = (EditText)findViewById(R.id.IdTextView);
                EditText passwordText = (EditText)findViewById(R.id.PasswordTextView);
                try{
                    String result = new JsonTask().execute(getString(R.string.address),"login",loginText.getText().toString(),passwordText.getText().toString()).get();
                    if(result.equals("0")){
                        Intent intent2 = new Intent(MainActivity.this,activity_driver.class);
                        intent2.putExtra("id",loginText.getText().toString());
                        Toast.makeText(getApplicationContext(),"로그인이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        startActivity(intent2);
                        finish();
                    }
                    else if(result.equals("1")){
                        Intent intent = new Intent(MainActivity.this,activity_map.class);
                        intent.putExtra("id",loginText.getText().toString());
                        Toast.makeText(getApplicationContext(),"로그인이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    }
                    else if(result.equals("2")){
                        Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                    else if(result.equals("3")){
                        Toast.makeText(getApplicationContext(), "이미 로그인중인 계정입니다.", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "서버와 연결이 불가능합니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
