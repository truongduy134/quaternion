package com.nus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuaternionTest {

  public static final double EPSILON = 0.0000000001;

  public static void assertQuaternionEquals(Quaternion p, Quaternion q) {
    assertEquals(p.getX(), q.getX(), EPSILON);
    assertEquals(p.getY(), q.getY(), EPSILON);
    assertEquals(p.getZ(), q.getZ(), EPSILON);
    assertEquals(p.getW(), q.getW(), EPSILON);
  }

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testDefaultConstructor() {
    Quaternion q = new Quaternion();
    assertEquals(q.getX(), 0.0, EPSILON);
    assertEquals(q.getY(), 0.0, EPSILON);
    assertEquals(q.getZ(), 0.0, EPSILON);
    assertEquals(q.getW(), 1.0, EPSILON);
  }

  @Test
  public void testFullConstructor() {
    Quaternion q = new Quaternion(1.23, 5.12, 124, -1231.0);
    assertEquals(q.getX(), 1.23, EPSILON);
    assertEquals(q.getY(), 5.12, EPSILON);
    assertEquals(q.getZ(), 124.0, EPSILON);
    assertEquals(q.getW(), -1231.0, EPSILON);
  }

  @Test
  public void testCopyConstructor() {
    Quaternion p = new Quaternion(1.2, 1.3, 0.4, 0.5);
    Quaternion q = new Quaternion(p);
    assertEquals(q.getX(), 1.2, EPSILON);
    assertEquals(q.getY(), 1.3, EPSILON);
    assertEquals(q.getZ(), 0.4, EPSILON);
    assertEquals(q.getW(), 0.5, EPSILON);
  }

  @Test
  public void testNorm() {
    Quaternion q = new Quaternion(1.0, 1.0, 1.0, 1.0);
    assertEquals(q.norm(), 2.0, EPSILON);

    q = new Quaternion(2.0, -1.5, 7.123, 0.1205);
    assertEquals(q.norm(), 7.549943658730176, EPSILON);
  }

  @Test
  public void testSquaredNorm() {
    Quaternion q = new Quaternion(1.0, 1.0, 1.0, 1.0);
    assertEquals(q.squaredNorm(), 4.0, EPSILON);

    q = new Quaternion(2.0, -1.5, 7.123, 0.1205);
    assertEquals(q.squaredNorm(), 57.00164925, EPSILON);
  }

  @Test
  public void testNormalize() {
    Quaternion q = new Quaternion(0.0, 0.0, 1.0, 0.0);
    q.normalize();
    assertEquals(q.squaredNorm(), 1.0, EPSILON);

    q = new Quaternion(2.0, 3.0, 4.0, -5.0);
    q.normalize();
    assertEquals(q.squaredNorm(), 1.0, EPSILON);

    q = new Quaternion(-123.87, 1231.0, 0.0, -0.0001);
    q.toUnit();
    assertEquals(q.squaredNorm(), 1.0, EPSILON);
  }

  @Test
  public void testConjugate() {
    Quaternion q = new Quaternion(0.0, 1.23, 5.12, -10);
    Quaternion expectQ = new Quaternion(0.0, -1.23, -5.12, -10);
    assertQuaternionEquals(q.conjugate(), expectQ);

    q = new Quaternion(-10.99999, 12.001, -11, 23.59);
    expectQ = new Quaternion(10.99999, -12.001, 11, 23.59);
    assertQuaternionEquals(q.conjugate(), expectQ);

    q = new Quaternion(11.11, 22.22, 0.00000001, 1000);
    expectQ = new Quaternion(-11.11, -22.22, -0.00000001, 1000);
    q.conjugateEq();
    assertQuaternionEquals(q, expectQ);
  }

  @Test
  public void testAdd() {
    Quaternion p = new Quaternion(0.0, 1.10, -2.22, 50.123);
    Quaternion q = new Quaternion(-12.0, 5.95, 8.6, -23);
    Quaternion expectSum = new Quaternion(-12.0, 7.05, 6.38, 27.123);

    assertQuaternionEquals(p.add(q), expectSum);
    assertQuaternionEquals(q.add(p), expectSum);

    p.addEq(q);
    assertQuaternionEquals(p, expectSum);
  }
}