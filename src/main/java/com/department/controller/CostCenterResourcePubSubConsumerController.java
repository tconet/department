package com.department.controller;

import com.department.dto.costcenterresource.CostcenterResourceMessageDTO;
import com.department.exceptions.BusinessException;
import com.department.service.CostcenterResourceService;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  This is a special Controller, but this time, not for serve HTTP Rest request,
 *  instead, we're receiving messages from a Pub/Sub channel from GCP, specifically
 *  for messages that is related to {@link com.department.entity.CostcenterResource}
 * </>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CostCenterResourcePubSubConsumerController {

    private final CostcenterResourceService service;
    private final MessageSource messageSource;
    @Value("${pubsub.costcenter-resource-subscription-id}")
    private String subscriptionId;

    /**
     * <p>
     *  This is a special Message consumer, specifically to receive information
     *  about {@link com.department.entity.CostcenterResource}. After receive and convert
     *  the message, we'll call the appropriated service to save all new data.
     *  The entity that represents the message above is @see {@link CostcenterResourceMessageDTO}
     *
     * @param message @see {@link BasicAcknowledgeablePubsubMessage}
     * @param value Must be a {@link String} representation of {@link CostcenterResourceMessageDTO}
     */
    public void consume(BasicAcknowledgeablePubsubMessage message, String value) {
        Gson gson = new Gson();
        try {
            // Convert from Json String to desired entity.
            CostcenterResourceMessageDTO entity = gson.fromJson(value, CostcenterResourceMessageDTO.class);
            // Call the service...
            service.createOrUpdate(entity.toBusiness());

            // Just in debug mode, log the message payload.
            log.debug("Message received from PubSub : {}", entity);

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

