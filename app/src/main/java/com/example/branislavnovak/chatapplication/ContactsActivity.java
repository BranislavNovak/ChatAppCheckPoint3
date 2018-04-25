package com.example.branislavnovak.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bLogout;
    private ListView list;
    private ChatDbHelper chatDbHelper;
    private Contact[] contacts;
    public static final String PREFERENCES_NAME = "PreferenceFile";
    public String userId;
    public ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        bLogout = findViewById(R.id.logoutButton);
        list = findViewById(R.id.listOfContacts);
        bLogout.setOnClickListener(this);

        // new chatDataBase instance and reading contacts from database
        chatDbHelper = new ChatDbHelper(this);
        contacts = chatDbHelper.readContacts();

        // Getting logged user userid, from SharedPreference file
        SharedPreferences sharedPref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        userId = sharedPref.getString("senderId", null);
        adapter = new ContactsAdapter(this);

        list.setAdapter(adapter);
        adapter.updateContacts(contacts);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.logoutButton:
                Intent i1 = new Intent(this, MainActivity.class);
                startActivity(i1);
                break;
            case R.id.nextButton:
                Intent i2 = new Intent(this, MessageActivity.class);
                startActivity(i2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Deleting logged user from contacts list
        deleteLoggedUserFromList();
    }

    public void deleteLoggedUserFromList() {
        contacts = chatDbHelper.readContacts();
        adapter.updateContacts(contacts);

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getmID().compareTo(userId) == 0) {
                    adapter.removeContact(i);
                    break;
                }
            }
        }
    }

}
