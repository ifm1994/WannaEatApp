package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {

    CardView registerButton;
    EditText password, name, phone, email;
    Toolbar toolbar;
    static final GlobalResources globalResources = GlobalResources.getInstance();
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        initUI();
        setupToolbar();
        bindEvents();
    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        registerButton = findViewById(R.id.registerButton);
        name = findViewById(R.id.register_name);
        phone = findViewById(R.id.register_phone);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("Registrarse");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEvents() {
        registerButton.setOnClickListener(v -> {
            if(!validateAllInputs()){
                Snackbar.make(mainLayout ,"Debe rellenar correctamente todos los datos",Snackbar.LENGTH_SHORT).show();
            }else{
                name.setTextColor(getResources().getColor(R.color.white));
                phone.setTextColor(getResources().getColor(R.color.white));
                email.setTextColor(getResources().getColor(R.color.white));
                password.setTextColor(getResources().getColor(R.color.white));
                registerUser();
            }
        });

        name.setOnTouchListener((arg0, arg1) -> {
            name.setTextColor(getResources().getColor(R.color.white));
            return false;
        });
        phone.setOnTouchListener((arg0, arg1) -> {
            phone.setTextColor(getResources().getColor(R.color.white));
            return false;
        });
        email.setOnTouchListener((arg0, arg1) -> {
            email.setTextColor(getResources().getColor(R.color.white));
            return false;
        });
        password.setOnTouchListener((arg0, arg1) -> {
            password.setTextColor(getResources().getColor(R.color.white));
            return false;
        });
    }

    private boolean validateAllInputs() {
        return validateInputName() && validateInputPhoneNumber() && validateInputMail() && validatePasswordInput();
    }

    private boolean validateInputName() {
        if(name.getText().toString().length() >= 3 && !name.getText().toString().equals(" ")){
            return true;
        }else{
            name.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validatePasswordInput() {
        if(!password.getText().toString().isEmpty()){
            return true;
        }else{
            password.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputMail() {
        if(regularExpressionValidator("^[a-zA-Z0-9\\._-]+@[a-zA-Z0-9-]{2,}[.][a-zA-Z]{2,4}$", email.getText().toString())){
            return true;
        }else{
            email.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputPhoneNumber() {
        if(phone.getText().toString().length() == 9){
            return true;
        }else{
            phone.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    public Boolean regularExpressionValidator(String stringPatron, String textToValidate){
        Pattern patron = Pattern.compile(stringPatron);
        String fecha = textToValidate.trim();
        return patron.matcher(fecha).matches();
    }

    private void registerUser() {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPeticion,
                response -> {
                    User user = new User(name.getText().toString(),email.getText().toString(),phone.getText().toString());
                    globalResources.user_logIn(user);

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error a la hora de registrar el usuario",Snackbar.LENGTH_SHORT).show();
                }){

            @SuppressLint("SetTextI18n")
            @Override
            protected Map<String,String> getParams() {
                Map<String,String>params = new HashMap<>();

                params.put("name", "" + name.getText());
                params.put("phone", "" + phone.getText());
                params.put("email", "" + email.getText());
                params.put("password", "" + password.getText());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
