package tech.jhipster.lite.generator.buildtool.gradle.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModule;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class GradleModuleFactoryTest {

  private static final GradleModuleFactory factory = new GradleModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myApp")
      .build();

    JHipsterModule module = factory.buildGradleModule(properties);

    assertThatModule(module)
      .hasFile(".gitignore")
      .containing(
        """
        # Gradle
        /.gradle/
        /build/
        ./buildSrc/.gradle/
        ./buildSrc/build/\
        """
      )
      .and()
      .hasFile("build.gradle.kts")
      .containing("group = \"tech.jhipster.jhlitest\"")
      .containing("testImplementation(libs.junit.engine)")
      .containing("testImplementation(libs.junit.params)")
      .containing("testImplementation(libs.assertj)")
      .containing("testImplementation(libs.mockito)")
      .and()
      .hasFile("settings.gradle.kts")
      .containing("my-app")
      .and()
      .hasFile("gradle/libs.versions.toml")
      .containing("junit-jupiter = \"")
      .containing("assertj = \"")
      .containing("mockito = \"")
      .containing("[libraries.junit-engine]")
      .containing("[libraries.junit-params]")
      .containing("[libraries.assertj]")
      .containing("[libraries.mockito]");
  }

  @Test
  void shouldBuildGradleWrapperModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

    JHipsterModule module = factory.buildGradleWrapperModule(properties);

    assertThatModuleWithFiles(module)
      .hasFile(".gitignore")
      .containing(
        """
        # Gradle Wrapper
        !gradle/wrapper/gradle-wrapper.jar\
        """
      )
      .and()
      .hasExecutableFiles("gradlew", "gradlew.bat")
      .hasPrefixedFiles("gradle/wrapper", "gradle-wrapper.jar", "gradle-wrapper.properties");
  }
}
