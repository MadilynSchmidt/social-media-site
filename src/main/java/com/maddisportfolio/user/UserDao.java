package com.maddisportfolio.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findOneByEmailAddressIgnoreCase(String emailAddress);

    List<User> findByEmailAddressIgnoreCaseContaining(String emailAddress);

}

