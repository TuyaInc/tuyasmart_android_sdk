### 1.13.3 (2018-04-13)

* support the group timer
* optimize the timer interface
* fix some bugs

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

*  fix crash in some language.

### 1.7.5
*  4.1兼容问题修复

### 1.7.4
* 三方Mqtt库迁移出来，底层库稳定性增加,升级时注意添加相关依赖，可参考demo build.gradle配置。
* 增加蓝牙mesh相关功能
* 针对分享进行了升级
* rx混淆配置增加


### 1.5.11
* 修复定时接口传参有误的问题

### 1.5.10
* 修复安卓在低版本系统上崩溃的问题

### 1.5.9
* 修复安卓6.0、7.0个别手机兼容性问题。
* 修复一些空指针问题。
* 增强通信安全
* 第三方类库拆分出来，升级时注意添加相关依赖，可参考demo build.gradle配置。

### 1.4.6
* 修复个别设备出现非法请求的情况
* 修复异常若干

### 1.4.5
* 修复局域网连接问题

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
* 局域网通信不稳定问题修复
* 设备网络监听状态变化，导致设备显示离线。问题修复 IDevListener onNetworkStatusChanged();

### 1.3.2
* 修复只读dp无法上报问题

