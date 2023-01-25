package com.refactorizando.example.optimisticlocking;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.refactorizando.example.optimisticlocking.domain.UserEntity;
import com.refactorizando.example.optimisticlocking.repository.UserRepository;
import com.refactorizando.example.optimisticlocking.service.SavingsService;
import com.refactorizando.example.optimisticlocking.service.UserService;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @SpyBean
  private SavingsService savingsService;

  private final List<Integer> savings = Arrays.asList(2, 2);

  @Test
  void given_savings_when_updated_user_then_version_is_incremented() {

    final UserEntity user = userRepository.save(new UserEntity());
    assertEquals(0, user.getHello());

    savings.forEach(saving -> {
      userService.updateSavings(user.getId(), saving);
    });

    final UserEntity userUpdated = userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("No user found!"));

    assertAll(
        () -> assertEquals(2, userUpdated.getHello()),
        () -> assertEquals(4, userUpdated.getSavings())
    );
  }

  @Test
  void given_savings_when_updated_user_in_a_concurrent_way_then_version_is_incremented() throws InterruptedException {

    final UserEntity user = userRepository.save(new UserEntity());
    assertEquals(0, user.getHello());

    final ExecutorService executor = Executors.newFixedThreadPool(savings.size());

    savings.forEach(saving -> {
      executor.execute(() -> userService.updateSavings(user.getId(), saving));
    });

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

    final var userSaved = userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    assertAll(
        () -> assertEquals(2, userSaved.getHello()),
        () -> assertEquals(4, userSaved.getSavings())
    );
  }
}
