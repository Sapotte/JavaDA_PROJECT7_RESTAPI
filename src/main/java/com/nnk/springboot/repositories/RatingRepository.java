package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Modifying
    @Query("UPDATE Rating SET moodysRating = :moodysRating, sandPRating = :sandPRating, fitchRating = :fitchRating, orderNumber = :orderNumber WHERE id = :id")
    void updateRating(Integer id, String moodysRating, String sandPRating, String fitchRating, Integer orderNumber);
}
