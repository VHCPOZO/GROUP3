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
        final String explicitUrl = property(env, "SPRING_DATASOURCE_URL");
        if (StringUtils.hasText(explicitUrl)) {
            if (explicitUrl.startsWith("jdbc:")) {
                return new Credentials(
                    explicitUrl,
                    property(env, "SPRING_DATASOURCE_USERNAME", ""),
                    property(env, "SPRING_DATASOURCE_PASSWORD", ""),
                    hostFromJdbcUrl(explicitUrl)
                );
            }
            final Credentials fromUrl = fromDatabaseUrl(explicitUrl);
            final String username = firstNonBlank(
                property(env, "SPRING_DATASOURCE_USERNAME"),
                fromUrl.username()
            );
            final String password = firstNonBlank(
                property(env, "SPRING_DATASOURCE_PASSWORD"),
                fromUrl.password()
            );
            return new Credentials(fromUrl.jdbcUrl(), username, password, fromUrl.hostForLog());
        }

        for (final String urlKey : new String[]{
            "DATABASE_PRIVATE_URL",
            "DATABASE_URL",
            "DATABASE_PUBLIC_URL",
            "POSTGRES_URL",
            "POSTGRES_PRIVATE_URL"
        }) {
            final String databaseUrl = property(env, urlKey);
            if (StringUtils.hasText(databaseUrl)) {
                return fromDatabaseUrl(databaseUrl);
            }
        }

        final String pgHost = firstNonBlank(
            property(env, "PGHOST"),
            property(env, "POSTGRES_HOST"),
            property(env, "POSTGRES_HOSTNAME")
        );
        if (StringUtils.hasText(pgHost)) {
            final int port = intProperty(env, "PGPORT", "POSTGRES_PORT", 5432);
            final String database = firstNonBlank(
                property(env, "PGDATABASE"),
                property(env, "POSTGRES_DB"),
                "railway"
            );
            final String jdbcUrl = "jdbc:postgresql://%s:%d/%s".formatted(pgHost, port, database);
            return new Credentials(
                jdbcUrl,
                firstNonBlank(property(env, "PGUSER"), property(env, "POSTGRES_USER"), ""),
                firstNonBlank(property(env, "PGPASSWORD"), property(env, "POSTGRES_PASSWORD"), ""),
                pgHost
            );
        }

        if (isRailwayDeployment(env)) {
            logDatabaseDiagnostics(env);
            throw new IllegalStateException(
                "PostgreSQL no configurado en el servicio de la APP. "
                    + "En Railway: servicio APP -> Variables -> pega DATABASE_URL copiada desde Postgres "
                    + "(o Add Reference -> DATABASE_URL). Luego redeploy."
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
        return StringUtils.hasText(property(env, "RAILWAY_ENVIRONMENT"))
            || StringUtils.hasText(property(env, "RAILWAY_ENVIRONMENT_NAME"))
            || StringUtils.hasText(property(env, "RAILWAY_PROJECT_ID"))
            || StringUtils.hasText(property(env, "RAILWAY_SERVICE_ID"));
    }

    private static String property(final Environment env, final String key) {
        final String value = env.getProperty(key);
        return value != null ? value.strip() : null;
    }

    private static String property(final Environment env, final String key, final String defaultValue) {
        final String value = property(env, key);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private static int intProperty(
        final Environment env,
        final String primaryKey,
        final String secondaryKey,
        final int defaultValue
    ) {
        final String primary = property(env, primaryKey);
        if (StringUtils.hasText(primary)) {
            return Integer.parseInt(primary);
        }
        final String secondary = property(env, secondaryKey);
        if (StringUtils.hasText(secondary)) {
            return Integer.parseInt(secondary);
        }
        return defaultValue;
    }

    private static String firstNonBlank(final String... values) {
        for (final String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private static void logDatabaseDiagnostics(final Environment env) {
        log.error(
            "Diagnostico BD en Railway: DATABASE_URL={}, DATABASE_PRIVATE_URL={}, PGHOST={}, "
                + "SPRING_DATASOURCE_URL={}",
            isSet(env, "DATABASE_URL"),
            isSet(env, "DATABASE_PRIVATE_URL"),
            isSet(env, "PGHOST"),
            isSet(env, "SPRING_DATASOURCE_URL")
        );
    }

    private static String isSet(final Environment env, final String key) {
        return StringUtils.hasText(property(env, key)) ? "SI" : "NO";
    }

    private static Credentials fromDatabaseUrl(final String databaseUrl) {
        if (databaseUrl.startsWith("jdbc:")) {
            return new Credentials(databaseUrl, "", "", hostFromJdbcUrl(databaseUrl));
        }

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
