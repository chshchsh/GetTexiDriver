package com.jct.bd.gettexidriver.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
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
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.Action;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;
import com.jct.bd.gettexidriver.model.entities.Driver;

import static com.jct.bd.gettexidriver.model.entities.Ride.IDCheck;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView login;
    private TextInputLayout InputPassword, InputEmail, InputCreditCard, InputIdNumber, InputUserName, InputPhone;
    EditText userName, id, phoneNumber, email, password, CreditCard;
    CardView register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    public void findViews() {
        InputPassword = findViewById(R.id.InputPassword);
        InputEmail = findViewById(R.id.InputEmail);
        InputCreditCard = findViewById(R.id.InputCreditCard);
        InputIdNumber = findViewById(R.id.InputIdNumber);
        InputUserName = findViewById(R.id.InputUserName);
        InputPhone = findViewById(R.id.InputPhoneNumber);
        login = findViewById(R.id.textView3);
        userName = (EditText) findViewById(R.id.UserName);
        id = (EditText) findViewById(R.id.idNumber);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        CreditCard = (EditText) findViewById(R.id.CreditCard);
        register = (CardView) findViewById(R.id.Register);
        register.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        String text = getString(R.string.account);
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.YELLOW);
            }
        };
        ss.setSpan(clickableSpan, 17, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private boolean validateEmail(){
        String emailInput = InputEmail.getEditText().getText().toString();
        if(emailInput.isEmpty()){
            InputEmail.setError(getString(R.string.fill_email));
            InputIdNumber.setErrorEnabled(true);
            email.requestFocus();
            Toast.makeText(this,getString(R.string.fill_email),Toast.LENGTH_LONG).show();
            return false;
        }else if (!emailInput.contains("@")){
            InputIdNumber.setErrorEnabled(true);
            InputIdNumber.setError(getString(R.string.contains));
            email.requestFocus();
            Toast.makeText(this,getString(R.string.contains),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputEmail.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword(){
        String passwordInput = InputPassword.getEditText().getText().toString();
        if(passwordInput.isEmpty()){
            InputPassword.setError(getString(R.string.fill_password));
            InputPassword.setErrorEnabled(true);
            password.requestFocus();
            Toast.makeText(this,getString(R.string.fill_password),Toast.LENGTH_LONG).show();
            return false;
        }else if(passwordInput.length()<6){
            InputPassword.setError(getString(R.string.length_password));
            InputPassword.setErrorEnabled(true);
            password.requestFocus();
            Toast.makeText(this,getString(R.string.length_password),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputPassword.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateId(){
        String IdNumberInput = InputIdNumber.getEditText().getText().toString();
        if(IdNumberInput.isEmpty()){
            InputIdNumber.setError(getString(R.string.fill_id));
            InputIdNumber.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.fill_id),Toast.LENGTH_LONG).show();
            return false;
        }else if(!IDCheck(IdNumberInput)){
            InputIdNumber.setError(getString(R.string.Extract_id));
            InputIdNumber.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.Extract_id),Toast.LENGTH_LONG).show();
            return false;
        }else if(IdNumberInput.length()!=9){
            InputIdNumber.setError(getString(R.string.length_id));
            InputIdNumber.setErrorEnabled(true);
            id.requestFocus();
            Toast.makeText(this,getString(R.string.length_id),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputIdNumber.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateUserName(){
        String UserNameInput = InputUserName.getEditText().getText().toString();
        if(UserNameInput.isEmpty()){
            InputUserName.setError(getString(R.string.fill_userName));
            InputUserName.setErrorEnabled(true);
            userName.requestFocus();
            Toast.makeText(this,getString(R.string.fill_userName),Toast.LENGTH_LONG).show();
            return false;
        }else {
            InputUserName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateCreditCard(){
        String creditCardInput = InputCreditCard.getEditText().getText().toString();
        if(creditCardInput.isEmpty()) {
            InputCreditCard.setError(getString(R.string.fill_creditCard));
            InputCreditCard.setErrorEnabled(true);
            CreditCard.requestFocus();
            Toast.makeText(this,getString(R.string.fill_creditCard),Toast.LENGTH_LONG).show();
            return false;
        }else {
            InputCreditCard.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone(){
        String phoneInput = InputPhone.getEditText().getText().toString();
        if(phoneInput.isEmpty()){
            InputPhone.setError(getString(R.string.fill_phone));
            phoneNumber.requestFocus();
            InputPhone.setErrorEnabled(true);
            Toast.makeText(this,getString(R.string.fill_phone),Toast.LENGTH_LONG).show();
            return false;
        }else if(phoneInput.length()!=9||phoneInput.length()!=10){
            InputPhone.setError(getString(R.string.length_phone));
            phoneNumber.requestFocus();
            InputPhone.setErrorEnabled(true);
            Toast.makeText(this,getString(R.string.length_phone),Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            InputPhone.setErrorEnabled(false);
            return true;
        }
    }
    public void confirmInput(View v){
        if(!validateUserName()||!validateId()||!validatePhone()||!validateEmail()||!validateCreditCard()||!validatePassword())
            return;
        else
            registerUser();
    }

    private void registerUser() {
        try {
            final Driver driver = new Driver();
            driver.setCreditCard(CreditCard.getText().toString());
            driver.setEmail(email.getText().toString());
            driver.setId(id.getText().toString());
            driver.setFullName(userName.getText().toString());
            driver.setPhoneNumber(phoneNumber.getText().toString());
            driver.setPassword(password.getText().toString());
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                new AsyncTask<Void,Void,Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        return FactoryBackend.getInstance().addDriver(driver, new Action<String>() {
                                            @Override
                                            public void onSuccess(String obj) {
                                                register.setEnabled(true);
                                                Toast.makeText(getBaseContext(), getString(R.string.insert) + obj, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onFailure(Exception exception) {
                                                Toast.makeText(getBaseContext(), getString(R.string.error) + exception.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            public void onProgress(String status, double percent) {
                                                if (percent != 100)
                                                    register.setEnabled(false);
                                            }
                                        });
                                    }
                                }.execute();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, R.string.firebase_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == register) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            register.startAnimation(animation);
            confirmInput(v);
        }
    }
}
