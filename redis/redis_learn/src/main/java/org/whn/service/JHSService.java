package org.whn.service;

import org.whn.po.Product;

import java.util.List;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 11:00
 * @Description: 聚划算Service
 */
public interface JHSService {

    List<Product> getProductS(int page, int size);

    List<Product> getProductSAB(int page, int size);
}
