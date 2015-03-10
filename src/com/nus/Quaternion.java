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
