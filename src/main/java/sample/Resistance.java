package sample;

interface Resistance{
    public double resistance();
    public double area();
}

class tube implements Resistance {
    double len;
    double diameter;
    double s;
    double bends, in, out;
    boolean isAus = false;
    double d;
    tube(double diameter, double s, double len){
        this.diameter = diameter;
        this.len = len;
        this.s = s;
        this.d = diameter - 2*s;
    }
    private double lambda(){
        double z = 1/(4*(Math.log10(3.7*(d/0.08))*Math.log10(3.7*(d/0.08))))/(d/1000);
        return z;
    }
    public double resistance(){
        return this.lambda()*len;
    }
    public double area(){
        return Math.PI*((d/1000.0)*(d/1000.0))/4.0;
    }
}