# iFunny Video Downloader app
Downloads ifunny posts. \
Just share a post through the iFunny app to this app and then it'll download the content to ``/Pictures/iFunnyDL`` or ``/Pictures/iFunny``.
## Features
- **Automatically removes watermarks.** 
- Supports Image, GIF, and **Video** downloads.  
- Custom naming schemes (kinda)
- Download Logs

You can see some of these features in the [videos below](https://github.com/anthony1x6000/iFunnyDL#video-tutorials). 

## Downloads
[Check out releases.](https://github.com/anthony1x6000/iFunnyDL/releases) 

If you want the latest unreleased builds, download the latest build by going to [actions](https://github.com/anthony1x6000/iFunnyDL/actions) and checking out the artifacts from the last successful build. \
Or, you can use [nightly.link](https://nightly.link/anthony1x6000/iFunnyDL/workflows/build/main/iFunnyDL.zip) to download the latest build.

## Compatibility
Best experience with Android 10+

## Usage
Download the APK, install it, and share an iFunny post to the app to download. 

## App crashing
The app may crash due to background battery restrictions such as having Battery Saver enabled. Bypass this by giving this app <a href="#batusage">Unrestricted App battery usage</a>.

## Video tutorials
<table>
  <tr>
    <th id="videoDL">Video downloading</th>
    <th id="imgDL">Image downloading, autocropping, and settings</th>
    <th id="logs">Download Log</th>
  </tr>
  <tr>
    <td><a href="https://imgur.com/KhgcT8b"><img width=350 src="https://i.imgur.com/00012377123.jpg" /></a></td>
    <td><a href="https://imgur.com/rsARcO0"><img width=350 src="https://i.imgur.com/UenbhS3.png" /></a></td>
    <td><img width=350 src="https://user-images.githubusercontent.com/33004321/229006556-282cc7de-42a0-48a4-858f-3fd06b46a06f.png" /></td>
  </tr>
  <tr>
    <th id="batusage">Battery Usage (Android 12)</th>
    <th id="batusage">Battery Usage (&lt;Android 12)</th>
  </tr>
  <tr>
    <td><a href="https://imgur.com/FpwwmSR"><img width=350 src="https://i.imgur.com/QxmFQfe.png" /></a><br>Optimized also works, usually.</td>
    <td><a href="https://imgur.com/pgZTq2J"><img width=350 src="https://i.imgur.com/BEIwnlW.png" /></a></td>
  </tr>
</table>

## Credit
- [iFunny-Cropper](https://github.com/switchswap/iFunny-Cropper) by [switchswap](https://github.com/switchswap) helped me figure out crop service.
<!-- ## Known bugs -->
<!-- The following error message comes up after the app is done cropping an image. This is caused by the app not asking FileInputStream to close. 
```agsl
    E/System: java.io.IOException: close failed: EACCES (Permission denied)
``` -->
