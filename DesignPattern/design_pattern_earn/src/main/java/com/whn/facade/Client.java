package com.whn.facade;

import com.whn.facade.service.LetterProcess;
import com.whn.facade.service.impl.LetterProcessImpl;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 16:16
 * @Description: 我开始给朋友写信了
 */
public class Client {
    public static void main(String[] args) {
        //创建一个处理信件的过程
        LetterProcess letterProcess = new LetterProcessImpl();

        String context = "Hello,It's me,do you know who I am? I'm your old lover. I'd like to....";
        String lope = "Happy Road No. 666,God Province,Heaven";
        ////开始写信
        //letterProcess.writeContext(context);
        //
        ////开始写信封
        //letterProcess.fillEnvelope(lope);
        //
        ////把信放到信封里，并封装好
        //letterProcess.letterInotoEnvelope();

        //跑到邮局把信塞到邮箱，投递
        letterProcess.sendLetter(context,lope);
    }
}
