package com.linmoblog.server.Service;

import com.linmoblog.server.Entity.Image;
import com.linmoblog.server.Entity.Result;
import com.linmoblog.server.Mapper.ImageMapper;
import com.linmoblog.server.enums.ResultCode;
import com.linmoblog.server.enums.StoreTypeEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 *  增加阿里云存储，在 application 中或在 sys_config 中增加配置
 */
@Service
public class ImageService {
    @Resource
    private ImageMapper imageMapper;
    @Value("store.type")
    private String storyType;
    private ResourceService resourceService;
    @PostConstruct

    @Transactional
    public Result<String> upload(MultipartFile  file) {
        //上传文件
        String fileUrl = resourceService.upload(file);
        //保存上传文件的地址到数据库
        imageMapper.upload(fileUrl);
        return new Result<>(ResultCode.SUCCESS_UPLOAD, fileUrl);
    }
    @Transactional
    public void deleteFile(String imageUrl) {
        //1.删除文件
        resourceService.delete(Collections.singletonList(imageUrl));
        //2. 删除数据库中的文件
        imageMapper.delete(imageUrl);
    }

    public Result<List<Image>> getImages() {
        List<Image> result = imageMapper.getImages();
        return new Result<List<Image>>(ResultCode.SUCCESS,result);
    }
}
