package com.shudong.common.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.UUID;

/**
 * 腾讯云COS文件上传工具类
 */
@Slf4j
@Component
public class UploadUtil {
    
    @Value("${cos.client.accessKey}")
    private String accessKey;

    @Value("${cos.client.secretKey}")
    private String secretKey;

    @Value("${cos.client.bucket}")
    private String bucket;

    @Value("${cos.client.region}")
    private String region;

    @Value("${cos.client.cosHost}")
    private String cosHost;

    // COS客户端单例
    private COSClient cosClient;

    // 支持的文件类型
    private static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "webp", "gif"};
    
    // 图片大小限制（字节）
    private static final long AVATAR_MAX_SIZE = 2 * 1024 * 1024; // 2MB
    private static final long IMAGE_MAX_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long GIF_MAX_SIZE = 5 * 1024 * 1024; // 5MB
    
    // 压缩参数
    private static final float COMPRESSION_QUALITY = 0.9f;
    private static final int MAX_COMPRESSION_ATTEMPTS = 2;

    /**
     * 初始化COS客户端
     */
    @PostConstruct
    public void initCOSClient() {
        try {
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            this.cosClient = new COSClient(cred, clientConfig);
            log.info("COS客户端初始化成功");
        } catch (Exception e) {
            log.error("COS客户端初始化失败", e);
        }
    }

    /**
     * 销毁COS客户端
     */
    @PreDestroy
    public void destroyCOSClient() {
        if (this.cosClient != null) {
            this.cosClient.shutdown();
            log.info("COS客户端已关闭");
        }
    }

    /**
     * 上传头像到avatars目录
     * @param file 上传的文件
     * @return 上传成功后的图片URL
     */
    public String uploadAvatar(MultipartFile file) {
        // 校验文件类型
        if (!isValidImageType(file)) {
            throw new IllegalArgumentException(getFileTypeErrorMessage(file));
        }

        // 校验大小
        if (file.getSize() > AVATAR_MAX_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制，最大支持2MB");
        }

        MultipartFile processedFile = file;
        String fileExtension = getFileExtension(file).toLowerCase();
        
        // 对JPG/JPEG/PNG图片进行有损WebP转换
        if ("jpg".equals(fileExtension) || "jpeg".equals(fileExtension) || "png".equals(fileExtension)) {
            processedFile = convertToWebPLossy(file, COMPRESSION_QUALITY);
            if (processedFile == null) {
                throw new RuntimeException("图片格式转换失败");
            }
            fileExtension = "webp";
        }

        // 生成唯一文件名
        String fileName = generateFileName("avatar", fileExtension);

        return uploadToCOS(processedFile, "avatars", fileName);
    }

    /**
     * 上传图片到images目录
     * @param file 上传的文件
     * @return 上传成功后的图片URL
     */
    public String uploadImage(MultipartFile file) {
        // 验证文件类型
        if (!isValidImageType(file)) {
            throw new IllegalArgumentException(getFileTypeErrorMessage(file));
        }

        MultipartFile processedFile = file;
        String fileExtension = getFileExtension(file).toLowerCase();
        
        // 先将图片无损转换为WebP格式
        if (!"gif".equals(fileExtension) && !"webp".equals(fileExtension)) {
            processedFile = convertToWebPLossless(file);
            if (processedFile == null) {
                throw new RuntimeException("图片格式转换失败");
            }
            fileExtension = "webp";
        }

        // 如果转换后的大小仍然超过限制，进行有损压缩
        if (processedFile.getSize() > IMAGE_MAX_SIZE) {
            processedFile = compressImageWithWebP(processedFile, COMPRESSION_QUALITY, 1);
            if (processedFile == null) {
                throw new RuntimeException("图片压缩失败，请尝试上传较小的图片");
            }
        }

        String fileName = generateFileName("image", fileExtension);

        return uploadToCOS(processedFile, "images", fileName);
    }

    /**
     * 上传文件到腾讯云COS
     * @param file 上传的文件
     * @param directory 目录名称
     * @param fileName 文件名
     * @return 上传成功后的文件URL
     */
    private String uploadToCOS(MultipartFile file, String directory, String fileName) {
        // 检查COS客户端是否已初始化
        if (this.cosClient == null) {
            log.error("COS客户端未初始化");
            throw new RuntimeException("云存储服务不可用");
        }
        
        try {
            // 设置对象元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // 创建上传请求
            String key = directory + "/" + fileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, 
                    file.getInputStream(), metadata);
            
            // 执行上传
            this.cosClient.putObject(putObjectRequest);
            
            // 生成访问URL
            String fileUrl = cosHost + "/" + key;
            
            log.info("文件上传成功: {}, 大小: {} bytes", fileUrl, file.getSize());
            return fileUrl;
            
        } catch (Exception e) {
            log.error("上传文件到COS失败", e);
            throw new RuntimeException("上传到云存储失败: " + e.getMessage());
        }
    }


    /**
     * 获取文件扩展名
     * @param file 等待上传的图片或 GIF
     * @return 文件扩展名
     */
    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return originalFilename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 生成唯一文件名
     * @param prefix 文件名前缀
     * @param extension 文件扩展名
     * @return 唯一文件名
     */
    private String generateFileName(String prefix, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return String.format("%s_%s_%s.%s", prefix, timestamp, uuid, extension);
    }


    /**
     * 压缩后的MultipartFile实现
     */
    private static class CompressedMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public CompressedMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("压缩文件不支持 transferTo 操作");
        }
    }

    /**
     * 将图片转换为WebP格式（无损）
     * @param file 原始图片文件
     * @return 转换后的WebP文件
     */
    private MultipartFile convertToWebPLossless(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return null;
            }

            ByteArrayOutputStream webpStream = new ByteArrayOutputStream();
            
            // 使用WebP格式写入
            ImageIO.write(image, "webp", webpStream);
            
            byte[] webpBytes = webpStream.toByteArray();
            
            return new CompressedMultipartFile(
                file.getName(),
                file.getOriginalFilename().replaceAll("\\.[^.]+$", ".webp"),
                "image/webp",
                webpBytes
            );
            
        } catch (Exception e) {
            log.error("WebP无损转换失败", e);
            return null;
        }
    }

    /**
     * 将图片转换为WebP格式（有损）
     * @param file 原始图片文件
     * @param quality 压缩质量为 0.9
     * @return 转换后的WebP文件
     */
    private MultipartFile convertToWebPLossy(MultipartFile file, float quality) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return null;
            }

            ByteArrayOutputStream webpStream = new ByteArrayOutputStream();
            
            // 获取WebP图片写入器
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                log.error("找不到WebP图片写入器");
                return null;
            }
            
            ImageWriter writer = writers.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(webpStream);
            writer.setOutput(ios);
            
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                
                // 设置压缩类型为有损
                try {
                    param.setCompressionType("Lossy");
                } catch (IllegalArgumentException e) {
                    // 如果"Lossy"类型不支持，尝试其他可能的类型
                    log.warn("Lossy压缩类型不支持，尝试使用默认压缩类型");
                    String[] compressionTypes = param.getCompressionTypes();
                    if (compressionTypes != null && compressionTypes.length > 0) {
                        param.setCompressionType(compressionTypes[0]);
                    }
                }
                
                param.setCompressionQuality(quality);
            }
            
            writer.write(null, new IIOImage(image, null, null), param);
            
            ios.close();
            writer.dispose();
            
            byte[] webpBytes = webpStream.toByteArray();
            
            return new CompressedMultipartFile(
                file.getName(),
                file.getOriginalFilename().replaceAll("\\.[^.]+$", ".webp"),
                "image/webp",
                webpBytes
            );
            
        } catch (Exception e) {
            log.error("WebP有损转换失败", e);
            return null;
        }
    }

    /**
     * 对WebP图片进行有损压缩（最多压缩 2 次）
     * @param file WebP图片文件
     * @param quality 压缩质量
     * @param currentAttempt 当前尝试次数
     * @return 压缩后的文件，如果压缩后仍然超过大小限制则返回null，不给上传
     */
    private MultipartFile compressImageWithWebP(MultipartFile file, float quality, int currentAttempt) {
        try {
            // 检查是否超过最大尝试次数
            if (currentAttempt > MAX_COMPRESSION_ATTEMPTS) {
                log.warn("已达到最大压缩次数({})，文件大小仍然超过限制，不予上传", MAX_COMPRESSION_ATTEMPTS);
                return null;
            }

            // 进行有损压缩
            MultipartFile compressedFile = convertToWebPLossy(file, quality);
            if (compressedFile == null) {
                return null;
            }

            // 检查压缩后的大小
            if (compressedFile.getSize() <= IMAGE_MAX_SIZE) {
                log.info("第{}次压缩成功，文件大小: {} bytes", currentAttempt, compressedFile.getSize());
                return compressedFile;
            } else {
                log.info("第{}次压缩后大小: {} bytes，仍然超过限制，继续压缩", currentAttempt, compressedFile.getSize());
                return compressImageWithWebP(compressedFile, quality, currentAttempt + 1);
            }
            
        } catch (Exception e) {
            log.error("WebP图片压缩失败（第{}次尝试）", currentAttempt, e);
            return null;
        }
    }

    /**
     * 文件类型检查
     * @param file 上传的文件
     * @return 是否支持的文件类型
     */
    private boolean isValidImageType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        String extension = getFileExtension(file).toLowerCase();
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (allowedType.equals(extension)) {
                if ("gif".equals(extension) && file.getSize() > GIF_MAX_SIZE) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 文件类型错误
     * @param file 上传的文件
     * @return 错误消息
     */
    private String getFileTypeErrorMessage(MultipartFile file) {
        String extension = getFileExtension(file).toLowerCase();
        if ("gif".equals(extension) && file.getSize() > GIF_MAX_SIZE) {
            return "GIF文件大小超过限制，最大支持5MB";
        }
        return "不支持的文件类型，仅支持: jpg, jpeg, png, webp, gif";
    }
}
