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
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextView register;
    EditText userName,password;
    CardView login;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }
    public void findViews(){
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (CardView) findViewById(R.id.Login);
        login.setOnClickListener(this);
        login.setEnabled(false);
        userName.addTextChangedListener(AddTextWatcer);
        password.addTextChangedListener(AddTextWatcer);
        register = findViewById(R.id.textView2);
        auth = FirebaseAuth.getInstance();
        String text = "Dont have you account? register her!";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.YELLOW);
            }
        };
        ss.setSpan(clickableSpan,23,36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(ss);
        register.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private TextWatcher AddTextWatcer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputName = userName.getText().toString().trim();
            String inputPassword = password.getText().toString().trim();
            login.setEnabled(!inputName.isEmpty()&&!inputPassword.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void loginUser(){
                final String email = userName.getText().toString();
                final String Ipassword = password.getText().toString();
                try {
                    auth.signInWithEmailAndPassword(email, Ipassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "the email or the password is not correct!", Toast.LENGTH_LONG).show();
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
    @Override
    public void onClick(View v) {
        if(v == login)
        {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            login.startAnimation(animation);
            loginUser();
        }
    }
}
