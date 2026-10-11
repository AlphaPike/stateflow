package com.stateflow.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.RemoteFileService;
import com.stateflow.system.api.domain.SysFile;
import com.stateflow.system.api.domain.oss.OssPresignForm;
import com.stateflow.system.api.domain.oss.OssPresignResultVo;

/**
 * 文件服务降级处理
 * 
 * @author stateflow
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService()
        {
            @Override
            public R<SysFile> upload(MultipartFile file)
            {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> delete(String fileUrl)
            {
                return R.fail("删除文件失败:" + throwable.getMessage());
            }

            @Override
            public R<String> rootPath() {
                return R.fail("获取文件根目录失败");
            }

            @Override
            public R<OssPresignResultVo> ossPresign(OssPresignForm form, String source)
            {
                return R.fail("OSS预签名失败:" + throwable.getMessage());
            }

            @Override
            public R<String> ossRootPath(String bucket, String source)
            {
                return R.fail("获取OSS根目录失败:" + throwable.getMessage());
            }

            @Override
            public R<SysFile> ossUpload(String bucket, MultipartFile file, String source)
            {
                return R.fail("OSS上传失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> ossDelete(String bucket, String fileUrl, String source)
            {
                return R.fail("OSS删除失败:" + throwable.getMessage());
            }
        };
    }
}
