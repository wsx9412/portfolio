package com.example.crt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.InputStream;
import java.util.Arrays;

public class CamActivity extends AppCompatActivity{
    static final int REQUEST_IMAGE_CAPTURE = 1; //직접 촬영시 인텐트전달값
    static final int REQUEST_CODE = 0; //갤러리에서 이미지 가저올때의 전달값

    int permissionCam;
    int permissionWrite;
    int permissionRead;
    int permissionInternet;
    int index;

    String language;
    FirebaseVisionImage fireImage;
    FirebaseVisionTextRecognizer recognizer;

    ImageView image;
    TextView imageText;
    TextView translateText;

    Intent camera;
    Intent galary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        //init
        Button cam = (Button)findViewById(R.id.camera);
        Button gal = (Button)findViewById(R.id.galary);
        image = (ImageView)findViewById(R.id.image);
        imageText = (TextView)findViewById(R.id.imageText);
        translateText = (TextView)findViewById(R.id.translateText);
        camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        galary = new Intent();
        galary.setType("image/*");
        galary.setAction(Intent.ACTION_GET_CONTENT);
        //init
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                // get Camera Permission
                getPermission(camera);
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                getPermission(galary);
            }
        });


    }
    private void getPermission(Intent intent){
        permissionCam = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA);
        permissionRead = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionInternet = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.INTERNET);
        permissionWrite = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCam != PackageManager.PERMISSION_GRANTED && permissionWrite != PackageManager.PERMISSION_GRANTED && permissionRead != PackageManager.PERMISSION_GRANTED && permissionInternet != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(CamActivity.this,Manifest.permission.CAMERA)){
            }
            else{
                ActivityCompat.requestPermissions(CamActivity.this, new String[]{Manifest.permission.CAMERA,  Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)==  PackageManager.PERMISSION_GRANTED){
                    if(intent.resolveActivity(getPackageManager())!=null){
                        if(index == 0)
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        else
                            startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            }
        }else{
            if(index == 0)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            else
                startActivityForResult(intent, REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            image.setImageBitmap(imageBitmap);
            imageToText(imageBitmap);
        }
        else if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(in);
                in.close();
                image.setImageBitmap(bmp);
                imageToText(bmp);
            }catch (Exception e){

            }
        }
    }
    //이미지에서 텍스트추출
    private void imageToText(Bitmap bmp){
        fireImage = FirebaseVisionImage.fromBitmap(bmp);
        recognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en","ko"))
                .build();
        Task<FirebaseVisionText> result = recognizer.processImage(fireImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        String resultText = firebaseVisionText.getText();
                        resultText = resultText.trim().replaceAll(System.lineSeparator()," ");
                        imageText.setText(resultText);
                        getCountry(resultText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    //추출된 텍스트의 언어감지
    private void getCountry(String text){
        FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(text).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != "und") {
                                    language = languageCode;
                                    translate(text);
                                } else {
                                    language = null;
                                    Toast.makeText(getApplicationContext(),"식별할 수 없는 언어입니다.",Toast.LENGTH_SHORT);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                language = null;
                                Toast.makeText(getApplicationContext(),"언어식별에 실패했습니다.",Toast.LENGTH_SHORT);
                            }
                        });
    }
    //번역
    private void translate(String text){
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.languageForLanguageCode(language))
                        .setTargetLanguage(FirebaseTranslateLanguage.KO)
                        .build();
        final FirebaseTranslator translator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        //번역시 필요한 파일 설치
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                translator.translate(text).addOnSuccessListener(
                                        new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(@NonNull String translatedText) {
                                                translateText.setText(translatedText);
                                            }
                                        }
                                ).addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        }
                                );
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

    }

}
