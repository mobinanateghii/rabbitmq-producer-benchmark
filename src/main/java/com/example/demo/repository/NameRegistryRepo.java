package com.example.demo.repository;

import com.example.demo.model.NameRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NameRegistryRepo extends JpaRepository<NameRegistry, Long>, JpaSpecificationExecutor<NameRegistry> {
}
