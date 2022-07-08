package ru.job4j.hql;

import java.util.List;

public interface RepoInterface {
    Candidate add(Candidate item);

    boolean replace(String id, Candidate item);

    boolean delete(String id);

    List<Candidate> findAll();

    List<Candidate> findByName(String key);

    Candidate findById(String id);
}
