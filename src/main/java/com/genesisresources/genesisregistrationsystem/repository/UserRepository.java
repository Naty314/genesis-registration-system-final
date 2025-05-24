package com.genesisresources.genesisregistrationsystem.repository;

import com.genesisresources.genesisregistrationsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPersonId(String personId);

    Optional<User> findByUuid(String uuid);
}