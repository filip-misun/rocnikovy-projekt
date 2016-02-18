package rocnikovyprojekt;

import conversions.Conversions;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
            NFAEpsilon a1 = new NFAEpsilon(new Scanner(new File("C:\\Users\\Dodo\\Documents\\Skola\\Programovanie\\RocnikovyProjekt\\rocnikovy-projekt\\a1.txt")));
            System.out.println(a1.accepts(new Word("aaabbb")));
            System.out.println(a1.accepts(Word.EMPTYWORD));
            System.out.println(a1.accepts(new Word("aabbbbb")));
            System.out.println(a1.accepts(new Word("baba")));
            System.out.println();
            NFA a2 = (NFA) Conversions.convert(a1, NFA.class);
            a2.print(System.out);
            System.out.println(a2.accepts(new Word("aaabbb")));
            System.out.println(a2.accepts(Word.EMPTYWORD));
            System.out.println(a2.accepts(new Word("aabbbbb")));
            System.out.println(a2.accepts(new Word("baba")));
            System.out.println();
            DFA a3 = (DFA) Conversions.convert(a1, DFA.class);
            System.out.println(a3.accepts(new Word("aaabbb")));
            System.out.println(a3.accepts(Word.EMPTYWORD));
            System.out.println(a3.accepts(new Word("aabbbbb")));
            System.out.println(a3.accepts(new Word("baba")));
            System.out.println();
	}
}
