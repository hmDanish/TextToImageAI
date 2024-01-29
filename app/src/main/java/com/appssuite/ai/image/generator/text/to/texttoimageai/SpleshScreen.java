package com.appssuite.ai.image.generator.text.to.texttoimageai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SpleshScreen extends AppCompatActivity {
    final int splashTimeOut = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh_screen);


        Thread splashThread = new Thread(){
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while(wait < splashTimeOut){
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                }finally{
                    startActivity(new Intent(SpleshScreen.this,MainActivity.class));
                    finish();
                }
            }
        };
        splashThread.start();

    }
}