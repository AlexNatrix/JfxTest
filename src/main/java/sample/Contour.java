package sample;


import com.sun.istack.internal.Nullable;
import javafx.scene.chart.XYChart;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AbstractUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.NoBracketingException;

import java.util.*;


class elementsList extends AbstractList<Element>{
    Element head;
    elementsList tail;
    private ArrayList<Element> copyList = new ArrayList<Element>();
    elementsList(Element...args)  {
        List<Element> xs = Arrays.asList(args);
        if (xs.size()>1){
            this.head = xs.get(0);
            tail = new elementsList(Arrays.copyOfRange(args, 1, xs.size()));
        }else{
            this.head = xs.get(0);
            this.tail = this.copy();
        }
    }
    public elementsList copy(){
        elementsList k = new elementsList();
        if (this.head==null){
            return k;
        }
        k.head = this.head;
        k.tail = this.tail==null?new elementsList():this.tail.copy();
        return k;
    }

    /**
     * This is constructor of null object
     * ist quite questionable
     * because of
     * bugs
     */
    elementsList(){
        this.head = null;
        this.tail = null;
    }

    elementsList(Element o){
        this.head =o;
        this.tail = null;
    }
    public int size() {
        int i = 0;
        elementsList k = this;
        while (k.head!=null){
            i++;
            k = k.tail;
        }
        return i;
    }

    public void update(Element o){
        ArrayList<Element> buf = new ArrayList<Element>(copyList);
        buf.set(0,o);
        this.clear();
        copyList.clear();
        this.addAll(buf);

    }

    public Element copyFirst(){
        return this.tail.head==null?this.head.copy():this.tail.copyFirst();
    }

    public Element first(){
        return this.tail.head==null?this.head:this.tail.first();
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public boolean contains(Object o) {
        if (this.tail==null){
            return false;
        }else{
            return o == this.head || this.tail.contains(o);
        }
    }

    @Override
    public Iterator<Element> iterator() {
        return new Iterator<Element>() {
            elementsList temp = elementsList.this;
            @Override
            public boolean hasNext() {
                return temp.tail!=null;
            }

            @Override
            public Element next() {
                Element tempElement = temp.head;
                temp = temp.tail;
                if (temp==null){
                    throw new NoSuchElementException("To much elements ");
                }
                return tempElement;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] buf;
        buf = copyList.toArray();
        return buf;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] buf;
        buf = copyList.toArray(a);
        return buf;
    }

    public boolean add(Element o) {
        this.tail = this.copy();
        this.head = o;
        if (o!=null){
            copyList.add(o.copy());
        }
        if (this.tail.head!=null){
            assert this.head != null;
            this.head.dischargeRate = this.tail.head.dischargeRate;
            this.head.currentState = this.tail.head.nextState;

        }
        System.out.println("Next called on "+head.name);
        this.head.next();
        return this.head.equals(o);
    }

    @Override
    public boolean remove(Object o) {
        System.out.println("NotImplemented remove(Object o)");
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el:c){
            Boolean k = this.contains(el);
            if (!k){
                return !k;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Element> c) {
        for(Element el:c){
            this.add(el);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Element> c) {
        System.out.println("NotImplemented addAll(int index, Collection<? extends Element> c)");
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        System.out.println("NotImplemented removeAll(Collection<?> c)");
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        System.out.println("NotImplemented retainAll(Collection<?> c) ");
        return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
    }

    @Override
    public Element get(int index) {
        if(index<0||index>size())
            throw new ArrayIndexOutOfBoundsException();
        System.out.println("Test");
        return this.size()==index?this.head:this.tail.get(index);
    }

    @Override
    public Element set(int index, Element element) {
        System.out.println("NotImplemented set(int index, Element element)");
        return null;
    }

    @Override
    public void add(int index, Element element) {
        System.out.println("NotImplemented add(int index, Element element)");
    }

    @Override
    public Element remove(int index) {
        System.out.println("NotImplemented remove(int index)");
        return null;
    }

    @Override
    public int indexOf(Object o) {
        System.out.println("NotImplemented indexOf(Object o)");
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        System.out.println("NotImplemented lastIndexOf(Object o)");
        return 0;
    }

    @Override
    public ListIterator<Element> listIterator() {
        System.out.println("NotImplemented listIterator()");
        return null;
    }

    @Override
    public ListIterator<Element> listIterator(int index) {
        System.out.println("NotImplemented listIterator(int index)");
        return null;
    }

    @Override
    public List<Element> subList(int fromIndex, int toIndex) {
        System.out.println("NotImplemented subList(int fromIndex, int toIndex)");
        return null;
    }

    public String toString(){
        String res = (this.tail.tail)==null?this.head.name + "]":this.head.name+", "+this.tail.toString().substring(1);
        return "[" + res;
    }

    public static void main(String[] args) {
        elementsList ys = new elementsList();
        for (int i=1;i<5; i++){
            Element k = new Helement(String.valueOf(i), new State(),60,80.0,50,50,new tube(60,6,20));
            ys.add(k);
        }
        System.out.println(ys);
        for(Element el:ys){
            System.out.println("Name " + el.name);
            System.out.println("dP   " +(el.currentState.pressure-el.nextState.pressure));
            System.out.println("Vel  "+el.currentState.velocity);
        }
        ys.update(new Helement(String.valueOf("mock"), new State(),60,80.0,200,50,new tube(60,6,20)));
        for(Element el:ys){
            System.out.println("Name " + el.name);
            System.out.println("dP   " +(el.currentState.pressure-el.nextState.pressure));
            System.out.println("Vel  "+el.currentState.velocity);
        }
    }
}

public class Contour {
    public elementsList el;
    AbstractUnivariateSolver solver;
    dataSeries series;
    Contour(elementsList el){
        this.el =el;
        series = new dataSeries(el.toString());
        solver = new BrentSolver(1e-4,1e-4);
    }
    public void solve(){
        double convergencePoint = -1;
        double upperIntervalBound = 35;
        int retriesLeft = 8;
        double k = 0.5;
        UnivariateFunction function = v ->{
            Element element = this.el.copyFirst();
            element.setDischarge(v);
            this.el.update(element);
            return this.el.head.getParam()-this.el.first().getParam();
        };
        while (retriesLeft > 0 && convergencePoint == -1) {
            System.out.println(this.el.toString());
            try {
                convergencePoint = solver.solve(100, function, 20, k + upperIntervalBound,35);
                series.add(new XYChart.Data<>(k, convergencePoint));
                System.out.println("Solution is = " + convergencePoint);
            } catch (NoBracketingException e) {
                retriesLeft--;
                upperIntervalBound /= 2;
                System.out.println(String.format("No solution could be found in interval %f for %f, " +
                        "number of retries %d", upperIntervalBound, k, retriesLeft));
            }
        }
        if (convergencePoint == -1) {
            throw new IllegalStateException(String.format("No solution could be found in interval %f", upperIntervalBound));
        }
    }


    public double solveWithReturn() throws IllegalStateException{
        double convergencePoint = -1;
        double upperIntervalBound = 35;
        int retriesLeft = 8;
        double k = 0.5;
        UnivariateFunction function = v ->{
            Element element = this.el.copyFirst();
            element.setDischarge(v);
            this.el.update(element);
            return this.el.head.getParam()-this.el.first().getParam();
        };
        while (retriesLeft > 0 && convergencePoint == -1) {
            System.out.println(this.el.toString());
            try {
                convergencePoint = solver.solve(100, function, 20, k + upperIntervalBound,35);
                series.add(new XYChart.Data<>(k, convergencePoint));
                System.out.println("Solution is = " + convergencePoint);
                return convergencePoint;
            } catch (NoBracketingException e) {
                retriesLeft--;
                upperIntervalBound /= 2;
                System.out.println(String.format("No solution could be found in interval %f for %f, " +
                        "number of retries %d", upperIntervalBound, k, retriesLeft));
            }
        }
        if (convergencePoint == -1) {
            throw new IllegalStateException(String.format("No solution could be found in interval %f", upperIntervalBound));
        }
        return -1;
    }

    public static double test(double h){
        elementsList ys = new elementsList();
        State init = new State(102.30/10.1972, 336.26*4.1868, 2);
        //String name,State state, double height, double heatFlux, double dischargeRate,int n, Resistance res
        Helement op =  new Helement("Waterfall", init, -25.2,0,30, 1,new tube(83,5,28.29 ));
        Helement wt = new Helement("WaterWall", new State(),21.82, h, 30, 9 , new tube(60,6,21.82));
        Helement st = new Helement("Steam", new State(), 4.18,0,30,1,new tube(133,10,10.7));
        ys.add(op);
        ys.add(wt);
        ys.add(st);
        Contour c = new Contour(ys);
        c.solve();
        return  c.solveWithReturn();
    }

    public static void main(String[] args) {
        elementsList ys = new elementsList();
        State init = new State(102.30/10.1972, 336.26*4.1868, 2);
        //String name,State state, double height, double heatFlux, double dischargeRate,int n, Resistance res
        Helement op =  new Helement("Waterfall", init, -25.2,0,30, 1,new tube(83,5,28.29 ));
        Helement wt = new Helement("WaterWall", new State(),21.82, 2304.46, 30, 9 , new tube(60,6,21.82));
        Helement st = new Helement("Steam", new State(), 4.18,0,30,1,new tube(133,10,10.7));
        ys.add(op);
        ys.add(wt);
        ys.add(st);
        Contour c = new Contour(ys);
        c.solve();
    }
}
