package com.Grupo3.arquiteconvencionales.config;

import com.Grupo3.arquiteconvencionales.model.Pregunta;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import com.Grupo3.arquiteconvencionales.repository.PreguntaRepository;
import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_PASSWORD_HASH =
        "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final UsuarioRepository usuarioRepository;
    private final PreguntaRepository preguntaRepository;

    @Override
    public void run(final String... args) {
        if (usuarioRepository.count() == 0) {
            final Usuario admin = new Usuario(
                "admin",
                ADMIN_PASSWORD_HASH,
                "admin@universidad.com",
                Constantes.ROLE_ADMIN
            );
            usuarioRepository.save(admin);
        }

        if (preguntaRepository.count() == 0) {
            preguntaRepository.saveAll(preguntasIniciales());
        }
    }

    private static List<Pregunta> preguntasIniciales() {
        return List.of(
            new Pregunta(
                "¿Qué es la arquitectura convencional?",
                "Es un estilo de construcción que utiliza materiales tradicionales y métodos probados históricamente",
                "Es un tipo de arquitectura sin ningún fundamento técnico",
                "Es un sistema computacional para diseñar edificios",
                "Es una arquitectura exclusivamente moderna",
                "A",
                "Conceptos Básicos"
            ),
            new Pregunta(
                "¿Cuál es una característica principal de la arquitectura brutalista?",
                "Uso de concreto visto (hormigón) sin acabado ornamental",
                "Decoración excesiva en las fachadas",
                "Uso exclusivo de materiales reciclados",
                "Predominio de formas curvas orgánicas",
                "A",
                "Estilos Arquitectónicos"
            ),
            new Pregunta(
                "El movimiento moderno en arquitectura se caracterizó por:",
                "Priorizar la función sobre la forma (forma sigue función)",
                "Agregar elementos decorativos a cada estructura",
                "Usar únicamente materiales nobles como mármol",
                "Construir exclusivamente edificios religiosos",
                "A",
                "Historia"
            ),
            new Pregunta(
                "¿Qué material es característico de la arquitectura convencional tradicional?",
                "Ladrillo, concreto y acero",
                "Plástico y materiales sintéticos",
                "Vidrio templado exclusivamente",
                "Materiales biodegradables únicamente",
                "A",
                "Materiales"
            ),
            new Pregunta(
                "La arquitectura postmoderna surge como reacción a:",
                "El modernismo y su austeridad formal",
                "La arquitectura gótica",
                "Las construcciones prehistóricas",
                "El arte abstracto",
                "A",
                "Historia"
            ),
            new Pregunta(
                "¿Qué significa el término \"planta libre\" en arquitectura?",
                "Espacios interiores sin muros load-bearing que permiten flexibilidad",
                "Edificios sin techo",
                "Construcciones sin cimientos",
                "Casas construidas en un solo nivel",
                "A",
                "Conceptos Técnicos"
            ),
            new Pregunta(
                "¿Cuál es uno de los principios del diseño arquitectónico sostenible?",
                "Optimizar el uso de recursos naturales y energía",
                "Maximizar el consumo de materiales nuevos",
                "Construir sin considerar el impacto ambiental",
                "Ignorar las condiciones climáticas del lugar",
                "A",
                "Sostenibilidad"
            ),
            new Pregunta(
                "Los elementos estructurales básicos en una edificación convencional incluyen:",
                "Cimientos, muros, vigas, columnas, losas y techos",
                "Solo muros y techos",
                "Únicamente columnas de vidrio",
                "Exclusivamente sistemas eléctricos",
                "A",
                "Elementos Constructivos"
            ),
            new Pregunta(
                "¿Qué estilo arquitectónico se caracteriza por formas geométricas simples y volúmenes limpios?",
                "Arquitectura moderna",
                "Arquitectura barroca",
                "Arquitectura neoclásica ornamental",
                "Arquitectura renacentista",
                "A",
                "Estilos Arquitectónicos"
            ),
            new Pregunta(
                "La función principal de los cimientos en una estructura es:",
                "Soportar y distribuir las cargas de toda la construcción al suelo",
                "Decorar la base del edificio",
                "Permitir el paso de servicios básicos",
                "Crear espacios habitables en el subsuelo",
                "A",
                "Elementos Constructivos"
            )
        );
    }
}
