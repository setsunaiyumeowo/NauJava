/*
package ru.KodinMD.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.KodinMD.NauJava.console.CmdProcessor;
import java.util.Scanner;

@Configuration
public class ConsoleCFG {
    private final CmdProcessor commandProcessor;
    @Autowired
    public ConsoleCFG(CmdProcessor commandProcessor){
        this.commandProcessor = commandProcessor;
    }
    @Bean
    public CommandLineRunner commandScanner(){
        return args -> {
            try (Scanner scanner = new Scanner(System.in)){
                System.out.println("Write command (Command list - !help)");
                while (true){
                    System.out.print(">");
                    String input = scanner.nextLine();
                    if ("!exit".equalsIgnoreCase(input.trim())){
                        System.out.print("Exit");
                        for (int i = 0; i < 3; i++){
                            System.out.print(".");
                            System.out.flush();
                            try {
                                Thread.sleep(750);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        break;
                    }
                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
*/
