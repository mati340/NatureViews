package com.colman.natureviews.model;

//singleton user class
public class User {

    private static User theUser = null;

    public String name;
    public String userEmail;
    public String profileImageUrl;
    public String userId;
    public String userInfo;
    public long lastUpdated;

    private User()
    {
        userEmail = null;
        name = null;
        profileImageUrl = null;
        userId = null;
        userInfo = null;
        lastUpdated = 0;
    }

    // static method to create instance of Singleton class
    public static User getInstance()
    {
        if (theUser == null)
            theUser = new User();

        return theUser;
    }

}
