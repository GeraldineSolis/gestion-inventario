# 📄 Proyecto: Control de Inventario para Pequeñas Empresas

**Curso:** Aplicaciones Móviles con Android (Kotlin + Jetpack Compose)

Este proyecto tiene como objetivo desarrollar una aplicación móvil con Android (Kotlin + Jetpack Compose) para ayudar a pequeñas bodegas y comercios minoristas a automatizar el control de su inventario, ventas y gastos.

---

## 👥 Roles del Equipo y Enfoque Funcional

| Rol | Estudiante | Enfoque Principal | Contribución en Figma (Día 1) | 
 | ----- | ----- | ----- | ----- | 
| **Líder Técnico** | Geraldine Solis | **Arquitectura UX**, Persistencia de Datos (Room), Lógica de **Inventario y Compras/Gastos**. | **Diseño Estructural (Wireframes) de: Registro de Producto y Compras/Insumos.** | 
| **Diseñador UI** | Travezaño Sayuri | Interfaz de Usuario (**UI**), Componentes Visuales (**Design System**), Flujo de **Ventas y Reportes**. | **Diseño Visual (Alta Fidelidad) y Estructura de: Home, Ventas y Cierre de Caja/Reportes.** | 

---

## III. Avance Consolidado: Arquitectura de Datos (Días 3 al 6)

Como Líder Técnico, se ha implementado la arquitectura de datos completa (**Clean Architecture** y **MVVM**) con un enfoque **Offline-First**, asegurando la robustez de la aplicación y su preparación para la conexión con un *backend* real.

### A. Persistencia, ViewModel y Lógica Base (Días 3-5)

* **Room/Persistencia:** Implementación del patrón Singleton para `AppDatabase`, con **DAOs** para `Producto` y `Compra` que exponen los datos mediante **`Flow`**.
* **Capas:** Creación de `InventoryRepository.kt` y ViewModels (`ProductViewModel`, `PurchaseViewModel`) usando **`StateFlow`** para la gestión del estado de la UI.
* **Integración de Red:** Implementación de Retrofit (`InventarioApiService.kt`) y configuración del `NetworkModule.kt` para la comunicación con el servidor (vía Mock API).
* **Stock Reactivo:** Implementación de la lógica de negocio para que `insertCompra` **SUME** automáticamente el stock del producto, asegurando la consistencia de los datos en tiempo real.

### B. Sistema de Sincronización Pendiente

* **Robustez:** Se implementó una lógica a prueba de fallos de red: si el intento de envío a la API falla (por `IOException` o `HttpException`), la operación se guarda en una cola local (`PendingSync.kt`) para ser reintentada posteriormente.

---

## IV. Ventajas del Mock Implementado (Estrategia de Simulación)

Para demostrar las capacidades de sincronización y estados de carga de Retrofit sin depender de un servidor activo, se utilizó un servicio de *mocking* externo con las siguientes ventajas:

* ✅ **Datos simulados** basados en productos comunes para una mejor simulación.
* ✅ **Latencia simulada** (300-800ms) para probar correctamente los estados de carga (`isLoading`).
* ✅ **IDs autoincrementables** para simular operaciones CRUD completas.
* ✅ **Stock bajo realista** para probar la lógica de alertas.
* ✅ **Historial de compras** con fechas variadas para las estadísticas.
* ✅ **Actualización automática de stock** al registrar compras (simulación de *triggers* de la API).
* ✅ **Fácil *switch* a API real** cambiando solo la URL base en `NetworkModule.kt`.

---

## V. Avance Consolidado: Business Intelligence (Día 6)

El trabajo se centró en la implementación de la lógica de negocio avanzada dentro del Repositorio para generar las métricas ejecutivas clave que alimentarán el Dashboard.

### 1. Métricas e Indicadores Clave

Se implementaron las funciones necesarias en `InventoryRepository.kt` y `ProductoDao.kt` para la toma de decisiones:

* **Alerta de Stock Bajo:** Lógica para identificar y contar todos los productos cuyo `stockActual` es menor o igual al `stockMinimo` definido.
* **Cálculo de Ganancia Neta:** Función para obtener la diferencia entre el total de ventas y el costo total de los insumos (compras).
* **Costo Total de Inventario:** Consulta para sumar el costo actual de todo el stock disponible en bodega.

### 2. Preparación de la Interfaz

* Se creó el modelo `DashboardStats.kt` (o similar) para consolidar todas estas métricas de rendimiento en un único objeto de estado, facilitando su consumo reactivo por parte del futuro `HomeViewModel.kt`.

---

## 🏁 Próximos Pasos (Día Final)

Con toda la lógica de persistencia, red, sincronización y *Business Intelligence* completada por el Líder Técnico, el trabajo restante se centra en la integración final de la UI para cerrar el proyecto:

* **Integración UI (Diseñador UI):** Consumir el `DashboardStats` y el estado de los ViewModels para construir las Tarjetas de Resumen en el `HomeScreen`, mostrando alertas de stock bajo y los resúmenes financieros calculados.

---

## 🎨 Diseño en Figma

Puedes ver el diseño completo del proyecto aquí:

👉 [Ver diseño en Figma](https://www.figma.com/make/KjvJCQCRjX914zPcAdQ7Ic/Wireframes-de-Formularios?fullscreen=1)