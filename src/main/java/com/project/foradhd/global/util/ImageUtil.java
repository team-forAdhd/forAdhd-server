package com.project.foradhd.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class ImageUtil {

    public static int[] getImageDimensions(MultipartFile image) {
        try (InputStream inputStream = image.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            return new int[]{width, height};
        } catch (IOException e) {
            log.error("Failed to convert MultipartFile to InputStream");
            return new int[]{0, 0};
        }
    }
}
