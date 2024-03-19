package com.whn;

/**
 * @Author: WangHn
 * @Date: 2024/2/22 11:50
 * @Description: synchornized 作用域
 */
class Phone{
   public void sendEmail(){
       System.out.println(Thread.currentThread().getName()+"sendEmail");
   }
   public void sendMess(){}

    public void tel(){}
}

public class Three {
}
