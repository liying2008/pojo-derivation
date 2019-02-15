# pojo-derivation

[![bintray](https://api.bintray.com/packages/liying2008/util/pojo-derivation-annotations/images/download.svg)](https://bintray.com/liying2008/util/pojo-derivation-annotations/_latestVersion)
[![maven-central](https://img.shields.io/maven-central/v/cc.duduhuo.util/pojo-derivation-annotations.svg?style=flat)](https://mvnrepository.com/artifact/cc.duduhuo.util/pojo-derivation-annotations)
[![license](https://img.shields.io/github/license/liying2008/pojo-derivation.svg?style=flat)](https://github.com/liying2008/pojo-derivation/blob/master/LICENSE)

pojos are combined to derive other pojos.

## 1 Setup

+ Kotlin Project

```gradle
apply plugin: 'kotlin-kapt'

dependencies {
    compileOnly('cc.duduhuo.util:pojo-derivation-annotations:1.0.0.beta7')
    kapt('cc.duduhuo.util:pojo-derivation-compiler:1.0.0.beta7')
}
```

+ Pure Java Project

```gradle
dependencies {
    compileOnly('cc.duduhuo.util:pojo-derivation-annotations:1.0.0.beta7')
    annotationProcessor('cc.duduhuo.util:pojo-derivation-compiler:1.0.0.beta7')
}
```

## 2 Use

You can use the Derivation annotation on any class:

```java
@Derivation(
        name = "ABC",
        sourceTypes = {A.class, B.class, C.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS}
)
class ABCCombine {}
```

Then you get a class like this:

```java
// This file is automatically generated by pojo-derivation (https://github.com/liying2008/pojo-derivation).
// Do not modify this file -- YOUR CHANGES WILL BE ERASED!
// File generation time: Fri Feb 15 20:32:07 CST 2019
package cc.duduhuo.util.pojo.derivation.sample.readme;

import java.lang.String;
import org.jetbrains.annotations.NotNull;

/**
 * Generated according to {@link cc.duduhuo.util.pojo.derivation.sample.readme.ABCCombine}.
 */
public class ABC {
  private int a1;

  private String a2;

  private boolean b1;

  private double b2;

  private boolean c1;

  private char c2;

  @NotNull
  private String c3;

  public ABC() {
  }

  public ABC(int a1, String a2, boolean b1, double b2, boolean c1, char c2, String c3) {
    this.a1 = a1;
    this.a2 = a2;
    this.b1 = b1;
    this.b2 = b2;
    this.c1 = c1;
    this.c2 = c2;
    this.c3 = c3;
  }

  public ABC(A a, B b, C c) {
    this.setA1(a.getA1());
    this.setA2(a.getA2());
    this.setB1(b.isB1());
    this.setB2(b.getB2());
    this.setC1(c.getC1());
    this.setC2(c.getC2());
    this.setC3(c.getC3());
  }

  public int getA1() {
    return a1;
  }

  public void setA1(int a1) {
    this.a1 = a1;
  }

  public String getA2() {
    return a2;
  }

  public void setA2(String a2) {
    this.a2 = a2;
  }

  public boolean isB1() {
    return b1;
  }

  public void setB1(boolean b1) {
    this.b1 = b1;
  }

  public double getB2() {
    return b2;
  }

  public void setB2(double b2) {
    this.b2 = b2;
  }

  public boolean isC1() {
    return c1;
  }

  public void setC1(boolean c1) {
    this.c1 = c1;
  }

  public char getC2() {
    return c2;
  }

  public void setC2(char c2) {
    this.c2 = c2;
  }

  public String getC3() {
    return c3;
  }

  public void setC3(String c3) {
    this.c3 = c3;
  }
}
```

See `sample` module for more examples.

## 3 License

[MIT](LICENSE)
