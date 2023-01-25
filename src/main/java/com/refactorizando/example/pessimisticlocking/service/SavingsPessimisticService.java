package com.refactorizando.example.pessimisticlocking.service;

import com.refactorizando.example.pessimisticlocking.domain.Savings;
import com.refactorizando.example.pessimisticlocking.repository.SavingsRepository;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SavingsPessimisticService {

  private final SavingsRepository savingsRepository;

  @Transactional
  public void updateSavings(UUID id, int saving) throws Exception {

    try {
      var savings = savingsRepository.findSavingToWriteById(id);
      savings.setAmount(savings.getAmount().add(new BigDecimal(saving)));

    } catch (CannotAcquireLockException ex) {
      Thread.sleep(1_000);
      var savings = savingsRepository.findSavingToWriteById(id);
      savings.setAmount(savings.getAmount().add(new BigDecimal(saving)));
    }


  }

  @Transactional
  public void updateTwoSavings(UUID id1, UUID id2, int saving) throws Exception {

    updateSavings(id1, saving);
    updateSavings(id2, saving);
    Thread.sleep(1_000);


  }

  @Transactional
  public void updateTwoSavingsReverse(UUID id1, UUID id2, int saving) throws Exception {

    updateSavings(id2, saving);
    updateSavings(id1, saving);
    Thread.sleep(1_000);

  }


  public Savings getSavings(UUID id) {
    return savingsRepository.findSavingToReadById(id);

  }
}
