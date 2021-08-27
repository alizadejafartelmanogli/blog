package com.example.blog.repository;

import com.example.blog.models.Administrator;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Administrator, Long> {
}
