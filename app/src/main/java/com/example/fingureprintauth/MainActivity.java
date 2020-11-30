package com.example.fingureprintauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Now that we have made our layout we will add the app dependencies and user permission//
        // Now Let's select our button and message text//

        TextView msg_txt = findViewById(R.id.tv_two);
        Button btn_login = findViewById(R.id.btn_login);


        //Create the BiometricManager and Let's check if our user can use the fingerprint sensor or not /

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){  //we will switch some constant to check different possibility
            case BiometricManager.BIOMETRIC_SUCCESS:
                msg_txt.setText("You can the fingerprint sensor to login");
                msg_txt.setTextColor(Color.parseColor("#Fafafa"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: // this is mean that the device don't have a fingerprint
                 msg_txt.setText("the device don't have a fingerprint sensor");
                btn_login.setVisibility(View.GONE);
                 break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                 msg_txt.setText("the biometric sensor is currently unavailable");
                 btn_login.setVisibility(View.GONE);
                 break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                 msg_txt.setText("your device don't have any fingerprint saved, please check your security settings");
                btn_login.setVisibility(View.GONE);
                 break;

        }

        // now we have created if we are able or not use the biometric sensors Let's start create our
        // biometric dialog box


        // First we need to create an executor
        Executor executor = ContextCompat.getMainExecutor(this);

        // now we need to create the biometric prompt callback
        //this will give us the result of the authentication and if we can login or not

        // just type this line and your biometric prompt will be generated automatically
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override  // this method is called when there is an error while the authentication

            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override // this method is called when the authentication is an success

            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Success !",Toast.LENGTH_SHORT).show();
            }

            @Override // this method called if we have failed the authentication

            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //Now Let's create our Biometric Dialog
        BiometricPrompt.PromptInfo promptInfo = new  BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User your fingerprint to login to your app")
                .setNegativeButtonText("Cancel")
                .build();

        // now everything is ready, all what we have to do is call the dialog when the user press the login button

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
                // now Let's run our app
            }
        });

    }
}