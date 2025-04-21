# Jetpack Compose App with Authentication with Supabase


# Disclaimer sobre carpetas vacías

En este proyecto, algunas carpetas pueden aparecer vacías.Por lo que para asegurar de que estas carpetas se suban al repositorio, se ha incluido un archivo llamado `.keep` (o `.gitkeep`) en las carpetas vacías.


## Screenshots

Imagenes de la aplicacion 


<div align="left">
  <img src="docs/screenshots/test_prueba.jpg" width="150">
</div>



## Features


## Project Structure

Este proyecto está desarrollado con una arquitectura fundamentada en características, en la que cada función de la aplicación se organiza como un módulo separado, lo que simplifica su desarrollo y mantenimiento. La aplicación se basa en los principios de Clean Architecture, dividiendo el código en tres niveles bien claros: la Capa de Presentación (que incluye el ViewModel y la interfaz de usuario, como Fragments o Activities), la Capa de Dominio (que contiene la lógica del negocio y los casos de uso), y la Capa de Datos (responsable de los repositorios y las fuentes de información, como APIs y bases de datos). Se aplica el patrón MVVM (Modelo-Vista-ViewModel) para diferenciar la lógica del negocio de la interfaz de usuario, permitiendo que el ViewModel se conecte con el Modelo y controle el estado de la Vista. Para realizar la Inyección de Dependencias, se utiliza Koin, un contenedor ligero y fácil de usar para manejar las dependencias, fomentando un código más ordenado, modular y sencillo de probar. Esta arquitectura modular y escalable garantiza una estructura que se puede mantener y ampliar para grandes proyectos Android o aquellos que se desarrollan a largo plazo.

Orientatorio 

com.example.app
│
├── auth
│   ├── data
│   ├── domain
│   ├── presentation
│   │   ├── login
│   │   ├── signup
│   │   └── otpVerification
│
├── home (una de tus tabs)
│   ├── data
│   ├── domain
│   ├── presentation
│   │   └── HomeScreen.kt
│
├── products (otra tab del BottomNav)
│   ├── data
│   │   └── ProductRepository.kt
│   ├── domain
│   │   └── GetProductsUseCase.kt
│   ├── presentation
│   │   ├── ProductScreen.kt
│   │   └── ProductsViewModel.kt
│
├── cart (otra tab)
│   ├── ...
│
├── profile (otra tab)
│   ├── ...
│
├── navigation
│   ├── AppNavGraph.kt
│   ├── BottomNavItem.kt
│   └── BottomBar.kt
│
├── di
│   ├── AppModule.kt
│   └── ViewModelModule.kt
│
└── MainActivity.kt


