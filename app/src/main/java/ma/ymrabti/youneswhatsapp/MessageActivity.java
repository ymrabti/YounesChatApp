package ma.ymrabti.youneswhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ymrabti.youneswhatsapp.Model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView pdp;
    TextView user_name;
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;
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
        intent = getIntent();
        String userid = intent.getStringExtra("userid");


        user = FirebaseAuth.getInstance().getCurrentUser();
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
}
