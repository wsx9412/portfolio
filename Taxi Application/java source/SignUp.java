package com.example.uitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button cancelButton = (Button)findViewById(R.id.SignCancelButton);
        Button signButton = (Button)findViewById(R.id.SignUpButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"취소했습니다",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText name_edit = (EditText)findViewById(R.id.NameEdit);
                EditText phone_edit = (EditText)findViewById(R.id.PhoneEdit);
                EditText id_edit = (EditText)findViewById(R.id.IdEdit);
                EditText password_edit = (EditText)findViewById(R.id.PasswordEdit);

                String name_text = name_edit.getText().toString().trim();
                String phone_text = phone_edit.getText().toString().trim();
                String id_text = id_edit.getText().toString().trim();
                String password_text = password_edit.getText().toString().trim();
                String id_type;

                RadioGroup type_button = (RadioGroup)findViewById(R.id.AccoutnRadio);

                if(type_button.getCheckedRadioButtonId() == R.id.DriverRadio)
                    id_type = "0";
                else if(type_button.getCheckedRadioButtonId() == R.id.CustomerRadio)
                    id_type = "1";
                else
                    id_type = null;

                if(name_text.getBytes().length <= 0 || phone_text.getBytes().length <= 0 || id_text.getBytes().length <= 0 || password_text.getBytes().length <= 0 || id_type.getBytes().length <= 0 )
                    Toast.makeText(getApplicationContext(),"빈칸을 모두 입력해주세요",Toast.LENGTH_LONG).show();
                else{
                    try{
                        String result = new JsonTask().execute("http://175.196.158.156:3000/post","sign_up",name_text,phone_text,id_text,password_text,id_type).get();
                        if(result.equals("0")){
                            Toast.makeText(getApplicationContext(), "아이디가 중복됩니다.", Toast.LENGTH_LONG).show();
                        }
                        else if(result.equals("1")){
                            Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), "서버와 연결이 불가능합니다.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
