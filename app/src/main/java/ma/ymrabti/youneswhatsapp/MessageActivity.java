package ma.ymrabti.youneswhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ymrabti.youneswhatsapp.Model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView pdp;
    TextView user_name;
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;

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
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user_name.setText(user.getUsername());
                }
                if (user != null) {
                    if (user.getImageURL().equals("default")){
                        pdp.setImageResource(R.drawable.avatar_mini);
                    }
                    else {
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(pdp);
                    }
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
}
