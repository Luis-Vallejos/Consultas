package com.practica.consultas.service.impl;

import com.practica.consultas.service.ISalaLockService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Service
public class SalaLockServiceImpl implements ISalaLockService {

    private final ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

    @Override
    public <T> T runWithLock(Long salaId, Supplier<T> criticalOperation) {
        Lock lock = locks.computeIfAbsent(salaId, k -> new ReentrantLock());

        lock.lock();
        try {
            return criticalOperation.get();
        } finally {
            lock.unlock();
        }
    }
}
