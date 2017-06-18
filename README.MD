
###

开机自动启动,无GUI,无服务进程,开机自动更新`sdcard`目录下php二进制文件


### 交叉编译PHP7.1



```
[PHP Modules]
Core
curl
date
filter
hash
json
mbstring
openssl
pcre
Reflection
session
SPL
standard
tokenizer

[Zend Modules]
```

php7.1 for android 二进制文件  http://share.suconghou.cn/files/arm/php_android.xz

交叉编译参见 http://blog.suconghou.cn


### busybox httpd

`busybox httpd` 仅支持http1.0,对于http1.1的`range`,有简单实现,但是不能seek指定块.

且是采用fork多进程工作,效率不佳,故采用`golang`实现`http server`,作为替代.


golang microhttp 二进制文件 http://share.suconghou.cn/files/arm/microhttp.xz

