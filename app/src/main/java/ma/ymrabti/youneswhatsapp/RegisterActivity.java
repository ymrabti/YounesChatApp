package ma.ymrabti.youneswhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;private DatabaseReference reference;
    private MaterialEditText user_name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        user_name=findViewById(R.id.user_name);email=findViewById(R.id.email);password=findViewById(R.id.password);
        Button button_register = findViewById(R.id.creer_compte);
        mAuth = FirebaseAuth.getInstance();
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_user_name = Objects.requireNonNull(user_name.getText()).toString();
                String text_email = Objects.requireNonNull(email.getText()).toString();
                String text_password = Objects.requireNonNull(password.getText()).toString();
                if (TextUtils.isEmpty(text_user_name)||TextUtils.isEmpty(text_email)||TextUtils.isEmpty(text_password)){
                    Toast.makeText(RegisterActivity.this,
                            "All fields are required",Toast.LENGTH_LONG).show();
                }
                else{
                    register(text_user_name,text_email,text_password);
                }
            }
        });
    }
    private void register(final String username, String email_adress, String password_){
        mAuth.createUserWithEmailAndPassword(email_adress, password_)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userid= null;
                        if (user != null) {
                            userid = user.getUid();
                        }
                        assert userid != null;
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("id",userid);hashMap.put("username",username);hashMap.put("imageURL","default");
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);finish();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "You can't register with this email address or password",Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}