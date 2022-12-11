package com.refactorizando.example.optimisticlocking.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final SavingsService savingService;

    public void updateSavings(UUID id, int saving) {
        try {
            savingService.updateSavings(id, saving);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Savings has been updated before in concurrent transaction");
            savingService.updateSavings(id, saving);
        }
    }

}
