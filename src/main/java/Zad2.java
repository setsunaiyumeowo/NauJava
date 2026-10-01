import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Zad2 {
    public static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("n = ?");
        int n = scanner.nextInt();
        while (n<=0){
            System.out.println("n !<= 0");
            System.out.println("n = ?");
            n = scanner.nextInt();
        }
        ArrayList<Double> list = new ArrayList<>(n);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            double variable = random.nextDouble(201.0) - 100;
            list.add(variable);
        }
        System.out.println("Список:" + list) ;
        for (int i = 0; i < list.size() - 1;i++){
            int min = i;
            for (int j = i + 1; j < list.size();j++){
                if (list.get(j) < list.get(min)){
                    min = j;
                }
            }
            double temporary = list.get(i);
            list.set(i,list.get(min));
            list.set(min,temporary);
        }
        System.out.println("Отсортированный списко методом сортировки выбором:");
        System.out.println(list);
    }
}
