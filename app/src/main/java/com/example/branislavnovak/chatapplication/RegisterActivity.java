package com.example.branislavnovak.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bRegister;
    private EditText usernameTypedText;
    private EditText passwordTypedText;
    private EditText emailTypedText;
    private EditText firstNameTypedText;
    private EditText lastNameTypedText;
    private DatePicker calendar;

    private ChatDbHelper chatDbHelper;

    // function that checks if mail is typed correctly
    public boolean checkMail(String email){
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // variables for checking if text was typed

        final boolean usernameEntered[] = {false};
        final boolean passwordEntered[] = {false};
        final boolean emailEntered[] = {false};


        // setting variables to their Id
        usernameTypedText = findViewById(R.id.usernameReg);
        passwordTypedText = findViewById(R.id.passwordReg);
        emailTypedText = findViewById(R.id.email);
        firstNameTypedText = findViewById(R.id.firstName);
        lastNameTypedText = findViewById(R.id.lastName);
        calendar = findViewById(R.id.date);
        bRegister = findViewById(R.id.registerButton);

        bRegister.setOnClickListener(this);
        bRegister.setEnabled(false);

        chatDbHelper = new ChatDbHelper(this);

        calendar.setMaxDate(System.currentTimeMillis() - 1000);

        usernameTypedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String checkLength = usernameTypedText.getText().toString();
                if (checkLength.length() != 0){
                    usernameEntered[0] = true;
                    if ((passwordEntered[0] == true) && (emailEntered[0] == true)){
                        bRegister.setEnabled(true);
                    }
                }else{
                    usernameEntered[0] = false;
                    bRegister.setEnabled(false);
                }
            }
        });

        passwordTypedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String checkLength = passwordTypedText.getText().toString();
                if (checkLength.length() >= 6){
                    passwordEntered[0] = true;
                    if ((usernameEntered[0] == true) && (emailEntered[0] == true)){
                        bRegister.setEnabled(true);
                    }
                }else{
                    passwordEntered[0] = false;
                    bRegister.setEnabled(false);
                }

            }
        });

        emailTypedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String checkLength = emailTypedText.getText().toString();

                if (checkLength.length() != 0) {
                    if (checkMail(checkLength)) {
                        emailEntered[0] = true;
                        if ((usernameEntered[0] == true) && (passwordEntered[0] == true)) {
                            bRegister.setEnabled(true);
                        }
                    } else {
                        emailEntered[0] = false;
                        bRegister.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerButton:
                int alreadyExist = 0;                                                               // flag for already existing contacts username

                Contact[] contacts = chatDbHelper.readContacts();                                   // loading all existing contacts

                if (contacts != null){
                    for (int i = 0; i < contacts.length; i++){
                        if (contacts[i].getmUserName().compareTo(usernameTypedText.getText().toString()) == 0){     // checking if typed user already exist
                            alreadyExist = 1;                                                                       // and setting flag on true if it does
                            break;
                        }
                    }
                }

                if (alreadyExist == 1){
                    Toast.makeText(this, getText(R.string.sErrorAlreadyExist), Toast.LENGTH_LONG).show();
                }else{
                    Contact contact = new Contact(null, usernameTypedText.getText().toString(), firstNameTypedText.getText().toString(), lastNameTypedText.getText().toString());
                    chatDbHelper.insertContact(contact);                                            // inserting typed user into contact list

                    Intent i = new Intent(this, MainActivity.class);                  // jumping on login activity
                    startActivity(i);
                }
        }
    }
}
