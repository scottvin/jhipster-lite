package tech.jhipster.lite.statistic.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug;
import tech.jhipster.lite.statistic.domain.AppliedModule;
import tech.jhipster.lite.statistic.domain.AppliedModuleFixture;
import tech.jhipster.lite.statistic.domain.criteria.StatisticsCriteria;

@UnitTest
class InMemoryStatisticsRepositoryTest {

  private final InMemoryStatisticsRepository inMemoryStatisticsRepository = new InMemoryStatisticsRepository();

  static Stream<StatisticsCriteria> provideFilterCriteria() {
    return Stream.of(
      StatisticsCriteria.builder().startTime(null).endTime(null).moduleSlug(JHLiteModuleSlug.ANGULAR_CORE.get()).build(),
      StatisticsCriteria.builder().startTime(null).endTime(Instant.parse("2021-12-04T10:15:30.00Z")).moduleSlug(null).build(),
      StatisticsCriteria.builder().startTime(null).endTime(Instant.parse("2021-12-04T10:15:30.00Z")).moduleSlug(null).build()
    );
  }

  static Stream<StatisticsCriteria> provideFalseCriteria() {
    return Stream.of(
      StatisticsCriteria.builder()
        .startTime(Instant.parse("2020-12-03T10:15:30.00Z"))
        .endTime(Instant.parse("2022-12-03T10:15:30.00Z"))
        .moduleSlug(JHLiteModuleSlug.ANGULAR_CORE.get())
        .build(),
      StatisticsCriteria.builder()
        .startTime(Instant.parse("2022-12-03T10:15:30.00Z"))
        .endTime(Instant.parse("2022-12-03T10:15:30.00Z"))
        .moduleSlug(null)
        .build(),
      StatisticsCriteria.builder()
        .startTime(Instant.parse("2020-12-03T10:15:30.00Z"))
        .endTime(Instant.parse("2020-12-03T10:15:30.00Z"))
        .moduleSlug(null)
        .build()
    );
  }

  @BeforeEach
  void beforeEach() {
    inMemoryStatisticsRepository.clear();
  }

  @Test
  void shouldGetZeroForNullCriteria() {
    assertThat(inMemoryStatisticsRepository.get(StatisticsCriteria.builder().build()).appliedModules()).isZero();
  }

  @ParameterizedTest
  @MethodSource("provideFilterCriteria")
  void shouldGetOneForEachFilteredCriteria(StatisticsCriteria criteria) {
    AppliedModule appliedModule = AppliedModuleFixture.appliedModule(JHLiteModuleSlug.ANGULAR_CORE.get());
    inMemoryStatisticsRepository.save(appliedModule);

    assertThat(inMemoryStatisticsRepository.get(criteria).appliedModules()).isEqualTo(1);
  }

  @ParameterizedTest
  @MethodSource("provideFalseCriteria")
  void shouldGetZeroForFalseCriteria(StatisticsCriteria criteria) {
    AppliedModule appliedModule = AppliedModuleFixture.appliedModule();

    inMemoryStatisticsRepository.save(appliedModule);

    assertThat(inMemoryStatisticsRepository.get(criteria).appliedModules()).isZero();
  }
}
