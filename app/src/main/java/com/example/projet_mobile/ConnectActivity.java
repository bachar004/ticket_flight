package com.example.projet_mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isconnected())
            Toast.makeText(this, "There is No Internet Connexion", Toast.LENGTH_LONG).show();
    }
    private boolean isconnected(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();}
        return false;
    }
}
