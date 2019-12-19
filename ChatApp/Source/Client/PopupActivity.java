package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        Button okButton = (Button)findViewById(R.id.ok);


        final Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        nameText.setText(data);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.putExtra("result",nameText.getText().toString());
                setResult(RESULT_OK,intent2);
                finish();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return  true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
