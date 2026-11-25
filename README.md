# ABD Universal — minimal Android overlay starter

This is a small Android project that builds a basic floating ABD panel APK. It is a UI starter, **not a universal game mod/injector**.

## Build on GitHub
1. Create a new GitHub repository.
2. Upload the contents of this ZIP (the files inside the `ABD_Universal_Minimal` folder) to the repository root.
3. Open **Actions** and run **Build ABD APK**, or push to `main`.
4. Open the completed workflow run and download the `ABD-Universal-debug` artifact.
5. Extract `app-debug.apk` from the artifact.

## Install / use
- Install the APK on a compatible Android/Quest environment.
- Open ABD and grant **Display over other apps** permission if the OS offers it.
- Tap **Open ABD panel**.
- The panel is draggable by its header and can be closed from its bottom button.

## Important Quest limitation
Android `TYPE_APPLICATION_OVERLAY` windows are not guaranteed to appear in the immersive VR compositor over a running Quest game. A normal APK also cannot globally listen for the Quest controller X button while another app/game owns input. The included X key handler only works while ABD's own activity has focus.

For a true in-game overlay over an immersive Quest title, a supported Quest compositor/overlay integration or a game-side plugin is required. Game-changing mods also need per-game integration; this project intentionally contains no code injection, anti-cheat bypass, or game modification.

## Project scope
- Minimal dark/purple ABD UI scaffold
- Android overlay permission flow
- Draggable Android overlay panel
- GitHub Actions debug APK build
- Placeholder categories ready for supported, legitimate integrations
