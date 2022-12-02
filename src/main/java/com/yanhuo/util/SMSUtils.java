package com.yanhuo.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送工具类
 */

@Slf4j
public class SMSUtils {

    /**
     * 发送短信
     *
     * @param signName     签名
     * @param templateCode 模板
     * @param phoneNumbers 手机号
     * @param param        参数
     */
    public static void sendMessage(String signName, String templateCode, String phoneNumbers, String param) {
        log.info("signName:{}", signName);
        log.info("templateCode:{}", templateCode);
        log.info("phoneNumbers:{}", phoneNumbers);
        log.info("param:{}", param);
//		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
//		IAcsClient client = new DefaultAcsClient(profile);
//
//		SendSmsRequest request = new SendSmsRequest();
//		request.setSysRegionId("cn-hangzhou");
//		request.setPhoneNumbers(phoneNumbers);
//		request.setSignName(signName);
//		request.setTemplateCode(templateCode);
//		request.setTemplateParam("{\"code\":\""+param+"\"}");
//		try {
//			SendSmsResponse response = client.getAcsResponse(request);
//			System.out.println("短信发送成功");
//		}catch (ClientException e) {
//			e.printStackTrace();
//		}
    }

}
