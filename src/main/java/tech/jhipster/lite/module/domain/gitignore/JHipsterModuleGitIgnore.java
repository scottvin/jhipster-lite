package tech.jhipster.lite.module.domain.gitignore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import tech.jhipster.lite.module.domain.JHipsterModule.JHipsterModuleBuilder;
import tech.jhipster.lite.module.domain.gitignore.GitIgnoreEntry.GitIgnoreComment;
import tech.jhipster.lite.module.domain.gitignore.GitIgnoreEntry.GitIgnorePattern;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterModuleGitIgnore {

  private final Collection<GitIgnoreEntry> entries;

  private JHipsterModuleGitIgnore(Collection<GitIgnoreEntry> entries) {
    Assert.field("entries", entries).notNull().noNullElement();
    this.entries = entries;
  }

  public void forEach(Consumer<GitIgnoreEntry> consumer) {
    Assert.notNull("consumer", consumer);

    entries.forEach(consumer);
  }

  public boolean isNotEmpty() {
    return !entries.isEmpty();
  }

  @Override
  public String toString() {
    return entries.toString();
  }

  public static JHipsterModuleGitIgnoreBuilder builder(JHipsterModuleBuilder parentModuleBuilder) {
    return new JHipsterModuleGitIgnoreBuilder(parentModuleBuilder);
  }

  public static final class JHipsterModuleGitIgnoreBuilder {

    private final JHipsterModuleBuilder parentModuleBuilder;
    private final Collection<GitIgnoreEntry> entries = new ArrayList<>();

    private JHipsterModuleGitIgnoreBuilder(JHipsterModuleBuilder parentModuleBuilder) {
      Assert.notNull("module", parentModuleBuilder);

      this.parentModuleBuilder = parentModuleBuilder;
    }

    /**
     * Declare a pattern to be added to the {@code .gitignore} file.
     */
    public JHipsterModuleGitIgnoreBuilder pattern(GitIgnorePattern pattern) {
      Assert.notNull("pattern", pattern);
      entries.add(pattern);

      return this;
    }

    /**
     * Declare a pattern to be added to the {@code .gitignore} file.
     */
    public JHipsterModuleGitIgnoreBuilder pattern(String pattern) {
      return pattern(new GitIgnorePattern(pattern));
    }

    /**
     * Declare a comment to be added to the {@code .gitignore} file.
     */
    public JHipsterModuleGitIgnoreBuilder comment(GitIgnoreComment comment) {
      Assert.notNull("comment", comment);
      entries.add(comment);

      return this;
    }

    /**
     * Declare a comment to be added to the {@code .gitignore} file.
     */
    public JHipsterModuleGitIgnoreBuilder comment(String comment) {
      return comment(new GitIgnoreComment(comment));
    }

    public JHipsterModuleBuilder and() {
      return parentModuleBuilder;
    }

    public JHipsterModuleGitIgnore build() {
      return new JHipsterModuleGitIgnore(entries);
    }
  }
}
