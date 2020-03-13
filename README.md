# VirtualMenu v2 Bukkit, Sponge, Nukkit

这个插件允许你在Minecraft服务器中创建自定义的菜单，类似ChestCommands和TrMenu，但这个插件基于 Packet 的拦截和发送而不是 BukkitAPI，这样可以保证安全性，同时可以方便地移植到其他平台。本插件旨在提供安全，简单，高效的自定义菜单。
__目前v2仍然在开发中，请勿在生产环境使用__
从v1到v2可能还需要一些时间，尽管目前v2 Bukkit平台版已经基本可以用了，但目前没有经过大量测试，所以生产环境请使用旧的版本（0.0.X）。
https://www.mcbbs.net/thread-894621-1-1.html 
***


## 插件统计
https://bstats.org/plugin/bukkit/VirtualMenu/5355  
***


## 相比于v1
v2 几乎重写了整个插件，把与服务端无关的代码分离出来，同时采用了表达能力更强的配置方式
***


## TODO
- [ ] 平台支持
  - [x] Bukkit
  - [ ] Sponge
  - [ ] Nukkit
- [ ] Bukkit
  - [x] Vault
  - [x] GemEconomy
  - [x] PlayerPoints
  - [x] NBT Support
  - [x] PlaceholderAPI
  - [ ] CommandPrompter
- [ ] 核心功能
  - [x] 变量支持
  - [ ] 自定义动画
  - [x] ChestCommands 菜单加载
  - [ ] ChestCommands 自动转换
  - [ ] GUI 菜单编辑器
  - [ ] 更多的自带命令
  - [ ] 精确的错误提示（进行中，不完全）
***

## 授权
__GPL-3.0__
***

## 如何构建
你需要jdk8和maven来构建这个插件
```
git clone https://github.com/ColorsWind/VirtualMenu.git
mvn clean install
```
***


## 下载
https://github.com/ColorsWind/VirtualMenu/releases
