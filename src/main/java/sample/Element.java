package sample;

import java.util.*;

import com.hummeling.if97.IF97;
import com.sun.istack.internal.NotNull;


abstract class Element {
    public State midState = new State();
    public String name;
    public State currentState;
    public State nextState;
    @NotNull
    public double dischargeRate;
    public void getMidState(){
        midState.stater(0.5*(currentState.pressure+nextState.pressure),
               0.5*(currentState.enthalpy+nextState.enthalpy),
               0.5*(currentState.velocity+nextState.velocity));
    };
    abstract public void setDischarge(double d);
    public double getParam(){
        return currentState.pressure;
    }
    abstract public void next();
    abstract public Element copy();
}



class Helement extends Element {
    double pressureDrop,height;
    int n;
    double heatFlux;
    double massVelocity;
    Resistance res;
    double staticDrop, dynamicDrop;
    Helement(State state, double height, Resistance res) {
        this.currentState = state;
        this.res = res;
        this.height = height;
    }

    Helement(State state, double height, double heatFlux, double dischargeRate,int n, Resistance res) {
        this.currentState = state;
        this.res = res;
        this.n = n;
        this.height = height;
        this.dischargeRate = dischargeRate;
        this.heatFlux = heatFlux;
        currentState.velocity = dischargeRate/(res.area()*3.6*currentState.liquidDens);
    }
    public void printState(){
        System.out.format("%90s",this.name + "\n");
        Map<String, Double> in = currentState.getState();
        Map<String, Double> out = nextState.getState();
        Map<String, Double> mid = midState.getState();
        System.out.format("%24s%32s%32s%32s%32s","Parameter" , "Input" , "Middle","Output", "Value" + "\n");
        in.forEach((k,v)->System.out.format("%24s%32f%32f%32f%32s",k , v , mid.get(k),out.get(k), State.units.getOrDefault(k,"%") + "\n"));
    }
    Helement(String name,State state, double height, double heatFlux, double dischargeRate,int n, Resistance res) {
        this.name = name;
        this.height = height;
        this.currentState = state;
        this.res = res;
        this.n = n;
        this.dischargeRate = dischargeRate;
        this.heatFlux = heatFlux;
        currentState.velocity = this.dischargeRate/(res.area()*3.6*currentState.liquidDens);
    }

    @Override
    public void setDischarge(double d) {
        this.dischargeRate = d;
    }


    @Override
    public Helement copy(){
        return new Helement(name,currentState.copy(),height, heatFlux, this.dischargeRate ,n,res);
    }
    private void twoPhaseNext(double accuracy) {
        System.out.println(this.name + " enters classic two phase region");
        this.pressureDrop = 0;
        double out = currentState.pressure - 0.01;
        while (Math.abs(currentState.pressure - pressureDrop - out) > accuracy) {
            System.out.println("Delta =" + (currentState.pressure - pressureDrop - out));
            System.out.println("Pressure drop = " + pressureDrop);
            out = currentState.pressure - pressureDrop;
            this.nextState = new State();
            nextState.stater(out, currentState.enthalpy + (this.heatFlux/this.dischargeRate), currentState.velocity);
            this.massVelocity = this.dischargeRate / (3.6 * res.area() * this.n);
            double dimlessDense = midState.liquidDens /midState.vapourDens   - 1;
            double head = (massVelocity * massVelocity) / (2 * midState.liquidDens * IF97.g);
            dynamicDrop = head * this.res.resistance() * (1 + midState.x * Psi.get(currentState.velocity, midState.pressure * 10.1972) * dimlessDense);
            staticDrop = -this.height * midState.density;
            pressureDrop = (dynamicDrop - staticDrop) / 10000;
            getMidState();
            printState();
        }
        System.out.println(name+" outs classic two phase region");
        printState();
        System.out.println("DISCHARGE =" + this.dischargeRate);
        System.out.println("Heigth = " + this.height);
        System.out.println("Mass velocity =" + this.massVelocity);
        System.out.println("dimlessDense =" + midState.liquidDens +"/"+ midState.vapourDens);
        System.out.println("Dense =" + midState.density);
        System.out.println("Resistance =" + this.res.resistance());
        System.out.println("Area =" + this.res.area());
        System.out.printf("P1 & P2 = %f  %f MPa\n", this.currentState.pressure, this.nextState.pressure);
        System.out.printf("Static + Dynamic =  %f  %f  kPa\n",staticDrop, dynamicDrop);
        System.out.printf("Pressure drop = %f MPa \n",this.pressureDrop);
    }

    private void onePhaseNext(double accuracy) {
        System.out.println(this.name + " enters onephase region");
        this.pressureDrop = 0;
        double out = currentState.pressure - 0.01;
        while (Math.abs(currentState.pressure - pressureDrop - out) > accuracy) {
            System.out.println("Pressure drop = " + pressureDrop);
            out = currentState.pressure - pressureDrop;
            this.nextState = new State();
            nextState.stater(out, currentState.enthalpy + (this.heatFlux/this.dischargeRate), currentState.velocity);
            printState();
            this.massVelocity = this.dischargeRate / (3.6*res.area() * this.n);
            getMidState();
            double head = (massVelocity * massVelocity) / (2 * midState.liquidDens * IF97.g);
            dynamicDrop = head * this.res.resistance();
            staticDrop = -this.height * midState.density;
            pressureDrop = (dynamicDrop - staticDrop) / 10000;
            System.out.printf("massVelocity  =  %f  %f  kPa\n", head, this.res.resistance());
            System.out.printf("Static + Dynamic =  %f  %f  kPa\n", staticDrop, dynamicDrop);
        }
        System.out.println(name+" outs classic one phase region");
        printState();
        System.out.println("DISCHARGE =" + this.dischargeRate);
        System.out.println("Heigth = " + this.height);
        System.out.println("Mass velocity =" + this.massVelocity);
        System.out.println("dimlessDense =" + midState.liquidDens +"/"+ midState.vapourDens);
        System.out.println("Dense =" + midState.density);
        System.out.println("Resistance =" + this.res.resistance());
        System.out.println("Area =" + this.res.area());
        System.out.printf("P1 & P2 = %f  %f MPa\n", this.currentState.pressure, this.nextState.pressure);
        System.out.printf("Static + Dynamic =  %f  %f  kPa\n",staticDrop, dynamicDrop);
        System.out.printf("Pressure drop = %f MPa \n",this.pressureDrop);
    }
    @Override
    public void next() {
        if ((0 < currentState.x && currentState.x < 1) || (new IF97().vapourFractionPH(currentState.pressure,currentState.enthalpy+(this.heatFlux/this.dischargeRate))>0 && new IF97().vapourFractionPH(currentState.pressure,currentState.enthalpy+(this.heatFlux/this.dischargeRate))<1)) {
            twoPhaseNext(1e-6);
        } else {
            onePhaseNext(1e-6);
        }
    }
}

