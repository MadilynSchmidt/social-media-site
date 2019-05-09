package com.maddisportfolio.friendrequest;

import com.maddisportfolio.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestDao extends JpaRepository<FriendRequest, Long> {

  FriendRequest findOneBySenderAndRecipient(User sender, User recipient);

  List<FriendRequest> findAllBySenderAndRecipientIn(User sender, List<User> recipients);


}



