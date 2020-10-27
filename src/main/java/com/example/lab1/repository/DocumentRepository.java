package com.example.lab1.repository;

import com.example.lab1.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document save(Document document);

    Optional<Document> findDocumentById(Long id);

    @Query("SELECT COUNT (d) FROM Document d")
    Integer countAll();

    @Query("SELECT d FROM Document d")
    List<Document> findAll();
}
