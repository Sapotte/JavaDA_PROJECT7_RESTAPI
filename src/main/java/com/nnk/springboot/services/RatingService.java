package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private static final Logger LOG = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    RatingRepository ratingRepository;

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void updateRating(Integer id, String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        if(!ratingRepository.existsById(id)) {
            LOG.error("Update rating failed");
            throw new RuntimeException("Could not find rating with id: " + id);
        }
        ratingRepository.updateRating(id, moodysRating, sandPRating, fitchRating, orderNumber);
        LOG.info("Rating with id " + id + " has been updated");
    }

    public void deleteRating(Integer id) {
        if(!ratingRepository.existsById(id)) {
            LOG.error("Delete rating failed");
            throw new RuntimeException("Could not find rating with id: " + id);
        }
        ratingRepository.deleteById(id);
        LOG.info("Rating with id " + id + " has been deleted");
    }
}
