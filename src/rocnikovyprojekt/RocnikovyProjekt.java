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
public class RocnikovyProjekt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A4 a = new A4("hello",47);
        Conversions c = new Conversions();
        c.print();
        A1 b = (A1) c.convert(a, A1.class);
        A4 d = (A4) c.convert(a, A4.class);
        System.out.println(b.a + " " + b.b);
        System.out.println(d.s + " " + d.a);
    }
    
}
