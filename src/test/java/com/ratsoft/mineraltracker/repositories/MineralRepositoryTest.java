package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Mineral;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the mineral repository.
 *
 * @author mpeter
 */
@SuppressWarnings("PackageVisibleField")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@NoArgsConstructor
class MineralRepositoryTest {

    /**
     * Repository to access the minerals.
     */
    @Autowired
    @NonNull
    MineralRepository mineralRepository;

    /**
     * Test the find by id method of the repository.
     */
    @Test
    public void testFindById() {
        final Optional<Mineral> mineralOptional = mineralRepository.findById(1L);
        assertThat(mineralOptional).isPresent();

        final Mineral mineral = mineralOptional.get();

        assertThat(mineral.getId()).isEqualTo(1L);
        assertThat(mineral.getName()).isEqualTo("Eisen");
    }
}