package br.com.subscontrol;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(
        basePackages = "br.com.subscontrol.infraestructure",
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[PostgreSQLGateway]")
        }
)
@DataJpaTest
@ExtendWith(CleanUpExtension.class)
public @interface PostgreSQLGatewayTest {}
