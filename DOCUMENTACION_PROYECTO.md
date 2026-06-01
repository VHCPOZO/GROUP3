    # Documentación del Proyecto - Arquitectura Convencionales

    ## 📋 Índice
    1. [Modelo (Model)](#modelo)
    2. [Repositorios (Repository)](#repositorios)
    3. [Servicios (Service)](#servicios)
    4. [Controladores (Controller)](#controladores)
    5. [Configuración](#configuración)
    6. [Anotaciones Explicadas](#anotaciones)

    ---

    ## 1. Modelo (Model) 📦

    ### Usuario.java
    ```java
    @Entity
    @Table(name = "usuarios")
    @Getter
    @Setter
    public class Usuario { ... }
    ```

    **Propiedades:**
    | Campo | Tipo | Descripción |
    |-------|------|-------------|
    | `id` | Long | Identificador único del usuario |
    | `username` | String | Nombre de usuario (único) |
    | `password` | String | Contraseña encriptada |
    | `email` | String | Correo electrónico (único) |
    | `fechaRegistro` | LocalDateTime | Fecha de registro automático |
    | `rol` | String | Rol del usuario (ROLE_USER o ROLE_ADMIN) |

    ---

    ### Pregunta.java
    ```java
    @Entity
    @Table(name = "preguntas")
    @Getter
    @Setter
    public class Pregunta { ... }
    ```

    **Propiedades:**
    | Campo | Tipo | Descripción |
    |-------|------|-------------|
    | `id` | Long | Identificador único |
    | `pregunta` | String | Texto de la pregunta |
    | `opcionA` | String | Opción A |
    | `opcionB` | String | Opción B |
    | `opcionC` | String | Opción C |
    | `opcionD` | String | Opción D |
    | `respuestaCorrecta` | String | Respuesta correcta (A, B, C o D) |
    | `tema` | String | Tema/categoría de la pregunta |

    ---

    ### Resultado.java
    ```java
    @Entity
    @Table(name = "resultados")
    @Getter
    @Setter
    public class Resultado { ... }
    ```

    **Propiedades:**
    | Campo | Tipo | Descripción |
    |-------|------|-------------|
    | `id` | Long | Identificador único |
    | `usuario` | Usuario | Relación ManyToOne con Usuario |
    | `puntaje` | Integer | Porcentaje de aciertos (0-100) |
    | `totalPreguntas` | Integer | Total de preguntas del cuestionario |
    | `respuestasCorrectas` | Integer | Número de respuestas correctas |
    | `fechaRealizacion` | LocalDateTime | Fecha de realización |
    | `tema` | String | Tema del cuestionario |

    ---

    ## 2. Repositorios (Repository) 🗄️

    ### UsuarioRepository.java
    ```java
    @Repository
    public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
        Optional<Usuario> findByUsername(String username);
        Optional<Usuario> findByEmail(String email);
        boolean existsByUsername(String username);
        boolean existsByEmail(String email);
    }
    ```

    **Métodos:**
    - `findByUsername()` - Busca usuario por nombre de usuario
    - `findByEmail()` - Busca usuario por correo electrónico
    - `existsByUsername()` - Verifica si existe un username
    - `existsByEmail()` - Verifica si existe un email

    ---

    ### PreguntaRepository.java
    ```java
    @Repository
    public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
        List<Pregunta> findByTema(String tema);
        List<Pregunta> findAllByOrderByIdAsc();
    }
    ```

    **Métodos:**
    - `findByTema()` - Obtiene preguntas por tema específico
    - `findAllByOrderByIdAsc()` - Obtiene todas las preguntas ordenadas por ID

    ---

    ### ResultadoRepository.java
    ```java
    @Repository
    public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
        List<Resultado> findByUsuarioOrderByFechaRealizacionDesc(Usuario usuario);
        List<Resultado> findByUsuarioUsername(String username);
        List<Resultado> findAllByOrderByFechaRealizacionDesc();
    }
    ```

    **Métodos:**
    - `findByUsuarioOrderByFechaRealizacionDesc()` - Resultados de un usuario ordenados por fecha
    - `findByUsuarioUsername()` - Resultados por nombre de usuario
    - `findAllByOrderByFechaRealizacionDesc()` - Todos los resultados ordenados por fecha

    ---

    ## 3. Servicios (Service) ⚙️

    ### CuestionarioService.java
    ```java
    @Service
    @RequiredArgsConstructor
    public class CuestionarioService { ... }
    ```

    **Métodos:**

    | Método | Descripción |
    |--------|-------------|
    | `obtenerTodasLasPreguntas()` | Retorna todas las preguntas del cuestionario |
    | `obtenerPreguntasPorTema(tema)` | Filtra preguntas por tema específico |
    | `registrarUsuario(username, password, email)` | Registra un nuevo usuario con contraseña encriptada |
    | `filtrarRespuestas(params)` | Filtra solo las respuestas del formulario (pregunta_*) |
    | `guardarResultado(usuarioId, respuestas)` | Calcula el puntaje y guarda el resultado |
    | `obtenerResultadosUsuario(username)` | Obtiene el historial de resultados de un usuario |
    | `obtenerTodosLosResultados()` | Obtiene todos los resultados (para admin) |

    **Flujo de guardarResultado:**
    1. Busca el usuario por ID
    2. Obtiene todas las preguntas
    3. Compara cada respuesta del usuario con la respuesta correcta
    4. Calcula el porcentaje: `(correctas / total) * 100`
    5. Crea y guarda el resultado en la base de datos

    ---

    ## 4. Controladores (Controller) 🎮

    ### AuthController.java
    ```java
    @Controller
    @RequiredArgsConstructor
    public class AuthController { ... }
    ```

    **Endpoints:**

    | Método | Ruta | Descripción |
    |--------|------|-------------|
    | `login()` | GET `/login` | Muestra la página de login |
    | `registro()` | GET `/registro` | Muestra la página de registro |
    | `procesarRegistro()` | POST `/registro` | Procesa el registro de nuevo usuario |

    ---

    ### CuestionarioController.java
    ```java
    @Controller
    @RequiredArgsConstructor
    public class CuestionarioController { ... }
    ```

    **Endpoints:**

    | Método | Ruta | Descripción |
    |--------|------|-------------|
    | `mostrarCuestionario()` | GET `/cuestionario` | Muestra el cuestionario con todas las preguntas |
    | `procesarCuestionario()` | POST `/cuestionario` | Procesa las respuestas y guarda el resultado |

    ---

    ### InfoController.java
    ```java
    @Controller
    @RequiredArgsConstructor
    public class InfoController { ... }
    ```

    **Endpoints:**

    | Método | Ruta | Descripción |
    |--------|------|-------------|
    | `index()` | GET `/` | Página principal (redirige a login) |
    | `infoArquitectura()` | GET `/info-arquitectura` | Información sobre arquitecturas |

    ---

    ### ReporteController.java
    ```java
    @Controller
    @RequiredArgsConstructor
    public class ReporteController { ... }
    ```

    **Endpoints:**

    | Método | Ruta | Descripción |
    |--------|------|-------------|
    | `verMisNotas()` | GET `/mis-notas` | Ver resultados del usuario actual |
    | `verTodosLosReportes()` | GET `/admin/reportes` | Ver todos los resultados (solo admin) |

    ---

    ## 5. Configuración ⚡

    ### Constantes.java
    ```java
    public final class Constantes { ... }
    ```

    **Constantes definidas:**

    | Constante | Valor | Descripción |
    |-----------|-------|-------------|
    | `ROLE_USER` | `"ROLE_USER"` | Rol de usuario normal |
    | `ROLE_ADMIN` | `"ROLE_ADMIN"` | Rol de administrador |
    | `DEFAULT_ROLE` | `"ROLE_USER"` | Rol por defecto al registrar |
    | `PREFIJO_PREGUNTA` | `"pregunta_"` | Prefijo para identificar respuestas |
    | `VIEW_LOGIN` | `"login"` | Nombre de la vista Thymeleaf |
    | `VIEW_REGISTRO` | `"registro"` | Nombre de la vista Thymeleaf |
    | `VIEW_CUESTIONARIO` | `"cuestionario"` | Nombre de la vista Thymeleaf |
    | `VIEW_RESULTADO` | `"resultado"` | Nombre de la vista Thymeleaf |
    | `VIEW_MIS_NOTAS` | `"mis-notas"` | Nombre de la vista Thymeleaf |
    | `VIEW_ADMIN_REPORTES` | `"admin-reportes"` | Nombre de la vista Thymeleaf |
    | `TEMA_DEFAULT` | `"General"` | Tema por defecto |
    | `RUTAS_PUBLICAS` | `"/,/login,/registro,/css/**,/js/**,/img/**"` | Rutas sin autenticación |
    | `RUTAS_ADMIN` | `"/admin/**"` | Rutas solo para admin |
    | `RUTAS_AUTENTICADOS` | `"/cuestionario,/info-arquitectura,/mis-notas"` | Rutas autenticadas |

    ---

    ### SecurityConfig.java
    ```java
    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig { ... }
    ```

    **Configuración de seguridad:**

    | Bean | Descripción |
    |------|-------------|
    | `passwordEncoder()` | Codificador de contraseñas usando BCrypt |
    | `userDetailsService()` | Carga usuarios desde la BD para Spring Security |
    | `filterChain()` | Configura las reglas de acceso y autenticación |

    **Reglas de acceso:**
    - Rutas públicas: `/`, `/login`, `/registro`, recursos estáticos
    - Rutas admin: Solo usuarios con `ROLE_ADMIN`
    - Rutas autenticadas: `/cuestionario`, `/info-arquitectura`, `/mis-notas`
    - Login: Formulario personalizado en `/login`
    - Logout: Cierra sesión y redirige a `/login?logout`

    ---

    ## 6. Anotaciones Explicadas 📝

    ### 📦@Entity
    Indica que la clase es una **entidad JPA**, es decir, una clase que se mapea a una tabla en la base de datos.

    ```java
    @Entity
    public class Usuario { ... }
    ```

    ---

    ### 📋@Table(name = "nombre_tabla")
    Especifica el **nombre de la tabla** en la base de datos. Si se omite, Spring usa el nombre de la clase.

    ```java
    @Table(name = "usuarios")
    ```

    ---

    ### 🔑@Id
    Marca el campo como la **clave primaria** de la entidad.

    ```java
    @Id
    private Long id;
    ```

    ---

    ### 🔄@GeneratedValue(strategy = GenerationType.IDENTITY)
    Configura la **generación automática del ID**. `IDENTITY` usa auto-increment en la base de datos.

    ```java
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ```

    ---

    ### 📝@Column
    Configura las propiedades de la columna en la base de datos.

    | Atributo | Descripción |
    |----------|-------------|
    | `name` | Nombre de la columna |
    | `unique` | ¿Valores únicos? |
    | `nullable` | ¿Permite valores nulos? |
    | `columnDefinition` | Definición SQL personalizada |

    ```java
    @Column(unique = true, nullable = false)
    private String username;
    ```

    ---

    ### 🔗@ManyToOne
    Define una **relación muchos a uno**. En `Resultado`, muchos resultados pueden pertenecer a un usuario.

    ```java
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    ```

    ---

    ### 🔗@JoinColumn
    Especifica la **columna de foreign key** en la relación.

    ```java
    @JoinColumn(name = "usuario_id", nullable = false)
    ```

    ---

    ### 📦@Getter y @Setter (Lombok)
    Genera automáticamente **getters y setters** para todos los campos de la clase.

    ```java
    @Getter
    @Setter
    public class Usuario { ... }
    // Genera: getId(), setId(), getUsername(), setUsername(), etc.
    ```

    ---

    ### 🏷️@RequiredArgsConstructor (Lombok)
    Genera un **constructor con todos los campos final**. Se usa para inyección de dependencias.

    ```java
    @RequiredArgsConstructor
    public class AuthController {
        private final CuestionarioService cuestionarioService;
    }
    // Genera: public AuthController(CuestionarioService cuestionarioService)
    ```

    ---

    ### 📂@Repository
    Marca la clase como un **componente de repositorio** de Spring. Permite la detección automática de beans.

    ```java
    @Repository
    public interface UsuarioRepository extends JpaRepository<Usuario, Long> { }
    ```

    ---

    ### 🛠️@Service
    Marca la clase como un **componente de servicio** de Spring.

    ```java
    @Service
    public class CuestionarioService { ... }
    ```

    ---

    ### 🎮@Controller
    Marca la clase como un **controlador de Spring MVC** que maneja solicitudes web.

    ```java
    @Controller
    public class AuthController { ... }
    ```

    ---

    ### 🌐@GetMapping y @PostMapping
    Mapean solicitudes HTTP a métodos del controlador.

    ```java
    @GetMapping("/login")      // GET request
    @PostMapping("/registro")  // POST request
    ```

    ---

    ### 📥@RequestParam
    Extrae parámetros de la **query string** o datos de formulario.

    ```java
    @PostMapping("/registro")
    public String procesarRegistro(
        @RequestParam final String username,
        @RequestParam final String password,
        ...
    )
    ```

    ---

    ### 🔒@Transactional
    Garantiza que el método se ejecute dentro de una **transacción de base de datos**.

    ```java
    @Transactional
    public Usuario registrarUsuario(...) { ... }
    ```

    ---

    ### 🔐@Configuration
    Marca la clase como una **clase de configuración** de Spring.

    ```java
    @Configuration
    public class SecurityConfig { ... }
    ```

    ---

    ### 🛡️@EnableWebSecurity
    Habilita la **configuración de seguridad web** de Spring Security.

    ```java
    @EnableWebSecurity
    ```

    ---

    ### 🔏@Bean
    Define un **bean** que será gestionado por el contenedor de Spring.

    ```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    ```

    ---

    ### 🔒@Autowired
    Inyecta una **dependencia automáticamente**. (En este proyecto se usa constructor con `@RequiredArgsConstructor`).

    ---

    ## 📊 Diagrama de Flujo

    ```
    ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
    │   Usuario   │────▶│  Controller  │────▶│   Service   │
    │   (Model)   │     │   (MVC)      │     │  (Lógica)   │
    └─────────────┘     └─────────────┘     └─────────────┘
                            │                    │
                            │                    ▼
                        ┌──────┴──────┐     ┌─────────────┐
                        │  Thymeleaf  │◀────│  Repository │
                        │   (Views)   │     │   (JPA)     │
                        └─────────────┘     └─────────────┘
                                                    │
                                                    ▼
                                            ┌─────────────┐
                                            │  PostgreSQL │
                                            │  (Datos)    │
                                            └─────────────┘
    ```

    ---

    ## 🔐 Flujo de Autenticación

    1. **Registro:** Usuario → Controller → Service → Repository → BD
    2. **Login:** Usuario → SecurityConfig → Validación BCrypt
    3. **Cuestionario:** Usuario autenticado → GET preguntas → POST respuestas → Service calcula → Guarda Resultado
    4. **Resultados:** Usuario → Service → Repository → Vista

    ---

    *Documento generado automáticamente*