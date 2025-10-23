# 📄 Proyecto: Control de Inventario para Pequeñas Empresas

**Curso:** Aplicaciones Móviles con Android (Kotlin + Jetpack Compose)

Este proyecto tiene como objetivo desarrollar una aplicación móvil con Android (Kotlin + Jetpack Compose) para ayudar a pequeñas bodegas y comercios minoristas a automatizar el control de su inventario, ventas y gastos.

---

## 👥 Roles del Equipo y Enfoque Funcional

| Rol | Estudiante | Enfoque Principal | Contribución en Figma (Día 1) |
| :--- | :--- | :--- | :--- |
| **Líder Técnico** | Geraldine Solis | **Arquitectura UX**, Persistencia de Datos (Room), Lógica de **Inventario y Compras/Gastos**. | **Diseño Estructural (Wireframes) de: Registro de Producto y Compras/Insumos.** |
| **Diseñador UI** | Travezaño Sayuri | Interfaz de Usuario (**UI**), Componentes Visuales (**Design System**), Flujo de **Ventas y Reportes**. | **Diseño Visual (Alta Fidelidad) y Estructura de: Home, Ventas y Cierre de Caja/Reportes.** |

---

## 🎨 Prototipo Visual (Día 1 - Figma)

El diseño del prototipo visual ha sido completado en Figma para definir el alcance y el flujo de usuario inicial.

### 🛠️ Enfoque del Líder Técnico (UX y Estructura)

Como **Líder Técnico**, mi contribución inicial se centró en la **Usabilidad (UX)** y la **Arquitectura de la Información** de las secciones críticas de gestión de datos:

* **Registro de Productos:** Se definió un *wireframe* simple y funcional que prioriza la eficiencia en la entrada de datos (nombre, precio, stock) y la accesibilidad al escaneo.
* **Compras/Insumos:** Se estructuró el formulario para registrar egresos de dinero, asegurando la distinción entre gastos operativos y reabastecimiento de stock.

*(El diseño final de alta fidelidad y el sistema de colores se construirán sobre esta base, a cargo del Diseñador UI).*

**🔗 Enlace al Prototipo en Figma:**
https://www.figma.com/make/KjvJCQCRjX914zPcAdQ7Ic/Wireframes-de-Formularios?node-id=0-4&t=7Y180olK7AYdMqd6-1
---

## ⚙️ Estructura de Trabajo y Ramas de GitHub

El trabajo se dividirá en ramas funcionales para permitir el desarrollo en paralelo y minimizar dependencias.

| Rama | Propósito | Responsable |
| :--- | :--- | :--- |
| `main` | Versión estable y consolidada del proyecto. | **Ambos** (Merge de *features*) |
| `feature/inventory-and-costs` | Implementación de las funcionalidades de **Inventario, Productos y Compras/Gastos**. | **Líder Técnico** |
| `feature/sales-and-reports` | Implementación de las funcionalidades de **Registro de Ventas, Cierre de Caja y Reportes**. | **Diseñador UI** |

---

## 🏁 Próximos Pasos (Día 2)

Comenzaremos la configuración del proyecto base en Android Studio, estableciendo la estructura de paquetes (`model`, `data`, `navigation`, `ui`) y la navegación inicial en nuestras respectivas ramas de funcionalidad.