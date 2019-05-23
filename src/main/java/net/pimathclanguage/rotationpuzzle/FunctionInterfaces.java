package net.pimathclanguage.rotationpuzzle;

// Different interfaces for using of lambda functions (without or with return)
public class FunctionInterfaces {
  interface Function0 {
    void func();
  }

  interface Function1<T1> {
    void func(T1 v1);

  }

  interface Function2<T1, T2> {
    void func(T1 v1, T2 v2);
  }

  interface Function3<T1, T2, T3> {
    void func(T1 v1, T2 v2, T3 v3);
  }

  interface Function0R<R> {
    R func();
  }

  interface Function1R<T1, R> {
    R func(T1 v1);
  }

  interface Function2R<T1, T2, R> {
    R func(T1 v1, T2 v2);
  }

  interface Function3R<T1, T2, T3, R> {
    R func(T1 v1, T2 v2, T3 v3);
  }
}
