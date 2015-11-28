package de.bitkings.jbrotli;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliErrorCheckerTest {

  @Test
  public void error_codes_which_are_positive_are_OK() throws Exception {
    assertThat(BrotliErrorChecker.isBrotliOk(-1)).isFalse();
    assertThat(BrotliErrorChecker.isBrotliOk(0)).isTrue();
    assertThat(BrotliErrorChecker.isBrotliOk(1)).isTrue();
  }

  @Test(expectedExceptions = BrotliException.class)
  public void when_valid_error_exception_is_thrown() throws Exception {

    BrotliErrorChecker.assertBrotliOk(-1);

    // expect UncheckedIOException
  }

  @Test
  public void when_no_error_nothing_happens() throws Exception {

    BrotliErrorChecker.assertBrotliOk(0);
    BrotliErrorChecker.assertBrotliOk(1);

    // expect nothing happens
  }

  @Test
  public void assertBroltiOk_returns_the_error_code_if_all_ok() throws Exception {
    int actual = BrotliErrorChecker.assertBrotliOk(42_42);

    assertThat(actual).isEqualTo(42_42);
  }
}