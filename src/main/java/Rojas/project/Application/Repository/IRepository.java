package Rojas.project.Application.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IRepository<T extends Object> {

    T Create(T entity);
    void Delete(String id);
    T Update(T entity);
    T Get(String id);
    List<T> GetAll();

}
