# video-to-ascii
#### 介绍
该项目实现了将视频文件转换为 ASCII 码图像并在终端中播放的功能。通过使用 OpenCV 和 JCode(效果较差) 库，读取视频帧并将每一帧的像素数据映射为相应的 ASCII 字符，生成 ASCII 视频。

#### 主要功能
视频转 ASCII：将视频的每一帧图像转化为等比例缩放后的 ASCII 字符图像。 
终端播放：将生成的 ASCII 图像按帧显示在终端中，形成视频效果。

#### 环境说明
1. java version 17.0.7
2. maven 依赖
jcodec 0.2.5
opencv 4.9.0-0
#### 使用说明
使用OpenCV需要安装opencv库，具体操作可以参考官网，下载安装opencv-4.9.0-windows.exe。
然后将64位dll文件复制到java bin路径下。
