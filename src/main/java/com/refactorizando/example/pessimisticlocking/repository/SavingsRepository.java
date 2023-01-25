package com.refactorizando.example.pessimisticlocking.repository;

import com.refactorizando.example.pessimisticlocking.domain.Savings;
import java.util.UUID;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SavingsRepository extends CrudRepository<Savings, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="10000")})
  @Query("select a from Savings a where a.id = :id")
  Savings findSavingToWriteById(@Param("id") UUID id);

  @Lock(LockModeType.PESSIMISTIC_READ)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="10000")})
  @Query("select a from Savings a where a.id = :id")
  Savings findSavingToReadById(@Param("id") UUID id);

}
