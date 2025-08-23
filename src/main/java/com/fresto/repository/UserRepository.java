package com.fresto.repository;

import com.fresto.constant.UserType;
import com.fresto.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRepository is the interface for accessing User data from the database.
 * It extends JpaRepository to provide CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserEmail(String email);

    List<User> findAllByUserType(UserType userType);

    @Transactional
    default User updateOrInsert(User entity) {
        return save(entity);
    }
}
