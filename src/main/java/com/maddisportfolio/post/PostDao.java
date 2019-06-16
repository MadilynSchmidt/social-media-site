package com.maddisportfolio.post;

import com.maddisportfolio.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

List<Post> findAllByUser(User user);



}
