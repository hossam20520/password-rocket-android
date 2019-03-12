package com.example.passwordrocket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

class FingeprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    CancellationSignal cancellationSignal;

    public FingeprintHandler(Context mcontext){
        context = mcontext;
    }
    public void startAuth(FingerprintManager  manager, FingerprintManager.CryptoObject cryptoObject){
        cancellationSignal =  new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal,0 , this , null);
    }

    @Override
    public void onAuthenticationError(int errMsgid , CharSequence errString){
        Toast.makeText(context, "aut: error" + errString , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationFailed(){
        Toast.makeText(context,"auth : failed" , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAuthenticationHelp(int help , CharSequence helpString){
        Toast.makeText(context,"auth: faild" , Toast.LENGTH_LONG);
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
        //Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show();
        Intent intente = new Intent(context.getApplicationContext(), Display.class);
        intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intente);
    }
}

