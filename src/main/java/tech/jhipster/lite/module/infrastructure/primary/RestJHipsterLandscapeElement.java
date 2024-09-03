package tech.jhipster.lite.module.infrastructure.primary;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.jhipster.lite.module.domain.landscape.JHipsterLandscapeElement;
import tech.jhipster.lite.module.domain.landscape.JHipsterLandscapeElementType;
import tech.jhipster.lite.module.domain.landscape.JHipsterLandscapeFeature;
import tech.jhipster.lite.module.domain.landscape.JHipsterLandscapeModule;

@Schema(name = "JHipsterLandscapeElement", description = "An element in a landscape, can be either a module or a feature")
sealed interface RestJHipsterLandscapeElement permits RestJHipsterLandscapeModule, RestJHipsterLandscapeFeature {
  JHipsterLandscapeElementType getType();

  static RestJHipsterLandscapeElement from(JHipsterLandscapeElement element) {
    return switch (element) {
      case JHipsterLandscapeModule module -> RestJHipsterLandscapeModule.fromModule(module);
      case JHipsterLandscapeFeature feature -> RestJHipsterLandscapeFeature.fromFeature(feature);
    };
  }
}
