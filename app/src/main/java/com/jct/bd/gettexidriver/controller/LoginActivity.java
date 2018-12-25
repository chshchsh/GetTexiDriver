package com.jct.bd.gettexidriver.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextView register;
    EditText userName,password;
    CardView login;
    public static final String mypreference = "mypref";
    public static final String Name = "person_name";
    public static final String passwordS = "password";
    SharedPreferences sharedpreferences;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }
    public void findViews(){
        login = (CardView) findViewById(R.id.Login);
        login.setOnClickListener(this);
        login.setEnabled(false);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        Fetch();
        userName.addTextChangedListener(AddTextWatcer);
        password.addTextChangedListener(AddTextWatcer);
        register = findViewById(R.id.textView2);
        auth = FirebaseAuth.getInstance();
        String text = getString(R.string.registerbutton);
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
    public void loginUser() {
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
                                Intent intent = getIntent();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("driver email",email).putExtra("driver password",Ipassword));
                                Store();
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
    public void Store() {
        if (!userName.getText().toString().isEmpty() &&  !password.getText().toString().isEmpty()) {
            String n = userName.getText().toString();
            String p = password.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, n);
            editor.putString(passwordS, p);
            editor.commit();
            Toast.makeText(getApplicationContext(), R.string.store, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
        }
    }
    public void Fetch() {
        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)&&sharedpreferences.contains(passwordS)) {
            userName.setText(sharedpreferences.getString(Name, ""));
            password.setText(sharedpreferences.getString(passwordS,""));
            login.setEnabled(true);
            Toast.makeText(getApplicationContext(), R.string.fetch, Toast.LENGTH_SHORT).show();
        }else{
            return;
        }
    }
}
