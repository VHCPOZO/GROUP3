package com.Grupo3.arquiteconvencionales.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    DataSource dataSource(final Environment env, final DataSourceProperties properties) {
        final Credentials credentials = resolveCredentials(env);

        final HikariDataSource dataSource = properties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        dataSource.setJdbcUrl(credentials.jdbcUrl());
        dataSource.setUsername(credentials.username());
        dataSource.setPassword(credentials.password());
        dataSource.setDriverClassName("org.postgresql.Driver");

        log.info("Conectando a PostgreSQL en host={}", credentials.hostForLog());
        return dataSource;
    }

    private static Credentials resolveCredentials(final Environment env) {
        final String explicitUrl = env.getProperty("SPRING_DATASOURCE_URL");
        if (StringUtils.hasText(explicitUrl)) {
            return new Credentials(
                explicitUrl,
                env.getProperty("SPRING_DATASOURCE_USERNAME", ""),
                env.getProperty("SPRING_DATASOURCE_PASSWORD", ""),
                hostFromJdbcUrl(explicitUrl)
            );
        }

        final String privateUrl = env.getProperty("DATABASE_PRIVATE_URL");
        if (StringUtils.hasText(privateUrl)) {
            return fromDatabaseUrl(privateUrl);
        }

        final String databaseUrl = env.getProperty("DATABASE_URL");
        if (StringUtils.hasText(databaseUrl)) {
            return fromDatabaseUrl(databaseUrl);
        }

        final String pgHost = env.getProperty("PGHOST");
        if (StringUtils.hasText(pgHost)) {
            final int port = env.getProperty("PGPORT", Integer.class, 5432);
            final String database = env.getProperty("PGDATABASE", "railway");
            final String jdbcUrl = "jdbc:postgresql://%s:%d/%s".formatted(pgHost, port, database);
            return new Credentials(
                jdbcUrl,
                env.getProperty("PGUSER", ""),
                env.getProperty("PGPASSWORD", ""),
                pgHost
            );
        }

        if (isRailwayDeployment(env)) {
            throw new IllegalStateException(
                "PostgreSQL no configurado en Railway. Crea un servicio PostgreSQL, vinculalo a esta app "
                    + "y agrega la referencia DATABASE_URL (o PGHOST/PGUSER/PGPASSWORD) en Variables."
            );
        }

        log.warn("Sin variables de BD; usando localhost (solo desarrollo local)");
        return new Credentials(
            "jdbc:postgresql://localhost:5432/arquiteconvencionales",
            "postgres",
            "postgres",
            "localhost"
        );
    }

    private static boolean isRailwayDeployment(final Environment env) {
        return StringUtils.hasText(env.getProperty("RAILWAY_ENVIRONMENT"))
            || StringUtils.hasText(env.getProperty("RAILWAY_PROJECT_ID"))
            || StringUtils.hasText(env.getProperty("RAILWAY_SERVICE_ID"));
    }

    private static Credentials fromDatabaseUrl(final String databaseUrl) {
        try {
            final String normalized = databaseUrl.replace("postgres://", "postgresql://");
            final URI uri = URI.create(normalized);

            final String username;
            final String password;
            final String userInfo = uri.getUserInfo();
            if (userInfo != null && userInfo.contains(":")) {
                final int separator = userInfo.indexOf(':');
                username = decode(userInfo.substring(0, separator));
                password = decode(userInfo.substring(separator + 1));
            } else {
                username = "";
                password = "";
            }

            final int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            final String jdbcUrl = "jdbc:postgresql://%s:%d%s".formatted(uri.getHost(), port, uri.getPath());

            return new Credentials(jdbcUrl, username, password, uri.getHost());
        } catch (RuntimeException ex) {
            throw new IllegalStateException("No se pudo interpretar DATABASE_URL", ex);
        }
    }

    private static String decode(final String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String hostFromJdbcUrl(final String jdbcUrl) {
        try {
            final String withoutPrefix = jdbcUrl.replace("jdbc:postgresql://", "");
            final int slash = withoutPrefix.indexOf('/');
            final String hostPort = slash >= 0 ? withoutPrefix.substring(0, slash) : withoutPrefix;
            final int colon = hostPort.indexOf(':');
            return colon >= 0 ? hostPort.substring(0, colon) : hostPort;
        } catch (RuntimeException ex) {
            return "desconocido";
        }
    }

    private record Credentials(String jdbcUrl, String username, String password, String hostForLog) {}
}
