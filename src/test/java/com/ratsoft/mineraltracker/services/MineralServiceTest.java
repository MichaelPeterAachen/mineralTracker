package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.impl.MineralServiceImpl;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("DataFlowIssue")
@NoArgsConstructor
class MineralServiceTest {
    @Mock
    private @NonNull MineralRepository mineralRepository;

    private @NonNull MineralService mineralService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final MineralMapper mapper = Mappers.getMapper(MineralMapper.class);

        mineralService = new MineralServiceImpl(mineralRepository, mapper);
    }

    @Test
    public void getAllMinerals() {
        final Mineral mineral1 = new Mineral(1L, "IRON1", null);
        final Mineral mineral2 = new Mineral(2L, "SELEN1", null);

        final List<Mineral> mineralList = List.of(mineral1, mineral2);

        when(mineralRepository.findAll()).thenReturn(mineralList);

        final Set<Mineral> mineralResult = mineralService.getAllMinerals();

        assertThat(mineralResult).containsExactlyInAnyOrder(mineral1, mineral2);
    }

    @Test
    public void getMineralPresent() {
        final Mineral mineral2 = new Mineral(2L, "SELEN2", null);

        when(mineralRepository.findById(2L)).thenReturn(Optional.of(mineral2));

        final Optional<Mineral> mineralResult = mineralService.getMineral(2L);

        assertThat(mineralResult).isPresent()
                                 .contains(mineral2);
    }

    @Test
    public void getMineralNotPresent() {
        when(mineralRepository.findById(4L)).thenReturn(Optional.empty());

        final Optional<Mineral> mineralResult = mineralService.getMineral(4L);

        assertThat(mineralResult).isEmpty();
    }

    @Test
    public void saveMineral() {
        final Mineral mineralNew = new Mineral(null, "NEWMINERAL", null);
        final Mineral mineralNewSaved = new Mineral(3L, "NEWMINERAL", null);

        when(mineralRepository.save(mineralNew)).thenReturn(mineralNewSaved);

        final Mineral newMineral = new Mineral(null, "NEWMINERAL", null);

        final Mineral newMineralSaved = mineralService.saveMineral(newMineral);

        assertThat(newMineralSaved.getId()).isEqualTo(3L);
        assertThat(newMineralSaved.getName()).isEqualTo("NEWMINERAL");
    }

    @Test
    public void deleteMineral() {
        mineralService.deleteMineral(2L);
        verify(mineralRepository, times(1)).deleteById(2L);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void saveImageFile() throws IOException {
        final Long id = 1L;
        // TODO final MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring Framework Test".getBytes(StandardCharsets.UTF_8));

        final Mineral mineral = new Mineral(id, "TestMineral", null);
        final Optional<Mineral> mineralOptional = Optional.of(mineral);

        when(mineralRepository.findById(anyLong())).thenReturn(mineralOptional);

        final byte[] imageBytes = "Spring Framework Test".getBytes(StandardCharsets.UTF_8);

        final Byte[] imageDomain = transformImageForDomain(imageBytes);

        mineralService.saveImageFile(id, imageDomain);

        final ArgumentCaptor<Mineral> argumentCaptor = ArgumentCaptor.forClass(Mineral.class);

        verify(mineralRepository, times(1)).save(argumentCaptor.capture());

        final Mineral savedMineral = argumentCaptor.getValue();

        assertThat(imageDomain.length).isEqualTo(savedMineral.getImage().length);
        assertThat(imageDomain).isEqualTo(savedMineral.getImage());
    }

    private static @NonNull Byte @NonNull [] transformImageForDomain(final @NonNull byte @NonNull [] imageBytes) throws IOException {
        final Byte[] imageByteObject = new Byte[imageBytes.length];
        for (int i = 0; i < imageBytes.length; i++) {
            imageByteObject[i] = imageBytes[i];
        }
        return imageByteObject;
    }
}