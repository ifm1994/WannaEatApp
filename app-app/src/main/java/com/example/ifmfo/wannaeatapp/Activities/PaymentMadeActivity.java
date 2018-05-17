package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

public class PaymentMadeActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    TextView bookingTimeLabel;
    TextView bookingTime;
    TextView userNameLoggedLabel;
    TextView userEmailLoggedLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_made);

        initUI();
        setUpToolbar();
        bindEvents();
    }


    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navview2);
        bookingTimeLabel = findViewById(R.id.booking_time_label);
        bookingTime = findViewById(R.id.booking_time);
        navView.setItemIconTintList(null);

        userNameLoggedLabel = navView.getHeaderView(0).findViewById(R.id.user_name_logged);
        userEmailLoggedLabel= navView.getHeaderView(0).findViewById(R.id.user_email_logged);

        if(globalResources.getBookingForToday()){
            bookingTimeLabel.setText("Su reserva estará preparada hoy a las:");
        }else{
            bookingTimeLabel.setText("Su reserva estará preparada mañana a las:");
        }
        bookingTime.setText(globalResources.getReservationTime());
        navView.getMenu().findItem(R.id.log_in_out_button).setTitle("Cerrar sesión");
        userNameLoggedLabel.setText(globalResources.getUserLogged().getName());
        userEmailLoggedLabel.setText(globalResources.getUserLogged().getEmail());
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_orange_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindEvents() {
        navView.setNavigationItemSelectedListener(menuItem -> {
            Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            Intent goSearchRestaurant = new Intent(getApplicationContext(), HomeActivity.class);

            switch (menuItem.getItemId()) {

                case R.id.restaurants_section_button:
                    startActivityForResult(goSearchRestaurant, 1);
                    break;

                case R.id.bookings_section_button:
                    startActivityForResult(new Intent(PaymentMadeActivity.this, MyBookingsActivity.class), 1);
                    break;

                case R.id.coupons_section_button:
                    startActivityForResult(new Intent(PaymentMadeActivity.this, MyCouponsActicity.class), 1);
                    break;

                case R.id.configuration_button:
                    startActivityForResult(new Intent(PaymentMadeActivity.this, ConfigurationActivity.class), 1);
                    break;

                case R.id.log_in_out_button:
                    if(globalResources.getUser_isLogged()){
                        globalResources.getUserLogged().getUserCoupons().clear();
                        globalResources.getUserLogged().getUserBookings().clear();
                        globalResources.user_logOut();
                        TextView userNameLoggedLabel = navView.getHeaderView(0).findViewById(R.id.user_name_logged);
                        TextView userEmailLoggedLabel= navView.getHeaderView(0).findViewById(R.id.user_email_logged);

                        navView.getMenu().findItem(R.id.log_in_out_button).setTitle("Iniciar sesión");
                        Snackbar.make( drawerLayout ,"Hasta pronto " + userNameLoggedLabel.getText().toString() + "!",Snackbar.LENGTH_LONG).show();
                        userNameLoggedLabel.setText("");
                        userEmailLoggedLabel.setText("");
                    }else{
                        startActivityForResult(goLoginActivity, 1);
                    }
                    break;
            }
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();

            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
