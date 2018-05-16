package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WriteOpinionActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView sendOpinionButton;
    ImageView star1, star2, star3, star4, star5;
    Boolean valued;
    int inputRating;
    TextView opinionName;
    TextView opinionRateLabel;
    TextView opinionNameLabel;
    TextView opinionCommentary;
    TextView opinionCommentaryLabel;
    RelativeLayout mainLayout;
    static final GlobalResources globalResources = GlobalResources.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_opinion);

        initUI();
        setupToolbar();
        bindEvents();

    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        valued = false;
        sendOpinionButton = findViewById(R.id.sendOpinionButton);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        opinionCommentary = findViewById(R.id.opinion_commentary);
        opinionCommentaryLabel = findViewById(R.id.opinion_commentary_label);
        opinionName = findViewById(R.id.opinion_name);
        opinionNameLabel = findViewById(R.id.opinion_name_label);
        opinionRateLabel = findViewById(R.id.opinion_rate_label);
        mainLayout = findViewById(R.id.main_layout);

        opinionName.setText(globalResources.getUserLogged().getName());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Escribir reseña");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEvents() {
        star1.setOnClickListener(v -> {
            drawStars(1);
            opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
        });
        star2.setOnClickListener(v -> {
            drawStars(2);
            opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
        });
        star3.setOnClickListener(v -> {
            drawStars(3);
            opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
        });
        star4.setOnClickListener(v -> {
            drawStars(4);
            opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
        });
        star5.setOnClickListener(v -> {
            drawStars(5);
            opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
        });

        opinionName.setOnTouchListener((arg0, arg1) -> {
            opinionNameLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });

        opinionCommentary.setOnTouchListener((arg0, arg1) -> {
            opinionCommentaryLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });

        sendOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateAllInputs()){
                    Snackbar.make(mainLayout ,"Debe rellenar correctamente todos los datos",Snackbar.LENGTH_SHORT).show();
                }else{
                    opinionNameLabel.setTextColor(getResources().getColor(R.color.black));
                    opinionCommentaryLabel.setTextColor(getResources().getColor(R.color.black));
                    opinionRateLabel.setTextColor(getResources().getColor(R.color.black));
                    updateBooking();
                }
            }
        });
    }

    public void registerOpinion(){
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/opinions";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPeticion,
                response -> {
                    Snackbar.make(mainLayout ,"Reseña registrada",Snackbar.LENGTH_SHORT).show();
                    finishWriteOpinionActivity();
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error al registrar su reseña",Snackbar.LENGTH_SHORT).show();
                }){

            @SuppressLint("SetTextI18n")
            @Override
            protected Map<String,String> getParams() {
                Map<String,String>params = new HashMap<>();

                params.put("name", "" + opinionName.getText());
                params.put("rating", "" + inputRating);
                params.put("description", "" + opinionCommentary.getText());
                params.put("id_user", "" + globalResources.getUserLogged().getId());
                params.put("id_restaurant", "" + globalResources.getLast_single_booking_visited().getId_restaurant());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updateBooking(){
        globalResources.updateStateKeepLogged(globalResources.getLast_single_booking_visited().getId());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/bookings/canrate/" + globalResources.getLast_single_booking_visited().getId() + "/false";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, urlPeticion,
                response -> {
                    registerOpinion();
                    globalResources.getLast_single_booking_visited().setCanrate(false);
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error al actualizar la reserva",Snackbar.LENGTH_SHORT).show();
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void finishWriteOpinionActivity(){
        startActivityForResult(new Intent(getApplicationContext(), HomeActivity.class),1);
    }

    public Boolean validateAllInputs(){
        return validateInputName() && validateIfValued() && validateInputCommentary();
    }

    private Boolean validateIfValued(){
        if(valued){
            return true;
        }else{
            opinionRateLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputName() {
        if(!opinionName.getText().toString().isEmpty()){
            return true;
        }else{
            opinionNameLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }

    }

    private boolean validateInputCommentary() {
        if(!opinionCommentary.getText().toString().isEmpty()){
            return true;
        }else{
            opinionCommentaryLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private void drawStars(int stars) {
        valued = true;
        switch (stars){
            case 1:
                star1.setImageResource(R.drawable.ic_action_star_filled);
                star2.setImageResource(R.drawable.ic_action_star_empty);
                star3.setImageResource(R.drawable.ic_action_star_empty);
                star4.setImageResource(R.drawable.ic_action_star_empty);
                star5.setImageResource(R.drawable.ic_action_star_empty);
                inputRating = 1;
                break;
            case 2:
                star1.setImageResource(R.drawable.ic_action_star_filled);
                star2.setImageResource(R.drawable.ic_action_star_filled);
                star3.setImageResource(R.drawable.ic_action_star_empty);
                star4.setImageResource(R.drawable.ic_action_star_empty);
                star5.setImageResource(R.drawable.ic_action_star_empty);
                inputRating = 2;
                break;
            case 3:
                star1.setImageResource(R.drawable.ic_action_star_filled);
                star2.setImageResource(R.drawable.ic_action_star_filled);
                star3.setImageResource(R.drawable.ic_action_star_filled);
                star4.setImageResource(R.drawable.ic_action_star_empty);
                star5.setImageResource(R.drawable.ic_action_star_empty);
                inputRating = 3;
                break;
            case 4:
                star1.setImageResource(R.drawable.ic_action_star_filled);
                star2.setImageResource(R.drawable.ic_action_star_filled);
                star3.setImageResource(R.drawable.ic_action_star_filled);
                star4.setImageResource(R.drawable.ic_action_star_filled);
                star5.setImageResource(R.drawable.ic_action_star_empty);
                inputRating = 4;
                break;
            case 5:
                star1.setImageResource(R.drawable.ic_action_star_filled);
                star2.setImageResource(R.drawable.ic_action_star_filled);
                star3.setImageResource(R.drawable.ic_action_star_filled);
                star4.setImageResource(R.drawable.ic_action_star_filled);
                star5.setImageResource(R.drawable.ic_action_star_filled);
                inputRating = 5;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
