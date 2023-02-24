package dev.akif.crud.config

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class MySqlDBTestContext: ApplicationContextInitializer<ConfigurableApplicationContext?>{

     override fun initialize(applicationContext: ConfigurableApplicationContext) {
            val mySQLContainer = MySQLContainer("mysql:8.0.24");
            mySQLContainer.start()

            TestPropertyValues.of(
                "spring.datasource.url=$mySQLContainer.getJdbcUrl()",
                "spring.datasource.username=$mySQLContainer.getUsername()",
                "spring.datasource.password=$mySQLContainer.getPassword()"
            ).applyTo(applicationContext)
        }
}