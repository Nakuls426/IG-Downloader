package com.ig.igdownloader.datahold;

public class UserDetails {
    String userPic;
    String userName;
    String userFullName;
    String userPk;
    String userPicURL;

    public UserDetails(String userPic, String userName, String userFullName, String userPk, String userPicURL) {
        this.userPic = userPic;
        this.userName = userName;
        this.userFullName = userFullName;
        this.userPk = userPk;
        this.userPicURL = userPicURL;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserPk() {
        return userPk;
    }

    public String getUserPicURL() {
        return userPicURL;
    }
}
