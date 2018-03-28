package com.example.hp.trial;

/**
 * Created by hp on 21/03/2018.
 */

public class UserProfile {

    public String userName;
    public String userEmail;
    public String userNumber;
    public String userAge;

    //Another constructor -> function overloading concept
    public  UserProfile(){

    }

    //constructor created -> assign values to these variable from activities before it
    public UserProfile(String userName, String userEmail, String userNumber, String userAge) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNumber = userNumber;
        this.userAge = userAge;

    }

    //get-set methods for data retrieval and updation
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

}
