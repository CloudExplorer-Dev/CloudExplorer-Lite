package com.fit2cloud.controller.request.vm;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateServerRequest {

    private String accountId;

    private String createRequest;

    private String fromInfo;

    public CreateServerRequest copy() {
        return new CreateServerRequest().setCreateRequest(this.createRequest).setFromInfo(this.fromInfo).setAccountId(this.accountId);
    }

}
