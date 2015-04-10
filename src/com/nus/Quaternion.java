package com.nus;

/**
 * Created by duy on 9/3/15.
 */
public class Quaternion {
  private double x;
  private double y;
  private double z;
  private double w;

  private final double EPSILON = 0.00000000001;
  public static final String VECTOR_INVALID_LENGTH_MSG =
    "Input vector must be an array of size 3";

  /**
   * Default Constructor. Constructs an identity Quaternion (0.0, 0.0, 0.0, 1.0)
   */
  public Quaternion() {
    this(0.0, 0.0, 0.0, 1.0);
  }

  /**
   * Constructs and initializes a Quaternion with 4 input parameters
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @param w the scalar component
   */
  public Quaternion(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   * Copy constructor
   *
   * @param another the Quaternion to be copied
   */
  public Quaternion(Quaternion another) {
    this.x = another.x;
    this.y = another.y;
    this.z = another.z;
    this.w = another.w;
  }

  /**
   * Gets x-coordinate of this quaternion
   *
   * @return The x-coordinate of this quaternion
   */
  public double getX() {
    return x;
  }

  /**
   * Gets y-coordinate of this quaternion
   *
   * @return The y-coordinate of this quaternion
   */
  public double getY() {
    return y;
  }

  /**
   * Gets z-coordinate of this quaternion
   *
   * @return The z-coordinate of this quaternion
   */
  public double getZ() {
    return z;
  }

  /**
   * Gets w-component (scalar component) of this quaternion
   *
   * @return The w-component of this quaternion
   */
  public double getW() {
    return w;
  }

  /**
   * Gets the angle (in degree) in the angle-axis representation of the rotation
   * that this Quaternion represents
   *
   * @return The angle (in degree) of the rotation
   */
  public double getAngle() {
    return radianToDegree(this.getAngleRad());
  }

  /**
   * Gets the angle (in radian) in the angle-axis representation of the rotation
   * that this Quaternion represents
   *
   * @return The angle (in radian) of the rotation
   */
  public double getAngleRad() {
    Quaternion unitQ = new Quaternion(this);
    unitQ.normalize();

    double x = unitQ.getX();
    double y = unitQ.getY();
    double z = unitQ.getZ();
    double vNorm = Math.sqrt(x * x + y * y + z * z);

    return 2 * Math.atan2(vNorm, unitQ.getW());
  }

  /**
   * Gets the vector axis in the angle-axis representation of the rotation that
   * this Quaternion represents
   *
   * @return A unit vector for the rotation axis, or a zero vector in
   *         degenerate case
   */
  public double[] getRotationAxis() {
    double angleRad = this.getAngleRad();
    double norm = this.norm();

    double[] axis = new double[] {0.0, 0.0, 0.0};
    if (Math.abs(angleRad) > EPSILON) {
      double sinTerm = Math.sin(angleRad / 2.0);
      axis[0] = this.x / (norm * sinTerm);
      axis[1] = this.y / (norm * sinTerm);
      axis[2] = this.z / (norm * sinTerm);
    }

    return axis;
  }

  /**
   * Checks if this Quaternion is an identity quaternion (0.0, 0.0, 0.0, 1.0)
   *
   * @return {@code true} if this Quaternion is an identity quaternion, or
   * {@code false} otherwise
   */
  public boolean isIdentity() {
    return Math.abs(this.squaredNorm() - 1.0) < EPSILON &&
      Math.abs(this.w - 1.0) < EPSILON;
  }

  /**
   * Checks if this Quaternion is a unit quaternion
   * @return {@code true} if this Quaternion is a unit quaternion, or
   * {@code false} otherwise
   */
  public boolean isUnit() {
    return Math.abs(this.norm() - 1.0) < EPSILON;
  }

  /**
   * Computes the norm of this quaternion
   *
   * @return the norm of this quaternion
   */
  public final double norm() {
    return Math.sqrt(x * x + y * y + z * z + w * w);
  }

  /**
   * Computes the square of the norm of this quaternion
   *
   * @return the square of the norm
   */
  public final double squaredNorm() {
    return x * x + y * y + z * z + w * w;
  }

  /**
   * Normalizes the quaternion so that it has norm 1
   */
  public final void normalize() {
    double qNorm = this.norm();
    this.x /= qNorm;
    this.y /= qNorm;
    this.z /= qNorm;
    this.w /= qNorm;
  }

  /**
   * Normalizes the quaternion so that it has norm 1
   *
   * @see #normalize()
   */
  public final void toUnit() {
    this.normalize();
  }

  /**
   * Gets the conjugate of this quaternion
   *
   * @return the conjugate quaternion
   */
  public final Quaternion conjugate() {
    return new Quaternion(-this.x, -this.y, -this.z, this.w);
  }

  /**
   * Gets the conjugate of this quaternion and assigns it to this object
   */
  public final void conjugateEq() {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
  }

  /**
   * Performs addition of two quaternions
   *
   * @param another The other quaternion involving in the addition
   * @return The quaternion which is the sum result
   */
  public final Quaternion add(Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.addEq(another);
    return result;
  }

  /**
   * Performs addition of two quaternions and assigns the result to this object
   *
   * @param another The other quaternion involving in the addition
   */
  public final void addEq(Quaternion another) {
    this.x += another.x;
    this.y += another.y;
    this.z += another.z;
    this.w += another.w;
  }

  /**
   * Performs multiplication of this quaternion with the input quaternion, i.e.
   * {@code this * another}
   *
   * @param another The other quaternion involving in the multiplication
   * @return The quaternion which is the multiplication result
   */
  public final Quaternion multiply(Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.multiplyEq(another);
    return result;
  }

  /**
   * Performs multiplication of this quaternion with the input quaternion, i.e.
   * {@code this * another}. Assigns the result to this object
   *
   * @param another The other quaternion involving in the multiplication
   */
  public final void multiplyEq(Quaternion another) {
    double newW = another.w * this.w - another.x * this.x -
      another.y * this.y - another.z * this.z;
    double newX = another.w * this.x + another.x * this.w -
      another.y * this.z + another.z * this.y;
    double newY = another.w * this.y + another.x * this.z +
      another.y * this.w - another.z * this.x;
    double newZ = another.w * this.z - another.x * this.y +
      another.y * this.x + another.z * this.w;
    this.w = newW;
    this.x = newX;
    this.y = newY;
    this.z = newZ;
  }

  /**
   * Gets the inverse (reciprocal) of this quaternion
   *
   * @return The inverse quaternion
   */
  public final Quaternion inverse() {
    Quaternion result = new Quaternion(this);
    result.invert();
    return result;
  }

  /**
   * Inverts this quaternion
   *
   * @see #invert()
   */
  public final void inverseEq() {
    this.invert();
  }

  /**
   * Inverts this quaternion
   */
  public final void invert() {
    double sqNorm = this.squaredNorm();
    this.conjugateEq();
    this.x /= sqNorm;
    this.y /= sqNorm;
    this.z /= sqNorm;
    this.w /= sqNorm;
  }

  /**
   * Performs division of this quaternion with the input quaternion, i.e.
   * {@code this / another}
   *
   * @param another The other quaternion involving in the division
   * @return The quaternion which is the division result
   */
  public final Quaternion divide(Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.divideEq(another);
    return result;
  }

  /**
   * Performs division of this quaternion with the input quaternion, i.e.
   * {@code this / another}. Assigns the result to this object
   *
   * @param another The other quaternion involving in the division
   */
  public final void divideEq(Quaternion another) {
    this.multiplyEq(another.inverse());
  }

  /**
   * Returns the rotation matrix represented by the normalized version of
   * this quaternion
   *
   * @return A 3 x 3 rotation matrix
   */
  public final double[][] getRotationMatrix() {
    double sqNorm = this.squaredNorm();
    double[][] mat = new double[][] {
      {sqNorm - 2 * (y * y + z * z), 2 * (x * y - z * w), 2 * (x * z + y * w)},
      {2 * (x * y + z * w), sqNorm - 2 * (x * x + z * z), 2 * (y * z - x * w)},
      {2 * (x * z - y * w), 2 * (y * z + x * w), sqNorm - 2 * (x * x + y * y)},
    };
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        mat[i][j] /= sqNorm;
      }
    }
    return mat;
  }

  /**
   * Rotates a 3D vector by the rotation represented by this quaternion
   *
   * @param vector An array of size 3 representing a 3D vector
   * @return The image of the input vector after the rotation
   * @throws IllegalArgumentException if input vector is not an array of size 3
   */
  public final double[] rotate(double[] vector)
      throws IllegalArgumentException {
    if (vector.length != 3) {
      throw new IllegalArgumentException("Input must be an array of size 3");
    }

    double[][] rotationMat = this.getRotationMatrix();
    double[] imageVector = new double[3];

    for (int r = 0; r < 3; r++) {
      imageVector[r] = 0.0;
      for (int c = 0; c < 3; c++) {
        imageVector[r] += rotationMat[r][c] * vector[c];
      }
    }

    return imageVector;
  }

  /**
   * Gets a string representation of this Quaternion for display purposes
   *
   * @return A string contains information about this Quaternion
   */
  @Override
  public String toString() {
    return String.format("Quaternion(%f, %f, %f, %f)",
      this.x, this.y, this.z, this.w);
  }

  // Static functions to create Quaternion

  /**
   * Gets an identity Quaternion (0.0, 0.0, 0.0, 1.0)
   * @return An identity Quaternion
   */
  public static Quaternion getIdentity() {
    return new Quaternion();
  }

  /**
   * Gets the unit Quaternion of a rotation which is given by the input axis,
   * and angle (in degrees)
   *
   * @param axis An array of size 3 representing the vector (x, y, z)
   * @param angleInDeg The angle (in degrees) of the rotation
   * @return The unit Quaternion of the rotation given by the input axis-angle
   *         representation
   * @throws IllegalArgumentException if input vector is not an array of size 3
   */
  public static Quaternion fromAxisAngle(double[] axis, double angleInDeg)
      throws IllegalArgumentException {
    if (axis.length != 3) {
      throw new IllegalArgumentException(Quaternion.VECTOR_INVALID_LENGTH_MSG);
    }

    double angleInRad = degreeToRadian(angleInDeg);
    return fromAxisAngleRad(axis, angleInRad);
  }

  /**
   * Gets the unit Quaternion of a rotation which is given by the input axis,
   * and angle (in radians)
   *
   * @param axis An array of size 3 representing the vector (x, y, z)
   * @param angleInRad The angle (in radians) of the rotation
   * @return The unit Quaternion of the rotation given by the input axis-angle
   *         representation
   * @throws IllegalArgumentException if input vector is not an array of size 3
   */
  public static Quaternion fromAxisAngleRad(double[] axis, double angleInRad)
      throws IllegalArgumentException {
    if (axis.length != 3) {
      throw new IllegalArgumentException(Quaternion.VECTOR_INVALID_LENGTH_MSG);
    }

    // Normalize the input vector
    double vectorNorm = Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] +
      axis[2] * axis[2]);
    axis[0] /= vectorNorm;
    axis[1] /= vectorNorm;
    axis[2] /= vectorNorm;

    double halfAngle = angleInRad / 2.0;
    double sinTerm = Math.sin(halfAngle);
    double x = axis[0] * sinTerm;
    double y = axis[1] * sinTerm;
    double z = axis[2] * sinTerm;
    double w = Math.cos(halfAngle);
    return new Quaternion(x, y, z, w);
  }

  private static double degreeToRadian(double degree) {
    return Math.PI * degree / 180;
  }

  private static double radianToDegree(double radian) {
    return radian / Math.PI * 180;
  }
}
