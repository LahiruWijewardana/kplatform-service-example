package com.kolvin.kplatform.apigateway.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${queue.name}")
  private String queueName;

  @Value("${queue.exchange}")
  private String exchange;

  @Value("${queue.routingKey}")
  private String routingKey;

  @Bean
  public Queue queue() {
    return new Queue(queueName, true);
  }

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange(exchange);
  }

  @Bean
  public Binding binding(Queue rabbitQueue, DirectExchange rabbitDirectExchange) {
    return BindingBuilder.bind(rabbitQueue).to(rabbitDirectExchange).with(routingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public AmqpTemplate rabbitQueueTemplate(ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }
}
