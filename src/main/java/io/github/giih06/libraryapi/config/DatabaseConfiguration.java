package io.github.giih06.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuração de DataSource para a aplicação.
 * Fornece:
 * - DriverManagerDataSource simples (desabilitado) para cenários sem pool de conexões.
 * - HikariCP DataSource com configurações de pool otimizadas.
 */
@Configuration
@Slf4j
public class DatabaseConfiguration {

    // URL JDBC utilizada para conectar ao banco de dados.
    @Value("${spring.datasource.url}")
    String url;

    // Nome de usuário para autenticação no banco.
    @Value("${spring.datasource.username}")
    String username;

    // Senha para autenticação no banco.
    @Value("${spring.datasource.password}")
    String password;

    // Classe do driver JDBC.
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    /**
     * Cria e configura o DataSource principal utilizando o HikariCP.
     *
     * Configurações otimizadas para controle de conexões e desempenho da aplicação.
     *
     * @return DataSource configurado com pool Hikari
     */
    @Bean
    public DataSource hikariDataSource() {
        log.info("Iniciando conexão com o banco na URL: {} " + url);
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(100); //máximo de conexões liberadas
        config.setMinimumIdle(1); // Tamanho inicial do pool
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000); // tamanho máximo do tempo ( em milissegundos ) de uma conexão
        config.setConnectionTimeout(100000); // tempo gasto para obter uma conexão, caso falhe, lança um erro de timeout
        config.setConnectionTestQuery("select 1"); // teste para verificar se o banco está conectado

        return new HikariDataSource(config);
    }
}
