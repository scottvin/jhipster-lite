package tech.jhipster.lite.statistic.infrastructure.secondary;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;
import tech.jhipster.lite.module.domain.JHipsterModuleSlug;
import tech.jhipster.lite.shared.error.domain.Assert;
import tech.jhipster.lite.statistic.domain.*;
import tech.jhipster.lite.statistic.domain.criteria.StatisticsCriteria;

@Repository
@WithoutMongoDB
class InMemoryStatisticsRepository implements StatisticsRepository {

  private final Collection<AppliedModule> appliedModules = Collections.newSetFromMap(new ConcurrentHashMap<>());

  @Override
  public void save(AppliedModule moduleApplied) {
    Assert.notNull("moduleApplied", moduleApplied);

    appliedModules.add(moduleApplied);
  }

  @Override
  public Statistics get(StatisticsCriteria criteria) {
    Assert.notNull("criteria", criteria);

    long appliedModulesCount = appliedModules.size();
    if (criteria.isAnyCriteriaApplied()) {
      appliedModulesCount = appliedModules
        .stream()
        .filter(isAfter(criteria.startTime()))
        .filter(isBefore(criteria.endTime()))
        .filter(hasModuleSlug(criteria.moduleSlug()))
        .count();
    }
    return new Statistics(appliedModulesCount);
  }

  private static Predicate<AppliedModule> isAfter(Optional<Instant> startTime) {
    return appliedModule -> (startTime.isEmpty() || appliedModule.date().isAfter(startTime.get()));
  }

  private static Predicate<AppliedModule> isBefore(Optional<Instant> endTime) {
    return appliedModule -> (endTime.isEmpty() || appliedModule.date().isBefore(endTime.get()));
  }

  private static Predicate<AppliedModule> hasModuleSlug(Optional<JHipsterModuleSlug> moduleSlug) {
    return appliedModule -> moduleSlug.map(JHipsterModuleSlug::get).map(slug -> appliedModule.module().slug().equals(slug)).orElse(true);
  }

  void clear() {
    appliedModules.clear();
  }
}
