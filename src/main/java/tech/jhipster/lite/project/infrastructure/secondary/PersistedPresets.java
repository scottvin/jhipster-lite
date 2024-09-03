package tech.jhipster.lite.project.infrastructure.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import tech.jhipster.lite.project.domain.preset.Preset;

record PersistedPresets(@JsonProperty("presets") Collection<PersistedPreset> presets) {
  public Collection<Preset> toDomain() {
    return presets.stream().map(PersistedPreset::toDomain).toList();
  }
}
