package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AccountChangePasswordActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    EditText oldPassword, newPassword, newPasswordTwo;
    TextView oldPasswordLabel, newPasswordLabel, newPasswordTwoLabel;
    CardView updateAccountPasswordButton;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change_password);

        initUI();
        setupToolbar();
        bindEventts();
    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        newPasswordTwo = findViewById(R.id.new_password_two);
        oldPasswordLabel = findViewById(R.id.old_password_label);
        newPasswordLabel = findViewById(R.id.new_password_label);
        newPasswordTwoLabel = findViewById(R.id.new_password_two_label);
        updateAccountPasswordButton = findViewById(R.id.updatePasswordButton);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Cambiar contraseña");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEventts() {
        updateAccountPasswordButton.setOnClickListener(v -> {
            if(!validateAllInputs()){
                Snackbar.make(mainLayout ,"Debe rellenar correctamente todos los datos",Snackbar.LENGTH_SHORT).show();
            }else{
                oldPasswordLabel.setTextColor(getResources().getColor(R.color.black));
                newPasswordLabel.setTextColor(getResources().getColor(R.color.black));
                newPasswordTwoLabel.setTextColor(getResources().getColor(R.color.black));
                updatePassword();
            }
        });

        oldPassword.setOnTouchListener((arg0, arg1) -> {
            oldPasswordLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        newPassword.setOnTouchListener((arg0, arg1) -> {
            newPasswordLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
        newPasswordTwo.setOnTouchListener((arg0, arg1) -> {
            newPasswordTwoLabel.setTextColor(getResources().getColor(R.color.black));
            return false;
        });
    }

    private void updatePassword() {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/updatepassword/"+ globalResources.getUserLogged().getId() +"/"+ oldPassword.getText() +"/" + newPassword.getText();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, urlPeticion,
                response -> {
                    changeUserPasswordFirebase();
                },
                error -> {
                    Snackbar.make(mainLayout ,"Error al actualizar la contraseña",Snackbar.LENGTH_SHORT).show();
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void changeUserPasswordFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(mainLayout ,"Contraseña actualizada",Snackbar.LENGTH_SHORT).show();
                                finishChangePasswordActivity();
                            }
                        }
                    });
        }
    }

    private void finishChangePasswordActivity() {
        finish();
    }

    private boolean validateAllInputs() {
        return validateOldPassword() && validateNewPassword() && validateNewPasswordTwo() && validateThatAreTheSameNewPassword();
    }

    private boolean validateThatAreTheSameNewPassword() {
        if(newPassword.getText().toString().equals(newPasswordTwo.getText().toString())){
            return true;
        }else{
            Snackbar.make(mainLayout ,"Las contraseñas deben coincidir",Snackbar.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean validateOldPassword() {
        if(!oldPassword.getText().toString().isEmpty()){
            return true;
        }else{
            oldPasswordLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateNewPasswordTwo() {
        if(!newPasswordTwo.getText().toString().isEmpty()){
            return true;
        }else{
            newPasswordTwoLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    private boolean validateNewPassword() {
        if(!newPassword.getText().toString().isEmpty() && newPassword.getText().toString().length() >= 6){
            return true;
        }else{
            newPasswordLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
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
