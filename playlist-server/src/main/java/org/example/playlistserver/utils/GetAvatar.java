package org.example.playlistserver.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetAvatar {
    @NotNull
    public static ResponseEntity<Resource> getResourceResponseEntity(String singerPath) {
        try {
            // 获取文件的完整路径
            Path path = Paths.get(singerPath).normalize(); // 转换为正规化的Path对象
            Resource resource = new UrlResource(path.toUri());
            // 确保文件存在且可读
            if (resource.exists() && resource.isReadable()) {
                // 尝试确定文件的内容类型
                String contentType = Files.probeContentType(path);
                if(contentType == null || contentType.isEmpty()) {
                    // 处理无法确定MIME类型的默认逻辑
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                // 返回文件资源响应实体，设置相应的内容类型
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            // 如果过程中发生错误，返回内部服务器错误响应实体
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
