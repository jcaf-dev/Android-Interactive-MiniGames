# 🎮 QuizzGame

Aplicación móvil Android que integra distintos **minijuegos interactivos** para demostrar el uso de múltiples funcionalidades del sistema Android como gestos táctiles, sensores, animaciones y renderizado gráfico.

El objetivo del proyecto es consolidar diferentes conceptos aprendidos durante el desarrollo del curso mediante una única aplicación con múltiples mecánicas de juego.

---

# 📑 Índice

- [Descripción](#-Características-principales)
- [Características principales](#-características-principales)
- [Pantallas de la aplicación](#-pantallas-de-la-aplicación)
- [Arquitectura y clases principales](#-arquitectura-y-clases-principales)
- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Dificultades encontradas](#-dificultades-encontradas)
- [Conclusiones](#-conclusiones)
- [Autor](#-autor)

---

# 📱 Descripción

**QuizzGame** es una aplicación Android que combina diferentes minijuegos interactivos para poner en práctica funcionalidades avanzadas del sistema.

Entre las funcionalidades implementadas se encuentran:

- Single Touch
- Multi Touch
- Gestos (Gestures)
- Double Tap
- Acelerómetro
- Animaciones
- Drag & Drop
- Renderizado gráfico personalizado
- Realidad Aumentada

Cada minijuego utiliza una mecánica diferente, permitiendo explorar distintos eventos y sensores del sistema Android.

---

# 🚀 Características principales

✔ Sistema de login y registro de usuarios  
✔ Base de datos de preguntas editable  
✔ Sistema de pistas dentro del juego  
✔ Tienda de pistas para ayudar al jugador  
✔ Animaciones y efectos visuales  
✔ Integración de sensores del dispositivo  
✔ Minijuegos basados en gestos y acelerómetro  
✔ Implementación de Realidad Aumentada  

---

# 🖥 Pantallas de la aplicación

## 🔐 Login

La aplicación comienza con una pantalla de autenticación donde el usuario puede:

- Iniciar sesión
- Registrarse
- Cambiar la fuente de los textos
- Acceder al gestor de preguntas
- Acceder a la tienda de pistas

El botón **Sign In** se activa cuando el usuario introduce al menos un carácter en ambos campos.

Si el usuario se registra, los datos se devuelven automáticamente a la pantalla de login utilizando **OnActivityResult**.

---

## 🧠 Minijuego 1 — Quiz

El primer minijuego consiste en un **cuestionario de preguntas y respuestas**.

Funcionalidades destacadas:

- Sistema de pistas
- Animación de la respuesta correcta
- Animaciones implementadas con **Animator**

Cuando el jugador utiliza una pista, el botón con la respuesta correcta realiza una animación para facilitar la elección.

---

## 🧩 Minijuego 2 — Drag & Drop

En este minijuego el usuario debe **arrastrar elementos hasta la respuesta correcta**.

Se utilizan:

- `DragListener`
- Evento `ACTION_DROP`

La lógica del juego compara el elemento soltado con la respuesta correcta.

---

## 🔎 Minijuego 3 — Encuentra las diferencias

El jugador debe encontrar las diferencias entre dos imágenes.

Para ello se implementa:

- **Zoom con multitouch**
- **Double tap**
- Gestos táctiles para navegar por la imagen

Una vez encontradas las diferencias el jugador debe seleccionar la respuesta correcta.

---

## 🚀 Minijuego 4 — Nave espacial

En este minijuego el jugador controla una nave espacial evitando obstáculos.

Funcionalidades implementadas:

- Captura de eventos mediante `dispatchKeyEvent`
- Control táctil con `onTouchEvent`
- Control del botón atrás con `onBackPressed`

Si el jugador pulsa dos veces el botón atrás el juego termina, si solo pulsa una vez aparece un **Toast de confirmación**.

---

## 👽 Minijuego 5 — Juego con acelerómetro

En este minijuego el jugador controla un personaje utilizando el **acelerómetro del dispositivo**.

Características técnicas:

- Implementación de `SensorEventListener`
- Uso de matriz de rotación para calcular la orientación
- Renderizado con `SurfaceView`
- Sistema de animación mediante sprites
- Sistema de obstáculos dinámicos

Para mejorar el rendimiento se ejecuta el juego dentro de un **hilo dedicado (MainThread)**.

---

## 🧊 Realidad Aumentada

Se implementó una funcionalidad de **Realidad Aumentada** utilizando:

- **Google Sceneform**

Al completar todos los minijuegos el jugador obtiene un trofeo que puede visualizar en **3D mediante AR**.

---

# 🧱 Arquitectura y clases principales

Durante el desarrollo del proyecto se utilizaron varias clases clave del sistema Android:

### Timer
Permite programar tareas para ejecutarse en segundo plano dentro de los minijuegos.

### Handler
Se utiliza para ejecutar acciones en el hilo principal en momentos posteriores.

### MotionEvent
Permite capturar y procesar eventos táctiles del usuario.

### Glide
Biblioteca utilizada para cargar imágenes y GIFs dentro de la aplicación.

### WindowManager
Utilizado para obtener dimensiones de pantalla y gestionar ventanas.

### KeyEvent
Permite capturar eventos de botones físicos.

### Animator
Se utiliza para crear animaciones dentro de los minijuegos.

### SensorEventListener
Interfaz utilizada para manejar los datos del acelerómetro.

---

# 🧰 Tecnologías utilizadas

- **Java**
- **Android SDK**
- **Glide**
- **Google Sceneform (AR)**
- **SurfaceView**
- **Sensor APIs**
- **Gestures API**
- **Animations API**

---

# ⚠ Dificultades encontradas

Durante el desarrollo del proyecto surgieron varios retos técnicos:

- Reproducción de imágenes animadas en actividades Android
- Implementación de animaciones interactivas
- Integración de Realidad Aumentada
- Compatibilidad con versiones de Android
- Mantenimiento del código al añadir nuevas funcionalidades

Una de las mayores dificultades fue mantener estable la aplicación mientras se añadían nuevos minijuegos y funcionalidades.

---

# 📊 Conclusiones

Este proyecto ha permitido desarrollar una aplicación compleja que integra múltiples funcionalidades del sistema Android.

Entre los aspectos más destacados del proyecto:

- Uso de sensores del dispositivo
- Gestión de múltiples eventos táctiles
- Implementación de animaciones
- Renderizado gráfico personalizado
- Integración de Realidad Aumentada

El proyecto final cuenta con:

- Más de **40 clases**
- Aproximadamente **7000 líneas de código**

Como posibles mejoras futuras se podrían incluir:

- Nuevos minijuegos
- Mejor diseño visual
- Sistema de puntuaciones online
- Mayor optimización del rendimiento

---

# 👨‍💻 Autor

**Juan Carlos Angulo**

📧 juancarlos@jcaf.es
🐙 GitHub: https://github.com/jcaf-dev
