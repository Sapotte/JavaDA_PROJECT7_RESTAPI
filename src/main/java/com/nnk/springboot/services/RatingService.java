package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating findRatingById(int id) {
        return ratingRepository.findById(id).orElseThrow(()-> new RuntimeException("Could not find rating point with id: " + id));
    }

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void updateRating(Integer id, String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        if(!ratingRepository.existsById(id)) {
            throw new RuntimeException("Could not find rating with id: " + id);
        }
        ratingRepository.updateRating(id, moodysRating, sandPRating, fitchRating, orderNumber);
    }

    public void deleteRating(Integer id) {
        if(!ratingRepository.existsById(id)) {
            throw new RuntimeException("Could not find rating with id: " + id);
        }
        ratingRepository.deleteById(id);
    }
}
