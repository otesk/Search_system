package com.example.lab1.repository;

import com.example.lab1.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT w FROM Word w WHERE w.text = ?1")
    Optional<Word> findByText(String text);

    @Query(value = "SELECT (w) FROM Word w")
    List<Word> findAll();

    Word save(Word word);

    @Modifying
    @Query(value = "INSERT INTO document_word (document_id, word_id, count) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void saveWordDocumentCount(Long docId, Long wordId, Integer count);

    @Query(value = "SELECT count FROM document_word WHERE document_id = ?1 AND word_id = ?2", nativeQuery = true)
    Optional<Integer> getWordCountInDocument(Long documentId, Long word_id);

    @Query(value = "SELECT COUNT(*) as count FROM document_word WHERE word_id = ?1 ", nativeQuery = true)
    Integer findCountDocumentsWithWord(Long wordId);
}
