package com.Grupo3.arquiteconvencionales.config;

public final class Constantes {

    private Constantes() {
    }

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String DEFAULT_ROLE = ROLE_USER;

    public static final String PREFIJO_PREGUNTA = "pregunta_";

    public static final String ERR_USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    public static final String ERR_USUARIO_YA_EXISTE = "El nombre de usuario ya existe";
    public static final String ERR_EMAIL_YA_REGISTRADO = "El correo electrónico ya está registrado";

    // VIEW THYMELEAF
    public static final String VIEW_LOGIN = "login";
    public static final String VIEW_REGISTRO = "registro";
    public static final String VIEW_INDEX = "index";
    public static final String VIEW_INFO_ARQUITECTURA = "info-arquitectura";
    public static final String VIEW_CUESTIONARIO = "cuestionario";
    public static final String VIEW_RESULTADO = "resultado";
    public static final String VIEW_MIS_NOTAS = "mis-notas";
    public static final String VIEW_ADMIN_REPORTES = "admin-reportes";

    public static final String TEMA_DEFAULT = "General";

    public static final String RUTAS_PUBLICAS = "/,/login,/registro,/css/**,/js/**,/img/**";
    public static final String RUTAS_ADMIN = "/admin/**";
    public static final String RUTAS_AUTENTICADOS = "/cuestionario,/info-arquitectura,/mis-notas";
}