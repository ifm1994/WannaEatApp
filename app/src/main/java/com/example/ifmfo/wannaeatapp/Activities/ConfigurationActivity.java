package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

public class ConfigurationActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    Button accountInfoButton, changePasswordButton, removeAcountButton, temsButton, privacityButton;
    LinearLayout userConfigurationContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        initUI();
        setupToolbar();
        bindEventts();

    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        accountInfoButton = findViewById(R.id.accountInfoButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        removeAcountButton = findViewById(R.id.removeAcountButton);
        temsButton = findViewById(R.id.temsButton);
        privacityButton = findViewById(R.id.privacityButton);
        userConfigurationContainer = findViewById(R.id.user_configuration_container);

        if(globalResources.getUserLogged() != null){
            userConfigurationContainer.setVisibility(View.VISIBLE);
        }else{
            userConfigurationContainer.setVisibility(View.GONE );
        }

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Configuración");
    }

    private void bindEventts() {
        accountInfoButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), AccountInfoActivity.class),1);
        });
        changePasswordButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), AccountChangePasswordActivity.class),1);
        });
        removeAcountButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), DeleteAccountActivity.class),1);
        });
        temsButton.setOnClickListener(v -> {

        });
        privacityButton.setOnClickListener(v -> {

        });
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
