import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Zad3 {
    public static void main() {
        ArrayList<Employee> employee = new ArrayList<>();
        employee.add(new Employee("Гэйб Ньюэлл",63, "CEO", 10000000000000000.0));
        employee.add(new Employee("Робин Уокер",49, "Game Design", 250000.0));
        employee.add(new Employee("Пьер-Лу Гриффе",38, "Software Engineering", 220000.0));
        employee.add(new Employee("Майк Мораски",58, "Audio", 1800000.0));
        employee.add(new Employee("Лоуренс Янг",42, "Product Design", 1900000.0));
        employee.add(new Employee("Джей Пинкертон",47, "Writing", 1700000.0));
        List<String> formattedList = employee.stream().map(person -> person.getFullName() + " - " + person.getDepartment()).collect(Collectors.toList());
        System.out.println("Результат работы: ");
        formattedList.forEach(System.out::println);
    }
}
