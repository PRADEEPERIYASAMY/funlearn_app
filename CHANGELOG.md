# Changelog

All notable changes to FunlearnV2 are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
Versions follow a `MAJOR.MINOR.PATCH` scheme once the project reaches a stable release.

## [Unreleased]

The project is currently in active development. The items below track work in progress against the [V1 feature set](https://github.com/PRADEEPERIYASAMY/FunLearn).

### In Progress

- Migrating remaining game and quiz stub screens to feature-complete state
- Extracting game logic from Fragments into ViewModels for testability
- Offline-first cache layer extending `DataStoreRepository` with Firestore as sync source

### Done (since initial V2 commit)

- Full Kotlin rewrite of the Java V1 codebase
- Hilt dependency injection wired across all ViewModels, repositories, and Firebase sources (`FirebaseModules.kt`)
- Role-based navigation: `AuthenticationActivity` → `ParentActivity` / `ChildActivity` via `Roles` enum
- Dual Firebase backend: Firestore for structured data (Users, Messages, ClassRoom, Questions, Orders), Realtime Database for presence and light key/value data
- `DataStoreRepository` replacing V1's raw `SQLiteOpenHelper` — typed, coroutine-native local cache
- Scanline flood-fill coloring engine ported to Kotlin (`FloodFill.kt` object singleton)
- Three-mode chat (Public, Private, Group) modeled on shared Firestore collections with `Mode` enum
- Phone verification gating parent accounts (`PhoneVerificationFragment`, `ParentVerificationFragment`)
- ML Kit text recognition integration for handwriting OCR
- Glide-only image loading (Picasso removed from V1)
- ViewBinding enabled at module level (replacing `findViewById` throughout)
- Navigation Component + Safe Args replacing 42 separate Activities from V1
- Biometric authentication gate (`androidx.biometric`)
- Firebase Crashlytics and Analytics integration

## [1.0] — Target (not yet released)

Will be tagged once full V1 feature parity is confirmed and stub screens are complete.
