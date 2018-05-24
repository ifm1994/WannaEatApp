package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.Booking;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class PaymentBookingActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    Spinner paymentMethodSpinner;
    EditText clientName, clientNumber, clientMail, cardName, cardNumber, cardCaducity, cardCVV;
    TextView totalPrice, clientNameLabel, clientNumberLabel, clientMailLabel, cardNameLabel, cardNumberLabel, cardCaducityLabel, cardCVVLabel;
    CardView continueButton;
    LinearLayout visaPayMethodContainer;
    RelativeLayout paypalPayMethodContainer, mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_booking);
        initUI();
        setUpToolbar();
        bindEvents();
        rellenarDesplegableMetodoDePago();
    }

    private void rellenarDesplegableMetodoDePago() {
        String [] paymentMethods = {"Tarjeta bancaria", "PayPal"};
        ArrayAdapter<String> adapterHours = new ArrayAdapter<>(this, R.layout.spinner_item, paymentMethods);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapterHours);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        paymentMethodSpinner = findViewById(R.id.payment_method_spinner);
        totalPrice = findViewById(R.id.booking_final_price);
        clientName = findViewById(R.id.booking_complete_name);
        clientNumber = findViewById(R.id.booking_contact_phone);
        clientMail = findViewById(R.id.booking_mail);
        cardName = findViewById(R.id.booking_card_name);
        cardNumber = findViewById(R.id.booking_card_number);
        cardCaducity = findViewById(R.id.booking_card_caducity);
        cardCVV = findViewById(R.id.booking_card_cvv);
        continueButton = findViewById(R.id.continueBookingButon);
        clientNameLabel = findViewById(R.id.booking_complete_name_label);
        clientNumberLabel = findViewById(R.id.booking_contact_phone_label);
        cardNameLabel = findViewById(R.id.booking_card_name_label);
        cardNumberLabel = findViewById(R.id.booking_card_number_label);
        cardCaducityLabel = findViewById(R.id.booking_card_caducity_label);
        cardCVVLabel = findViewById(R.id.booking_card_cvv_label);
        clientMailLabel = findViewById(R.id.booking_mail_label);
        visaPayMethodContainer = findViewById(R.id.visa_method_container);
        paypalPayMethodContainer = findViewById(R.id.paypal_method_container);
        mainLayout = findViewById(R.id.main_layout);

        if(globalResources.getCouponApplied() != null){
            totalPrice.setText(String.format("%.2f",globalResources.getShopping_basket_totalPrice_with_discount()) + "€");
        }else{
            totalPrice.setText(String.format("%.2f",globalResources.shopping_basket_getTotalPrice()) + "€");
        }

        clientName.setText(globalResources.getUserLogged().getName());
        clientNumber.setText(globalResources.getUserLogged().getPhone());
        clientMail.setText(globalResources.getUserLogged().getEmail());
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(ic_action_orange_back);
            getSupportActionBar().setTitle("Pedido");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEvents() {
        continueButton.setOnClickListener(arg0 -> {
            if(!validateAllInputs() && paymentMethodSpinner.getSelectedItem().toString().equals("Tarjeta bancaria")){
                Snackbar.make(mainLayout ,"Debe rellenar correctamente todos los datos",Snackbar.LENGTH_SHORT).show();
            }else{
                clientNameLabel.setTextColor(getResources().getColor(R.color.black));
                clientNumberLabel.setTextColor(getResources().getColor(R.color.black));
                clientMailLabel.setTextColor(getResources().getColor(R.color.black));
                cardNameLabel.setTextColor(getResources().getColor(R.color.black));
                cardNumberLabel.setTextColor(getResources().getColor(R.color.black));
                cardCaducityLabel.setTextColor(getResources().getColor(R.color.black));
                cardCVVLabel.setTextColor(getResources().getColor(R.color.black));
                realizarPago();
            }
        });

        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(id == 0){
                    visaPayMethodContainer.setVisibility(View.VISIBLE);
                    paypalPayMethodContainer.setVisibility(View.GONE);
                }else if(id == 1){
                    visaPayMethodContainer.setVisibility(View.GONE);
                    paypalPayMethodContainer.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        clientName.setOnTouchListener((arg0, arg1) -> {
            clientNameLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        clientNumber.setOnTouchListener((arg0, arg1) -> {
            clientNumberLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        clientMail.setOnTouchListener((arg0, arg1) -> {
            clientMailLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        cardName.setOnTouchListener((arg0, arg1) -> {
            cardNameLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        cardNumber.setOnTouchListener((arg0, arg1) -> {
            cardNumberLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        cardCaducity.setOnTouchListener((arg0, arg1) -> {
            cardCaducityLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        cardCVV.setOnTouchListener((arg0, arg1) -> {
            cardCVVLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });

    }

    private void realizarPago() {
        //todo: hacer pasarela de pago
        if(paymentMethodSpinner.getSelectedItem().toString().equals("Tarjeta bancaria")){
            //Pagar mediante tarjeta Tarjeta bancaria

        }else if(paymentMethodSpinner.getSelectedItem().toString().equals("PayPal")){
            //Pagar mediante paypal
        }

        //Esto se ejecuta si el pago se ha realizado correctamente
        registrarReserva(createFinalBooking());
        removeCouponIfUsed();
    }

    private void removeCouponIfUsed() {
        if(globalResources.getCouponApplied() != null){
            String urlPeticion = "https://wannaeatservice.herokuapp.com/api/coupons/" + globalResources.getCouponApplied().getId();
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlPeticion,
                    response -> {
                        Snackbar.make(mainLayout ,"Cupón utilizado",Snackbar.LENGTH_SHORT).show();
                        globalResources.getUserLogged().removeCoupon(globalResources.getCouponApplied());
                        globalResources.setCouponApplied(null);
                        finishBookingPayment();
                    },
                    error -> {
                        Snackbar.make(mainLayout ,"Error al intentar aplicar el cupón. Disculpe las molestas",Snackbar.LENGTH_SHORT).show();
                        finishBookingPayment();
                    }){
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private Booking createFinalBooking() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String exactlyTimeOfBooking;
        Double totalPrice;
        int id_user = globalResources.getUserLogged().getId();

        String AllProductsInString = globalResources.generateProductsToString();

        if(globalResources.getBookingForToday()){
            exactlyTimeOfBooking = dateFormat.format(new Date()) + "-" + globalResources.getReservationTime();
        }else{
            exactlyTimeOfBooking = dateFormat.format(new Date(new Date().getTime() + 86400000)) + "-" + globalResources.getReservationTime();
        }
        String transactionId = generateTransactionId(exactlyTimeOfBooking);

        if(globalResources.getCouponApplied() != null){
            totalPrice = globalResources.getShopping_basket_totalPrice_with_discount();
        }else{
            totalPrice = globalResources.shopping_basket_getTotalPrice();
        }

        Booking booking = new Booking(
                globalResources.getCurrentRestaurantIdOfBooking(),
                id_user,
                exactlyTimeOfBooking,
                totalPrice.toString(),
                transactionId,
                AllProductsInString,
                paymentMethodSpinner.getSelectedItem().toString(),
                clientName.getText().toString().replace(" ","_"),
                clientNumber.getText().toString(),
                clientMail.getText().toString(),
                globalResources.getNumber_of_commensals(),
                globalResources.getClient_commentary(),
                true,
                0);

        globalResources.getUserLogged().addNewBooking(booking);
        return booking;
    }

    private void registrarReserva(Booking finalBooking) {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/bookings";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPeticion,
            response -> {
                Snackbar.make(mainLayout ,"Reserva realizada",Snackbar.LENGTH_SHORT).show();
                finishBookingPayment();
            },
            error -> {
                Snackbar.make(mainLayout ,"Error al registrar la reserva",Snackbar.LENGTH_SHORT).show();
                finishBookingPayment();
            }){

            @SuppressLint("SetTextI18n")
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String>params = new HashMap<String, String>();

                params.put("id_restaurant", "" + finalBooking.getId_restaurant());
                params.put("id_user", "" + finalBooking.getId_user());
                params.put("time", "" + finalBooking.getTime());
                params.put("price", "" + finalBooking.getPrice());
                params.put("id_transaction", "" + finalBooking.getId_transaction());
                params.put("products_and_amount", "" + finalBooking.getProducts_and_amount());
                params.put("payment_method", "" + finalBooking.getPayment_method());
                params.put("client_name", "" + finalBooking.getClient_name());
                params.put("client_phone", "" + finalBooking.getClient_phone());
                params.put("client_email", "" + finalBooking.getClient_email());
                params.put("number_of_commensals", "" + finalBooking.getNumber_of_commensals());
                params.put("client_commentary", "" + finalBooking.getClient_commentary());
                params.put("canrate", "true");
                params.put("status", "0");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void finishBookingPayment(){
        globalResources.shopping_basket_emptyShoppingCart();
        Intent intent = new Intent(getApplicationContext(), PaymentMadeActivity.class);
        startActivityForResult(intent, 1);
    }

    private String generateTransactionId(String time) {
        time = time.replace(" ", "");
        time = time.replace("-", "");
        time = time.replace(":", "");

        return "r" + globalResources.getCurrentRestaurantIdOfBooking() + time + "p" + globalResources.shopping_basket_getNumberOfProductsAdded();
    }

    private boolean validateAllInputs() {
        return validateInputName() && validateInputPhoneNumber() && validateInputMail() && validateCardName() &&validateCardNumber() && validateCardCaducity() && validateCardCVV();
    }

    private boolean validateInputName() {
        if(clientName.getText().toString().length() >= 3 && !clientName.getText().toString().equals(" ")){
            return true;
        }else{
            clientNameLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputMail() {
        if(regularExpressionValidator("^[a-zA-Z0-9\\._-]+@[a-zA-Z0-9-]{2,}[.][a-zA-Z]{2,4}$", clientMail.getText().toString())){
            return true;
        }else{
            clientMailLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputPhoneNumber() {
        if(clientNumber.getText().toString().length() == 9){
            return true;
        }else{
            clientNumberLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateCardCVV() {
        if(cardCVV.getText().toString().length() == 3){
            return true;
        }else{
            cardCVVLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateCardCaducity() {
        if(regularExpressionValidator("^([012][1-9]|3[01])(/)(0[1-9]|1[012])$", cardCaducity.getText().toString())){
            return true;
        }else{
            cardCaducityLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateCardName() {
        if(cardName.getText().toString().length() >= 3 && !clientName.getText().toString().equals(" ")){
            return true;
        }else{
            cardNameLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateCardNumber() {
        if(cardNumber.getText().toString().length() == 16){
            return true;
        }else{
            cardNumberLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
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

    public Boolean regularExpressionValidator(String stringPatron, String textToValidate){
        Pattern patron = Pattern.compile(stringPatron);
        String fecha = textToValidate.trim();
        return patron.matcher(fecha).matches();
    }
}
