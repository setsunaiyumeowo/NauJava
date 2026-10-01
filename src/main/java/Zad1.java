import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Zad1 {
    public static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("n = ?");
        int n = scanner.nextInt();
        while (n<=0){
            System.out.println("n !<= 0");
            System.out.println("n = ?");
            n = scanner.nextInt();
        }
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(201) - 100;
        }
        System.out.println("Массив:" + Arrays.toString(array));
        Integer lastPositive = null;
        for (int i = array.length - 1; i >= 0; i--){
            if (array[i] > 0){
                lastPositive = array[i];
                break;
            }
        }
        if (lastPositive != null){
            System.out.println("Последний положительный элемент в массиве = " + lastPositive);
        } else {
            System.out.println("Последний положительный элемент отсуствует");
        }
    }
}
