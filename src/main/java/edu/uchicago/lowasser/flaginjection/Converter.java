package edu.uchicago.lowasser.flaginjection;

public interface Converter<T> {
  T parse(String string);
}
