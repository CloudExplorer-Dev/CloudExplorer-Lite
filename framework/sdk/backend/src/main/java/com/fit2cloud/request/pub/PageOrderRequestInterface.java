package com.fit2cloud.request.pub;

public interface PageOrderRequestInterface {

    OrderRequest getOrder();

    Integer getCurrentPage();

    Integer getPageSize();
}
