package com.maddisportfolio.friendrequest;
import com.maddisportfolio.user.User;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendRequestStatus friendRequestStatus;

    public FriendRequestStatus getFriendRequestStatus() {
        return friendRequestStatus;
    }

    public void setFriendRequestStatus(FriendRequestStatus friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }




}
