import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class SornN {
    public static void main(String[] args) {
        int N = 10;
        int[] arr1 = new int[N];
        int[] arr2 = new int[N];
        int[] arr = new int[2 * N];
        for (int i = 0; i < N; i++) {
            arr1[i] = StdRandom.uniform(0, 255);
            arr2[i] = StdRandom.uniform(0, 255);
        }
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        System.arraycopy(arr1, 0, arr, 0, N);
        System.arraycopy(arr2, 0, arr, N, N);
        StdOut.print("Before: ");
        for (int i : arr
             ) {
            StdOut.print(i + "\t");
        }
        StdOut.println();
        int i = 0;
        int j = N;
        int count = 0;
        while (i < N){
            if(arr1[i] <= arr[j]){
                arr[count++] = arr1[i++];
            }else if(arr1[i] > arr[j]){
                arr[count++] = arr[j++];
            }
        }
        StdOut.print("After: ");
        for (int c : arr
        ) {
            StdOut.print(c + "\t");
        }
        StdOut.println();
    }
}
