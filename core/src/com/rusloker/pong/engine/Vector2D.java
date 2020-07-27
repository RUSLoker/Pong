package com.rusloker.pong.engine;


public class Vector2D {

    public final float x, y, length, sqrLength;
    public final static Vector2D zero = new Vector2D();

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
        sqrLength = sqrLength(x, y);
        length = length(x, y);
    }

    private Vector2D(Vector2D v){
        this(v.x, v.y, v.length, v.sqrLength);
    }

    private Vector2D(float x, float y, float len, float sqrLength){
        this.x = x;
        this.y = y;
        this.length = len;
        this.sqrLength = sqrLength;
    }

    private Vector2D() {
        x = 0;
        y = 0;
        length = 0;
        sqrLength = 0;
    }

    public Vector2D add(Vector2D v){
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public static Vector2D sum(Vector2D[] vArr){
        Vector2D vector = new Vector2D();
        for (Vector2D i : vArr) {
            vector = vector.add(i);
        }
        return vector;
    }

    public Vector2D sub(Vector2D v){
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D scale(float mult){
        return new Vector2D(this.x * mult, this.y * mult,
                length * mult, sqrLength * mult*mult);
    }

    public Vector2D reverse(){
        return new Vector2D(-this.x, -this.y, length, sqrLength);
    }

    public Vector2D reverseX(){
        return new Vector2D(-this.x, this.y, length, sqrLength);
    }

    public Vector2D reverseY(){
        return new Vector2D(this.x, -this.y, length, sqrLength);
    }

    static float length(float x, float y){
        return (float) Math.sqrt(x*x + y*y);
    }

    static float sqrLength(float x, float y){
        return x*x + y*y;
    }

    public static Vector2D mean(Vector2D a, Vector2D b){
        return new Vector2D((a.x + b.x)/2, (a.y + b.y)/2);
    }

    public Vector2D rotate(float angle){
        return rotate((float) Math.sin(angle), (float) Math.cos(angle));
    }

    public Vector2D rotate(float sinA, float cosA){
        float x = this.x * cosA - this.y * sinA;
        float y = this.y * cosA + this.x * sinA;
        return new Vector2D(x, y).setLength(length);
    }

    public Vector2D normalize(){
        if (length == 0){
            return new Vector2D(this);
        }
        float len = length(x, y);
        return new Vector2D(x/len, y/len, 1, 1);
    }

    float scalar(Vector2D v){
        return this.x * v.x + this.y * v.y;
    }

    public static Vector2D intersection(Vector2D a, Vector2D b, Vector2D c, Vector2D d){
        float[] x = {0, a.x, b.x, c.x, d.x},
                y = {0, a.y, b.y, c.y, d.y};
        float fp = x[1] * y[2] - y[1] * x[2];
        float sp = x[3] * y[4] - y[3] * x[4];
        float tp = (x[1] - x[2]) * (y[3] - y[4]) - (y[1] - y[2]) * (x[3] - x[4]);
        return new Vector2D(
                (fp *(x[3] - x[4]) - (x[1] - x[2])* sp)/ tp,
                (fp *(y[3] - y[4]) - (y[1] - y[2])* sp)/ tp

        );
    }

    public static float prodZ(Vector2D a, Vector2D b){
        return a.x * b.y - a.y * b.x;
    }

    public static float angleBetween(Vector2D a, Vector2D b){
        return (float) Math.acos(a.normalize().scalar(b.normalize()));
    }

    public static float angleBetweenDirective(Vector2D a, Vector2D b){
        Vector2D xBase = a,
                yBase = a.rotate(1, 0);
        float y = (b.x*xBase.y-b.y*xBase.x)/(yBase.x*xBase.y-xBase.x*yBase.y);
        return (float) (Math.signum(y)*Math.acos(a.normalize().scalar(b.normalize())));
    }

    public Vector2D setLength(float len){
        if (length == len){
            return this;
        }
        return this.normalize().scale(len);
    }

    public float projection(Vector2D b){
        b = b.normalize();
        return this.scalar(b);
    }

    @Override
    public String toString() {
        return "( " + x + ", " + y + " )";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == getClass()) {
            Vector2D v = (Vector2D) obj;
            return x == v.x && y == v.y;
        }
        return false;
    }
}
