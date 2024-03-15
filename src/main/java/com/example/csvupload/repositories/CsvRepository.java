package com.example.csvupload.repositories;

import com.example.csvupload.entities.CsvData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsvRepository extends JpaRepository<CsvData , Integer> {
    Optional<CsvData> findByCode(String code);
}
