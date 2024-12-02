https://github.com/NicolasJRuax/Feedback-Novelas.git

# Feedback6

Gestor de Novelas es una aplicación Android diseñada para gestionar una colección de novelas. Ofrece funcionalidades como agregar nuevas novelas, eliminar existentes, marcar novelas como favoritas y visualizar detalles completos. Además, incluye características avanzadas como sincronización de datos con Firebase Firestore, soporte para un widget en la pantalla de inicio y opciones de configuración como modo oscuro.

---

## **Características Principales**

- **Agregar Novelas**: Permite añadir nuevas novelas proporcionando título, autor, año de publicación y sinopsis.
- **Lista de Novelas**: Visualización de todas las novelas en un `RecyclerView`, optimizado para un rendimiento eficiente.
- **Detalles de Novela**: Muestra los detalles completos de cada novela al seleccionarla.
- **Eliminar Novelas**: Posibilidad de eliminar novelas de la colección.
- **Favoritos**: Los usuarios pueden marcar novelas como favoritas, visibles en un widget de la pantalla de inicio.
- **Sincronización de Datos**: Integración con Firebase Firestore y persistencia en caché para un uso eficiente de la red.
- **Optimización de Recursos**: Mejoras en el uso de memoria, red y batería.
- **Ubicación en las novelas**: Ahora puedes agregarle una ubicación a cada novela para poder visualizarla en el mapa.
- **Geocodificación**: Se implementa la funcionalidad para convertir direcciones en coordenadas de latitud/longitud (geocodificación) y viceversa (geocodificación inversa).



---

## **Cumplimiento de Requisitos**

### **1. Optimización del Uso de la Memoria**
- **Identificación y Corrección de Fugas de Memoria**: Uso de `Memory Profiler` para detectar y corregir fugas. Se implementó `WeakReference` para manejar el contexto de forma segura.
```java
// Ejemplo en NovelAdapter.java
private WeakReference<Context> contextRef;

public NovelAdapter(Context context, ...) {
    this.contextRef = new WeakReference<>(context);
    // ...
}    java´´´
 

### **2. Mejora del Rendimiento de la Red**

Análisis con Network Profiler: Optimizamos llamadas a Firebase Firestore mediante consultas eficientes.
Persistencia en Caché: Configuración de Firestore para reducir llamadas de red y mejorar tiempos de respuesta.

// En MyApplication.java
FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build();
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.setFirestoreSettings(settings);
Uso Eficiente de Datos: Sincronización selectiva de datos para minimizar el consumo de datos móviles.

### **3. Optimización del Uso de la Batería**

Análisis con Battery Historian: Identificación y solución de procesos que consumían mucha batería.
Reducción de Actualizaciones en Segundo Plano: Frecuencia de sincronización ajustada a una vez al día.
java
// En SyncUtils.java
long interval = AlarmManager.INTERVAL_DAY; // Sincroniza cada día
Optimización de Servicios: Los servicios y tareas en segundo plano se ejecutan solo cuando es necesario.

Optimización de Vistas: Implementación de técnicas de reciclaje de vistas para maximizar la eficiencia.

### **4. Implementación de la Lógica en MainActivity.java**

Gestión Completa de Novelas: Implementación de lógica para añadir, eliminar y mostrar detalles de novelas.
java
// Ejemplo de adición de novelas en MainActivity.java
buttonAddBook.setOnClickListener(v -> showAddNovelDialog());

private void showAddNovelDialog() {
    // Configuración del diálogo para agregar novelas
    // ...
    builder.setPositiveButton("Agregar", (dialog, which) -> {
        // Lógica para agregar la novela
        // ...
    });
    // ...
}
