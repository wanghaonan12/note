package org.whn.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: WangHn
 * @Date: 2024/2/21 14:32
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable
{
    private Integer id;

    private String cname;

    private Integer age;

    private String phone;

    private Byte sex;

    private Date birth;

}
