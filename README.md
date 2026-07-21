# FunlearnV2

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Min%20SDK](https://img.shields.io/badge/minSdk-23-blue)
![Target%20SDK](https://img.shields.io/badge/targetSdk-30-blue)
![Backend](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)
![DI](https://img.shields.io/badge/DI-Hilt-3DDC84)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

**FunlearnV2** is a from-scratch Kotlin rebuild of the original [FunLearn](https://github.com/PRADEEPERIYASAMY/FunLearn) — a two-sided (parent/child) Android learning platform covering tutorials, quizzes, handwriting OCR, a custom coloring engine, and public/private/group chat, built on a dual Firebase backend (Firestore + Realtime Database) with Hilt-driven dependency injection.

Where V1 proved the product concept end-to-end as a solo build, V2 is a deliberate architecture upgrade: DI, a layered repository/ViewModel structure, typed local storage, and role-based navigation replacing V1's single-Activity-per-screen model. The core data layer, auth/role system, chat, and coloring engine are wired and working; several game and quiz screens are still stubs pending the full migration — see [Roadmap](#6-roadmap).

---

## Asset Preview

Static art assets from `res/drawable/`.

<table>
<tr>
<td align="center"><img src="https://raw.githubusercontent.com/PRADEEPERIYASAMY/funlearn_app/main/app/src/main/res/drawable/xo_grid.png" width="180"/><br/><sub>Tic-Tac-Toe game board asset</sub></td>
<td align="center"><img src="https://raw.githubusercontent.com/PRADEEPERIYASAMY/funlearn_app/main/app/src/main/res/drawable/xo_back.png" width="180"/><br/><sub>Game background</sub></td>
<td align="center">
<img src="https://raw.githubusercontent.com/PRADEEPERIYASAMY/funlearn_app/main/app/src/main/res/drawable/chess_king_white.png" width="70"/>
<img src="https://raw.githubusercontent.com/PRADEEPERIYASAMY/funlearn_app/main/app/src/main/res/drawable/chess_queen_black.png" width="70"/>
<img src="https://raw.githubusercontent.com/PRADEEPERIYASAMY/funlearn_app/main/app/src/main/res/drawable/chess_rook_white.png" width="70"/>
<br/><sub>Chess piece set (GameFourFragment)</sub></td>
</tr>
</table>

---

## Origin

FunlearnV2 began as a proposed project for **Delta Winter of Code (DWoC)**, an initiative run by [Delta](https://github.com/delta-nitt), NIT Trichy's software development club, to get students contributing to real, ongoing codebases in the open-source model. Turnout that cycle was light — this was during COVID — so I took the project forward independently and used the opportunity to scope it well beyond the original: Hilt for DI, a backend split between Firestore and Realtime Database by access pattern, typed DataStore in place of raw SQLite, and a role-scoped navigation model built around 4 host Activities instead of 42 flat ones.

---

## Table of Contents

1. [What Changed from V1](#1-what-changed-from-v1)
2. [System Architecture](#2-system-architecture)
3. [Feature Notes](#3-feature-notes)
   - [3.1 Roles: Parent, Child & Authentication](#31-roles-parent-child--authentication)
   - [3.2 Coloring Engine — Flood Fill](#32-coloring-engine--flood-fill)
   - [3.3 Chat — Public, Private & Group](#33-chat--public-private--group)
   - [3.4 Quiz, Classroom & Resources](#34-quiz-classroom--resources)
   - [3.5 Local State — DataStore](#35-local-state--datastore)
4. [Module Reference](#4-module-reference)
5. [Tech Stack](#5-tech-stack)
6. [Roadmap](#6-roadmap)
7. [Getting Started](#7-getting-started)
8. [License & Contributing](#8-license--contributing)

---

## 1. What Changed from V1

| Concern | FunLearn V1 | FunlearnV2 | Why it matters |
|---|---|---|---|
| Language | Java | Kotlin, full rewrite | Coroutines/Flow-native repository layer |
| Navigation | 42 Activities, `Intent`-extra passing | 4 host Activities + Fragments, Navigation Component + Safe Args | Type-safe transitions, shared back-stack handling per role |
| Dependency management | Manual wiring | Hilt across ViewModels, repositories, Firebase sources (`FirebaseModules.kt`) | Testable, swappable dependencies |
| Backend | Realtime DB only | Realtime DB for presence/light data + Firestore for structured, queryable collections (chat, classes, quizzes, orders) | Firestore's query model fits chat/classroom/quiz data better than flat key lookups |
| Local storage | Raw `SQLiteOpenHelper` | Jetpack DataStore (`DataStoreRepository.kt`), coroutine-native typed preferences | Cleaner reads/writes, no manual cursor handling |
| Account model | No parent/child distinction | Explicit `Roles` enum + separate `ParentActivity`/`ChildActivity` entry points, phone-verified parent accounts | Matches how the product is actually used |
| Async | RxJava2 | Kotlin Coroutines + `kotlinx-coroutines-play-services`, `Flow`-based repositories | Idiomatic Kotlin, sealed `viewmodels/actions` state contracts |
| Image loading | Glide + Picasso both present | Glide only | One dependency, one caching behavior |

## 2. System Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                          AuthenticationActivity                      │
│                     SignInFragment / SignUpFragment /                │
│                          UserTypeFragment                            │
└───────────────────────────────┬────────────────────────────────────--┘
                                 │  writes Users(uid, role, profile)
                                 ▼
                     ┌───────────────────────┐
                     │   Roles: PARENT/CHILD  │
                     └──────────┬────────────┘
              ┌─────────────────┴─────────────────┐
              ▼                                   ▼
   ┌─────────────────────┐             ┌─────────────────────┐
   │   ParentActivity     │             │    ChildActivity     │
   │ ParentDashBoard,      │             │ DashBoard, Games,     │
   │ ParentVerification    │             │ Tutorial, Chat, Quiz  │
   └──────────┬───────────┘             └──────────┬───────────┘
              │                                    │
              └─────────────────┬──────────────────┘
                                 ▼
                 ┌──────────────────────────────────┐
                 │      Hilt DI container            │
                 │  FirebaseModules.kt provides:      │
                 │  FirebaseAuth · Firestore · RTDB   │
                 └────────────────┬─────────────────┘
                                  │
              ┌───────────────────┼────────────────────┐
              ▼                                        ▼
 ┌─────────────────────────┐              ┌──────────────────────────┐
 │   FirebaseDbSource        │              │    FireStoreSource        │
 │   (Realtime Database)     │              │    (Firestore)            │
 │   → presence, counters,   │              │    → Users, Messages,     │
 │     light key/value data  │              │      ClassRoom, Questions,│
 │                            │              │      Orders, Comments     │
 └────────────┬─────────────┘              └────────────┬─────────────┘
              │                                          │
              └────────────────────┬─────────────────────┘
                                    ▼
                    ┌────────────────────────────────┐
                    │  Repository layer (ViewModels)   │
                    │  FireStoreRepository ·            │
                    │  FirebaseDbRepository ·            │
                    │  DataStoreRepository               │
                    └────────────────┬───────────────┘
                                     ▼
                    ┌────────────────────────────────┐
                    │   DataStoreRepository (local)     │
                    │   Jetpack DataStore Preferences    │
                    │   → profile cache, score, cash     │
                    │   → renders instantly while         │
                    │     Firestore/RTDB sync in the      │
                    │     background                      │
                    └────────────────────────────────┘
```

**Data split:** Firestore holds structured content and social data that benefits from querying (`Users`, `Messages`, `ClassRoom`, `Questions`, `Requests`, `Orders`); Realtime Database is reserved for lightweight, frequently-updated key/value data such as online presence. DataStore replaces the old SQLite `Level` table as the local cache for profile fields, score, and cash so the UI has something to render immediately while Firestore/RTDB catch up.

**DI:** `FirebaseModules.kt` provides `FirebaseAuth`, `FirebaseFirestore`, and `FirebaseDatabase` instances via Hilt; repositories and ViewModels (`FireStoreViewModel`, `FirebaseDbViewModel`, `BaseViewModel`) receive them by constructor injection rather than constructing clients themselves.

---

## 3. Feature Notes

### 3.1 Roles: Parent, Child & Authentication

`AuthenticationActivity` is the single launcher Activity (see [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml)), fronting `SignInFragment` / `SignUpFragment` / `UserTypeFragment`. Account data is modeled as one `Users` document (see [`models/FirestoreModels.kt`](app/src/main/java/com/example/funlearnv2/models/FirestoreModels.kt)) carrying **both** child and parent fields (`child_name`, `child_grade`, `parent_name`, `parent_grade`, etc.) plus a `Roles` enum, rather than separate user tables. `ParentVerificationFragment` and `PhoneVerificationFragment` gate access before handing off to `ParentActivity` or `ChildActivity`.

### 3.2 Coloring Engine — Flood Fill

The scanline flood-fill from V1 was ported to Kotlin as an `object` singleton ([`views/widgets/FloodFill.kt`](app/src/main/java/com/example/funlearnv2/views/widgets/FloodFill.kt)), preserving the same span-queue algorithm to avoid recursive stack overflow on large bitmaps:

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

Chat is modeled directly in Firestore via `Messages`, `Comments`, `Reactions`, `Requests`, and `GroupDetails` (all in [`FirestoreModels.kt`](app/src/main/java/com/example/funlearnv2/models/FirestoreModels.kt)), with a `Mode` enum (`PRIVATE`, `PUBLIC`, `GROUP`) distinguishing conversation types on the same collections rather than separate schemas per mode. Fragments split by mode: `PublicChatFragment`, `PrivateChatFragment`, `GroupChatFragment`, `CommonChatFragment` (shared list/composer logic), `CommentFragment`, and `ChatStatusFragment` for presence.

### 3.4 Quiz, Classroom & Resources

- `ClassRoom` / `ClassResource` — a class has a `meet_url`, `teacher_id`, and a list of `Resource` items typed as `LINK`, `PDF`, `VIDEO`, or `QUIZ`.
- `Questions` — flat MCQ shape (`option_A`..`option_D`, `Answer`) tied to a `resource_id`.
- `QuizResult` — per-attempt outcome (`score`, `attempted`, `un_attempted`, `wrong`), mirroring V1's separation of quiz content from quiz outcomes (`QuizMaster` vs `Rank` in the original app).
- UI: `ClassTypeFragment` → `ClassContentFragment` → `QuizFragment` / `PdfViewFragment` / `WebFragment`, depending on `Resource` type.

### 3.5 Local State — DataStore

[`repository/DataStoreRepository.kt`](app/src/main/java/com/example/funlearnv2/repository/DataStoreRepository.kt) wraps a single `androidx.datastore.preferences` instance (provided by `ResourceProvider`) with typed getters/setters for every profile field (child + parent), account credentials cache, `score`, and `cash`. Each field is exposed as a `Flow<String>` internally and read via `.first()` for one-shot suspend access — replacing V1's raw `SQLiteOpenHelper` table with a coroutine-friendly, type-checked key-value store.

---

## 4. Module Reference

```
app/src/main/java/com/example/funlearnv2/
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
│   │                      chat, quiz, classroom, settings (§3.1–§3.4); a subset are stubs
│   │                      pending the full V1 migration (see Roadmap)
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

Full dependency list: [`app/build.gradle`](app/build.gradle).

## 6. Roadmap

- **Finish stub screens** (a subset of Fragments across games/quiz/settings are wired for navigation but not yet feature-complete) and confirm full V1 → V2 parity, then retire the legacy Java module.
- **Extend `DataStoreRepository`** into a full offline-first cache layer, with Firestore as the sync source of truth across devices.
- **Formalize the `FirebaseDbSource` / `FireStoreSource` boundary** into a documented data-ownership map as more collections are added.
- **Add unit tests**, starting with `FloodFill` and `DataStoreRepository` — both are side-effect-isolated and straightforward to unit test; no test suite exists yet.
- **Continue extracting game logic into ViewModels** (in progress for the larger game Fragments) to keep game rules testable independent of the view layer.
- **Replace the Asset Preview with real screenshots/screen recordings** of the running app.

## 7. Getting Started

```bash
git clone https://github.com/PRADEEPERIYASAMY/funlearn_app.git
cd funlearn_app
```

1. Create a Firebase project; enable **Firestore**, **Realtime Database**, **Authentication** (email/password + phone), **Crashlytics**, and **ML Kit Text Recognition**.
2. Download `google-services.json` into `app/` (a placeholder already exists in this repo — replace it with your own project's file).
3. Open the repository root folder in Android Studio (Gradle + `com.google.gms.google-services` + Hilt + Navigation Safe Args plugins) and let it sync.
4. Build:

```bash
./gradlew build
```

## 8. License & Contributing

This project is built and maintained solo; `CONTRIBUTING.md` and `CODE_OF_CONDUCT.md`, if present, should be treated as boilerplate rather than an active open-collaboration process. If you'd like to contribute, open an issue first to discuss scope.

Licensed under [MIT](LICENSE).

---

**Author:** Pradeep Periyasamy
