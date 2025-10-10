package com.practica.consultas.service.generic;

/**
 *
 * @author Luis
 * @param <T>
 * @param <ID>
 */
public interface ICrudService<T, ID> extends IReadService<T, ID>, IWriteService<T, ID> {

}
