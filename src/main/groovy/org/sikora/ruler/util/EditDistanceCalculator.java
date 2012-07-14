package org.sikora.ruler.util;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.7.12
 * Time: 0:43
 * To change this template use File | Settings | File Templates.
 */
public class EditDistanceCalculator {
    private static final int INSERTION_COST = 1;
    private static final int DELETION_COST = 1;
    private static final int SWAPING_COST = 1;
    private static final int CASE_CHANGE_COST = 1;
    private static final int SUBSTITUTION_COST = 1;


    private final CharSequence original;
    private final int[][] distance;

    public static EditDistanceCalculator of(final CharSequence original) {
        return new EditDistanceCalculator(original);
    }

    public EditDistanceCalculator(final CharSequence original) {
        this.original = original;
        distance = new int[original.length() + 1][original.length() + 1];
    }

    public int distance(final CharSequence similar) {
        final int originalSize = original.length();
        final int similarSize = similar.length();
        if (similarSize > originalSize)
            return Integer.MAX_VALUE;
        for (int i = 0; i <= originalSize; i++) {
            distance[i][0] = i;
            distance[0][i] = i;
        }
        for (int i = 1; i <= originalSize; i++) {
            for (int j = 1; j <= similarSize; j++) {
                if (original.charAt(i - 1) == similar.charAt(j - 1)) {
                    distance[i][j] = distance[i - 1][j - 1];
                    continue;
                }
                distance[i][j] = minimum(distance[i][j - 1] + 1, distance[i -1][j] + 1, distance[i - 1][j -1] + 1);

            }
        }
        return distance[originalSize][similarSize];
    }

    private static int minimum(final int a, final int b, final int c) {
        int min = a;
        if (b < min)
            min = b;
        if (c < min)
            min = c;
        return  min;
    }

    private void dumpDistance() {
        for (int[] ints : distance) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
