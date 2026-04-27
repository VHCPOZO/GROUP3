// Scripts Generales para ArquiteConvencionales

document.addEventListener('DOMContentLoaded', function() {
    console.log('Arquitecturas Convencionales - Sistema Educativo');
    
    // Validación de formularios
    initFormValidation();
    
    // Animaciones suaves
    initAnimations();
});

// Validación de formularios
function initFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredInputs = form.querySelectorAll('[required]');
            let isValid = true;
            
            requiredInputs.forEach(input => {
                if (!input.value.trim()) {
                    isValid = false;
                    input.style.borderColor = '#e74c3c';
                } else {
                    input.style.borderColor = '#ddd';
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                alert('Por favor complete todos los campos requeridos');
            }
        });
    });
}

// Animaciones suaves
function initAnimations() {
    // Efecto de fade-in para elementos
    const elements = document.querySelectorAll('.pregunta-card, .info-section, .resultado-card');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, { threshold: 0.1 });
    
    elements.forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.5s, transform 0.5s';
        observer.observe(el);
    });
}

// Función para confirmar cierre de sesión
function confirmarLogout() {
    return confirm('¿Está seguro que desea cerrar sesión?');
}

// Agregar confirmación a los formularios de logout
document.querySelectorAll('form[action*="logout"]').forEach(form => {
    form.addEventListener('submit', function(e) {
        if (!confirmarLogout()) {
            e.preventDefault();
        }
    });
});