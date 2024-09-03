package tech.jhipster.lite.project.infrastructure.secondary;

import java.util.Collection;
import tech.jhipster.lite.project.domain.ModulesSlugs;
import tech.jhipster.lite.project.domain.preset.Preset;
import tech.jhipster.lite.project.domain.preset.PresetName;

record PersistedPreset(String name, Collection<String> modules) {
  public Preset toDomain() {
    return new Preset(PresetName.from(name), ModulesSlugs.from(modules));
  }
}
