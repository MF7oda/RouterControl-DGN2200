# Router Control V1 Prototype
Target: NETGEAR DGN2200M, local management at http://192.168.0.1

This first build is intentionally READ-ONLY. It tests Android -> router authentication and retrieval before enabling any configuration writes.

## Build
Open the root folder in Android Studio (JDK 17), let Gradle sync, then Build > Build APK(s).

## Test
Connect Android device to the router Wi-Fi. Enter the router admin credentials only inside the app and press the read button.

## Next implementation after real-device response
- structured Router Status parser
- Attached Devices parser
- Wi-Fi/guest profiles
- ADSL/Internet/LAN/WAN views
- diagnostics and reboot
- safe write operations after endpoints/form fields are verified on-device

## Build APK with GitHub Actions
1. Upload the contents of this folder to the root of a GitHub repository.
2. Open Actions > Build RouterControl APK > Run workflow.
3. After it succeeds, download the RouterControl-V1-APK artifact.
4. Unzip the artifact and install RouterControl-V1.apk on the Android phone.
