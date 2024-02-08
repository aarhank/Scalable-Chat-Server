package com.aarhankhan.scalablechatserver;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class ScalableChatServerApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(ScalableChatServerApplication.class);
        app.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass,
                                    PrintStream out) {
                out.print("\n\n\tJust learned we could also change the banner lol!\n\n".toUpperCase());
            }
        });
        app.run(args);
    }

}
