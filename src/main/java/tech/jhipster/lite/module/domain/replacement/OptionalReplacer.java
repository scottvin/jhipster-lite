package tech.jhipster.lite.module.domain.replacement;

import tech.jhipster.lite.shared.error.domain.Assert;

public record OptionalReplacer(ElementReplacer replacer, String updatedValue) {
  public OptionalReplacer {
    Assert.notNull("replacer", replacer);
    Assert.notNull("updatedValue", updatedValue);
  }

  public String apply(String content) {
    if (replacer.dontNeedReplacement(content, updatedValue())) {
      return content;
    }

    return replacer().replacement().apply(content, updatedValue());
  }
}
