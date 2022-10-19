package com.department.controller;

import com.department.dto.costcenterresource.CostcenterResourceMessageDTO;
import com.department.exceptions.BusinessException;
import com.department.service.CostcenterResourceService;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
public class CostCenterResourcePubSubConsumerController {

    private CostcenterResourceService service;
    private MessageSource messageSource;
    private String subscription;

    public CostCenterResourcePubSubConsumerController(
            CostcenterResourceService service,
            MessageSource messageSource,
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.costcenter-resource-subscription-id}") String subscription) {

        this.service = service;
        this.messageSource = messageSource;
        this.subscription = subscription;
        startListening(pubSubTemplate);
    }

    /**
     * <p>
     *  Subscribe to a subscription with a message handler. Asynchronously pulls messages and passes
     *  them to messageConsumer, that int this case will be this done by this controller through the
     *  consume method, that will consume all message related to
     *  {@link com.department.entity.CostcenterResource}
     *
     * @param pubSubTemplate @see {@link PubSubTemplate}
     * @return @see {@link Subscriber}
     */
    private void startListening(PubSubTemplate pubSubTemplate) {

        // Subscribe to the subscription. When the message arrives, call
        // the consumer to accomplish this task.
        pubSubTemplate.subscribe(subscription, message -> {
            String msg = message.getPubsubMessage().getData().toStringUtf8();
            log.debug("Message arrived! Payload: " +  msg);
            consume(message, msg);
        });
    }


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
            log.info("Error while reading from PubSub subscription = {} : error message => {}", subscription, err.getMessage());
        } catch (BusinessException err) {
            String msg = err.translate(messageSource);
            log.info("Error while reading from PubSub subscription = {} : error message => {}", subscription, msg);
        } finally {
            // No matter what, always remove the message from the pub/sub
            message.ack();
        }
    }


}

