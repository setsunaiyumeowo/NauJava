package ru.KodinMD.NauJava.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.KodinMD.NauJava.entity.GeneratedPassword;

@Configuration
public class BeanCFG {
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public List<GeneratedPassword> passwordContainer(){
        return new ArrayList<>();
    }
}
