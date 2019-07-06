package com.maddisportfolio.post;

import com.maddisportfolio.user.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Table(name = "post")
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date")
    private Timestamp timestamp;

    @Transient
    private ZonedDateTime displayZonedDateTime;

    @Transient
    private String formattedDate;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ZonedDateTime getDisplayZonedDateTime() {
        return displayZonedDateTime;
    }

    public void
    setDisplayZonedDateTime(ZonedDateTime displayZonedDateTime) {
        this.displayZonedDateTime = displayZonedDateTime;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}


