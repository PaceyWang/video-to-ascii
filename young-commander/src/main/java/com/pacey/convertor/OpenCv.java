package com.pacey.convertor;

/**
 * @author Pacey &#x738B;&#x57F9;&#x8D24
 * @version 1.0
 * @classname OpenCv
 * @date 2024/10/01 17:56
 * @description
 * @since 1.0
 */

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.Objects;

public class OpenCv {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // 加载 OpenCV 库
    }

    private static final char[] ASCII_CHARS = " .,:;+*?%S#@".toCharArray(); // ASCII 字符从暗到亮

    public static void main(String[] args) {
        // 视频路径
        String videoPath = Objects.requireNonNull(VideoToAscii.class.getResource("/video/commander.mp4")).getPath();
        int newWidth = 100; // 输出宽度
        int newHeight = 0; // 输出高度（将根据宽高比自动计算）

        try {
            VideoCapture capture = new VideoCapture(videoPath);
            if (!capture.isOpened()) {
                System.err.println("无法打开视频文件: " + videoPath);
                return;
            }

            Mat frame = new Mat();
            while (true) {
                if (!capture.read(frame)) {
                    break; // 读取不到帧时退出循环
                }

                // 计算新的高度以保持宽高比
                if (newHeight == 0) {
                    double aspectRatio = (double) frame.height() / frame.width();
                    newHeight = (int) (aspectRatio * newWidth * 0.55);
                }

                // 调整帧大小
                Mat resizedFrame = new Mat();
                Imgproc.resize(frame, resizedFrame, new Size(newWidth, newHeight));

                // 转换为灰度图像
                Mat grayFrame = new Mat();
                Imgproc.cvtColor(resizedFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);

                // 显示 ASCII 图像
                printAsciiImage(grayFrame, newWidth, newHeight);

                // 控制播放速度
                Thread.sleep(33);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printAsciiImage(Mat grayFrame, int width, int height) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte grayValue = (byte) grayFrame.get(y, x)[0];
                int index = (int) (grayValue * (ASCII_CHARS.length - 1) / 255.0);
                if (index < 0) {
                    index = 0;
                } else if (index >= ASCII_CHARS.length) {
                    index = ASCII_CHARS.length - 1;
                }
                sb.append(ASCII_CHARS[index]);
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }
}
