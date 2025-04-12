package org.example.imageeditor2;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageFilterService {

    public byte[] greyFilter(byte[] file) throws IOException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(file);

        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                // Получаем цвет пикселя
                Color color = new Color(bufferedImage.getRGB(i, j));

                // Вычисление серого значения на основе RGB
                int grayValue = (int) (color.getRed() * 0.3 + color.getGreen() * 0.59 + color.getBlue() * 0.11);
                Color grayColor = new Color(grayValue, grayValue, grayValue);

                // Устанавливаем новый цвет в сером изображении
                newImage.setRGB(i, j, grayColor.getRGB());
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        boolean isWritten = false;
        String[] formats = {"jpg", "png", "bmp", "gif"};

        for (String format : formats) {
            if (ImageIO.write(newImage, format, byteArrayOutputStream)) {
                isWritten = true;
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                return imageBytes;
            }
        }

        return null;
    }
}
