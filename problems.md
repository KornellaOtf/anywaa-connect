# Android/Gradle build + runtime regression problems (analysis list)

This file lists the problems that are currently *most likely* and/or *must be confirmed* while getting a successful APK build and keeping the app’s core UX working after pruning features.

## How to use this file (recommended workflow)
1. Treat each section below like a “hypothesis”.
2. Build first. If build fails, only sections related to **build** are likely active; ignore runtime ones until APK installs.
3. After APK is produced, validate in order:
   - **Build correctness** (APK exists, size acceptable, no missing artifacts)
   - **Startup stability** (app launches without crash)
   - **Navigation / basic chat tools** (the minimal user path)
   - **Regressions** (premium + ads/consent must not block)
4. For every problem you confirm, append the observed symptom + log snippet to the corresponding bullet in this file.

This is intentionally verbose so you can analyze systematically without needing to scan the whole codebase.

## A) Build / Gradle pipeline problems

### A1) Build still not finished (no APK yet)
- The build is still running (currently around ~44–50%/KSP+resource processing stages in the log snapshot).
- Until `assembleDebug` completes and the APK is produced, we cannot confirm:
  - build success,
  - APK size,
  - whether runtime works.

### A2) “Stuck” phases may reappear with new pruning
Even if the original issue was “hang at mergeDebugNativeLibs”, pruning can shift the bottleneck to other tasks:
- dependency metadata resolution / AAR metadata checks,
- resource merging,
- KSP (Room),
- dex/code compilation,
- packaging step for native libs.

### A3) Gradle daemon / cache instability
- Earlier you saw Kotlin daemon crashes / “stop command received” and incremental cache corruption.
- We tried cache cleanup and `--no-daemon`, but Gradle may still:
  - fork a single-use daemon for parts of the build,
  - fail again due to remaining corruption (or memory pressure).

## B) Dependency pruning problems (compile-time + runtime)

### B1) Compile-time unresolved references (or indirect via generated code)
- Pruning removed large parts of the dependency graph (MediaPipe/ONNX/Nexa/QNN/LiteRT/RAG/PDF/CSV/etc.).
- If app code still references those modules, expected outcomes are:
  - compilation fails (best case, explicit errors),
  - or compilation succeeds but code paths crash at runtime (if reflection/dynamic lookup is used).

### B2) “Feature compiles but does nothing” regressions
- UI may still have buttons/screen routes that call into removed engine/services.
- Symptoms:
  - button does nothing,
  - endless loading spinners,
  - blank response views,
  - error toasts without clear messaging.

### B3) Navigation/start destination depends on removed services
- Even if Compose navigation compiles, routes may depend on view models that initialize removed services.
- Symptoms:
  - app opens but immediately navigates to error screen,
  - back stack gets corrupted,
  - startup crashes during `ViewModel` init.

## C) Asset pack / runtime asset problems

### C1) Asset pack modules disabled (qnn_pack / sd_pack / nexa_npu_pack)
- We commented out asset pack modules in `settings.gradle.kts`.
- If runtime still expects:
  - `assets/npu/**`
  - `assets/qnnlibs/**`
  - `assets/cvtbase/**`
  then features may fail hard or the app may stall during “engine/assets readiness” checks.

### C2) App loads assets unconditionally
- Common regression after disabling asset packs:
  - the app checks for files on first launch,
  - if missing, it blocks UI or throws.
- Symptoms:
  - crash on launch,
  - startup “loading…” forever,
  - engine initialization screen never finishes.

## D) Native library packaging problems

### D1) Missing native `.so` files after packaging rules
- Pruning included changes to `packaging { jniLibs { excludes/pickFirsts } }`.
- If any remaining features (or transitive libraries) still attempt to load removed `.so`:
  - `UnsatisfiedLinkError`
  - `System.loadLibrary` failures
  - dlopen failures
- Symptoms:
  - crash after selecting a feature requiring native libs,
  - crash only on some devices/ABIs.

### D2) Build-time native merge still heavy or inconsistent
- Even if we reduce `.so` count, Gradle native merge tasks may still:
  - pull many libs from transitive AARs,
  - or fail due to duplicate libs.
- Symptoms:
  - reappearance of the old hang (native merge),
  - or new packaging warnings/errors.

## E) Premium / Ad / Consent flow regression problems

### E1) Premium gating may still block navigation
- You removed/disabled premium gating at runtime intent-wise, but we must verify:
  - the premium state initialization happens early enough,
  - no “if premium” logic still redirects or blocks chat startup.
- Symptoms:
  - app always shows paywall,
  - core chat route redirects to subscription screen,
  - premium unlock state never becomes “enabled”.

### E2) Ads/UMP consent removal may leave dangling initialization
- Removing UMP consent call path can still leave:
  - Ad initialization code paths,
  - missing manifest metadata assumptions,
  - background coroutines expecting consent status.
- Symptoms:
  - crash on launch,
  - consent screen flashes then crashes,
  - ad manager throws at startup.

## F) Room/DB and KSP problems

### F1) KSP succeeds but runtime schema/migrations fail
- If the DB schema changed (or migration expectations exist), runtime can crash after install:
  - “no such table”
  - migration failures
  - DAO init exceptions
- Symptoms:
  - crash after opening any screen that touches the DB,
  - or blank screens if repositories fail.

## G) Resource/icon/string problems

### G1) UI assets referenced by code may be missing after pruning
- If some icons/drawables/fonts were introduced by dependencies removed, runtime can hit:
  - resource not found,
  - theme inflation issues,
  - missing strings for navigation labels.
- Symptoms:
  - crash only in specific screen compositions,
  - missing icons/buttons,
  - layout inflation exceptions.

## H) APK size success criteria not yet validated
- Even if the build eventually succeeds, we must verify:
  - the actual produced APK size is substantially smaller,
  - it matches the intended goal (e.g., <50MB if achievable).
- Without an artifact, size reduction is only “planned”, not proven.

## I) Testing gaps (currently none completed)
- No successful APK install/run has been completed after pruning.
- Therefore, these are unverified:
  - basic navigation,
  - chat/tools workflow end-to-end,
  - premium never blocking,
  - ads/consent never blocking/crashing,
  - any fallback behavior when pruned features are selected.
