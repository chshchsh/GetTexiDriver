package com.jct.bd.gettexidriver.controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.entities.Driver;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    TextView login;
    EditText userName,id,phoneNumber,email,password,CreditCard;
    CardView register;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null) {
            Toast.makeText(getApplicationContext(), "this username on use, please choose new username!", Toast.LENGTH_SHORT).show();
        }
    }

    public void findViews(){
        login = findViewById(R.id.textView3);
        userName = (EditText) findViewById(R.id.UserName);
        id = (EditText) findViewById(R.id.idNumber);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        CreditCard = (EditText) findViewById(R.id.CreditCard);
        register = (CardView) findViewById(R.id.Register);
        register.setOnClickListener(this);
        register.setEnabled(false);
        userName.addTextChangedListener(AddTextWatcer);
        id.addTextChangedListener(AddTextWatcer);
        phoneNumber.addTextChangedListener(AddTextWatcer);
        password.addTextChangedListener(AddTextWatcer);
        CreditCard.addTextChangedListener(AddTextWatcer);
        email.addTextChangedListener(AddTextWatcer);
        auth = FirebaseAuth.getInstance();
        String text = "have you account? login her!";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.YELLOW);
            }
        };
        ss.setSpan(clickableSpan,18,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());
    }
           private TextWatcher AddTextWatcer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputName = userName.getText().toString().trim();
                String inputEmail = email.getText().toString().trim();
                String inputId = id.getText().toString().trim();
                String inputPhone = phoneNumber.getText().toString().trim();
                String inputPassword = password.getText().toString().trim();
                String inputCreditCard = CreditCard.getText().toString().trim();
                register.setEnabled(!inputEmail.isEmpty() && !inputName.isEmpty() && !inputId.isEmpty() && !inputPhone.isEmpty()&&!inputCreditCard.isEmpty()&&!inputPassword.isEmpty());
            }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void registerUser() {
        try {
            final Driver driver = new Driver();
            driver.setCreditCard(CreditCard.getText().toString());
            driver.setEmail(email.getText().toString());
            driver.setId(id.getText().toString());
            driver.setFullName(userName.getText().toString());
            driver.setPhoneNumber(phoneNumber.getText().toString());
            driver.setPassword(password.getText().toString());
            auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Drivers")
                                        .child(driver.getId())
                                        .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "load to firebase,onCoplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        }else {
                                            Toast.makeText(RegisterActivity.this, "failure to load firebase", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        if(v == register)
        {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            register.startAnimation(animation);
            registerUser();
        }
    }
}
