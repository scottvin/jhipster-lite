package tech.jhipster.lite.project.infrastructure.primary;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import tech.jhipster.lite.project.domain.preset.Preset;

@Schema(name = "Presets", description = "Information on the predefined configurations")
record RestPresets(Collection<RestPreset> presets) {
  public static RestPresets from(Collection<Preset> presets) {
    return new RestPresets(presets.stream().map(RestPreset::from).toList());
  }
}
