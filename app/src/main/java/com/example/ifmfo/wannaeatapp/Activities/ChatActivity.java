package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.ChatMessageCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.AdminUser;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.ReceiveMessage;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.SendMessage;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class ChatActivity extends AppCompatActivity {

    private static final GlobalResources globalResources = GlobalResources.getInstance();
    private Toolbar toolbar;
    private TextView receiverUserName;
    private EditText messageInput;
    private Button sendMessage;
    private RecyclerView messagesContainer;
    private int id_admin;
    private Restaurant restaurantPassed;
    private ChatMessageCardAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver broadcastReceiver;
    private String comingFrom;
    private User receiverUser;
    private User senderUser;
    private String senderUserName;
    private LinearLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        comingFrom = getIntent().getStringExtra("comingFrom");

        if(comingFrom.equals("client")){
            restaurantPassed = (Restaurant) getIntent().getSerializableExtra("restaurant");
            obtainAdminUser();
        }else if(comingFrom.equals("admin")){
            receiverUser = (User) getIntent().getSerializableExtra("client");
        }
        initUI();
        setupToolbar();

        initDB();
        initMessageAdapter();

        bindEvents();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(ChatActivity.this,"El broadcast funciona",Toast.LENGTH_LONG).show();
            }
        };
    }

    private void initUI() {
        mainLayout = findViewById(R.id.main_layout);
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        receiverUserName = findViewById(R.id.restaurant_name);
        messageInput = findViewById(R.id.messageInput);
        sendMessage = findViewById(R.id.sendMessageButton);
        messagesContainer = findViewById(R.id.messages_container);
        senderUser = globalResources.getUserLogged();

        if(comingFrom.equals("client")){
            receiverUserName.setText(globalResources.getLast_single_booking_visited().getBooking_restaurant_name());
            senderUserName = globalResources.getUserLogged().getName();
            id_admin = restaurantPassed.getIdAdmin();
        }else if(comingFrom.equals("admin")){
            receiverUserName.setText(receiverUser.getName());
            senderUserName = getIntent().getStringExtra("sender_name");
            id_admin = globalResources.getUserLogged().getId();
        }

    }

    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(MENSAJE));
    }



    private void initDB() {
        database = FirebaseDatabase.getInstance();
        //Sala de chat (nombre donde se van a guardar todos nuestros mensjaes) .Constará de chat_idAdmin.idClient
        if(comingFrom.equals("client")){
            databaseReference = database.getReference("chat_" + id_admin + "-" + globalResources.getUserLogged().getId());
        }else if(comingFrom.equals("admin")){
            databaseReference = database.getReference("chat_" + id_admin + "-" + receiverUser.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ic_action_orange_back);
        getSupportActionBar().setTitle("Chat");
    }

    private void initMessageAdapter() {
        adapter = new ChatMessageCardAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesContainer.setLayoutManager(linearLayoutManager);
        messagesContainer.setAdapter(adapter);
    }

    private void bindEvents() {
        sendMessage.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            String token = FirebaseInstanceId.getInstance().getToken();
            if(!message.isEmpty()){
                databaseReference.push().setValue(new SendMessage(message, senderUserName, ServerValue.TIMESTAMP));
                if(receiverUser != null){
                    sendNotification();
                }
                messageInput.setText("");
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ReceiveMessage chatMessage = dataSnapshot.getValue(ReceiveMessage.class);
                adapter.addMessage(chatMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void obtainAdminUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/admin_users/" + restaurantPassed.getIdAdmin();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {

                    try{
                        // Get the current restaurant (json object) data
                        int id = response.getInt("id");
                        String email = response.getString("email");
                        String ftoken = response.getString("ftoken");
                        Boolean hasRestaurant = Boolean.parseBoolean(response.getString("hasRestaurant"));
                        AdminUser admin = new AdminUser(email, hasRestaurant);
                        admin.setId(id);
                        admin.setFirebaseToken(ftoken);

                        receiverUser = admin;

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Error a la hora de pedir los usuarios",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void sendNotification() {
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/notifications";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, urlPeticion,
                response -> {

                },
                error -> {

                }){

            @SuppressLint("SetTextI18n")
            @Override
            protected Map<String,String> getParams() {
                Map<String,String>params = new HashMap<>();

                params.put("receiver_token", "" + receiverUser.getFirebaseToken() );
                params.put("title", senderUserName + " te ha mandado un mensaje");
                params.put("body", messageInput.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setScrollbar(){
        messagesContainer.scrollToPosition(adapter.getItemCount()-1);
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
