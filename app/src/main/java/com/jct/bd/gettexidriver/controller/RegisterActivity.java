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
import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.datasource.Action;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;
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
                                            FireBase_DB_manager backend = new FireBase_DB_manager();
                                            backend.addDriver(driver, new Action<String>() {
                                                @Override
                                                public void onSuccess(String obj) {
                                                    Toast.makeText(getBaseContext(), "insert id " + obj, Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onFailure(Exception exception) {
                                                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                                                }

                                                public void onProgress(String status, double percent) {
                                                    if (percent != 100)
                                                        register.setEnabled(false);
                                                }
                                            });
                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        }else {
                                            Toast.makeText(RegisterActivity.this, "failure to load firebase", Toast.LENGTH_SHORT).show();
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
