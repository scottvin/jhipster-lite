package tech.jhipster.lite.statistic.domain.criteria;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.time.Instant;
import java.util.Optional;
import org.apache.commons.lang3.builder.ToStringBuilder;
import tech.jhipster.lite.module.domain.JHipsterModuleSlug;

/**
 * Criteria class for {@link tech.jhipster.lite.statistic.domain.Statistics}.
 */
public final class StatisticsCriteria {

  private final Optional<Instant> startTime;
  private final Optional<Instant> endTime;
  private final Optional<JHipsterModuleSlug> moduleSlug;

  private StatisticsCriteria(StatisticsCriteriaBuilder builder) {
    this.startTime = Optional.ofNullable(builder.startTime);
    this.endTime = Optional.ofNullable(builder.endTime);
    this.moduleSlug = Optional.ofNullable(builder.moduleSlug).map(JHipsterModuleSlug::new);
  }

  public boolean isAnyCriteriaApplied() {
    return this.startTime.isPresent() || this.endTime.isPresent() || this.moduleSlug.isPresent();
  }

  public static StatisticsCriteriaBuilder builder() {
    return new StatisticsCriteriaBuilder();
  }

  public Optional<Instant> startTime() {
    return startTime;
  }

  public Optional<Instant> endTime() {
    return endTime;
  }

  public Optional<JHipsterModuleSlug> moduleSlug() {
    return moduleSlug;
  }

  public static class StatisticsCriteriaBuilder {

    private Instant startTime;
    private Instant endTime;
    private String moduleSlug;

    public StatisticsCriteriaBuilder startTime(Instant startTime) {
      this.startTime = startTime;
      return this;
    }

    public StatisticsCriteriaBuilder endTime(Instant endTime) {
      this.endTime = endTime;
      return this;
    }

    public StatisticsCriteriaBuilder moduleSlug(String moduleSlug) {
      this.moduleSlug = moduleSlug;
      return this;
    }

    public StatisticsCriteria build() {
      return new StatisticsCriteria(this);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
      .append("startTime", startTime.map(Instant::toString).orElse(""))
      .append("endTime", endTime.map(Instant::toString).orElse(""))
      .append("moduleSlug", moduleSlug.map(JHipsterModuleSlug::get).orElse(""))
      .toString();
  }
}
