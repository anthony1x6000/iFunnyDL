# iFunny Video Downloader app
Downloads ifunny videos. \
Just share a post through the iFunny app to this app and then it'll download the video to /Pictures/iFunnyDL. \
Customizable file formatting (kinda), default is unixtime-iFunny.mp4

Can download images, but it doesn't crop out the iFunny watermark. \
Note: Release [1.4.0](https://github.com/anthony1x6000/iFunnyDL/releases/tag/1.4.0) adds support for removing iFunny watermarks from images. 

## Download
[Check out releases.](https://github.com/anthony1x6000/iFunnyDL/releases) 

If you want the latest unreleased builds, download the latest build by going to [actions](https://github.com/anthony1x6000/iFunnyDL/actions) and checking out the artifacts from the last successful build. \
Or, you can use [nightly.link](https://nightly.link/anthony1x6000/iFunnyDL/workflows/build/main/iFunnyDLzip.zip) to download the latest build.

## Compatibility
Verified compatibility with Android 6, 10, 13. \
Best experience with Android 10+

## Usage
Download the APK, install it, and share an iFunny post to the app to download. 
## Video tutorials
<table>
  <tr>
    <th>Video downloading</th>
    <th>Image downloading, autocropping, and settings</th>
  </tr>
  <tr>
    <td><a href="https://imgur.com/KhgcT8b"><img width=350 src="https://i.imgur.com/72w0sI0.png" /></a></td>
    <td><a href="https://imgur.com/rsARcO0"><img width=350 src="https://i.imgur.com/UenbhS3.png" /></a></td>
  </tr>
</table>

## Credit
- [iFunny-Cropper](https://github.com/switchswap/iFunny-Cropper) by [switchswap](https://github.com/switchswap) helped me figure out crop service.
<!-- ## Known bugs -->
<!-- The following error message comes up after the app is done cropping an image. This is caused by the app not asking FileInputStream to close. 
```agsl
    E/System: java.io.IOException: close failed: EACCES (Permission denied)
``` -->
