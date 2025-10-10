package com.practica.consultas.service.generic;

/**
 *
 * @author Luis
 * @param <T>
 * @param <ID>
 */
public interface IWriteService<T, ID> {

    T create(T entity);

    T edit(ID id, T updated);

    boolean delete(ID id);
}
