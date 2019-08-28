package com.nahuo.quicksale.upyun.api;

import java.io.File;

import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.upyun.api.utils.UpYunUtils;

public class UpYunAPI {

    /**
     * 上传图片到又拍云空间
     * 
     * @param shop 存储目录，一般固定为“shop”
     * @param shopId 店铺ID
     * @param fileName 文件名称，格式为：m+时间+.jpg
     * @param bucket 又拍云空间名，存储商品有关的图片（nahuo_img_server），非商品图片（banwo-img-server）
     * @param apiKey 又拍云表单api验证密钥，存储商品有关的图片（x0zSWZ16yenc7xViIWiolAXTvgg=），非商品图片（6+ GplJ/QkiXOQ/tRDxW0KWQE45g=）
     * @param sourceFile 本地图片路径
     * @return 又拍云空间文件路径
     * */
    public static String uploadImage(String shop, String shopId, String fileName, String bucket, String apiKey,
            String sourceFile) throws Exception {
        String serverPath;
        try {
            // 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
            // 例如 “/shop1234/tem/m1234234.jpg”
            String SAVE_KEY = "/" + shopId + File.separator + shop + File.separator + fileName;//"/u" + shopId + File.separator + shop + File.separator + fileName;
            // 取得base64编码后的policy
            String policy = UpYunUtils.makePolicy(SAVE_KEY, PublicData.getUpYunExpiration(), bucket);
            // 根据表单api签名密钥对policy进行签名
            // 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
            String signature = UpYunUtils.signature(policy + "&" + apiKey);
            // 上传文件到对应的bucket中去。
            Uploader.upload(policy, signature, bucket, sourceFile);
            serverPath = "upyun:"+bucket+":"+SAVE_KEY;
//            serverPath = Uploader.upload(policy, signature, bucket, sourceFile);
        } catch (Exception ex) {
            throw ex;
        }
        return serverPath;
    }



    /**
     * 上传图片到又拍云空间
     *
     * @param fileName
     *            文件名称，格式为：m+时间+.jpg
     * @param bucket
     *            又拍云空间名，存储商品有关的图片（nahuo_img_server），非商品图片（banwo-img-server）
     * @param apiKey
     *            又拍云表单api验证密钥，存储商品有关的图片（x0zSWZ16yenc7xViIWiolAXTvgg=），非商品图片（6+
     *            GplJ/QkiXOQ/tRDxW0KWQE45g=）
     * @param sourceFile
     *            本地图片路径
     * @return 又拍云空间文件路径
     * */
    public static String uploadImage(String fileName, String bucket, String apiKey, String sourceFile)
            throws Exception {
        String serverPath = "";
        try {
            // 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
            String SAVE_KEY = fileName;
            // 取得base64编码后的policy
            String policy = UpYunUtils.makePolicy(SAVE_KEY,
                    Const.UPYUN_EXPIRATION, bucket);
            // 根据表单api签名密钥对policy进行签名
            // 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
            String signature = UpYunUtils.signature(policy + "&" + apiKey);
            // 上传文件到对应的bucket中去。
            serverPath = Uploader.upload(policy, signature, bucket, sourceFile);
        } catch (Exception ex) {
            throw ex;
        }
        return serverPath;
    }

    /**
     * 上传图片到又拍云空间-身份验证
     * 
     * @param fileName 文件名称，格式为：m+时间+.jpg
     * @param bucket 又拍云空间名，存储商品有关的图片（nahuo_img_server），非商品图片（banwo-img-server）
     * @param apiKey 又拍云表单api验证密钥，存储商品有关的图片（x0zSWZ16yenc7xViIWiolAXTvgg=），非商品图片（6+ GplJ/QkiXOQ/tRDxW0KWQE45g=）
     * @param sourceFile 本地图片路径
     * @return 又拍云空间文件路径
     * */
    public static String uploadImage_IdentityAuth(String fileName, String bucket, String apiKey, String sourceFile)
            throws Exception {
        String serverPath = "";
        try {
            // 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
            String SAVE_KEY = "/pay/certify/" + fileName;
            // 取得base64编码后的policy
            String policy = UpYunUtils.makePolicy(SAVE_KEY, PublicData.getUpYunExpiration(), bucket);
            // 根据表单api签名密钥对policy进行签名
            // 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
            String signature = UpYunUtils.signature(policy + "&" + apiKey);
            // 上传文件到对应的bucket中去。
            serverPath = "upyun:" + bucket + ":" + Uploader.upload(policy, signature, bucket, sourceFile);
        } catch (Exception ex) {
            throw ex;
        }
        return serverPath;
    }
}
