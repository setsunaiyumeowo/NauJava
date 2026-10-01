import java.util.LinkedList;
import java.util.Queue;

public class Zad5 implements Task {
    private final Queue<String> queue = new LinkedList<>();
    private volatile boolean isRunning = false;
    private Thread workingThread;
    public Zad5(){
        for (int i = 1; i <= 10; i++){
            queue.add("Пакет данных №"+i);
        }
    }
    @Override
    public void start() {
        isRunning = true;
        workingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning && !queue.isEmpty()) {
                    String data = queue.poll();
                    System.out.println("Обработано: " + data);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("Прерван");
                    }
                }
                System.out.println("Конец");
            }
        });
        workingThread.start();
    }
    @Override
    public void stop(){
        System.out.println("Стоп");
        isRunning = false;
    }
    public static void main() throws Exception{
        Zad5 zad5 = new Zad5();
        zad5.start();
        Thread.sleep(3000);
        zad5.stop();
    }
}
