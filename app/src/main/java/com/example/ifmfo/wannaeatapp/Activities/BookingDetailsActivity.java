package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class BookingDetailsActivity extends AppCompatActivity {

    Spinner bookingHourDesplegable;
    ImageButton addCommensal;
    ImageButton removeCommensal;
    private int amountOfCommensal;
    Boolean bookingForToday;
    RelativeLayout todayButton;
    RelativeLayout otherDayButton;
    CardView continueBooking;
    Toolbar toolbar;
    TextView numberOfCommensal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        bookingHourDesplegable = findViewById(R.id.booking_hour_spinner);
        addCommensal = findViewById(R.id.addCommensal);
        removeCommensal = findViewById(R.id.removeCommensal);
        numberOfCommensal = findViewById(R.id.booking_commensal_number);
        amountOfCommensal = 0;
        todayButton = findViewById(R.id.day_switch_button_1);
        otherDayButton = findViewById(R.id.day_switch_button_2);
        bookingForToday = true;
        continueBooking = findViewById(R.id.continueBookingButon);

        numberOfCommensal.setText(Integer.toString(amountOfCommensal));

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(ic_action_orange_back);
            getSupportActionBar().setTitle("Pedido");
        }

        rellenarDesplegableHoraReserva();
        actualizarColorDeBotonesDelDia();

        bindEvents();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void bindEvents() {
        removeCommensal.setOnClickListener(v -> {
            if(amountOfCommensal > 0){
                amountOfCommensal--;
                numberOfCommensal.setText(Integer.toString(amountOfCommensal));
            }
        });

        addCommensal.setOnClickListener(v -> {
            if (amountOfCommensal < 20){
                amountOfCommensal++;
                numberOfCommensal.setText(Integer.toString(amountOfCommensal));
            }
        });

        continueBooking.setOnClickListener(arg0 -> {
            if(numberOfCommensal.getText().equals("0")){
                Toast.makeText(getApplicationContext(),"Número de comensales no válido", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Número válido", Toast.LENGTH_SHORT).show();
            }
        });

        todayButton.setOnClickListener(v -> {
            bookingForToday = true;
            actualizarColorDeBotonesDelDia();
        });

        otherDayButton.setOnClickListener(v -> {
            bookingForToday = false;
            actualizarColorDeBotonesDelDia();
        });
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void actualizarColorDeBotonesDelDia(){
        Resources res = getResources(); //resource handle
        Drawable dayButtonBackground = res.getDrawable(R.drawable.with_squared_orange_border);
        if(bookingForToday){
            todayButton.setBackgroundColor(getResources().getColor(R.color.orange));
            otherDayButton.setBackground(dayButtonBackground);
        }else{
            otherDayButton.setBackgroundColor(getResources().getColor(R.color.orange));
            todayButton.setBackground(dayButtonBackground);
        }
    }

    private void rellenarDesplegableHoraReserva() {
        String [] bookingHours = {"Seleccione hora","13:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, bookingHours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingHourDesplegable.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }

}
