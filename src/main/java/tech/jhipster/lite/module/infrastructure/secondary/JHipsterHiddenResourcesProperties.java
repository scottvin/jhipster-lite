package tech.jhipster.lite.module.infrastructure.secondary;

import java.util.Collection;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jhlite-hidden-resources")
class JHipsterHiddenResourcesProperties {

  private Collection<String> slugs;
  private Collection<String> tags;

  public void setSlugs(Collection<String> slugs) {
    this.slugs = slugs;
  }

  public void setTags(Collection<String> tags) {
    this.tags = tags;
  }

  public Collection<String> getSlugs() {
    return slugs;
  }

  public Collection<String> getTags() {
    return tags;
  }
}
