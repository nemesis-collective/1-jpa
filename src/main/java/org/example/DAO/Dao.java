package org.example.DAO;

import java.util.List;

public interface Dao<T> {
  void add(T t);

  T get(Long id);

  List<T> getAll();

  T update(T t);

  void delete(Long id);
}
