package src.backend_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendModuleApplication.class, args);
    }

}
