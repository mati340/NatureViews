package com.colman.natureviews.model;

//singleton user class
public class User {

    private static User theUser = null;

    public String userUsername;
    public String userEmail;
    public String profileImageUrl;
    public String userId;
    public String userInfo;

    private User()
    {
        userEmail = null;
        userUsername = null;
        profileImageUrl = null;
        userId = null;
        userInfo = null;
    }

    // static method to create instance of Singleton class
    public static User getInstance()
    {
        if (theUser == null)
            theUser = new User();

        return theUser;
    }

}
