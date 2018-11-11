
### 项目说明


##### 1.签名注意

 - 一定可行的方案： 只使用 v1 方案
 - 不一定可行的方案：同时使用 v1 和 v2 方案
 - 对 7.0 以下一定不行的方案：只使用 v2 方案

 结论：Signature Versions不能只选择 V2(Full APK Signature)，应该选择V1(Jar Signature)，或者选择 V1和 V


##### 2.打包说明

 使用美团的walle进行打包

 链接地址：https://github.com/Meituan-Dianping/walle