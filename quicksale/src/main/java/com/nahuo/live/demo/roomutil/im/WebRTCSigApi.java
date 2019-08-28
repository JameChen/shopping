package com.nahuo.live.demo.roomutil.im;


import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.zip.Deflater;


/**
 * Created by jame on 2019/4/13.
 */

public class WebRTCSigApi {
    private int mSdkAppid = 0;
    private PrivateKey mPrivateKey = null;
    private PublicKey mPublicKey = null;

    /**
     * 设置sdkappid
     * @param sdkappid
     */
    public void setSdkAppid(int sdkappid) {
        this.mSdkAppid = sdkappid;
    }

    /**
     * 设置私钥 如果要生成userSig和privateMapKey则需要私钥
     * @param privateKey 私钥文件内容
     */
    public void setPrivateKey(String privateKey) {
        String privateKeyPEM = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\r\n", "");
        char[] chars = privateKeyPEM.toCharArray();
        byte[] encodedKey = Base64.decode(chars);//Base64.getDecoder().decode(privateKeyPEM.getBytes());

        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            this.mPrivateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置公钥 如果要验证userSig和privateMapKey则需要公钥
     * @param publicKey 公钥文件内容
     */
    public void setPublicKey(String publicKey) {
        String publicKeyPEM = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "");
        byte[] encodedKey = Base64.decode(publicKeyPEM.toCharArray());//Base64.getDecoder().decode(publicKeyPEM.getBytes());

        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            this.mPublicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ECDSA-SHA256签名
     * @param data 需要签名的数据
     * @return 签名
     */
    public byte[] sign(byte[] data) {
        try {
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initSign(this.mPrivateKey);
            signer.update(data);
            return signer.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证ECDSA-SHA256签名
     * @param data 需要验证的数据原文
     * @param sig 需要验证的签名
     * @return true:验证成功 false:验证失败
     */
    public boolean verify(byte[] data, byte[] sig) {
        try {
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initVerify(this.mPublicKey);
            signer.update(data);
            return signer.verify(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 用于url的base64encode
     * '+' => '*', '/' => '-', '=' => '_'
     * @param data 需要编码的数据
     * @return 编码后的base64数据
     */
    private byte[] base64UrlEncode(byte[] data) {
        char[] encode1 = Base64.encode(data);
        byte[] encode =new String(encode1).getBytes();
//        byte[] encode = Base64.encode(data);//Base64.getEncoder().encode(data);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '+') {
                encode[i] = '*';
            } else if (encode[i] == '/') {
                encode[i] = '-';
            } else if (encode[i] == '=') {
                encode[i] = '_';
            }
        }
        return encode;
    }

    /**
     * 用于url的base64decode
     * '*' => '+', '-' => '/', '_' => '='
     * @param data 需要解码的数据
     * @return 解码后的数据
     */
    private byte[] base64UrlDecode(byte[] data) {
        byte[] encode = Arrays.copyOf(data, data.length);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '*') {
                encode[i] = '+';
            } else if (encode[i] == '-') {
                encode[i] = '/';
            } else if (encode[i] == '_') {
                encode[i] = '=';
            }
        }
        return encode;
    }

    /**
     * 生成userSig
     * @param userid 用户名
     * @param expire userSig有效期，出于安全考虑建议为300秒，您可以根据您的业务场景设置其他值。
     * @return 生成的userSig
     */
    public String genUserSig(String userid, int expire) {
        String time = String.valueOf(System.currentTimeMillis()/1000);
        String serialString =
                "TLS.appid_at_3rd:" + 0 + "\n" +
                        "TLS.account_type:" + 0 + "\n" +
                        "TLS.identifier:" + userid + "\n" +
                        "TLS.sdk_appid:" + this.mSdkAppid + "\n" +
                        "TLS.time:" + time + "\n" +
                        "TLS.expire_after:" + expire +"\n";

        byte[] signBytes = sign(serialString.getBytes(Charset.forName("UTF-8")));
        char[] encode = Base64.encode(signBytes);
        String sig = new String(encode);
//        String sig = Base64.encode(signBytes);//Base64.getEncoder().encodeToString(signBytes);

        String jsonString = "{"
                + "\"TLS.account_type\":\"" + 0 +"\","
                +"\"TLS.identifier\":\"" + userid +"\","
                +"\"TLS.appid_at_3rd\":\"" + 0 +"\","
                +"\"TLS.sdk_appid\":\"" + this.mSdkAppid +"\","
                +"\"TLS.expire_after\":\"" + expire +"\","
                +"\"TLS.sig\":\"" + sig +"\","
                +"\"TLS.time\":\"" + time +"\","
                +"\"TLS.version\": \"201512300000\""
                +"}";

        //compression
        Deflater compresser = new Deflater();
        compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));

        compresser.finish();
        byte [] compressBytes = new byte [512];
        int compressBytesLength = compresser.deflate(compressBytes);
        compresser.end();
        String userSig = new String(base64UrlEncode(Arrays.copyOfRange(compressBytes, 0, compressBytesLength)));

        return userSig;
    }

    /**
     * 生成privateMapKey
     * @param userid 用户名
     * @param roomid 房间号
     * @param expire privateMapKey有效期，出于安全考虑建议为300秒，您可以根据您的业务场景设置其他值。
     * @return 生成的privateMapKey
     */
    public String genPrivateMapKey(String userid, int roomid, int expire) {
        String time = String.valueOf(System.currentTimeMillis()/1000);

        //视频校验位需要用到的字段
        /*
         cVer    unsigned char/1 版本号，填0
         wAccountLen unsigned short /2   第三方自己的帐号长度
         buffAccount wAccountLen 第三方自己的帐号字符
         dwSdkAppid  unsigned int/4  sdkappid
         dwRoomId    unsigned int/4  群组号码
         dwExpTime   unsigned int/4  过期时间 （当前时间 + 有效期（单位：秒，建议300秒））
         dwPrivilegeMap  unsigned int/4  权限位
         dwAccountType   unsigned int/4  第三方帐号类型
         */
        int accountLength = userid.length();
        int offset = 0;
        byte[] bytes = new byte[1+2+accountLength+4+4+4+4+4];

        //cVer
        bytes[offset++] = 0;

        //wAccountLen
        bytes[offset++] = (byte)((accountLength & 0xFF00) >> 8);
        bytes[offset++] = (byte)(accountLength & 0x00FF);

        //buffAccount
        for (; offset < 3 + accountLength; ++offset) {
            bytes[offset] = (byte)userid.charAt(offset - 3);
        }

        //dwSdkAppid
        bytes[offset++] = (byte)((this.mSdkAppid & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((this.mSdkAppid & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((this.mSdkAppid & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(this.mSdkAppid & 0x000000FF);

        //dwAuthId
        long nRoomId = Long.valueOf(roomid);
        bytes[offset++] = (byte)((nRoomId & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((nRoomId & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((nRoomId & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(nRoomId & 0x000000FF);

        //dwExpTime
        long expiredTime = Long.valueOf(time) + expire;
        bytes[offset++] = (byte)((expiredTime & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((expiredTime & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((expiredTime & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(expiredTime & 0x000000FF);

        //dwPrivilegeMap
        bytes[offset++] = (byte)((255 & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((255 & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((255 & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(255 & 0x000000FF);

        //dwAccountType
        bytes[offset++] = (byte)((0 & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((0 & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((0 & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(0 & 0x000000FF);


        char[] encode = Base64.encode(bytes);
        String userbuf = new String(encode);
//        String userbuf = Base64.getEncoder().encodeToString(bytes);//Base64.getEncoder().encodeToString(bytes);

        String serialString =
                "TLS.appid_at_3rd:" + 0 + "\n" +
                        "TLS.account_type:" + 0 + "\n" +
                        "TLS.identifier:" + userid + "\n" +
                        "TLS.sdk_appid:" + this.mSdkAppid + "\n" +
                        "TLS.time:" + time + "\n" +
                        "TLS.expire_after:" + expire +"\n" +
                        "TLS.userbuf:" + userbuf + "\n";

        byte[] signBytes = sign(serialString.getBytes(Charset.forName("UTF-8")));

        char[] encodes = Base64.encode(signBytes);
        String sig = new String(encodes);
//        String sig = Base64.getEncoder().encodeToString(signBytes);//Base64.getEncoder().encodeToString(signBytes);

        String jsonString = "{"
                +"\"TLS.appid_at_3rd\":\"" + 0 +"\","
                +"\"TLS.account_type\":\"" + 0 +"\","
                +"\"TLS.identifier\":\"" + userid +"\","
                +"\"TLS.sdk_appid\":\"" + this.mSdkAppid +"\","
                +"\"TLS.expire_after\":\"" + expire +"\","
                +"\"TLS.sig\":\"" + sig +"\","
                +"\"TLS.time\":\"" + time +"\","
                +"\"TLS.userbuf\":\"" + userbuf +"\","
                +"\"TLS.version\": \"201512300000\""
                +"}";

        //compression
        Deflater compresser = new Deflater();
        compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));

        compresser.finish();
        byte [] compressBytes = new byte [512];
        int compressBytesLength = compresser.deflate(compressBytes);
        compresser.end();
        String privateMapKey = new String(base64UrlEncode(Arrays.copyOfRange(compressBytes, 0, compressBytesLength)));

        return privateMapKey;
    }


  /*  public static void main(String[] args) {
        int sdkappid = 140xxxxx;   //腾讯云云通信你创建的sdkappid
        int roomid = 1234;           //音视频房间号先随便填调试roomid
        String userid = "webrtc98";  //用户名userid 先随便填调试

        File privateKeyFile = new File("private_key");
        byte[] privateKey = new byte[(int)privateKeyFile.length()];

        File publicKeyFile = new File("public_key");
        byte[] publicKey = new byte[(int)publicKeyFile.length()];

        try {
            //读取私钥的内容
            //PS:不要把私钥文件暴露到外网直接下载了哦
            FileInputStream in1 = new FileInputStream(privateKeyFile);
            in1.read(privateKey);
            in1.close();

            //读取公钥的内容
            FileInputStream in2 = new FileInputStream(publicKeyFile);
            in2.read(publicKey);
            in2.close();

        } catch (Exception e ) {
            e.printStackTrace();
        }

        WebRTCSigApi api = new WebRTCSigApi();
        api.setSdkAppid(sdkappid);
        api.setPrivateKey(new String(privateKey));
        api.setPublicKey(new String(publicKey));

        //生成userSig
        String userSig = api.genUserSig(userid, 300);

        //生成privateMapKey
        String privateMapKey = api.genPrivateMapKey(userid, roomid, 300);

        System.out.println("userSig:\n" + userSig);
        System.out.println("privateMapKey:\n" + privateMapKey);
    }*/
}
