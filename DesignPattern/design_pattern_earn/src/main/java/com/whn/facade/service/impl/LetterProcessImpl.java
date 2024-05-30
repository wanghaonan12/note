package com.whn.facade.service.impl;

import com.whn.facade.service.LetterProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 16:13
 * @Description: 写信的具体实现了
 */
@Slf4j
public class LetterProcessImpl implements LetterProcess {
    /**
     * 写信
     * @param context
     */
    @Override
    public void writeContext(String context) {
        log.info("填写信的内容...." + context);
    }

    /**
     * 在信封上填写必要的信息
     * @param address
     */
    @Override
    public void fillEnvelope(String address) {
        log.info("填写收件人地址及姓名...." + address);
    }

    /**
     * 把信放到信封中，并封好
     */
    @Override
    public void letterInotoEnvelope() {
        log.info("把信放到信封中....");
    }

    @Override
    public void sendLetter(String context, String address) {
        LetterProcess.super.sendLetter(context, address);
    }

}
