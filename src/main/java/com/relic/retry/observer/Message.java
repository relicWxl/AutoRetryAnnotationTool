package com.relic.retry.observer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息体.
 *
 * @author wxl
 * @since v3.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String lockOwnerId;

}
