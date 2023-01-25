package com.refactorizando.example.pessimisticlocking;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.refactorizando.example.pessimisticlocking.domain.Savings;
import com.refactorizando.example.pessimisticlocking.repository.SavingsRepository;
import com.refactorizando.example.pessimisticlocking.service.SavingsPessimisticService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SavingsPessimisticServiceTest {

  private final List<Integer> savingsData = Arrays.asList(2, 2);
  @Autowired
  private SavingsPessimisticService savingsPessimisticService;

  //@SpyBean
  //private SavingsPessimisticService savingsPessimisticService;
  @Autowired
  private SavingsRepository savingsRepository;

  @Test
  void given_savings_when_updated_savings_then_savings_is_updated() throws Exception{

    final Savings savings = savingsRepository.save(new Savings());
    assertEquals(new BigDecimal(0), savings.getAmount());

    savingsData.forEach(saving -> {
      try {
        savingsPessimisticService.updateSavings(savings.getId(), saving);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    final Savings savingUpdate = savingsRepository.findById(savings.getId())
        .orElseThrow(() -> new IllegalArgumentException("No savings found!"));

    assertAll(
        () -> assertEquals(new BigInteger("4"), savingUpdate.getAmount().toBigInteger())
    );
  }

  @Test
  void given_savings_when_updated_user_in_a_concurrent_way_then_version_is_incremented()
      throws InterruptedException {

    final var savings = savingsRepository.save(new Savings());
    assertEquals(new BigDecimal(0), savings.getAmount());

    final ExecutorService executor = Executors.newFixedThreadPool(savingsData.size());

    savingsData.forEach(saving -> {
      executor.execute(() -> {
        try {
          savingsPessimisticService.updateSavings(savings.getId(), saving);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    });

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

    final Savings savingUpdate = savingsRepository.findById(savings.getId())
        .orElseThrow(() -> new IllegalArgumentException("No savings found!"));

    assertAll(
        () -> assertEquals(new BigInteger("4"), savingUpdate.getAmount().toBigInteger())
    );
  }

  @Test
  void given_two_savings_when_updated_savings_in_a_concurrent_way_then_savings_are_incremented()
      throws InterruptedException {

    final var savings = savingsRepository.save(new Savings());
    assertEquals(new BigDecimal(0), savings.getAmount());

    final var savings2 = savingsRepository.save(new Savings());
    assertEquals(new BigDecimal(0), savings.getAmount());

    final ExecutorService executor = Executors.newFixedThreadPool(savingsData.size());

    savingsData.forEach(saving -> {
      executor.execute(() -> {
        try {
          savingsPessimisticService.updateTwoSavings(savings.getId(), savings2.getId(), saving);
          savingsPessimisticService.updateTwoSavingsReverse(savings.getId(),savings2.getId(),  saving);


        } catch (Exception e) {
        }
      });
    });

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

    final Savings savingUpdate = savingsRepository.findById(savings.getId())
        .orElseThrow(() -> new IllegalArgumentException("No savings found!"));

    assertAll(
        () -> assertEquals(new BigInteger("6"), savingUpdate.getAmount().toBigInteger())
    );
  }
}
