package com.refactorizando.example.optimisticlocking.service;

import com.refactorizando.example.optimisticlocking.repository.UserRepository;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SavingsService {

  private final UserRepository userRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateSavings(UUID id, int saving) {
    var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    user.setSavings(user.getSavings() + saving);
  }

}
