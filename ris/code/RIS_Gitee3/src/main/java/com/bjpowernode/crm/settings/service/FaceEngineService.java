package com.bjpowernode.crm.settings.service;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.toolkit.ImageInfo;
import com.bjpowernode.crm.settings.domain.User;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;

public interface FaceEngineService {
    //void init();

    //byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException;

    //List<User> compareFaceFeature(byte[] faceFeature);

    byte[] extractFaceFeature(GenericObjectPool<FaceEngine> faceEngineObjectPool, ImageInfo imageInfo);


    List<User> compareFaceFeature(GenericObjectPool<FaceEngine> faceEngineObjectPool, ExecutorService executorService, byte[] bytes);
}
