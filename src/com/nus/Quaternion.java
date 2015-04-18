package com.nus;

/**
 * Self-contained and lightweight Java implementation of Quaternion. The class
 * supports a variety of Quaternion operations and functions
 *
 * @author Duy Nguyen-Truong (truongduy134@gmail.com)
 */
public class Quaternion {
  private double x;
  private double y;
  private double z;
  private double w;

  public static final double EPSILON = 0.00000000001;
  public static final String VECTOR_INVALID_LENGTH_MSG =
    "Input vector must be an array of size 3";
  public static final String UNDEFINED_LOG_ZERO_QUATERNION_MSG =
    "Logarithm of zero quaternion is undefined";

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
  public Quaternion(final Quaternion another) {
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
   * Gets the vector component (x, y, z) of this quaternion
   *
   * @return An array of size 3 representing the vector (x, y, z)
   */
  public double[] getVectorPart() {
    double[] vector = new double[] {this.x, this.y, this.z};
    return vector;
  }

  /**
   * Get scalar component (w-component) of this quaternion
   *
   * @return The scalar component (w-component) of this quaternion
   *
   * @see #getW()
   */
  public double getScalarPart() {
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
   * Checks if this Quaternion equals to the input Quaternion within the
   * specified tolerance threshold
   *
   * @param another Another Quaternion for comparison
   * @param threshold A tolerance threshold value
   * @return {@code true} if two Quaternions are equal within the tolerance
   *         threshold (that means corresponding components are equal within
   *         the tolerance threshold); {@code false} otherwise
   */
  public boolean equals(final Quaternion another, double threshold) {
    if (another == null) {
      return false;
    }

    return Math.abs(another.getX() - this.x) < threshold &&
    Math.abs(another.getY() - this.y) < threshold &&
    Math.abs(another.getZ() - this.z) < threshold &&
    Math.abs(another.getW() - this.w) < threshold;
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
  public final Quaternion add(final Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.addEq(another);
    return result;
  }

  /**
   * Performs addition of two quaternions and assigns the result to this object
   *
   * @param another The other quaternion involving in the addition
   */
  public final void addEq(final Quaternion another) {
    this.x += another.x;
    this.y += another.y;
    this.z += another.z;
    this.w += another.w;
  }

  /**
   * Performs multiplication of this quaternion with the input quaternion,
   * that is {@code this * another}
   *
   * @param another The other quaternion involving in the multiplication
   * @return The quaternion which is the multiplication result
   */
  public final Quaternion multiply(final Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.multiplyEq(another);
    return result;
  }

  /**
   * Performs scalar multiplication of this quaternion and the input number
   *
   * @param scalar A constant factor
   * @return The quaternion which is the multiplication result
   */
  public final Quaternion multiply(double scalar) {
    Quaternion result = new Quaternion(this);
    result.multiplyEq(scalar);
    return result;
  }

  /**
   * Performs multiplication of this quaternion with the input quaternion,
   * that is {@code this * another}. Assigns the result to this object
   *
   * @param another The other quaternion involving in the multiplication
   */
  public final void multiplyEq(final Quaternion another) {
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
   * Performs scalar multiplication of this quaternion and the input number.
   * Assigns the result to this object
   *
   * @param scalar A constant factor
   */
  public final void multiplyEq(double scalar) {
    this.x *= scalar;
    this.y *= scalar;
    this.z *= scalar;
    this.w *= scalar;
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
    this.multiplyEq(1.0 / sqNorm);
  }

  /**
   * Performs division of this quaternion with the input quaternion, that is
   * {@code this / another}
   *
   * @param another The other quaternion involving in the division
   * @return The quaternion which is the division result
   */
  public final Quaternion divide(final Quaternion another) {
    Quaternion result = new Quaternion(this);
    result.divideEq(another);
    return result;
  }

  /**
   * Performs division of this quaternion with the input quaternion, that is
   * {@code this / another}. Assigns the result to this object
   *
   * @param another The other quaternion involving in the division
   */
  public final void divideEq(final Quaternion another) {
    this.multiplyEq(another.inverse());
  }

  /**
   * Gets the exponential of this Quaternion
   *
   * @return The exponential Quaternion
   */
  public final Quaternion exp() {
    double[] vectorPart = this.getVectorPart();
    double vNorm = vectorNorm(vectorPart);

    if (vNorm < EPSILON) {
      return new Quaternion(0.0, 0.0, 0.0, Math.exp(this.w));
    }

    double scalar = Math.sin(vNorm) / vNorm;
    for (int i = 0; i < vectorPart.length; ++i) {
      vectorPart[i] *= scalar;
    }

    Quaternion result = new Quaternion(
      vectorPart[0], vectorPart[1], vectorPart[2], Math.cos(vNorm));
    result.multiplyEq(Math.exp(this.w));
    return result;
  }

  /**
   * Gets the natural logarithm of this Quaternion
   *
   * @return The logarithm Quaternion
   * @throws ArithmeticException if the Quaternion has norm approaching 0, that
   *         is the norm is less than {@link Quaternion#EPSILON}
   */
  public final Quaternion log() throws ArithmeticException {
    double qNorm = this.norm();
    if (qNorm < EPSILON) {
      throw new ArithmeticException(UNDEFINED_LOG_ZERO_QUATERNION_MSG);
    }

    double[] vectorPart = this.getVectorPart();
    double vNorm = vectorNorm(vectorPart);
    double[] resultVector = new double[] {0.0, 0.0, 0.0};
    if (!(vNorm < EPSILON)) {
      double factor = Math.acos(this.w / qNorm) / vNorm;
      for (int i = 0; i < vectorPart.length; ++i) {
        resultVector[i] = vectorPart[i] * factor;
      }
    }

    return new Quaternion(
      resultVector[0], resultVector[1], resultVector[2], Math.log(qNorm)
    );
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
  public final double[] rotate(final double[] vector)
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
   *         representation. If the norm of input axis vector is less than
   *         {@link Quaternion#EPSILON}, an identity Quaternion is returned
   * @throws IllegalArgumentException if input vector is not an array of size 3
   */
  public static Quaternion fromAxisAngle(final double[] axis, double angleInDeg)
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
   *         representation. If the norm of input axis vector is less than
   *         {@link Quaternion#EPSILON}, an identity Quaternion is returned
   * @throws IllegalArgumentException if input vector is not an array of size 3
   */
  public static Quaternion fromAxisAngleRad(
      final double[] axis, double angleInRad) throws IllegalArgumentException {
    if (axis.length != 3) {
      throw new IllegalArgumentException(Quaternion.VECTOR_INVALID_LENGTH_MSG);
    }

    // Normalize the input vector
    double vNorm = vectorNorm(axis);
    if (vNorm < EPSILON) {
      return new Quaternion();    // Identity Quaternion
    }

    axis[0] /= vNorm;
    axis[1] /= vNorm;
    axis[2] /= vNorm;

    double halfAngle = angleInRad / 2.0;
    double sinTerm = Math.sin(halfAngle);
    double x = axis[0] * sinTerm;
    double y = axis[1] * sinTerm;
    double z = axis[2] * sinTerm;
    double w = Math.cos(halfAngle);
    return new Quaternion(x, y, z, w);
  }

  /**
   * Gets the unit Quaternion of a rotation specified by Euler . The order of
   * rotation is applying yaw, then pitch, then roll (that is z -&gt; y -&gt; x)
   *
   * @param roll The roll angle (in radians)
   * @param pitch The pitch angle (in radians)
   * @param yaw The yaw angle (in radians)
   * @return The unit Quaternion of a rotation specified by {@code roll},
   *         {@code pitch}, {@code yaw} angles
   */
  public static Quaternion fromEulerAngles(
      double roll, double pitch, double yaw) {
    double cosHalfRoll = Math.cos(roll * 0.5);
    double cosHalfPitch = Math.cos(pitch * 0.5);
    double cosHalfYaw = Math.cos(yaw * 0.5);
    double sinHalfRoll = Math.sin(roll * 0.5);
    double sinHalfPitch = Math.sin(pitch * 0.5);
    double sinHalfYaw = Math.sin(yaw * 0.5);

    double w = cosHalfYaw * cosHalfPitch * cosHalfRoll +
      sinHalfYaw * sinHalfPitch * sinHalfRoll;
    double x = cosHalfYaw * cosHalfPitch * sinHalfRoll -
      sinHalfYaw * sinHalfPitch * cosHalfRoll;
    double y = cosHalfYaw * sinHalfPitch * cosHalfRoll +
      sinHalfYaw * cosHalfPitch * sinHalfRoll;
    double z = sinHalfYaw * cosHalfPitch * cosHalfRoll -
      cosHalfYaw * sinHalfPitch * sinHalfRoll;

    return new Quaternion(x, y, z, w);
  }

  private static double degreeToRadian(double degree) {
    return Math.PI * degree / 180;
  }

  private static double radianToDegree(double radian) {
    return radian / Math.PI * 180;
  }

  private static double vectorNorm(final double[] vector) {
    double result = 0.0;
    for (int i = 0; i < vector.length; ++i) {
      result += vector[i] * vector[i];
    }
    return Math.sqrt(result);
  }

  // Overridden methods inherited from Object

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

  @Override
  public boolean equals(Object another) {
    // Self comparison
    if (this == another) {
      return true;
    }

    if (!(another instanceof Quaternion)) {
      return false;
    }

    Quaternion anotherQ = (Quaternion) another;
    if (Double.compare(anotherQ.w, this.w) != 0 ||
        Double.compare(anotherQ.x, this.x) != 0 ||
        Double.compare(anotherQ.y, this.y) != 0 ||
        Double.compare(anotherQ.z, this.z) != 0) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 13;

    double[] fields = new double[] {this.x, this.y, this.z, this.w};
    for (int i = 0; i < fields.length; ++i) {
      long temp = Double.doubleToLongBits(fields[i]);
      result = prime * result + (int) (temp ^ (temp >>> 32));
    }

    return result;
  }
}
