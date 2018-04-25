package com.example.branislavnovak.chatapplication;

/**
 * Created by Branislav Novak on 20-Apr-18.
 */

public class Contact {
    private String mContactID;
    private String mUserName;
    private String mFirstName;
    private String mLastName;

    public Contact(String id, String userName, String firstName, String lastName){
        mContactID = id;
        mUserName = userName;
        mFirstName = firstName;
        mLastName = lastName;
    }

    public String getmID() {
        return mContactID;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }
}
