package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ifmfo.wannaeatapp.ChatMessageCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.ChatMessage;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.ReceiveMessage;
import com.example.ifmfo.wannaeatapp.Model.SendMessage;
import com.example.ifmfo.wannaeatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class ChatActivity extends AppCompatActivity {

    private static final GlobalResources globalResources = GlobalResources.getInstance();

    private Toolbar toolbar;
    private TextView restaurantName;
    private EditText messageInput;
    private Button sendMessage;
    private RecyclerView messagesContainer;

    private ChatMessageCardAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initUI();
        setupToolbar();
        initDB();
        initMessageAdapter();

        bindEvents();
    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        restaurantName = findViewById(R.id.restaurant_name);
        messageInput = findViewById(R.id.messageInput);
        sendMessage = findViewById(R.id.sendMessageButton);
        messagesContainer = findViewById(R.id.messages_container);
        restaurantName.setText(globalResources.getLast_single_booking_visited().getBooking_restaurant_name());

    }

    private void initDB() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat"); //Sala de chat (nombre donde se van a guardar todos nuestros mensjaes)
    }

    private void initMessageAdapter() {
        adapter = new ChatMessageCardAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messagesContainer.setLayoutManager(linearLayoutManager);
        messagesContainer.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ic_action_orange_back);
        getSupportActionBar().setTitle("Chat");
    }

    private void bindEvents() {
        sendMessage.setOnClickListener(v -> {
            if(!messageInput.getText().toString().isEmpty()){
                databaseReference.push().setValue(new SendMessage(messageInput.getText().toString(), globalResources.getUserLogged().getName(), ServerValue.TIMESTAMP));
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
