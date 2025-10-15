package com.practica.consultas.service;

import java.util.function.Supplier;

public interface ISalaLockService {

    <T> T runWithLock(Long salaId, Supplier<T> criticalOperation);
}
