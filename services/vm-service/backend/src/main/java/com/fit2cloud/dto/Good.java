package com.fit2cloud.dto;

import com.fit2cloud.base.entity.CloudAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Good extends CloudAccount {

    @Serial
    private static final long serialVersionUID = 7938329873497218384L;

    private long serverCount;
    private long diskCount;
    private Object balance;
    private boolean publicCloud;

}
