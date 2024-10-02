package com.pacey.convertor;

/**
 * @author Pacey &#x738B;&#x57F9;&#x8D24
 * @version 1.0
 * @classname VideoToAscii
 * @date 2024/10/01 16:32
 * @description 视频转ASCII
 * @since 1.0
 */

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public class VideoToAscii {

    private static final Logger logger = Logger.getLogger("VideoToAscii");

    private static final char[] ASCII_CHARS = " .,:;+*?%S#@".toCharArray(); // ASCII 字符从暗到亮

    public static void main(String[] args) {
        // 视频路径
        String videoPath = Objects.requireNonNull(VideoToAscii.class.getResource("/video/commander.mp4")).getPath();
//        System.out.println(videoPath);
//        String videoPath2 = "E:\\Documents\\IDEAProjects\\video-to-ascii\\young-commander\\src\\main\\resources\\video\\commander.mp4";
//        System.out.println(videoPath2);
        int width = 96; // 输出宽度
        int height = 64; // 输出高度

        FrameGrab grab;
        try {
            grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(new File(videoPath)));
            Picture frame;
            while ((frame = grab.getNativeFrame()) != null) {
                BufferedImage image = toBufferedImage(frame);
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_FAST);
                BufferedImage bwImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D graphics = bwImage.createGraphics();
                graphics.drawImage(scaledImage, 0, 0, null);
                graphics.dispose();

                StringBuilder sb = new StringBuilder();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int gray = bwImage.getRGB(x, y) & 0xFF; // 只取灰度值
                        sb.append(ASCII_CHARS[gray * (ASCII_CHARS.length - 1) / 255]);
                    }
                    sb.append('\n');
                }

                System.out.println(sb);
                Thread.sleep(33); // 控制播放速度
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage toBufferedImage(Picture picture) {
        int width = picture.getWidth();
        int height = picture.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        byte[] planeData = picture.getPlaneData(0); // 假设我们只处理 Y 平面（亮度）
        int stride = picture.getPlaneWidth(0); // 每行的字节数

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = y * stride + x;
                int luma = planeData[offset] & 0xFF; // 获取亮度值
                int argb = (luma << 16) | (luma << 8) | luma; // 创建灰度颜色
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }
}