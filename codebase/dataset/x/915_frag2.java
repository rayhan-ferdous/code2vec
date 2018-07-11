import net.sf.opendf.math.util.Maths;



/**

   Jama = Java Matrix class.

<P>

   The Java Matrix Class provides the fundamental operations of numerical

   linear algebra.  Various constructors create Matrices from two dimensional

   arrays of double precision floating point numbers.  Various "gets" and

   "sets" provide access to submatrices and matrix elements.  Several methods 

   implement basic matrix arithmetic, including matrix addition and

   multiplication, matrix norms, and element-by-element array operations.

   Methods for reading and printing matrices are also included.  All the

   operations in this version of the Matrix Class involve real matrices.

   Complex matrices may be handled in a future version.

<P>

   Five fundamental matrix decompositions, which consist of pairs or triples

   of matrices, permutation vectors, and the like, produce results in five

   decomposition classes.  These decompositions are accessed by the Matrix

   class to compute solutions of simultaneous linear equations, determinants,

   inverses and other matrix functions.  The five decompositions are:

<P><UL>

   <LI>Cholesky Decomposition of symmetric, positive definite matrices.

   <LI>LU Decomposition of rectangular matrices.

   <LI>QR Decomposition of rectangular matrices.

   <LI>Singular Value Decomposition of rectangular matrices.

   <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square matrices.

</UL>

<DL>

<DT><B>Example of use:</B></DT>

<P>

<DD>Solve a linear system A x = b and compute the residual norm, ||b - A x||.

<P><PRE>

      double[][] vals = {{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};

      Matrix A = new Matrix(vals);

      Matrix b = Matrix.random(3,1);

      Matrix x = A.solve(b);

      Matrix r = A.times(x).minus(b);

      double rnorm = r.normInf();

</PRE></DD>

</DL>



@author The MathWorks, Inc. and the National Institute of Standards and Technology.

@version 5 August 1998

*/

public class Matrix implements Cloneable, java.io.Serializable {



    /** Array for internal storage of elements.

   @serial internal array storage.

   */

    private double[][] A;



    /** Row and column dimensions.

   @serial row dimension.

   @serial column dimension.

   */

    private int m, n;



    /** Construct an m-by-n matrix of zeros. 

   @param m    Number of rows.

   @param n    Number of colums.

   */

    public Matrix(int m, int n) {

        this.m = m;

        this.n = n;

        A = new double[m][n];

    }



    /** Construct an m-by-n constant matrix.

   @param m    Number of rows.

   @param n    Number of colums.

   @param s    Fill the matrix with this scalar value.

   */

    public Matrix(int m, int n, double s) {

        this.m = m;

        this.n = n;

        A = new double[m][n];

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = s;

            }

        }

    }



    /** Construct a matrix from a 2-D array.

   @param A    Two-dimensional array of doubles.

   @exception  IllegalArgumentException All rows must have the same length

   @see        #constructWithCopy

   */

    public Matrix(double[][] A) {

        m = A.length;

        n = A[0].length;

        for (int i = 0; i < m; i++) {

            if (A[i].length != n) {

                throw new IllegalArgumentException("All rows must have the same length.");

            }

        }

        this.A = A;

    }



    /** Construct a matrix quickly without checking arguments.

   @param A    Two-dimensional array of doubles.

   @param m    Number of rows.

   @param n    Number of colums.

   */

    public Matrix(double[][] A, int m, int n) {

        this.A = A;

        this.m = m;

        this.n = n;

    }



    public Matrix(List<List<Number>> A) {
