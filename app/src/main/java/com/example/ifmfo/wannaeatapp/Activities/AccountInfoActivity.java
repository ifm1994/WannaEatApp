package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.regex.Pattern;

public class AccountInfoActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    EditText name, email, phone;
    TextView nameLabel, emailLabel, phoneLabel;
    CardView updateAccountInfoButton;
    TextView userNameLoggedLabel;
    TextView userEmailLoggedLabel;
    LinearLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        initUI();
        setupToolbar();
        bindEventts();
    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        name = findViewById(R.id.complete_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        nameLabel = findViewById(R.id.complete_name_label);
        emailLabel = findViewById(R.id.email_label);
        phoneLabel = findViewById(R.id.phone_label);
        updateAccountInfoButton = findViewById(R.id.updateAccountInfoButton);
        userNameLoggedLabel = globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_name_logged);
        userEmailLoggedLabel= globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_email_logged);
        mainLayout = findViewById(R.id.main_layout);

        email.setText(globalResources.getUserLogged().getEmail());
        name.setText(globalResources.getUserLogged().getName());
        phone.setText(globalResources.getUserLogged().getPhone());

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Información de la cuenta");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEventts() {
        updateAccountInfoButton.setOnClickListener(v -> {
            if(!validateAllInputs()){
                Snackbar.make(mainLayout ,"Debe rellenar correctamente todos los datos",Snackbar.LENGTH_SHORT).show();
            }else{
                nameLabel.setTextColor(getResources().getColor(R.color.black));
                emailLabel.setTextColor(getResources().getColor(R.color.black));
                phoneLabel.setTextColor(getResources().getColor(R.color.black));
                updateUser();
            }
        });

        name.setOnTouchListener((arg0, arg1) -> {
            nameLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        phone.setOnTouchListener((arg0, arg1) -> {
            phoneLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        email.setOnTouchListener((arg0, arg1) -> {
            emailLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }

    private void updateUser() {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/updateinfo/" + globalResources.getUserLogged().getId();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, urlPeticion,
                response -> {
                    Snackbar.make(mainLayout ,"Información actualizada",Snackbar.LENGTH_SHORT).show();
                    globalResources.getUserLogged().setName(name.getText().toString());
                    globalResources.getUserLogged().setEmail(email.getText().toString());
                    globalResources.getUserLogged().setPhone(phone.getText().toString());
                    userNameLoggedLabel.setText(globalResources.getUserLogged().getName());
                    userEmailLoggedLabel.setText(globalResources.getUserLogged().getEmail());

                    finishUpdateAccountInfoActivity();
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error al actualizar la información de la cuenta",Snackbar.LENGTH_SHORT).show();
                }){

            @SuppressLint("SetTextI18n")
            @Override
            protected Map<String,String> getParams() {
                Map<String,String>params = new HashMap<>();

                params.put("name", "" + name.getText());
                params.put("email", "" + email.getText());
                params.put("phone", "" + phone.getText());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void finishUpdateAccountInfoActivity() {
        finish();
    }

    private boolean validateAllInputs() {
        return validateInputName() && validateInputPhoneNumber() && validateInputMail();
    }

    private boolean validateInputName() {
        if(!name.getText().toString().isEmpty()){
            return true;
        }else{
            nameLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputMail() {
        if(regularExpressionValidator("^[a-zA-Z0-9\\._-]+@[a-zA-Z0-9-]{2,}[.][a-zA-Z]{2,4}$", email.getText().toString())){
            return true;
        }else{
            emailLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateInputPhoneNumber() {
        if(phone.getText().toString().length() == 9){
            return true;
        }else{
            phoneLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    public Boolean regularExpressionValidator(String stringPatron, String textToValidate){
        Pattern patron = Pattern.compile(stringPatron);
        String fecha = textToValidate.trim();
        return patron.matcher(fecha).matches();
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
