package math;

public class Vec2d {

    public double x;
    public double y;

    public Vec2d() {}
    
    public Vec2d(double degrees) {
        unit(degrees);
    }

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d(Vec2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vec2d copy() {
        return new Vec2d(this);
    }

    public Vec2d add(float v) {
        x += v;
        y += v;
        return this;
    }

    public Vec2d add(Vec2d v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vec2d sub(float v) {
        x -= v;
        y -= v;
        return this;
    }

    public Vec2d mul(float v) {
        x *= v;
        y *= v;
        return this;
    }

    public Vec2d mul(Vec2d v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    public Vec2d mul(Mat2x2 v) {
        Vec2d temp = copy();
        x = temp.x * v.get(0,0) + temp.y * v.get(0,1);
        y = temp.x * v.get(1,0) + temp.y * v.get(1,1);
        return this;
    }

    public Vec2d div(float v) {
        x /= v;
        y /= v;
        return this;
    }

    public Vec2d div(Vec2d v) {
        x /= v.x;
        y /= v.y;
        return this;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public Vec2d x(double x) {
        this.x = x;
        return this;
    }

    public Vec2d y(double y) {
        this.y = y;
        return this;
    }
    
    public Vec2d normal() {
        return new Vec2d(-y, x);
    }

    public Vec2d unit(double degrees) {
        return x(Math.cos(degrees * Math.PI / 180.0)).y(Math.sin(degrees * Math.PI / 180.0));
    }

    @Override
    public String toString() {
        return "[ " + x + " " + y + " ]";
    }

}