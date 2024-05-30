package com.whn.facade.service;

import java.util.logging.Logger;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 16:10
 * @Description: 寄信的步骤
 */
public interface LetterProcess {
    Logger logger = Logger.getLogger(LetterProcess.class.getName());

    /**
     * 首先要写信的内容
     *
     * @param context
     */
    void writeContext(String context) ;

    /**
     * 其次写信封
     * @param address
     */
     void fillEnvelope(String address);


    /**
     * 把信放到信封里
     */
     void letterInotoEnvelope();

    /**
     * 然后邮递
     */
     default void sendLetter(String context,String address){
        writeContext(context);
        fillEnvelope(address);
        letterInotoEnvelope();
         logger.info("这封信发送成功");
    }

}
