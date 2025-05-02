# 📍 Mapa Interactivo con Puntos Favoritos y Alertas

Esta es una aplicación de mapas desarrollada en Android Studio utilizando el SDK de Mapbox. Permite al usuario visualizar su ubicación actual, agregar puntos personalizados (normales o tipo alerta), cambiar el estilo del mapa, cargar puntos desde un archivo GeoJSON y guardar puntos favoritos en una base de datos local.

---

## 🚀 Características Principales

- 🌎 **Visualización del mapa** con diferentes estilos.
- 📍 **Agregar puntos personalizados** (normales o tipo alerta) con nombre.
- ⚠️ **Puntos de alerta** con animación destacada.
- 💾 **Almacenamiento local** de puntos favoritos utilizando Room.
- 📡 **Centrar en la ubicación actual** con un solo clic.
- 🌐 **Carga de datos GeoJSON** desde una URL externa.
- 🧭 **Navegación a puntos favoritos** desde una lista interactiva.

---

## 📱 Instalación

Para instalar la aplicación en tu dispositivo Android:

1. Descarga el archivo [`app-debug.apk`](./app-debug.apk).
2. Transfiérelo a tu dispositivo Android o ábrelo directamente desde el navegador.
3. Asegúrate de tener habilitada la opción de instalar aplicaciones desde fuentes desconocidas.
4. Ejecuta el APK e instala la app.

---

## 🛠️ Tecnologías utilizadas

- **Kotlin**
- **Mapbox SDK**
- **Room (persistencia local)**
- **Coroutines (tareas asincrónicas)**
- **Android Jetpack (ComponentActivity, Permissions, etc.)**

---

## 🧪 Cómo usar la app

1. **Abre la aplicación.**
2. Permite el acceso a la ubicación si se solicita.
3. Toca cualquier lugar del mapa para agregar un punto (elige entre normal o tipo alerta).
4. Asigna un nombre al punto y guárdalo.
5. Usa los botones:
   - **Cambiar Estilo**: alterna entre estilos de mapa.
   - **Centrar Ubicación**: enfoca el mapa en tu ubicación actual.
   - **Ver Favoritos**: accede a todos los puntos guardados y navega hacia ellos.

---

## 📂 Estructura del código (resumen)

- `MainActivity.kt`: Actividad principal que gestiona el mapa, los clics, la base de datos y la ubicación.
- `MapManager`, `LocationManager`, `MarkerManager`: Clases de ayuda para modularizar la lógica del mapa.
- `AppDatabase`, `FavoritePoint`: Configuración de Room para guardar puntos.
- `GeoJsonLoader`: Carga datos geoespaciales desde internet.

---
