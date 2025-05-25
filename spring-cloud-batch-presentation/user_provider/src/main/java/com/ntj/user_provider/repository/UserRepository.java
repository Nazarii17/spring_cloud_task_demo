package com.ntj.user_provider.repository;

import com.ntj.springcloudbatchcommon.entity.User;
import com.ntj.springcloudbatchcommon.enums.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> getAllByReadStatus(ReadStatus readStatus);

    @Query(value = "SELECT * FROM users u WHERE u.iteration = (SELECT MAX(iteration) FROM users) LIMIT 1", nativeQuery = true)
    Optional<User> findAnyWithMaxIteration();

}
