package com.maddisportfolio.friendrequest;

import com.maddisportfolio.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestDao extends JpaRepository<FriendRequest, Long> {


  FriendRequest findOneBySenderAndRecipient(User sender, User recipient);





}
