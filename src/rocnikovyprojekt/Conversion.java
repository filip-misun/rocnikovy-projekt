/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

/**
 *
 * @author Dodo
 */
public interface Conversion {
    public FiniteDescription convert(FiniteDescription a);
    public Class<? extends FiniteDescription> getFrom();
    public Class<? extends FiniteDescription> getTo();
}

/*Zopar tried na ilustraciu fungovania:*/

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

}