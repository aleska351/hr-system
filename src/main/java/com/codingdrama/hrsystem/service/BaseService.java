package com.codingdrama.hrsystem.service;

import java.util.List;

public interface BaseService<S> {

    List<S> getAll();

    S getById(Long id);

    void delete(Long id);
}
