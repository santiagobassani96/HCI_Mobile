package com.bassanidevelopment.santiago.hci_movil.API;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bassanidevelopment.santiago.hci_movil.BottomNavigationActivity;
import com.bassanidevelopment.santiago.hci_movil.R;

public class LoadingScreenActivity extends Activity {

    //Introduce a delay
    private final int WAIT_TIME = 2500;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHandler = new Handler(); // anything posted to this handler will run on the UI Thread
        System.out.println("LoadingScreenActivity  screen started");
        setContentView(R.layout.loading_screen);
        //findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        final Runnable onUi = new Runnable() {
            @Override
            public void run() {
                // this will run on the main UI thread
                Intent mainIntent = new Intent(LoadingScreenActivity.this, BottomNavigationActivity.class);
                LoadingScreenActivity.this.startActivity(mainIntent);
                LoadingScreenActivity.this.finish();
            }
        };

        final Runnable background = new Runnable() {
            @Override
            public void run() {
                try {
                    // This is the delay
                    Thread.sleep(WAIT_TIME);
                    // This will run on a background thread
                    //Simulating a long running task
                    Thread.sleep(1000);
                    System.out.println("Going to Profile Data");
                    uiHandler.post(onUi);
                }catch (Exception e){
                    System.out.println("se cago");
                }
            }
        };

        new Thread(background).start();
    }

}

