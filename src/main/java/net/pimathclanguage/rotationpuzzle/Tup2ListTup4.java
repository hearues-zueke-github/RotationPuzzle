package net.pimathclanguage.rotationpuzzle;

import lombok.Data;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.HashMap;

@Data
class Tup2ListTup4 {
  @Delegate //lombok
  public HashMap<Tuple.Tuple2<Integer, Integer>, ArrayList<Tuple.Tuple4<Integer, Integer, Integer, Boolean>>> v;

  Tup2ListTup4() {
    v = new HashMap<>();
  }
}
