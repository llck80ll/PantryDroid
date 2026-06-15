# PantryDroid

PantryDroid is a native Android app that helps answer:
**What can I cook with the ingredients I already have?**

Choose items from your kitchen and the app ranks bundled recipes by ingredient
match. Favorites, meal plans, and grocery checks remain on the device.

## Features

- Native Kotlin and Jetpack Compose interface
- Material 3 design with light and dark themes
- Offline recipe matching with no account or API key
- 12 bundled recipes
- Cuisine filters and match percentages
- Recipe ingredients, nutrition, and cooking steps
- Favorites
- Seven-day meal planner
- Consolidated grocery checklist
- SharedPreferences persistence
- Unit tests for ingredient matching

## Requirements

- Android Studio Ladybug or newer
- Android SDK 35
- JDK 17

## Run in Android Studio

1. Clone this repository.
2. Open the repository folder in Android Studio.
3. Allow Gradle sync to complete.
4. Select an Android emulator or connected phone.
5. Run the `app` configuration.

The app supports Android 8.0 (API 26) and newer.

## Command Line

After generating or downloading the Gradle wrapper:

```sh
./gradlew test
./gradlew assembleDebug
```

The debug APK will be written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Privacy

PantryDroid has no internet permission, analytics, advertising, login, or remote
backend. Pantry items, favorites, plans, and grocery checks are stored locally.

## License

MIT

