# DouYuLiveSample
  基于斗鱼第三方接口的直播App，软件使用了Rxjava+IjkPlayer+GreenDao+MVP
---
# 开发简介
软件借鉴于[video-live](https://github.com/littleMeng/video-live)重新架构了软件，重新设计了UI，抽取了基类，使用了Mvp的开发模式，加入了[Rxjava](https://github.com/ReactiveX/RxJava/tree/1.x)，使用[IjkPlayer](https://github.com/Bilibili/ijkplayer)进行视频播放，替换了原项目中的[vitamio](https://www.vitamio.org/)。由于偷懒弹幕模块摘自于[video-live](https://github.com/littleMeng/video-live)，使用的是[DanmakuFlame](https://github.com/Bilibili/DanmakuFlameMaster)。
## 软件截图
  ![image](https://github.com/LZzzzz/DouYuLiveSample/blob/master/screen-shot/Home.png)  ![image](https://github.com/LZzzzz/DouYuLiveSample/blob/master/screen-shot/Home2.png) ![image](https://github.com/LZzzzz/DouYuLiveSample/blob/master/screen-shot/Channels.png)<br>
  ![image](https://github.com/LZzzzz/DouYuLiveSample/blob/master/screen-shot/Play1.png) ![image](https://github.com/LZzzzz/DouYuLiveSample/blob/master/screen-shot/Play2.png)
## dependencies
```java
    dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:25.1.0'
    testCompile 'junit:junit:4.12'
    compile 'com.google.code.gson:gson:2.8.0'
    compile 'com.jakewharton:butterknife:8.4.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
    compile 'com.android.support:recyclerview-v7:25.1.0'
    compile 'io.reactivex:rxjava:1.0.14'
    compile 'io.reactivex:rxandroid:1.0.1'
    compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.6.8'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.android.support:design:25.1.0'
    compile 'com.github.Aspsine:SwipeToLoadLayout:1.0.3'
    compile 'me.drakeet.materialdialog:library:1.3.1'
    compile 'com.lzy.net:okgo:2.0.0'
    compile 'com.lzy.net:okrx:0.1.0'
    compile project(':swipeback')
    compile(name: 'ijkplayer-java-release', ext: 'aar')
    compile 'com.github.ctiao:DanmakuFlameMaster:0.6.2'
    compile 'org.greenrobot:greendao:3.1.1'
    compile 'com.android.support:multidex:1.0.1'
}
```
###联系方式
  * 邮箱 lcorekit@sina.com
