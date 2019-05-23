package net.pimathclanguage.rotationpuzzle;

import lombok.Data;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.HashMap;

import net.pimathclanguage.rotationpuzzle.Tuple.Tuple2;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple4;

class DataClasses {
  @Data
  public static final class HashTup2ListTup4 {
    @Delegate //lombok
    public HashMap<Tuple2<Integer, Integer>, ArrayList<Tuple4<Integer, Integer, Integer, Boolean>>> v;

    HashTup2ListTup4() {
      v = new HashMap<>();
    }
  }

  @Data
  public static final class HashIntHashTup2ListTup4 {
    @Delegate //lombok
    public HashMap<Integer, HashTup2ListTup4> v;

    HashIntHashTup2ListTup4() {
      v = new HashMap<>();
    }
  }

  @Data
  public static final class HashTup2HashIntHashTup2ListTup4 {
    @Delegate //lombok
    public HashMap<Tuple2<Integer, Integer>, HashIntHashTup2ListTup4> v;

    HashTup2HashIntHashTup2ListTup4() {
      v = new HashMap<>();
    }
  }
}
