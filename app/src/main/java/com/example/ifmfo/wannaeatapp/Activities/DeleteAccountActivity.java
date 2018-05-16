package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

public class DeleteAccountActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    Button acceptButton;
    Button cancelButton;
    TextView userNameLoggedLabel;
    TextView userEmailLoggedLabel;
    CardView deleteButton;
    LinearLayout containerOne;
    LinearLayout containerTwo;
    EditText password;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        initUI();
        setupToolbar();
        bindEventts();
    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        deleteButton = findViewById(R.id.deleteAccountButton);
        cancelButton = findViewById(R.id.cancelButton);
        acceptButton = findViewById(R.id.acceptButton);
        userNameLoggedLabel = globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_name_logged);
        userEmailLoggedLabel= globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_email_logged);
        containerOne = findViewById(R.id.container_1);
        containerTwo = findViewById(R.id.container_2);
        password = findViewById(R.id.password);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Eliminar cuenta");
    }

    private void bindEventts() {

        acceptButton.setOnClickListener(v -> {
            containerOne.setVisibility(View.GONE);
            containerTwo.setVisibility(View.VISIBLE);
        });

        cancelButton.setOnClickListener(v -> {
            finish();
        });

        deleteButton.setOnClickListener(v -> {
            confirmPassword();
        });
    }
    private void confirmPassword(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/" + globalResources.getUserLogged().getEmail() + "/" + password.getText();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    deleteAccount();
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Contraseña incorrecta",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteAccount() {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/" + globalResources.getUserLogged().getId();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlPeticion,
                response -> {
                    Snackbar.make(mainLayout ,"Cuenta eliminada",Snackbar.LENGTH_SHORT).show();
                    finishDeleteAccountActivity();
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error a la hora de eliminar la cuenta",Snackbar.LENGTH_SHORT).show();
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void finishDeleteAccountActivity() {
        globalResources.getUserLogged().getUserCoupons().clear();
        globalResources.getUserLogged().getUserBookings().clear();
        globalResources.user_logOut();
        globalResources.getNavigationView().getMenu().findItem(R.id.log_in_out_button).setTitle("Iniciar sesión");
        userNameLoggedLabel.setText("");
        userEmailLoggedLabel.setText("");
        startActivityForResult(new Intent(getApplicationContext(), HomeActivity.class),1);
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
