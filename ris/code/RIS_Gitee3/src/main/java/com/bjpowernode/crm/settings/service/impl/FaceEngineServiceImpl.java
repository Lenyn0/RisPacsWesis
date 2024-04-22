package com.bjpowernode.crm.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.FaceEngineService;
import com.bjpowernode.crm.utils.FaceEngineFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.google.common.collect.Lists;
//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;


public class FaceEngineServiceImpl implements FaceEngineService {
    public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

    private Integer passRate = 80;//通过率
    //private ExecutorService executorService;

    /*//ResourceBundle是解析properties文件的类
    ResourceBundle rb = ResourceBundle.getBundle("arcsoft");
    public String sdkLibPath = rb.getString("arcface-sdk.sdk-lib-path");
    public String appId = rb.getString("arcface-sdk.app-id");
    public String sdkKey = rb.getString("arcface-sdk.sdk-key");
    public Integer threadPoolSize = Integer.valueOf(rb.getString("arcface-sdk.thread-pool-size"));
    private Integer passRate = 80;//通过率
    private ExecutorService executorService;
    private GenericObjectPool<FaceEngine> faceEngineObjectPool;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        faceEngineObjectPool = new GenericObjectPool(new FaceEngineFactory(sdkLibPath, appId, sdkKey, engineConfiguration), poolConfig);//底层库算法对象池

    }*/

    public byte[] extractFaceFeature(GenericObjectPool<FaceEngine> faceEngineObjectPool, ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            int i = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            //System.out.println("length=====>"+faceInfoList.size());
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                FaceFeature faceFeature = new FaceFeature();
                //提取人脸特征
                faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);

                return faceFeature.getFeatureData();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    public List<User> compareFaceFeature(GenericObjectPool<FaceEngine> faceEngineObjectPool, ExecutorService executorService, byte[] faceFeature) {
        List<User> resultUserList = Lists.newLinkedList();//识别到的人脸列表

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);
        UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        List<User> userList = userDao.getUserList(); //从数据库中取出人脸库
        List<List<User>> userPartList = Lists.partition(userList, 1000);//分成1000一组，多线程处理
        CompletionService<List<User>> completionService = new ExecutorCompletionService(executorService);
        for (List<User> part : userPartList) {
            completionService.submit(new CompareFaceTask(faceEngineObjectPool, part, targetFaceFeature));
        }
        for (int i = 0; i < userPartList.size(); i++) {
            List<User> faceUserInfoList = null;
            try {
                faceUserInfoList = completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (CollectionUtil.isNotEmpty(userList)) {
                resultUserList.addAll(faceUserInfoList);
            }
        }

        resultUserList.sort((h1, h2) -> h2.getSimilarValue().compareTo(h1.getSimilarValue()));//从大到小排序

        return resultUserList;
    }

    private class CompareFaceTask implements Callable<List<User>> {

        private List<User> userList;
        private FaceFeature targetFaceFeature;
        private GenericObjectPool<FaceEngine> faceEngineObjectPool;


        public CompareFaceTask(GenericObjectPool<FaceEngine> faceEngineObjectPool, List<User> userList, FaceFeature targetFaceFeature) {
            this.userList = userList;
            this.targetFaceFeature = targetFaceFeature;
            this.faceEngineObjectPool = faceEngineObjectPool;
        }


        public List<User> call() throws Exception {
            FaceEngine faceEngine = null;
            List<User> resultUserList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = faceEngineObjectPool.borrowObject();
                for (User user : userList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(user.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    Integer similarValue = plusHundred(faceSimilar.getScore());//获取相似值
                    if (similarValue > passRate) {//相似值大于配置预期，加入到识别到人脸的列表

                        User passUser = new User();
                        passUser.setId(user.getId());
                        passUser.setLoginAct(user.getLoginAct());
                        passUser.setName(user.getName());
                        passUser.setLoginPwd(user.getLoginPwd());
                        passUser.setEmail(user.getEmail());
                        passUser.setExpireTime(user.getExpireTime());
                        passUser.setLockState(user.getLockState());
                        passUser.setAllowIps(user.getAllowIps());
                        passUser.setCreateTime(user.getCreateTime());
                        passUser.setCreateBy(user.getCreateBy());
                        passUser.setEditTime(user.getEditTime());
                        passUser.setEditBy(user.getEditBy());
                        passUser.setPrivileges(user.getPrivileges());
                        passUser.setPhoneNumber(user.getPhoneNumber());
                        passUser.setFaceFeature(user.getFaceFeature());
                        passUser.setSimilarValue(user.getSimilarValue());
                        resultUserList.add(passUser);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (faceEngine != null) {
                    faceEngineObjectPool.returnObject(faceEngine);
                }
            }
            return resultUserList;
        }

    }

    private Integer plusHundred(float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal(100f);
        return target.multiply(hundred).intValue();
    }
}
