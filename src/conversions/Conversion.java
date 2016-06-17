package conversions;

import languages.FiniteDescription;

public interface Conversion {
    public FiniteDescription convert(FiniteDescription a);
    public Class<? extends FiniteDescription> getFrom();
    public Class<? extends FiniteDescription> getTo();
}

/*Zopar tried na ilustraciu fungovania:*/
/*
class A1 implements FiniteDescription{
    int a;
    int b;
    
    public A1(int a_, int b_){
        a = a_;
        b = b_;
    }
}

class A2 implements FiniteDescription{
    int x;
    
    public A2(int x_){
        x = x_;
    }
}

class A3 implements FiniteDescription{
    String s;
    
    public A3(String s_){
        s = s_;
    }
}

class A4 implements FiniteDescription{
    String s;
    int a;
    
    public A4(String s_, int a_){
        s = s_;
        a = a_;
    }
}

class C1to2 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A1 a1 = (A1) a;
        return new A2(a1.a + a1.b);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A1.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A2.class;
    }

}

class C1to3 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A1 a1 = (A1) a;
        return new A3(a1.a + "," + a1.b);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A1.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A3.class;
    }

}

class C3to2 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A3 a3 = (A3) a;
        return new A2(a3.s.length());
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A3.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A2.class;
    }

}

class C2to1 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A2 a2 = (A2) a;
        return new A1(a2.x,47);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A2.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A1.class;
    }

}

class C4to3 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A4 a4 = (A4) a;
        return new A3(a4.s);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A4.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A3.class;
    }

}

class C4to2 implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        A4 a4 = (A4) a;
        return new A2(a4.s.length() + 100*a4.a);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return A4.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return A2.class;
    }

}*/