package top.fuyuaaa.shadowpuppets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author fuyuaaa
 * @creat: 2019-03-27 21:34
 */
@SpringBootApplication
@EnableCaching
public class ShadowPuppetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShadowPuppetsApplication.class, args);
    }

}
