package com.example.passwordrocket;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static com.example.passwordrocket.Sign_Up_Activity.PREFS;
import static com.example.passwordrocket.Sign_Up_Activity.email_pref;

public class MainActivity extends AppCompatActivity {
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private KeyGenerator keyGenerator;
    Login_internet login_internet;
    public DBHelpear db;
    TextView txtView;
    Cipher cipher;
    KeyStore keyStore;
    Button login , sign_uppx;
    Context context;
    private ProgressBar progressBar;
//    TextInputEditText

    String userNameTxt , passwordTxt;
    EditText username, password;

    private static String Url =  "http://192.168.43.189:60/hossam/test.php";  //"https://tomorz.com/develop/test.php";
    private StringRequest stringRequest;
    private RequestQueue reques;

    private  static  final String KEY_NAME = "Hossam";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        db = new DBHelpear(this);
         login = findViewById(R.id.login);
        username  =  (EditText) findViewById(R.id.username_txt);
        password =   (EditText)  findViewById(R.id.password_txt);
        sign_uppx = (Button) findViewById(R.id.sign_uppx);
   progressBar = (ProgressBar) findViewById(R.id.progressBar);

   progressBar.setVisibility(View.GONE);
         reques = Volley.newRequestQueue(this);

     login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             progressBar.setVisibility(View.VISIBLE);
             if(isNetworkConnected()){
                // Toast.makeText(getApplicationContext() , "connected", Toast.LENGTH_LONG).show();
             }else{
                // Toast.makeText(getApplicationContext() , "not Connected", Toast.LENGTH_LONG).show();
             }
             userNameTxt =  username.getText().toString();
             passwordTxt =  password.getText().toString();


             stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {


                     try {
                         JSONObject jsonObject = new JSONObject(response);

                         if(jsonObject.names().get(0).equals("success")){
                             //email_pref = userNameTxt;
                             setPrefered(userNameTxt);

                             progressBar.setVisibility(View.GONE);
                             Intent intentez = new Intent(getApplicationContext(), Display.class);
                             intentez.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             startActivity(intentez);
                         }else if(jsonObject.names().get(0).equals("wrong_password")){
                             progressBar.setVisibility(View.GONE);
                             Toast.makeText(getApplicationContext() , "wrong password" , Toast.LENGTH_LONG).show();
                         } else {

                             progressBar.setVisibility(View.GONE);
                             password.setTextColor(Color.RED);
                             Toast.makeText(getApplicationContext() , "error" , Toast.LENGTH_LONG).show();
                         }

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {

                 }
             }) {
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     HashMap<String, String> hasmap = new HashMap<String , String>();
                     hasmap.put("email",userNameTxt);
                     hasmap.put("password" ,passwordTxt);
                     return  hasmap;
                 }
             };
             reques.add(stringRequest);

         }
     });


        sign_uppx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrefered(null);
                Intent loginc = new Intent(getApplicationContext(), Sign_Up_Activity.class);
                loginc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginc);

            }
        });




        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M ){
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            txtView = (TextView) findViewById(R.id.textView);
            if(!fingerprintManager.isHardwareDetected()){
                txtView.setText("Your Device doesnt Support");
            }
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                txtView.setText("Please Enable fingerPrint Permision");
            }
            if(!fingerprintManager.hasEnrolledFingerprints()){
                txtView.setText("no finger print confi");
            }

            if(!keyguardManager.isKeyguardSecure()){
                txtView.setText("please enable lockscreeen security");
            }else{
                try{
                    generateKey();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(initCipher()){
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingeprintHandler helper = new FingeprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }

        }
    }

    private boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);


        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try{
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return true;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e){
            throw  new RuntimeException("Failed to init chiper" , e);
        }
    }

    private void generateKey(){

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        keyGenerator.generateKey();
    }

    private  boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void setPrefered(String pref){
        SharedPreferences sp = getSharedPreferences(PREFS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email" , pref);
        editor.commit();
    }

    @Override
    protected  void  onPause(){
        super.onPause();

        System.exit(1);

    }



    }

