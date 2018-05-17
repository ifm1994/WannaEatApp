package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.CommensalPerHour;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class BookingDetailsActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Spinner bookingHoursDesplegable;
    Spinner bookingMinutesDesplegable;
    ImageButton addCommensal;
    ImageButton removeCommensal;
    private int amountOfCommensals;
    Boolean bookingForToday;
    RelativeLayout todayButton;
    RelativeLayout otherDayButton;
    CardView continueBooking;
    Toolbar toolbar;
    TextView numberOfCommensals;
    EditText userComment;
    RelativeLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        initUI();
        setupToolbar();

        obtenerHorasDisponiblesDelRestaurante();
        actualizarColorDeBotonesDelDia();
        bindEvents();
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        bookingHoursDesplegable = findViewById(R.id.booking_hours_spinner);
        bookingMinutesDesplegable = findViewById(R.id.booking_minutes_spinner);
        addCommensal = findViewById(R.id.addCommensal);
        removeCommensal = findViewById(R.id.removeCommensal);
        numberOfCommensals = findViewById(R.id.booking_commensal_number);
        amountOfCommensals = 0;
        todayButton = findViewById(R.id.day_switch_button_1);
        otherDayButton = findViewById(R.id.day_switch_button_2);
        bookingForToday = true;
        continueBooking = findViewById(R.id.continueBookingButon);
        numberOfCommensals.setText(Integer.toString(amountOfCommensals));
        userComment = findViewById(R.id.booking_user_comment);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ic_action_orange_back);
        getSupportActionBar().setTitle("Pedido");
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void bindEvents() {
        removeCommensal.setOnClickListener(v -> {
            if(amountOfCommensals > 0){
                amountOfCommensals--;
                numberOfCommensals.setText(Integer.toString(amountOfCommensals));
            }
            updateHoursDesplegable();
        });

        addCommensal.setOnClickListener(v -> {
            if (amountOfCommensals < 20){
                amountOfCommensals++;
                numberOfCommensals.setText(Integer.toString(amountOfCommensals));
            }
            updateHoursDesplegable();
        });

        todayButton.setOnClickListener(v -> {
            bookingForToday = true;
            actualizarColorDeBotonesDelDia();
            updateHoursDesplegable();
        });

        otherDayButton.setOnClickListener(v -> {
            bookingForToday = false;
            actualizarColorDeBotonesDelDia();
            updateHoursDesplegable();
        });

        continueBooking.setOnClickListener(arg0 -> {
            if(numberOfCommensals.getText().equals("0")){
                Snackbar.make(mainLayout ,"Número de comensales no válido",Snackbar.LENGTH_SHORT).show();
            }else if(bookingHoursDesplegable.getSelectedItem() == "HH" || bookingMinutesDesplegable.getSelectedItem() == "MM"){
                Snackbar.make(mainLayout ,"Debe elegir la hora deseada",Snackbar.LENGTH_SHORT).show();
            }else{
                globalResources.setBookingForToday(bookingForToday);
                globalResources.setNumber_of_commensals(amountOfCommensals);
                globalResources.setReservationTime(bookingHoursDesplegable.getSelectedItem().toString() + ":" + bookingMinutesDesplegable.getSelectedItem().toString());
                globalResources.setClient_commentary(userComment.getText().toString());
                startActivityForResult(new Intent(BookingDetailsActivity.this, PaymentBookingActivity.class),1);
            }
        });

        bookingHoursDesplegable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateMinutesDesplegable();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
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

    private void obtenerHorasDisponiblesDelRestaurante() {
        if(globalResources.getCapacitiesOfRestaurant().size() == 0 || globalResources.getCapacitiesOfRestaurant().get(0).getId_restaurant() != RestaurantActivity.getCurrentRestaurant().getId()){
            globalResources.emptyCapacitiesOfCurrentRestaurant();

            RequestQueue requestQueue = Volley.newRequestQueue(BookingDetailsActivity.this);
            String urlPeticion = "https://wannaeatservice.herokuapp.com/api/commensals_capacity_per_hour/restaurant/" + RestaurantActivity.getCurrentRestaurant().getId();
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    urlPeticion,
                    null,
                    response -> {
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject cphObject = response.getJSONObject(i);
                                // Get the current restaurant (json object) data
                                int id = cphObject.getInt("id");
                                int id_restaurant = cphObject.getInt("id_restaurant");
                                String hour = cphObject.getString("hour");
                                int commensal_capacity = cphObject.getInt("commensal_capacity");

                                CommensalPerHour commensalPerHour = new CommensalPerHour(id, id_restaurant, hour, commensal_capacity);
                                globalResources.addCapacityPerHour(commensalPerHour);
                            }
                            updateHoursDesplegable();
                            updateMinutesDesplegable();

                            globalResources.orderOpenHoursOfCapacities();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Do something when error occurred
                        Snackbar.make(mainLayout ,"Error en la petición de capacidades por hora",Snackbar.LENGTH_SHORT).show();

                    }
            );
            requestQueue.add(jsonObjectRequest);
        }else{
            updateHoursDesplegable();
            updateMinutesDesplegable();
        }
    }

    private void updateHoursDesplegable() {
        List<String> hours = new ArrayList<>();
        hours.add("HH");

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        for (CommensalPerHour commensalPerHour : globalResources.getCapacitiesOfRestaurant()){
            if(bookingForToday){
                if(commensalPerHour.getCommensal_capacity() >= amountOfCommensals && Integer.parseInt(commensalPerHour.getHour()) > currentHour){
                    hours.add(commensalPerHour.getHour());
                }
            }else{
                hours.add(commensalPerHour.getHour());
            }
        }
        String[] result = new String[hours.size()];
        rellenarDesplegableHoras(hours.toArray(result));
        bookingMinutesDesplegable.setSelection(0);
    }

    private void updateMinutesDesplegable(){
        String [] bookingMinutes = {"MM","00","05","10","15","20","25","30","35","40","45","50","55"};
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currenMinute = Calendar.getInstance().get(Calendar.MINUTE);

        if( bookingHoursDesplegable.getSelectedItem() != "HH" && Integer.parseInt(bookingHoursDesplegable.getSelectedItem().toString()) == currentHour + 1){
            List <String> result = new ArrayList<>();
            result.add("MM");
            for(int i=1; i<bookingMinutes.length; i++){
                if(Integer.parseInt(bookingMinutes[i]) > currenMinute){
                    result.add(bookingMinutes[i]);
                }
            }
            String[] finalResult = new String[result.size()];
            rellenarDesplegableMinutos(result.toArray(finalResult));
        }else{
            rellenarDesplegableMinutos(bookingMinutes);
        }
    }


    private void rellenarDesplegableHoras(String [] bookingHours) {
        ArrayAdapter<String> adapterHours = new ArrayAdapter<>(this, R.layout.spinner_item, bookingHours);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingHoursDesplegable.setAdapter(adapterHours);
    }

    private void rellenarDesplegableMinutos(String [] bookingMinutes){
        ArrayAdapter<String> adapterMinutes = new ArrayAdapter<>(this, R.layout.spinner_item, bookingMinutes);
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingMinutesDesplegable.setAdapter(adapterMinutes);
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
