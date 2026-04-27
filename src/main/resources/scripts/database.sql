-- =====================================================
-- SISTEMA EDUCATIVO - ARQUITECTURAS CONVENCIONALES
-- Script para PostgreSQL
-- =====================================================

-- 1. CREAR TABLA USUARIOS
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    fecha_registro TIMESTAMP,
    rol VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER'
);

-- 2. CREAR TABLA PREGUNTAS
CREATE TABLE preguntas (
    id BIGSERIAL PRIMARY KEY,
    pregunta TEXT NOT NULL,
    opciona TEXT NOT NULL,
    opcionb TEXT NOT NULL,
    opcionc TEXT NOT NULL,
    opciond TEXT NOT NULL,
    respuesta_correcta VARCHAR(1) NOT NULL,
    tema VARCHAR(255)
);

-- 3. CREAR TABLA RESULTADOS
CREATE TABLE resultados (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    puntaje INTEGER NOT NULL,
    total_preguntas INTEGER NOT NULL,
    respuestas_correctas INTEGER NOT NULL,
    fecha_realizacion TIMESTAMP NOT NULL,
    tema VARCHAR(255)
);

-- =====================================================
-- DATOS DE PRUEBA
-- =====================================================

-- Insertar usuario de prueba (password: admin123)
-- La contraseña está encriptada con BCrypt
INSERT INTO usuarios (username, password, email, fecha_registro, rol) 
VALUES (
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'admin@universidad.com',
    NOW(),
    'ROLE_ADMIN'
);

-- Insertar preguntas de Arquitectura Convencional
INSERT INTO preguntas (pregunta, opciona, opcionb, opcionc, opciond, respuesta_correcta, tema) VALUES
(
    '¿Qué es la arquitectura convencional?',
    'Es un estilo de construcción que utiliza materiales tradicionales y métodos probados históricamente',
    'Es un tipo de arquitectura sin ningún fundamento técnico',
    'Es un sistema computacional para diseñar edificios',
    'Es una arquitectura exclusivamente moderna',
    'A',
    'Conceptos Básicos'
),
(
    '¿Cuál es una característica principal de la arquitectura brutalista?',
    'Uso de concreto visto (hormigón) sin acabado ornamental',
    'Decoración excesiva en las fachadas',
    'Uso exclusivo de materiales reciclados',
    'Predominio de formas curvas orgánicas',
    'A',
    'Estilos Arquitectónicos'
),
(
    'El movimiento moderno en arquitectura se caracterizó por:',
    'Priorizar la función sobre la forma (forma sigue función)',
    'Agregar elementos decorativos a cada estructura',
    'Usar únicamente materiales nobles como mármol',
    'Construir exclusivamente edificios religiosos',
    'A',
    'Historia'
),
(
    '¿Qué material es característico de la arquitectura convencional tradicional?',
    'Ladrillo, concreto y acero',
    'Plástico y materiales sintéticos',
    'Vidrio templado exclusivamente',
    'Materiales biodegradables únicamente',
    'A',
    'Materiales'
),
(
    'La arquitectura postmoderna surge como reacción a:',
    'El modernismo y su austeridad formal',
    'La arquitectura gótica',
    'Las construcciones prehistóricas',
    'El arte abstracto',
    'A',
    'Historia'
),
(
    '¿Qué significa el término "planta libre" en arquitectura?',
    'Espacios interiores sin muros load-bearing que permiten flexibilidad',
    'Edificios sin techo',
    'Construcciones sin cimientos',
    'Casas construidas en un solo nivel',
    'A',
    'Conceptos Técnicos'
),
(
    '¿Cuál es uno de los principios del diseño arquitectónico sostenible?',
    'Optimizar el uso de recursos naturales y energía',
    'Maximizar el consumo de materiales nuevos',
    'Construir sin considerar el impacto ambiental',
    'Ignorar las condiciones climáticas del lugar',
    'A',
    'Sostenibilidad'
),
(
    'Los elementos estructurales básicos en una edificación convencional incluyen:',
    'Cimientos, muros, vigas, columnas, losas y techos',
    'Solo muros y techos',
    'Únicamente columnas de vidrio',
    'Exclusivamente sistemas eléctricos',
    'A',
    'Elementos Constructivos'
),
(
    '¿Qué estilo arquitectónico se caracteriza por formas geométricas simples y volúmenes limpios?',
    'Arquitectura moderna',
    'Arquitectura barroca',
    'Arquitectura neoclásica ornamental',
    'Arquitectura renacentista',
    'A',
    'Estilos Arquitectónicos'
),
(
    'La función principal de los cimientos en una estructura es:',
    'Soportar y distribuir las cargas de toda la construcción al suelo',
    'Decorar la base del edificio',
    'Permitir el paso de servicios básicos',
    'Crear espacios habitables en el subsuelo',
    'A',
    'Elementos Constructivos'
);

-- =====================================================
-- VERIFICAR DATOS INSERTADOS
-- =====================================================

-- Ver usuarios
SELECT * FROM usuarios;

-- Ver preguntas
SELECT id, pregunta, respuesta_correcta, tema FROM preguntas;

-- Ver cantidad de preguntas por tema
SELECT tema, COUNT(*) as total FROM preguntas GROUP BY tema;