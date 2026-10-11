package com.stateflow.file.service;

import java.io.InputStream;

import com.stateflow.common.core.utils.file.FileTypeUtils;
import com.stateflow.common.core.utils.file.FileUtils;
import com.stateflow.common.core.utils.uuid.UUID;
import com.stateflow.system.api.domain.SysFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.nacos.common.utils.IoUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.file.config.MinioConfig;
import com.stateflow.file.utils.FileUploadUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

/**
 * Minio 文件存储
 *
 * @author stateflow
 */
@Service("minio")
public class MinioSysFileServiceImpl implements ISysFileService
{
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public SysFile uploadFile(MultipartFile file) throws Exception
    {
        InputStream inputStream = null;
        try
        {
//            String fileName = FileUploadUtils.extractFilename(file);
            String uuidFileName = UUID.randomUUID().toString(true);
            String extension = FileTypeUtils.getExtension(file);
            String fileName = uuidFileName + "." + extension;
            inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);

            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(fileName));
            sysFile.setUrl(getRootPath() + fileName);
            sysFile.setRelativeUrl(fileName);
            return sysFile;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Minio Failed to upload file", e);
        }
        finally
        {
            IoUtils.closeQuietly(inputStream);
        }
    }

    /**
     * Minio文件删除接口
     * 
     * @param fileUrl 文件访问URL
     * @throws Exception
     */
    @Override
    public void deleteFile(String fileUrl) throws Exception
    {
        try
        {
            String minioFile = StringUtils.substringAfter(fileUrl, minioConfig.getBucketName());
            client.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName()).object(minioFile).build());
        }
        catch (Exception e)
        {
            throw new RuntimeException("Minio Failed to delete file", e);
        }
    }

    @Override
    public String getRootPath() {
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/";
    }
}
