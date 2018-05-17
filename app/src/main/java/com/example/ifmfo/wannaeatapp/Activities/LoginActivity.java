package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText loginEmailInput;
    private EditText loginPasswordInput;
    private CardView loginButton;
    private TextView registerLink;
    private TextView loginLayout;
    private String comingFrom;
    private static final GlobalResources globalResources = GlobalResources.getInstance();
    private static Context context;
    private RelativeLayout mainLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setupToolbar();
        bindEvents();

        if(getIntent().getExtras() != null){
            comingFrom = getIntent().getExtras().getString("NavigationCode");
        }
        context = getApplicationContext();
    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        loginLayout = findViewById(R.id.login_label);
        mainLayout = findViewById(R.id.main_layout);
        mAuth = FirebaseAuth.getInstance();
        comingFrom = "";
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("Iniciar sesión");
    }

    private void bindEvents() {
        loginButton.setOnClickListener(arg0 -> {
            if(!validateAllInputs()){
                //Hacer peticion al servidor para ver si existe dicho usuario
                Snackbar.make(mainLayout ,"Debe rellenar su correo y su contraseña correctamente",Snackbar.LENGTH_SHORT).show();
            }else{
                //Ahora verifico que existe dicho usuario
                verificarUsuario();
            }
        });

        registerLink.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), RegisterUserActivity.class),1);
        });
    }

    private Boolean validateAllInputs(){
        return validateEmailInput() && validatePasswordInput();
    }

    private boolean validateEmailInput() {
        if(regularExpressionValidator("^[a-zA-Z0-9\\._-]+@[a-zA-Z0-9-]{2,}[.][a-zA-Z]{2,4}$", loginEmailInput.getText().toString())){
            return true;
        }else{
            loginEmailInput.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validatePasswordInput() {
        if(!loginPasswordInput.getText().toString().isEmpty()){
            return true;
        }else{
            loginPasswordInput.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    public Boolean regularExpressionValidator(String stringPatron, String textToValidate){
        Pattern patron = Pattern.compile(stringPatron);
        String fecha = textToValidate.trim();
        return patron.matcher(fecha).matches();
    }

    private void verificarUsuario(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/" + loginEmailInput.getText().toString() + "/" + loginPasswordInput.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try {
                        int id = response.getInt("id");
                        String name = response.getString("name");
                        String email = response.getString("email");
                        String phone = response.getString("phone");
                        User user = new User(name, email, phone);
                        user.setId(id);

                        globalResources.user_logIn(user);
                        LoginFirebaseUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make( loginLayout,"Email o contraseña incorrectas",Snackbar.LENGTH_LONG).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void LoginFirebaseUser() {
        mAuth.signInWithEmailAndPassword(loginEmailInput.getText().toString(), loginPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(comingFrom.equals("sinceHome")){
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }else if(comingFrom.equals("sinceBasket")){
                                Intent intent = new Intent(getApplicationContext(), FullShoppingBasketActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make( loginLayout,"Algo fue mal iniciando con firebase",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
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

    public static Context getContext() {
        return context;
    }
}


























