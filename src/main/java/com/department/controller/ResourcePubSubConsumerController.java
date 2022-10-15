package com.department.controller;

import com.department.entity.Resource;
import com.department.exceptions.BusinessException;
import com.department.service.ResourceService;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourcePubSubConsumerController {

    private final ResourceService service;
    private final MessageSource messageSource;

    @Value("${pubsub.resource-subscription-id}")
    private String subscriptionId;

    /**
     * <p>
     *  This is a special Message consumer, specifically to receive information
     *  about {@link com.department.entity.Resource}. After receive and convert
     *  the message, we'll call the appropriated service to save all new data.
     * </>
     * @param message @see {@link BasicAcknowledgeablePubsubMessage}
     * @param value Must be a {@link String} representation of {@link Resource}
     */
    public void consume(BasicAcknowledgeablePubsubMessage message, String value) {
        Gson gson = new Gson();
        try {
            // Convert from Json String to desired entity.
            Resource entity = gson.fromJson(value, Resource.class);
            // Call the service...
            service.create(entity);

            // Just in debug mode, log the message payload.
            log.info("Message received from PubSub : {}", entity);

        } catch (JsonSyntaxException err) {
            log.info("Error while reading from PubSub subscription = {} : error message => {}", subscriptionId, err.getMessage());
        } catch (BusinessException err) {
            String msg = err.translate(messageSource);
            log.info("Error while reading from PubSub subscription = {} : error message => {}", subscriptionId, msg);
        } finally {
            // No matter what, always remove the message from the pub/sub
            message.ack();
        }
    }
}
