package tech.jhipster.lite.project.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.JsonHelper;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.project.domain.ModuleSlug;
import tech.jhipster.lite.project.domain.ModulesSlugs;
import tech.jhipster.lite.project.domain.preset.Preset;
import tech.jhipster.lite.project.domain.preset.PresetName;

@UnitTest
class RestPresetsTest {

  @Test
  void shouldSerializeToJson() {
    assertThat(JsonHelper.writeAsString(RestPresets.from(presets()))).isEqualTo(json());
  }

  private static Collection<Preset> presets() {
    return List.of(
      new Preset(
        new PresetName("test preset one"),
        new ModulesSlugs(List.of(new ModuleSlug("test-module-one"), new ModuleSlug("test-module-two")))
      ),
      new Preset(
        new PresetName("test preset two"),
        new ModulesSlugs(List.of(new ModuleSlug("test-module-three"), new ModuleSlug("test-module-four")))
      )
    );
  }

  private static String json() {
    return """
    {\
    "presets":[\
    {\
    "name":"test preset one",\
    "modules":[\
    {"slug":"test-module-one"},\
    {"slug":"test-module-two"}\
    ]\
    },\
    {\
    "name":"test preset two",\
    "modules":[\
    {"slug":"test-module-three"},\
    {"slug":"test-module-four"}\
    ]\
    }\
    ]\
    }\
    """;
  }
}
