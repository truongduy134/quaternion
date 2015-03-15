package com.nus;

/**
 * Created by duy on 9/3/15.
 */
public class Quaternion {
  private double x;
  private double y;
  private double z;
  private double w;

  /**
   * Default Constructor. Constructs a Quaternion (0.0, 0.0, 0.0, 1.0)
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
   * Gets a string representation of this Quaternion for display purposes
   *
   * @return A string contains information about this Quaternion
   */
  @Override
  public String toString() {
    return String.format("Quaternion(%f, %f, %f, %f)",
      this.x, this.y, this.z, this.w);
  }
}
