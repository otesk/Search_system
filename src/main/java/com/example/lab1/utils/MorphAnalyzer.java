package com.example.lab1.utils;

import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;

public class MorphAnalyzer {
    private static JMorfSdk instance;

    public static synchronized JMorfSdk getInstance() {
        if (instance == null) {
            instance = JMorfSdkFactory.loadFullLibrary();
        }
        return instance;
    }
}
