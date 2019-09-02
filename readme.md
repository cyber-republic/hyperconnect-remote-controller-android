# HyperConnect IoT - Remote Controller for Android

The Remote Controller is the software component of the HyperConnect IoT Framework that runs on the mobile phone of a user and allows an overview of one or more connected [Edge Clients](https://github.com/cyber-republic/hyperconnect-edge-client) in a visual manner.

![HyperConnect IoT](/images/hyperconnect-banner.png)

### Installation

#### (Option One) Download from Google Play Store
- Download from Google Play Store: https://play.google.com/store/apps/details?id=com.hyper.connect

#### (Option Two) Download .APK
- The latest .APK file is located at: https://github.com/cyber-republic/hyperconnect-remote-controller-android/tree/master/demo
- Load the .APK onto your Android device manually.

#### (Option Three) Build from source code
**Step 1.** Download the source code for the Android Studio from: https://github.com/cyber-republic/hyperconnect-remote-controller-android

- Download by clicking the green button "Clone or download" on the GitHub repository.
- Or using Git:
```
git clone https://github.com/cyber-republic/hyperconnect-remote-controller-android
```

**Step 2.** The Elastos Carrier Android SDK in the next step requires the Elastos **Native Carrier** to be built beforehand. Follow the steps described at https://github.com/elastos/Elastos.NET.Carrier.Native.SDK to build the required files.

**Step 3.** Follow the steps to build the Elastos Carrier Android SDK: https://github.com/elastos/Elastos.NET.Carrier.Android.SDK . The Elastos Android SDK will require the files from the Elastos Carrier Native SDK previously built.

**Step 4.** Open the HyperConnect Android Studio project in the Android Studio IDE.

**Step 5.** Add the shared libraries created by the **Elastos Carrier Android SDK** in the HyperConnect Android project in the following manner:

```
app/libs
    |--armeabi-v7a
        |--libcrystal.so
        |--libelacarrier.so
        |--libelasession.so
        |--libcarrierjni.so
        |--libelafiletrans.so
    |--arm64-v8a
        |--libcrystal.so
        |--libelacarrier.so
        |--libelasession.so
        |--libcarrierjni.so
        |--libelafiletrans.so
    |--x86
        |--libcrystal.so
        |--libelacarrier.so
        |--libelasession.so
        |--libcarrierjni.so
        |--libelafiletrans.so
    |--x86_64
        |--libcrystal.so
        |--libelacarrier.so
        |--libelasession.so
        |--libcarrierjni.so
        |--libelafiletrans.so
```

**Step 6.** Build the project.

**Step 7.** Start the Android Emulator.

### Libraries

- Elastos Carrier Native SDK: https://github.com/elastos/Elastos.NET.Carrier.Native.SDK
- Elastos Carrier Android SDK: https://github.com/elastos/Elastos.NET.Carrier.Android.SDK

### Contribution
We welcome contributions to the HyperConnect IoT project.

### Acknowledgments
A sincere thank you to all teams and projects that we rely on directly or indirectly.

### License
This project is licensed under the terms of the [GPLv3 license](https://github.com/cyber-republic/hyperconnect-remote-controller-android/blob/readme/LICENSE).
