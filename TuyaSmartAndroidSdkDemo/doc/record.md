### 1.13.10（2018-07-02）
* add interface for get max message time

### 1.13.9 （2018-06-13）
* fix some bug of register by email

### 1.13.8 （2018-06-12）
* fix some bug
* add new api of get email verification code  
* add new api of register by email

### 1.13.6  (2018-05-23)
* fix method getAllTimerWithDeviceId error

### 1.13.5 （2018-05-22）
* Discarded the interface resetUidPassword
* Fix some bugs about new firmware
* Fix add timer error with the api 'addTimerWithTask'

### 1.13.3 (2018-04-13)

* Support the group timer
* Optimize the timer interface
* Fix some bugs

### 1.13.2

* Fixed some OOM
* New feature of the group dp timer

### 1.12.0

* 增加了场景、头像上传、意见反馈等功能
* 增加了局域网通信的稳定性，修复了部分ANR问题。
* AndroidManifest.xml 有变更，请参考下面文档。
* GwDevResp 被DeviceBean替代，请及时修正。涉及到配网。
* ConfigDeviceErrorCode 被 ErrorCode 替代

### 1.7.6

* Fix crash in some language.

### 1.7.5
* Fix some bugs on the Android Jelly Bean

### 1.7.4
* 三方Mqtt库迁移出来，底层库稳定性增加,升级时注意添加相关依赖，可参考demo build.gradle配置。
* 增加蓝牙mesh相关功能
* 针对分享进行了升级
* rx混淆配置增加


### 1.5.11
* 修复定时接口传参有误的问题

### 1.5.10
* Fix some bugs on the Android Jelly Bean

### 1.5.9
* 修复安卓6.0、7.0个别手机兼容性问题。
* Fix some bugs
* Upgrade Transfer Security
* 第三方类库拆分出来，升级时注意添加相关依赖，可参考demo build.gradle配置。

### 1.4.6
* 修复个别设备出现非法请求的情况
* 修复异常若干

### 1.4.5
* Fix some bugs about controlling device on the LAN

### 1.4.4
* 发布局域网搜索设备接口。

### 1.4.2
* 发布第三方登陆接口。

### 1.4.1
* IAddMemberCallback    void onSuccess(Integer shareId)  Integer 需要替换成Long。
* modifyMemberName(int **) 、removeMember(int **) int 需要替换成long 类型。
* Volley 去除。
* apache http server 替换成OKHttp3.0。
* 局域网通信优化。
* 退出接口崩溃bug 修复。

### 1.3.4
* Fix some bugs about controlling device on the LAN
* 设备网络监听状态变化，导致设备显示离线。问题修复 IDevListener onNetworkStatusChanged();

### 1.3.2
* 修复只读dp无法上报问题

