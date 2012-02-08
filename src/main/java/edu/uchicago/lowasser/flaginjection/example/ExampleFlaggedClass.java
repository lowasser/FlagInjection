/**
 * Copyright 2012 Louis Wasserman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uchicago.lowasser.flaginjection.example;

import com.google.common.base.Objects;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.Flags;

/**
 * An example of flag injection.
 * 
 * @author Louis Wasserman
 */
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
