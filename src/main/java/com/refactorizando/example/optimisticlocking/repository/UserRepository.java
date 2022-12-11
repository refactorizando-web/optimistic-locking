package com.refactorizando.example.optimisticlocking.repository;

import com.refactorizando.example.optimisticlocking.domain.UserEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
}
