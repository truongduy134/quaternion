package com.nus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
  public void testTrivialGetters() {
    Quaternion p = new Quaternion(-1.0, 2.99, 3.0, 4.5);
    double[] expectedVector = new double[] {-1.0, 2.99, 3.0};

    assertEquals(p.getX(), -1.0, EPSILON);
    assertEquals(p.getY(), 2.99, EPSILON);
    assertEquals(p.getZ(), 3.0, EPSILON);
    assertEquals(p.getW(), 4.5, EPSILON);
    assertEquals(p.getScalarPart(), 4.5, EPSILON);
    assertArrayEquals(p.getVectorPart(), expectedVector, EPSILON);
  }

  @Test
  public void testGetIdentity() {
    Quaternion identity = new Quaternion(0.0, 0.0, 0.0, 1.0);
    assertQuaternionEquals(Quaternion.getIdentity(), identity);
  }

  @Test
  public void testIsIdentity() {
    Quaternion identity = new Quaternion();
    assertTrue(identity.isIdentity());

    Quaternion p = new Quaternion(0.0, 1.0, 0.0, 1.0);
    assertFalse(p.isIdentity());

    p = new Quaternion(0.0, 0.0, 0.0, 0.0);
    assertFalse(p.isIdentity());

    p = new Quaternion(0.1, 0.1, 0.0, -0.00001);
    assertFalse(p.isIdentity());
  }

  @Test
  public void testIsUnit() {
    Quaternion identity = new Quaternion();
    assertTrue(identity.isUnit());

    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 0.0);
    assertTrue(q.isUnit());

    q = new Quaternion(0.5, 0.5, 0.5, 0.5);
    assertTrue(q.isUnit());

    q = new Quaternion(0.16666667, 0.3333333, 0.5, 0.666666668);
    assertFalse(q.isUnit());
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

  @Test
  public void testMultiple() {
    Quaternion p = new Quaternion(0.0, 1.0, 0.0, 1.0);
    Quaternion q = new Quaternion(0.5, 0.5, 0.75, 1.0);
    Quaternion expectProd = new Quaternion(1.25, 1.5, 0.25, 0.5);

    assertQuaternionEquals(p.multiply(q), expectProd);

    expectProd = new Quaternion(-0.25, 1.5, 1.25, 0.5);
    assertQuaternionEquals(q.multiply(p), expectProd);

    Quaternion expectSelfProd = new Quaternion(0.0, 2.0, 0.0, 0.0);
    p.multiplyEq(p);
    assertQuaternionEquals(p, expectSelfProd);

    expectSelfProd = new Quaternion(1.0, 1.0, 1.5, -0.0625);
    q.multiplyEq(q);
    assertQuaternionEquals(q, expectSelfProd);
  }

  @Test
  public void testScalarMultiple() {
    Quaternion p = new Quaternion(1.0, 2.0, 3.0, 4.0);
    double scalar = 0.5;
    Quaternion expected = new Quaternion(0.5, 1.0, 1.5, 2.0);
    assertQuaternionEquals(p.multiply(scalar), expected);

    p.multiplyEq(scalar);
    assertQuaternionEquals(p, expected);
  }

  @Test
  public void testInverse() {
    Quaternion p = new Quaternion(0.0, 1.0, 0.0, 1.0);
    Quaternion expectedInverse = new Quaternion(0.0, -0.5, 0.0, 0.5);

    assertQuaternionEquals(p.inverse(), expectedInverse);

    p = new Quaternion(2.0, -1.0, -3.0, 0.0);
    expectedInverse = new Quaternion(-1.0 / 7.0, 1.0 / 14.0, 3.0 / 14.0, 0.0);

    assertQuaternionEquals(p.inverse(), expectedInverse);
  }

  @Test
  public void testInvert() {
    Quaternion p = new Quaternion(0.0, 1.0, 0.0, 1.0);
    Quaternion expectedInverse = new Quaternion(0.0, -0.5, 0.0, 0.5);

    p.invert();
    assertQuaternionEquals(p, expectedInverse);

    p = new Quaternion(2.0, -1.0, -3.0, 0.0);
    expectedInverse = new Quaternion(-1.0 / 7.0, 1.0 / 14.0, 3.0 / 14.0, 0.0);

    p.inverseEq();
    assertQuaternionEquals(p, expectedInverse);
  }

  @Test
  public void testDivide() {
    Quaternion p = new Quaternion(0.5, 0.5, 0.75, 1.0);
    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 1.0);
    Quaternion expectDiv = new Quaternion(0.625, -0.25, 0.125, 0.75);

    assertQuaternionEquals(p.divide(q), expectDiv);

    p.divideEq(q);
    assertQuaternionEquals(p, expectDiv);

    expectDiv = new Quaternion(0.0, 0.0, 0.0, 1.0);
    q.divideEq(q);
    assertQuaternionEquals(q, expectDiv);
  }

  @Test
  public void testExp() {
    Quaternion zeroQ = new Quaternion(0.0, 0.0, 0.0, 0.0);
    Quaternion identity = new Quaternion();
    assertQuaternionEquals(zeroQ.exp(), identity);

    Quaternion q = new Quaternion(1.0, 1.0, 1.0, 0.0);
    Quaternion expectedQ = new Quaternion(
      0.5698600991825139, 0.5698600991825139, 0.5698600991825139,
      -0.16055653857469054
    );
    assertQuaternionEquals(q.exp(), expectedQ);

    q = new Quaternion(1.0, 1.0, 1.0, 1.0);
    expectedQ = new Quaternion(
      1.5490403523716967, 1.5490403523716967,
      1.5490403523716967, -0.436437921247865);
    assertQuaternionEquals(q.exp(), expectedQ);
  }

  @Test(expected = ArithmeticException.class)
  public void testLogWithZeroQuaternion() {
    Quaternion zero = new Quaternion(0.0, 0.0, 0.0, 0.0);
    zero.log();
  }

  @Test
  public void testLogWithNonZeroQuaternion() throws Exception {
    Quaternion q = new Quaternion(
      0.5698600991825139, 0.5698600991825139,
      0.5698600991825139, -0.16055653857469054);
    Quaternion expected = new Quaternion(1.0, 1.0, 1.0, 0.0);

    assertQuaternionEquals(q.log(), expected);

    q = new Quaternion();  // Unit quaternion
    expected = new Quaternion(0.0, 0.0, 0.0, 0.0);
    assertQuaternionEquals(q.log(), expected);

    q = new Quaternion(0.0, 0.0, 0.0, Math.E);
    expected = new Quaternion();
    assertQuaternionEquals(q.log(), expected);

    q = new Quaternion(
      3.9136670095, 3.1309336076, 5.2182226793, -18.7371131940);
    expected = new Quaternion(1.5, 1.2, 2, 3.0);
    assertQuaternionEquals(q.log(), expected);
  }

  @Test
  public void testRotate() {
    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 1.0);
    double[] v = new double[] {1.0, 1.0, 1.0};
    double[] expectImageV = new double[] {1.0, 1.0, -1.0};

    try {
      assertArrayEquals(q.rotate(v), expectImageV, EPSILON);
    } catch(Exception e) {}

    q = new Quaternion(2.0, -1.0, -3.0, 0.0);
    expectImageV = new double[] {-11.0 / 7.0, -5.0 / 7.0, -1.0 / 7.0};
    try {
      assertArrayEquals(q.rotate(v), expectImageV, EPSILON);
    } catch (Exception e) {}
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRotateWithInvalidInput() throws Exception {
    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 1.0);
    double[] v = new double[2];
    q.rotate(v);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRotateWithAnotherInvalidInput() throws Exception {
    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 1.0);
    double[] v = new double[20];
    q.rotate(v);
  }

  @Test(expected = NullPointerException.class)
  public void testRotateWithNullInput() throws Exception {
    Quaternion q = new Quaternion(0.0, 1.0, 0.0, 1.0);
    q.rotate(null);
  }

  @Test
  public void testGetQuaternionFromAxisAngle() throws Exception {
    double[] axis = new double[] {0.0, 0.0, 1.0};
    double angleDeg = 60.0;
    Quaternion expected = new Quaternion(0.0, 0.0, 0.5, 0.86602540378);
    Quaternion result = Quaternion.fromAxisAngle(axis, angleDeg);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isUnit());

    axis = new double[] {2.0, 5.0, -10.0};
    angleDeg = 90.0;
    expected = new Quaternion(
      0.12451456127, 0.31128640318, -0.62257280636, 0.70710678118);
    result = Quaternion.fromAxisAngle(axis, angleDeg);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isUnit());

    // Zero vector for axis
    axis = new double[] {0.0, 0.0, 0.0};
    expected = new Quaternion();
    angleDeg = 100;
    result = Quaternion.fromAxisAngle(axis, angleDeg);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isIdentity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAxisAngleWithInvalidVector() throws Exception {
    double[] nastyVector = new double[2];
    Quaternion.fromAxisAngle(nastyVector, 100);
  }

  @Test
  public void testGetQuaternionFromAxisAngleRad() throws Exception {
    double[] axis = new double[] {0.0, 0.0, 1.0};
    double angleRad = 1.0471975511965976;
    Quaternion expected = new Quaternion(0.0, 0.0, 0.5, 0.86602540378);
    Quaternion result = Quaternion.fromAxisAngleRad(axis, angleRad);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isUnit());

    axis = new double[] {0.5, 0.5, 0.7071067811865476};
    angleRad = Math.PI;
    expected = new Quaternion(0.5, 0.5, 0.7071067811865476, 0.0);
    result = Quaternion.fromAxisAngleRad(axis, angleRad);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isUnit());

    // Zero vector for axis
    axis = new double[] {0.0, 0.0, 0.0};
    expected = new Quaternion();
    angleRad = Math.PI / 8;
    result = Quaternion.fromAxisAngle(axis, angleRad);
    assertQuaternionEquals(result, expected);
    assertTrue(result.isIdentity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAxisAngleRadWithInvalidVector() throws Exception {
    double[] nastyVector = new double[10];
    Quaternion.fromAxisAngleRad(nastyVector, Math.PI);
  }

  @Test
  public void testGetAngle() {
    Quaternion q = new Quaternion(1, 0, 9, 0);
    assertEquals(q.getAngle(), 180.0, EPSILON);

    q = new Quaternion(1, 2, 3, 4);
    assertEquals(q.getAngle(), 86.17744627072567, EPSILON);

    q = new Quaternion();   // Identity quaternion
    assertEquals(q.getAngle(), 0.0, EPSILON);
  }

  @Test
  public void testGetAngleRad() {
    Quaternion q = new Quaternion(1, 2, 3, 4);
    assertEquals(q.getAngleRad(), 1.5040801783846716, EPSILON);

    q = new Quaternion(0, 0, 0, 1);
    assertEquals(q.getAngleRad(), 0, EPSILON);

    q = new Quaternion(1, 3, 0, 0);
    assertEquals(q.getAngleRad(), 3.141592653589793, EPSILON);
  }

  @Test
  public void testGetRotationAxis() throws Exception {
    double[] expectedAxis = new double[] {
      0.20628424925175867, 0.309426373877638, 0.928279121632914};
    double angle = 60.0;
    Quaternion q = Quaternion.fromAxisAngle(expectedAxis, angle);
    double [] axis = q.getRotationAxis();
    assertArrayEquals(axis, expectedAxis, EPSILON);

    expectedAxis = new double[] {0.0, 0.0, 0.0};
    q = new Quaternion();   // Identity quaternion
    axis = q.getRotationAxis();
    assertArrayEquals(axis, expectedAxis, EPSILON);
  }

  @Test
  public void testFromEulerAngles() {
    double yaw = 0.7854;
    double pitch = 0.1;
    double roll = 0.0;
    Quaternion expectedQ = new Quaternion(
      -0.019126242445565825, 0.046174713977463394,
      0.3822060250627864, 0.9227245726893359);

    Quaternion q = Quaternion.fromEulerAngles(roll, pitch, yaw);
    assertQuaternionEquals(q, expectedQ);
    assertTrue(q.isUnit());

    yaw = pitch = roll = 0.0;
    q = Quaternion.fromEulerAngles(roll, pitch, yaw);
    assertTrue(q.isIdentity());
  }

  @Test
  public void testEquals() {
    Quaternion q = new Quaternion(1.0, 2.234, 3.532, 4.125);
    Quaternion sameQ = new Quaternion(1.00, 2.234, 3.532, 4.125);
    Quaternion copyQ = new Quaternion(q);
    Quaternion differentQ = new Quaternion(1.0001, 2.234, 3.532, 4.125);

    assertTrue(q.equals(q));
    assertTrue(q.equals(sameQ));
    assertTrue(q.equals(copyQ));
    assertTrue(sameQ.equals(copyQ));

    assertFalse(q.equals(null));
    assertFalse(q.equals(differentQ));
  }

  @Test
  public void testHashCode() {
    Quaternion q = new Quaternion(1.12, 1.4351, 3.12545, 4.12567);
    Quaternion sameQ = new Quaternion(1.1200, 1.435100, 3.1254500, 4.1256700);

    assertTrue(q.equals(sameQ));
    assertEquals(q.hashCode(), sameQ.hashCode());
  }

  @Test
  public void testEqualsWithThreshold() {
    Quaternion q = new Quaternion(1.0, 2.0, 3.0, 4.0);
    Quaternion almostEqualQ = new Quaternion(
      1.000000002, 2.000000002, 3.000000002, 4.000000002);

    assertFalse(q.equals(null));
    assertFalse(q.equals(almostEqualQ, 0.0000000001));
    assertTrue(q.equals(almostEqualQ, 0.0000001));
  }

  @Test
  public void testSerialVersionUID() throws Exception {
    Quaternion q = new Quaternion(1.234, 34.12315, 4645.124236, -997.1242358);
    FileOutputStream fos = new FileOutputStream("unit-test.out");
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(q);

    FileInputStream fis = new FileInputStream("unit-test.out");
    ObjectInputStream ois = new ObjectInputStream(fis);
    Quaternion deserializedQ = (Quaternion) ois.readObject();

    assertTrue(q.equals(deserializedQ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLerpWithInvalidUpperRange() throws Exception {
    Quaternion from = new Quaternion(1.0, 2.0, 3.0, 4.0);
    Quaternion to = new Quaternion(2.2, 3.3, 4.4, 1.1);
    Quaternion.lerp(from, to, 1.00123);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLerpWithInvalidLowerRange() throws Exception {
    Quaternion from = new Quaternion(1.0, 2.0, 3.0, 4.0);
    Quaternion to = new Quaternion(2.2, 3.3, 4.4, 1.1);
    Quaternion.lerp(from, to, -5.0);
  }

  @Test
  public void testLerp() {
    Quaternion from = new Quaternion(0.0, 1.0, 0.0, 1.0);
    Quaternion to = new Quaternion(1.0, 0.0, 1.0, 0.0);

    Quaternion lerp = Quaternion.lerp(from, to, 0.0);
    assertQuaternionEquals(lerp, from);

    lerp = Quaternion.lerp(from, to, 1.0);
    assertQuaternionEquals(lerp, to);

    lerp = Quaternion.lerp(from, to, 0.5);
    Quaternion expected = new Quaternion(0.5, 0.5, 0.5, 0.5);
    assertQuaternionEquals(lerp, expected);

    lerp = Quaternion.lerp(from, to, 0.7);
    expected = new Quaternion(0.7, 0.3, 0.7, 0.3);
    assertQuaternionEquals(lerp, expected);
  }
}
