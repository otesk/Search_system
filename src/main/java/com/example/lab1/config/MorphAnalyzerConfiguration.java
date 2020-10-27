package com.example.lab1.config;

import com.example.lab1.utils.MorphAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;

@Configuration
public class MorphAnalyzerConfiguration {
    @Bean
    public JMorfSdk jmorfSdk() {
        return MorphAnalyzer.getInstance();
    }
}
