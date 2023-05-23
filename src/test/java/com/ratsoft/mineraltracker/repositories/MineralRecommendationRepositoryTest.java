package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the mineral recommendation repository.
 *
 * @author mpeter
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class MineralRecommendationRepositoryTest {

    @Autowired
    MineralRecommendationRepository mineralRecommendationRepository;

    /**
     * Test the find by id method of the repository.
     */
    @Test
    public void testFindById() {
        final Optional<MineralRecommendation> mineralRecommendationOptional = mineralRecommendationRepository.findById(1L);
        assertThat(mineralRecommendationOptional).isPresent();

        final MineralRecommendation mineralRecommendation = mineralRecommendationOptional.get();

        assertThat(mineralRecommendation.getId()).isEqualTo(1L);
        assertThat(mineralRecommendation.getMineral().getName()).isEqualTo("Eisen");
        assertThat(mineralRecommendation.getMinAmount()).isEqualTo(1.0f);
        assertThat(mineralRecommendation.getMaxAmount()).isEqualTo(11.0f);
        assertThat(mineralRecommendation.getTimePeriodLength()).isEqualTo(1);
        assertThat(mineralRecommendation.getTimePeriodDimension()).isEqualTo(RecommendationPeriodType.HOURS);
        assertThat(mineralRecommendation.getUnit()).isEqualTo(Unit.mg);
    }
}