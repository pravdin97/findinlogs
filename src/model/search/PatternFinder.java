package model.search;

import javafx.util.Pair;

import java.util.ArrayList;

public class PatternFinder {
    private static int NO_OF_CHARS = 256;

    private static int max (int a, int b) { return (a > b)? a: b; }

    private static void badChar(char []str, int size, int badchar[])
    {
        int i;

        for (i = 0; i < NO_OF_CHARS; i++)
            badchar[i] = -1;

        for (i = 0; i < size; i++)
            badchar[(int) str[i]] = i;
    }

    public static ArrayList<Pair<Integer, Integer>> search(char txt[], char pat[])
    {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        int m = pat.length;
        int n = txt.length;

        int badchar[] = new int[NO_OF_CHARS];

        badChar(pat, m, badchar);

        int begin = 0, end = 0;

        while(begin <= (n - m))
        {
            int j = m-1;
            end = begin + j;

            while(j >= 0 && pat[j] == txt[begin+j])
                j--;

            if (j < 0)
            {
                //debug
                System.out.println("Pattern find from: " + begin + " to: "+ end);

                //release
                result.add(new Pair<>(begin, end));

                begin += (begin+m < n)? m-badchar[txt[begin+m]] : 1;
            }

            else
                begin += max(1, j - badchar[txt[begin+j]]);
        }

        return result;
    }
}
