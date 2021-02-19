package sample;


import com.hummeling.if97.IF97;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;



class State {
    /**
     Out of range exception. The global IAPWS-IF97 limits are
     273.15 K <= T <= 1073.15 K  0 < p <= 100 MPa
     1073.15 K < T <= 2273.15 K  0 < p <= 50 MPa
     **/

    static Map<String, String> units = new HashMap< String, String>();
    static {
        units.put("Pressure", "MPa");
        units.put("Temperature", "C");
        units.put("Enthalpy", "kcal");
        units.put("Entropy", "kcal");
        units.put("Liquid density", "kg/m3");
        units.put("Density", "kg/m3");
        units.put("Vapour density", "kg/dm3");
    };
    IF97 calc = new IF97();
    double liquidDens;
    double vapourDens;
    double pressure;
    double temperature;
    double enthalpy;
    double entropy;
    double x,phi, beta;
    double density;
    double velocity = -1;
    boolean isTwoPhase = false;
    public Map<String, Double> getState() {
        Map<String, Double> myMap = new TreeMap< String, Double>();
        myMap.put("Pressure", pressure);
        myMap.put("Temperature", temperature-273.15);
        myMap.put("Enthalpy", enthalpy);
        myMap.put("Entropy", entropy);
        myMap.put("Density", density);
        myMap.put("Liquid density", liquidDens);
        myMap.put("Vapour density", vapourDens);
        myMap.put("Vapour fraction", x*100);
        myMap.put("Volume fraction", beta*100);
        myMap.put("Adjust volume fraction", phi*100);
        //myMap.put("Fraction", x*100);
        return myMap;
    }
    public void printState(){
        this.getState().forEach((k,v)->System.out.println(k + "=" + v + " " + units.getOrDefault(k,"%")));
    }

    public void stater(double pressure,double temperature){
        this.pressure = pressure;
        this.temperature = temperature;
        this.enthalpy =  calc.specificEnthalpyPT(pressure,temperature);
        this.entropy = calc.specificEntropyPT(pressure,temperature);
        this.x = temperature>calc.saturationTemperatureP(pressure)?1.0:0;
        this.density = calc.densityPT(pressure,temperature);
        this.phi = x;
        this.beta = x;
        this.liquidDens = this.vapourDens = this.density;
    }
    State(double pressure, double enthalpy, double velocity){
        this.pressure = pressure;
        this.enthalpy = enthalpy;
        this.stater(pressure,enthalpy,1);
    }
    State(){
        this.pressure = 10;
        this.temperature = 325+273.15;
        this.enthalpy =  calc.specificEnthalpyPT(pressure,temperature);
        this.entropy = calc.specificEntropyPT(pressure,temperature);
        this.x = temperature>calc.saturationTemperatureP(pressure)?1.0:0;
        this.density = calc.densityPT(pressure,temperature);
        this.phi = x;
        this.beta = x;
        this.liquidDens = this.vapourDens = this.density;
    }
    public void stater(double pressure, double enthalpy, double velocity){
        this.pressure = pressure;
        this.enthalpy = enthalpy;
        this.velocity = velocity;
        this.temperature = calc.temperaturePH(pressure,enthalpy);
        this.x = calc.vapourFractionPH(pressure,enthalpy);
        System.out.println("X is " + x);
        if (0<this.x && this.x<1) {
            //System.out.println("x is true");
            this.entropy = calc.specificEntropyPX(pressure, x);
            this.liquidDens = 1 / calc.specificVolumeSaturatedLiquidT(temperature);
            this.vapourDens = 1 / calc.specificVolumeSaturatedVapourT(temperature);
            this.beta = 1 / (1 + (vapourDens / liquidDens) * (1 / x - 1));
            this.phi = Phi.get(pressure * 10.1972, velocity*(1+this.x*(1+(-1+this.liquidDens/this.vapourDens)))) * beta;
            this.density = liquidDens * (1 - phi) + vapourDens * phi;
        }else{
            //System.out.println("x is false" + x);
            this.entropy = calc.specificEntropyPT(pressure,temperature);
            this.density = calc.densityPT(pressure,temperature);
            this.phi = x;
            this.beta = x;
            this.liquidDens = this.vapourDens = this.density;
        }
    }
    public State copy(){
        State k =  new State();
        k.stater(this.pressure,this.enthalpy,this.velocity);
        return k;
    }
}

