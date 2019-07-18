package com.maddisportfolio.user;

import javax.persistence.*;

@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "location")
    private String location;

    @Column(name = "timezone")
    private String timezone;

    @Transient
    private boolean hasBeenSentFriendRequestByLoggedInUser;

    @Transient
    private boolean hasReceivedRequestFromSearchedUser;

    @Transient
    private boolean isFriendsWithLoggedInUser;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isHasBeenSentFriendRequestByLoggedInUser() {
        return hasBeenSentFriendRequestByLoggedInUser;
    }
    public void setHasBeenSentFriendRequestByLoggedInUser(boolean hasBeenSentFriendRequestByLoggedInUser) {
        this.hasBeenSentFriendRequestByLoggedInUser = hasBeenSentFriendRequestByLoggedInUser;
    }

    public boolean isHasReceivedRequestFromSearchedUser() {
        return hasReceivedRequestFromSearchedUser;
    }

    public void setHasReceivedRequestFromSearchedUser(boolean hasReceivedRequestFromSearchedUser) {
        this.hasReceivedRequestFromSearchedUser = hasReceivedRequestFromSearchedUser;
    }

    public boolean isUserAndLoggedInUserAreFriends() {
        return isFriendsWithLoggedInUser;
    }

    public void setUserAndLoggedInUserAreFriends(boolean userAndLoggedInUserAreFriends) {
        this.isFriendsWithLoggedInUser = userAndLoggedInUserAreFriends;
    }
}
