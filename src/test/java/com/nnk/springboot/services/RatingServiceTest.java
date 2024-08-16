package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    RatingRepository ratingRepository;

    @InjectMocks
    RatingService ratingService;

    @Test
    public void testCreateRating() {
        Rating rating = new Rating("MoodysTest", "SandPRatingTest", "FitchRatingTest", 1);
        when(ratingRepository.save(rating)).thenReturn(rating);
        Rating result = ratingService.createRating(rating);
        assert(result.equals(rating));
    }
    @Test
    public void testUpdateRatingSuccess() {
        String moodysTest = "MoodysTest";
        String sandPRatingTest = "SandPRatingTest";
        String fitchRatingTest = "FitchRatingTest";
        Integer orderNumberTest = 1;
        Integer idTest = 1;
        Rating rating = new Rating(moodysTest, sandPRatingTest, fitchRatingTest, orderNumberTest);
        rating.setId(idTest);
        doNothing().when(ratingRepository).updateRating(idTest, moodysTest, sandPRatingTest, fitchRatingTest, orderNumberTest);
        when(ratingRepository.existsById(idTest)).thenReturn(true);
        ratingService.updateRating(idTest, moodysTest, sandPRatingTest, fitchRatingTest, orderNumberTest);
        verify(ratingRepository, times(1)).updateRating(idTest, moodysTest, sandPRatingTest, fitchRatingTest, orderNumberTest);
    }

    @Test
    public void testUpdateRatingFail() {
        when(ratingRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(NullPointerException.class, () -> ratingService.updateRating(1, "moody", "sand", "fitch", 1));
        verify(ratingRepository, times(0)).updateRating(1, "moody", "sand", "fitch", 1);
    }

    @Test
    public void testDeleteRatingSuccess() {
        Integer idTest = 1;
        when(ratingRepository.existsById(idTest)).thenReturn(true);
        doNothing().when(ratingRepository).deleteById(idTest);
        ratingService.deleteRating(idTest);
        verify(ratingRepository, times(1)).deleteById(idTest);
    }

    @Test
    public void testDeleteRatingFail() {
        Integer idTest = 1;
        when(ratingRepository.existsById(idTest)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> ratingService.deleteRating(idTest));
        verify(ratingRepository, times(0)).deleteById(idTest);
    }
}
