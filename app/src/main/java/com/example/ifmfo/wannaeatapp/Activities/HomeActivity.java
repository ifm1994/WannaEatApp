package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private EditText inputDireccion;
//    private CardView botonUbicacionActual;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inputDireccion = (EditText) findViewById(R.id.restaurantAddressInput);

        if(getIntent().getExtras() != null){
            inputDireccion.setText(getIntent().getExtras().getString("direccionAutomatica", ""));
        }

        inputDireccion.setSelection(inputDireccion.getText().length());
        CardView searchRestaurantsButton = findViewById(R.id.searchRestaurantButton);

        searchRestaurantsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                if(inputDireccion.getText().length() != 0){
                    Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
                    intent.putExtra("direccion", inputDireccion.getText().toString());
                    startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(HomeActivity.this, "Debe rellenar una dirección",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data.getExtras() != null){
            Log.i("info.", "a la vuelta reescribo " + data.getExtras().getString("direccionBuscada", ""));
            inputDireccion.setText(data.getExtras().getString("direccionBuscada", ""));
        }
    }
}
