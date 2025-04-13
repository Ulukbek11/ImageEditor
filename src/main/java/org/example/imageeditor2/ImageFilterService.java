package org.example.imageeditor2;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
                Color color = new Color(bufferedImage.getRGB(i, j));

                int grayValue = (int) (color.getRed() * 0.3 + color.getGreen() * 0.59 + color.getBlue() * 0.11);
                Color grayColor = new Color(grayValue, grayValue, grayValue);

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

    public byte[] colorInversionFilter(byte[] file) throws IOException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(file);

        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                // Получаем цвет пикселя
                Color color = new Color(bufferedImage.getRGB(i, j));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;

                Color inversion = new Color(red, green, blue);

                newImage.setRGB(i, j, inversion.getRGB());
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

    public byte[] blurEffect(byte[] file) throws IOException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(file);

        // Чтение изображения из потока
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        if (bufferedImage == null) {
            throw new IOException("Не удалось прочитать изображение.");
        }

        // Применение сильного размытия
        BufferedImage blurredImage = applyStrongGaussianBlur(bufferedImage);



        // Запись обработанного изображения в байтовый поток
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean isWritten = false;
        String[] formats = {"jpg", "png", "bmp", "gif"};

        // Пытаемся записать изображение в разные форматы
        for (String format : formats) {
            if (ImageIO.write(blurredImage, format, byteArrayOutputStream)) {
                isWritten = true;
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close(); // Закрываем поток
                return imageBytes;
            }
        }

        byteArrayOutputStream.close(); // Закрываем поток
        return null;
    }

    private BufferedImage applyStrongGaussianBlur(BufferedImage originalImage) {
        // Сильное размытие: ядро 9x9
        float[] matrix = {
                1f/81, 2f/81, 3f/81, 4f/81, 5f/81, 4f/81, 3f/81, 2f/81, 1f/81,
                2f/81, 4f/81, 6f/81, 8f/81, 10f/81, 8f/81, 6f/81, 4f/81, 2f/81,
                3f/81, 6f/81, 9f/81, 12f/81, 15f/81, 12f/81, 9f/81, 6f/81, 3f/81,
                4f/81, 8f/81, 12f/81, 16f/81, 20f/81, 16f/81, 12f/81, 8f/81, 4f/81,
                5f/81, 10f/81, 15f/81, 20f/81, 25f/81, 20f/81, 15f/81, 10f/81, 5f/81,
                4f/81, 8f/81, 12f/81, 16f/81, 20f/81, 16f/81, 12f/81, 8f/81, 4f/81,
                3f/81, 6f/81, 9f/81, 12f/81, 15f/81, 12f/81, 9f/81, 6f/81, 3f/81,
                2f/81, 4f/81, 6f/81, 8f/81, 10f/81, 8f/81, 6f/81, 4f/81, 2f/81,
                1f/81, 2f/81, 3f/81, 4f/81, 5f/81, 4f/81, 3f/81, 2f/81, 1f/81
        };

        // Применение ядра через ConvolveOp
        Kernel kernel = new Kernel(9, 9, matrix);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolveOp.filter(originalImage, null);
    }


}