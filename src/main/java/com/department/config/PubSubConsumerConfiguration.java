package com.department.config;

import com.department.controller.MessageConsumerController;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * <p>
 *  Create an inbound channel adapter
 *  An inbound channel adapter listens to messages from a Google Cloud Pub/Sub
 *  subscription and sends them to a Spring channel in an application.
 * </>
 */
@Slf4j
@Configuration
public class PubSubConsumerConfiguration {

    //Subscription

    /**
     * <p>
     *  Subscription value. Came from @see application.yml file.
     *  Using the @Value we can change the subscription id without
     *  need to compile the code again. So it's always a good approach
     * </>
     */
    @Value("${pubsub.subscription-id}")
    private String subscriptionId;

    /**
     * <p>
     *  This is the channel from where the massage came from.
     *  This one will be used by the adapter to receive the
     *  message. @see {@link PubSubInboundChannelAdapter}
     * </>
     * @return @see {@link DirectChannel}
     */
    @Bean
    public DirectChannel pubSubInputChannel() {
        return new DirectChannel();
    }

    /**
     * <p>
     *  Instantiating an inbound channel adapter requires a {@link PubSubTemplate}
     *  instance and the name of an existing subscription. PubSubTemplate is Spring’s
     *  abstraction to subscribe to Google Cloud Pub/Sub topics. he Spring Cloud GCP Pub/Sub Boot starter
     *  provides an auto-configured PubSubTemplate instance which you can simply inject as a method argument.
     * </>
     * @param inputChannel An implementation of {@link MessageChannel}
     * @param pubSubTemplate @see {@link PubSubTemplate}
     * @return @see {@link PubSubInboundChannelAdapter}
     */
    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubSubInputChannel") MessageChannel inputChannel, PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionId);
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    /**
     * <p>
     *  Attached to an inbound channel is a service activator
     *  which is used to process incoming messages.
     * </>
     * @param consumer @see {@link MessageConsumerController}
     * @return @see {@link MessageHandler}
     */
    @Bean("defaultMessageReceiver")
    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public MessageHandler messageReceiver(MessageConsumerController consumer) {
        return message -> {
            log.debug("Message arrived! Payload: " +  message.getPayload());
            BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
                    .get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
            consumer.consume(originalMessage, (String)message.getPayload());
        };
    }

}
