package math;

public class Mat2x2 {

    private double[][] m;

    public Mat2x2() {
        // row then column
        m = new double[2][2];
    }

    public Mat2x2(double m00, double m01, double m10, double m11) {
        this();
        m[0][0] = m00;
        m[0][1] = m01;
        m[1][0] = m10;
        m[1][1] = m11;
    }
    
    public int cols() {
        return 2;
    }
    
    public int rows() {
        return 2;
    }

    public double get(int row, int col) {
        return m[row][col];
    }

    public Mat2x2 set(int row, int col, double val) {
        m[row][col] = val;
        return this;
    }

    public Mat2x2(double degrees) {
        this();
        rot(degrees);
    }

    public Mat2x2 rot(double degrees) {
        m[0][0] = Math.cos(degrees * Math.PI / 180.0);
        m[1][1] = Math.cos(degrees * Math.PI / 180.0);

        m[0][1] = -Math.sin(degrees * Math.PI / 180.0);
        m[1][0] = Math.cos(degrees * Math.PI / 180.0);

        return this;
    }

    public String toString() {
        String m = "";
        for (int row = 0; row < 2;row++) {
            for (int col = 0; col < 2; col++) {
                m += get(row,col) + " ";
            }
            m += "\n";
        }
        return m;
    }

}