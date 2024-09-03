package tech.jhipster.lite.project.domain.preset;

import tech.jhipster.lite.project.domain.ModulesSlugs;
import tech.jhipster.lite.shared.error.domain.Assert;

public record Preset(PresetName name, ModulesSlugs modules) {
  public Preset {
    Assert.notNull("name", name);
    Assert.notNull("modules", modules);
  }
}
