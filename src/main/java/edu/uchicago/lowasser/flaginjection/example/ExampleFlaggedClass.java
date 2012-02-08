package edu.uchicago.lowasser.flaginjection.example;

import com.google.common.base.Objects;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.Flags;

public class ExampleFlaggedClass {
  private final int flaggedInteger;

  @Inject(optional = true)
  @Flag(
      name = "stringWithDefault",
      optional = true,
      description = "If this is omitted from the command line, it isn't injected and "
          + "stays on the default value")
  private String stringWithDefault = "default";

  @Inject
  ExampleFlaggedClass(
      @Flag(name = "flaggedInteger", description = "Required integer-valued flag") int flaggedInteger) {
    this.flaggedInteger = flaggedInteger;
  }

  @Override
  public String toString() {
    return Objects
        .toStringHelper(this)
        .add("flaggedInteger", flaggedInteger)
        .add("stringWithDefault", stringWithDefault)
        .toString();
  }

  public static void main(String[] args) {
    Injector injector = Flags.bootstrapFlagInjector(
        args,
        Flags.flagBindings(ExampleFlaggedClass.class),
        new AbstractModule() {

          @Override
          protected void configure() {
          }

          @Provides
          @Named("main")
          public String mainName() {
            // To print help messages, Apache CLI needs the name of the main class.
            return "ExampleFlaggedClass";
          }
        });
    ExampleFlaggedClass example = injector.getInstance(ExampleFlaggedClass.class);
    System.out.println(example);
  }
}
