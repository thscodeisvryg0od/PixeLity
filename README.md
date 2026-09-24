# Pixel Like Launcher (PixeLity)

Minimalist, fast, Pixel-inspired Android launcher built with **Jetpack Compose**, **Material 3**, **Hilt** and modern Android architecture.

## Features

- **HOME launcher** – can be set as the default home app
- Clean home screen with large clock & date (Pixel-style)
- Bottom **dock** for favorite apps (auto-seeded with common apps on first run)
- Full **app drawer** with search (swipe up or FAB)
- Long-press context menu: Open / Add to Home / Hide / App info / Uninstall
- Edge-to-edge immersive UI
- Dynamic Material You colors on Android 12+
- Preferences stored with DataStore (dock + hidden apps)
- Hilt dependency injection

## Requirements

- Android Studio Ladybug (2024.2+) or newer / AGP 8.6+
- JDK 17
- minSdk 28 (Android 9), targetSdk 34, compileSdk 35

## How to build & run

1. Open the project in Android Studio.
2. Let Gradle sync (wrapper uses Gradle 8.9).
3. Run the `app` configuration on a device or emulator (API 28+).

```bash
./gradlew :app:assembleDebug
```

APK location: `app/build/outputs/apk/debug/app-debug.apk`

After install, press the **Home** button and choose **Pixel Like Launcher** as default.

## Project structure

```
app/src/main/java/com/example/pixellauncher/
├── LauncherApp.kt
├── MainActivity.kt
├── data/
│   ├── model/AppInfo.kt
│   ├── preferences/LauncherPreferences.kt
│   └── repository/AppRepository.kt
├── di/AppModule.kt
└── ui/
    ├── components/   (AppIcon, ClockWidget)
    ├── drawer/       (AppDrawer)
    ├── home/         (HomeScreen, HomeViewModel)
    └── theme/
```

## License

Educational / starter project. Use and modify freely.
