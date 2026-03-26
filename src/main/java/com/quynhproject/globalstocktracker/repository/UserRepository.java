package com.quynhproject.globalstocktracker.repository;

import com.quynhproject.globalstocktracker.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM user WHERE email = :email", nativeQuery = true)
    void deleteByEmail(@Param("email") String email);

    Optional<User> findByUsername(String username);

}
