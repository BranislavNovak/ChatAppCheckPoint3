package com.example.branislavnovak.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bSend;
    private Button bLogout;
    private EditText enteredMessage;
    private Message[] messages;

    private String receiverId;
    private String senderId;

    public static final String PREFERENCES_NAME = "PreferenceFile";
    private MessageAdapter adapter = new MessageAdapter(this);
    public ChatDbHelper chatDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // getting all IDs from .xml
        bSend = findViewById(R.id.sendButton);
        bLogout = findViewById(R.id.logoutButton1);
        enteredMessage = findViewById(R.id.eMessage);

        // opening shared preference and taking sender nad receiver ID from it with their own keys
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        senderId = preferences.getString("senderId", null);
        receiverId = preferences.getString("receiverId", null);

        chatDbHelper = new ChatDbHelper(this);

        ListView listOfMessages = findViewById(R.id.listOfMessages);
        TextView nameOfContact = findViewById(R.id.contactNameInMessage);                           // nameOfContact taking a ID of a TextView field that needs to be changed

        bSend.setEnabled(false);
        bSend.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        // getting all extras (exactly the nameOfContact)
        Intent intent = getIntent();
        Bundle b = intent.getExtras();                                                              // getting a values from the first made Bundle in ContactsAdapter
        String name = b.getString("contactName");                                              // then set a value on a String through a key value
        nameOfContact.setText(name);                                                                // making a TextView in MessageActivity

        listOfMessages.setAdapter(adapter);

        // setting item of a list on LongClickListener for removing it from list and database
        listOfMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Message message = (Message) adapter.getItem(position);

                if (messages != null){
                    for(int i = 0; i < messages.length; i++){
                        if(messages[i].getmMessageId().compareTo(message.getmMessageId()) == 0){
                            chatDbHelper.deleteMessage(message.getmMessageId());
                            break;
                        }
                    }
                }

                updateMessagesList();
                return true;
            }
        });

        enteredMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = enteredMessage.getText().toString();
                if (s.length() != 0){
                    bSend.setEnabled(true);
                }else{
                    bSend.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sendButton:
                String message = enteredMessage.getText().toString();
                String messageSent = "Message is sent";
                Toast.makeText(this, messageSent, Toast.LENGTH_LONG).show();

                // inserting Message into database
                Message messageToSend = new Message(null, senderId, receiverId, message);
                chatDbHelper.insertMessage(messageToSend);
                updateMessagesList();

                enteredMessage.getText().clear();
                enteredMessage.setHint(R.string.sMessage);
                break;

            case R.id.logoutButton1:
                Intent j = new Intent(this, MainActivity.class);
                startActivity(j);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMessagesList();
    }

    public void updateMessagesList(){
        messages = chatDbHelper.readMessages(senderId, receiverId);
        adapter.addMessages(messages);
    }
}
