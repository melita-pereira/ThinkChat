package com.melita.thinkchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.melita.thinkchat.R;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    
    Button buttonSignIn;
    TextView textCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        textCreateNewAccount = findViewById(R.id.textCreateNewAccount);
        textCreateNewAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.buttonSignIn){
            addDataToFirestore();
        } else if (id == R.id.textCreateNewAccount) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

    private void addDataToFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("first_name", "Melita");
        data.put("last_name", "Pereira");
        db.collection("users")
                .add(data)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}