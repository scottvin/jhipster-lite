package tech.jhipster.lite.project.domain;

import java.util.Collection;
import tech.jhipster.lite.shared.error.domain.Assert;

public record ModulesSlugs(Collection<ModuleSlug> modules) {
  public ModulesSlugs {
    Assert.notEmpty("modules", modules);
  }

  public static ModulesSlugs from(Collection<String> modules) {
    return new ModulesSlugs(modules.stream().map(ModuleSlug::new).toList());
  }
}
