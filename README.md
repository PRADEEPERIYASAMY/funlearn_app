# FunLearn V2

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Min%20SDK](https://img.shields.io/badge/minSdk-23-blue)
![Target%20SDK](https://img.shields.io/badge/targetSdk-30-blue)
![Backend](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)
![DI](https://img.shields.io/badge/DI-Hilt-3DDC84)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

**FunLearn V2** is a Kotlin rewrite of the original [FunLearn](https://github.com/PRADEEPERIYASAMY/FunLearn) Android learning app for children. It keeps the same product goals — tutorials, quizzes, handwriting practice with on-device OCR, a custom coloring engine, and community chat — but rebuilds the app on a modern Android stack: Kotlin, Hilt DI, coroutines/Flow, ViewModels, DataStore, and a dual Firebase backend (Realtime Database + Firestore).

This module (`FunLearnV2/`) lives alongside the legacy Java module in this repository as an in-progress migration, and is documented here on its own terms.

---

## Table of Contents

1. [Why a V2](#1-why-a-v2)
2. [System Architecture](#2-system-architecture)
3. [Feature Notes](#3-feature-notes)
   - [3.1 Roles: Parent, Child & Authentication](#31-roles-parent-child--authentication)
   - [3.2 Coloring Engine — Flood Fill](#32-coloring-engine--flood-fill)
   - [3.3 Chat — Public, Private & Group](#33-chat--public-private--group)
   - [3.4 Quiz, Classroom & Resources](#34-quiz-classroom--resources)
   - [3.5 Local State — DataStore](#35-local-state--datastore)
4. [Module Reference](#4-module-reference)
5. [Tech Stack](#5-tech-stack)
6. [What's Next](#6-whats-next)
7. [Getting Started](#7-getting-started)
8. [License & Contributing](#8-license--contributing)

---

## 1. Why a V2

The original FunLearn was a single-Activity-per-screen Java app with 42 registered Activities, no dependency injection, and Firebase Realtime Database as the only backend. FunLearnV2 is a from-scratch rewrite aimed at fixing exactly those pain points:

| Concern in V1 | V2 change |
|---|---|
| 42 Activities, `Intent`-extra navigation | 4 host Activities (`AuthenticationActivity`, `ParentActivity`, `ChildActivity`, `BaseActivity`) + Fragments, so most screens are Fragment transactions inside a shared shell |
| No DI, manual object wiring | [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) across ViewModels, repositories, and Firebase sources (`FirebaseModules.kt`) |
| One backend (Realtime DB) for everything | Realtime DB kept for presence/simple key-value data (`FirebaseDbSource.kt`), Firestore added for structured, queryable collections — chat, classes, quizzes, orders (`FireStoreSource.kt`) |
| Manual SQLite table for local state | [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (`DataStoreRepository.kt`) for typed local preferences |
| No parent/child distinction at the account level | Explicit `Roles` enum (`teacher`, `child`, `parent`, `admin`) and separate `ParentActivity` / `ChildActivity` entry points |

## 2. System Architecture

```
                        ┌─────────────────────┐
                        │  AuthenticationActivity │
                        │  (Sign In / Sign Up)    │
                        └──────────┬───────────┘
                                   │ Users(role, uid)
                 ┌─────────────────┼─────────────────┐
                 ▼                                   ▼
        ParentActivity                        ChildActivity
        (ParentDashBoard,                     (DashBoard, Games,
         ParentVerification)                   Tutorial, Chat, Quiz)
                 │                                   │
                 └───────────────┬───────────────────┘
                                  ▼
                    ┌──────────────────────────┐
                    │   Hilt-provided sources    │
                    ├──────────────────────────┤
                    │ FirebaseDbSource  (RTDB)   │──► presence, simple counters
                    │ FireStoreSource   (Firestore) │──► Users, Messages, ClassRoom,
                    │                              │    Quiz/Questions, Orders, Comments
                    └──────────────┬───────────┘
                                   │
                    ┌──────────────▼───────────┐
                    │  DataStoreRepository       │
                    │  (local, per-device cache  │
                    │   of profile & score/cash) │
                    └──────────────────────────┘
```

**Data split (carried over from V1, refined):** Firestore holds structured content and social data that benefits from querying (`Users`, `Messages`, `ClassRoom`, `Questions`, `Requests`, `Orders`); Realtime Database is reserved for lightweight, frequently-updated key/value data such as online presence. DataStore replaces the old SQLite `Level` table as the local cache for profile fields, score, and cash so the UI has something to render immediately while Firestore/RTDB catch up.

**DI:** `FirebaseModules.kt` provides `FirebaseAuth`, `FirebaseFirestore`, and `FirebaseDatabase` instances via Hilt; repositories and ViewModels (`FireStoreViewModel`, `FirebaseDbViewModel`, `BaseViewModel`) receive them by constructor injection rather than constructing clients themselves.

---

## 3. Feature Notes

### 3.1 Roles: Parent, Child & Authentication

`AuthenticationActivity` is the single launcher Activity (see [`AndroidManifest.xml`](FunLearnV2/app/src/main/AndroidManifest.xml)), fronting `SignInFragment` / `SignUpFragment` / `UserTypeFragment`. Account data is modeled as one `Users` document (see [`models/FirestoreModels.kt`](FunLearnV2/app/src/main/java/com/example/funlearnv2/models/FirestoreModels.kt)) carrying **both** child and parent fields (`child_name`, `child_grade`, `parent_name`, `parent_grade`, etc.) plus a `Roles` enum, rather than separate user tables. `ParentVerificationFragment` and `PhoneVerificationFragment` gate access before handing off to `ParentActivity` or `ChildActivity`.

### 3.2 Coloring Engine — Flood Fill

The scanline flood-fill from V1 was ported to Kotlin as an `object` singleton ([`views/widgets/FloodFill.kt`](FunLearnV2/app/src/main/java/com/example/funlearnv2/views/widgets/FloodFill.kt)), preserving the same span-queue algorithm to avoid recursive stack overflow on large bitmaps:

```kotlin
object FloodFill {
    fun floodFill(bitmap: Bitmap, point: Point, targetColor: Int, newColor: Int) {
        // walks horizontal spans, queues only span boundaries above/below
        ...
    }
}
```

It drives `PaintView.kt` / `ColorView.kt` and is shared across `ColouringOneFragment`, `ColouringTwoFragment`, and `ColouringThreeFragment`, plus the tracing game in `Patterns.kt` — the same one-implementation-many-consumers structure as V1's `Pattern/` module.

### 3.3 Chat — Public, Private & Group

Chat is modeled directly in Firestore via `Messages`, `Comments`, `Reactions`, `Requests`, and `GroupDetails` (all in [`FirestoreModels.kt`](FunLearnV2/app/src/main/java/com/example/funlearnv2/models/FirestoreModels.kt)), with a `Mode` enum (`PRIVATE`, `PUBLIC`, `GROUP`) distinguishing conversation types on the same collections rather than separate schemas per mode. Fragments split by mode: `PublicChatFragment`, `PrivateChatFragment`, `GroupChatFragment`, `CommonChatFragment` (shared list/composer logic), `CommentFragment`, and `ChatStatusFragment` for presence.

### 3.4 Quiz, Classroom & Resources

- `ClassRoom` / `ClassResource` — a class has a `meet_url`, `teacher_id`, and a list of `Resource` items typed as `LINK`, `PDF`, `VIDEO`, or `QUIZ`.
- `Questions` — flat MCQ shape (`option_A`..`option_D`, `Answer`) tied to a `resource_id`.
- `QuizResult` — per-attempt outcome (`score`, `attempted`, `un_attempted`, `wrong`), mirroring V1's separation of quiz content from quiz outcomes (`QuizMaster` vs `Rank` in the original app).
- UI: `ClassTypeFragment` → `ClassContentFragment` → `QuizFragment` / `PdfViewFragment` / `WebFragment`, depending on `Resource` type.

### 3.5 Local State — DataStore

[`repository/DataStoreRepository.kt`](FunLearnV2/app/src/main/java/com/example/funlearnv2/repository/DataStoreRepository.kt) wraps a single `androidx.datastore.preferences` instance (provided by `ResourceProvider`) with typed getters/setters for every profile field (child + parent), account credentials cache, `score`, and `cash`. Each field is exposed as a `Flow<String>` internally and read via `.first()` for one-shot suspend access — replacing V1's raw `SQLiteOpenHelper` table with a coroutine-friendly, type-checked key-value store. It's still local-only per device, so cross-device sync is not yet solved here either (see [§6](#6-whats-next)).

---

## 4. Module Reference

```
FunLearnV2/app/src/main/java/com/example/funlearnv2/
├── FirebaseSource/     FirebaseDbSource, FireStoreSource, FirebaseModules, Collections — data-source layer (§2)
├── models/             DataModel, FirebaseDbModels, FirestoreModels, chatModel, Result — data/DTO layer (§3.3, §3.4)
├── repository/         DataStoreRepository (§3.5), FireStoreRepository, FirebaseDbRepository
├── viewmodels/         BaseViewModel, FireStoreViewModel, FirebaseDbViewModel
│   └── actions/         FireStoreAction, FirebaseDbAction — sealed action/state contracts for ViewModels
├── utils/              CallUtil, ContextUtil, DateUtil, DialogUtil, DimensionUtil, FileUtil, GlideUtil,
│                        HideKeyboardUtil, InputTextUtil, ProgressBarUtil, QueryUtil, SnackbarUtil,
│                        ViewGroupUtil, ViewUtil — shared Kotlin extension helpers
│   ├── constants/        Constant, ButtonStatus, FunType, OperatorTypes
│   └── resourceProvider/ ResourceProvider — DataStore + resource access wrapper
├── views/
│   ├── activities/      AuthenticationActivity, BaseActivity, ParentActivity, ChildActivity (§2, §3.1)
│   ├── fragments/        40+ Fragments — auth, dashboard, alphabets, numbers, games, coloring,
│   │                      chat, quiz, classroom, settings (§3.1–§3.4)
│   ├── adapters/         18 RecyclerView adapters — one per list-backed screen
│   └── widgets/          FloodFill, PaintView, ColorView, Patterns, GameImages (§3.2)
└── FunLearnApplication.kt   @HiltAndroidApp entry point
```

## 5. Tech Stack

| Concern | Choice | Notes |
|---|---|---|
| Language | Kotlin | Full rewrite of the Java V1 codebase |
| DI | Hilt (`hilt-android`, `hilt-compiler`) | Wires Firebase clients, repositories, ViewModels |
| Async | Kotlin Coroutines + `kotlinx-coroutines-play-services` | `Flow`-based repository layer (`DataStoreRepository`) |
| Backend — structured data | Firestore (`firebase-firestore-ktx`) | Users, chat, classes, quizzes, orders — see [§2](#2-system-architecture) |
| Backend — realtime/light data | Firebase Realtime Database (`firebase-database-ktx`) | Presence-style, low-latency key/value data |
| Auth | Firebase Auth (`firebase-auth-ktx`) | Email/password + phone verification (`PhoneVerificationFragment`) |
| On-device ML | `com.google.mlkit.vision.DEPENDENCIES` (`ocr`), `play-services-mlkit-text-recognition` | Handwriting recognition, offline |
| Local persistence | Jetpack DataStore (Preferences) | Typed local cache, replaces V1's SQLite table — see [§3.5](#35-local-state--datastore) |
| Navigation | Android Navigation Component (`navigation-fragment-ktx`, `navigation-ui-ktx`, Safe Args) | Type-safe Fragment transitions inside 4 host Activities |
| Networking | Retrofit2 + Gson converter | Non-Firebase HTTP needs |
| Maps/Location | `play-services-maps`, `play-services-location`, Google Places | Location-aware features |
| Image loading | Glide | Single library (Picasso dropped from V1) |
| Biometric | `androidx.biometric` | Optional local auth gate |
| Crash/Analytics | Firebase Crashlytics, Analytics | |
| UI toolkit | Material Components, ConstraintLayout, ViewBinding, RecyclerView | `viewBinding = true` enabled at module level |

Full dependency list: [`FunLearnV2/app/build.gradle`](FunLearnV2/app/build.gradle).

## 6. What's Next

- **Finish the V1 → V2 migration** and retire the legacy Java module once feature parity (all games, all coloring assets, quiz ranking history) is confirmed.
- **Sync local state across devices** — `DataStoreRepository` (§3.5) is still per-device; move authoritative profile/score/cash reads to Firestore with DataStore purely as a cache.
- **Consolidate `FirebaseDbSource` vs `FireStoreSource` usage** — some data (e.g. presence) still spans both backends; document or unify which backend owns which field to avoid drift.
- **Add unit tests** around `FloodFill`, `DataStoreRepository`, and the `viewmodels/actions` contracts — these are the most side-effect-isolated pieces and the best starting point for a JUnit/coroutines-test suite.
- **Extract game logic from Fragments** (e.g. `GameFourFragment` is large) into ViewModels to make game rules testable independent of the view layer.
- **Capture real screenshots** of the running V2 app for this README.

## 7. Getting Started

```bash
git clone https://github.com/PRADEEPERIYASAMY/funlearn_app.git
cd funlearn_app/FunLearnV2
```

1. Create a Firebase project; enable **Firestore**, **Realtime Database**, **Authentication** (email/password + phone), **Crashlytics**, and **ML Kit Text Recognition**.
2. Download `google-services.json` into `FunLearnV2/app/` (a placeholder already exists in this repo — replace it with your own project's file).
3. Open the `FunLearnV2` folder in Android Studio (Gradle + `com.google.gms.google-services` + Hilt + Navigation Safe Args plugins) and let it sync.
4. Build:

```bash
./gradlew build
```

## 8. License & Contributing

This project is built and maintained solo; `CONTRIBUTING.md` and `CODE_OF_CONDUCT.md`, if present, should be treated as boilerplate rather than an active open-collaboration process. If you'd like to contribute, open an issue first.

Licensed under [MIT](LICENSE).

---

**Author:** Pradeep Periyasamy
