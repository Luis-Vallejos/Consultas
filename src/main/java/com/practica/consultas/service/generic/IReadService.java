package com.practica.consultas.service.generic;

import java.util.ArrayList;

/**
 *
 * @author Luis
 * @param <T>
 * @param <ID>
 */
public interface IReadService<T, ID> {

    T findById(ID id);

    ArrayList<T> findAll();
}
