package ma.ymrabti.youneswhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ymrabti.youneswhatsapp.Adapters.MessageAdapter;
import ma.ymrabti.youneswhatsapp.Model.Chatt;
import ma.ymrabti.youneswhatsapp.Model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView pdp;
    TextView user_name;
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chatt> chattList;
    RecyclerView recyclerView;
    TextView message_zone;
    ImageButton send_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycle_view_send_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        user_name = findViewById(R.id.username_message);
        pdp = findViewById(R.id.profile_image_message);
        message_zone = findViewById(R.id.send_message);
        send_message = findViewById(R.id.button_send);
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");


        user = FirebaseAuth.getInstance().getCurrentUser();

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = message_zone.getText().toString();
                if (!message.equals("")){
                    sendMessage(user.getUid(),userid,message);
                }else {
                    Toast.makeText(getApplicationContext(),"Enter a message",Toast.LENGTH_LONG).show();
                }
                message_zone.setText("");
            }
        });

        if (userid != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_ = dataSnapshot.getValue(User.class);
                if (user_ != null) {
                    user_name.setText(user_.getUsername());
                }
                if (user_ != null) {
                    if (user_.getImageURL().equals("default")){
                        pdp.setImageResource(R.drawable.avatar_mini);
                    }
                    else {
                        Glide.with(getApplicationContext()).load(user_.getImageURL()).into(pdp);
                    }
                }
                if (user_ != null) {
                    readMessages(user.getUid(),userid,user_.getImageURL());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);hashMap.put("receiver",receiver);hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessages(final String receiver, final String sender, final String imageURL){
        chattList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chattList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chatt chatt = snapshot.getValue(Chatt.class);
                    if (chatt != null && ((chatt.getReceiver().equals(receiver) && chatt.getSender().equals(sender))
                            || (chatt.getReceiver().equals(sender) && chatt.getSender().equals(receiver)))) {
                        chattList.add(chatt);
                    }
                    messageAdapter = new MessageAdapter(chattList,getApplicationContext(),imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
