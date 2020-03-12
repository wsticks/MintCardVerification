package com.williams.mintbank.kafkaexcercisetest.repository;

import com.williams.mintbank.kafkaexcercisetest.model.entity.Card;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card, Long> {

    Card findByBin(String bin);
    List<Card> findAll();
}
