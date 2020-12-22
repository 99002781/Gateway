package com.example.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.consumer.Consumer;

@Configuration
public class RabbitMQDirectConfig {

	public static final String Blood_Pressure = "Blood_Pressure";
	public static final String Blood_oxygen_level = "Blood_oxygen_level";
	public static final String Heart_rate = "Heart_rate";

	@Bean
	Queue Blood_Pressure() {
		return new Queue("Blood_Pressure", false);
	}

	@Bean
	Queue Blood_oxygen_level() {
		return new Queue("Blood_oxygen_level", false);
	}

	@Bean
	Queue Heart_rate() {
		return new Queue("Heart_rate", false);
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange("direct-exchange");
	}

	@Bean
	Binding Blood_PressureBinding(Queue Blood_Pressure, DirectExchange exchange) {
		return BindingBuilder.bind(Blood_Pressure).to(exchange).with("Blood_Pressure");
	}

	@Bean
	Binding OxygenBinding(Queue Blood_oxygen_level, DirectExchange exchange) {
		return BindingBuilder.bind(Blood_oxygen_level).to(exchange).with("Blood_oxygen_level");
	}

	@Bean
	Binding HeartBinding(Queue Heart_rate, DirectExchange exchange) {
		return BindingBuilder.bind(Heart_rate).to(exchange).with("Heart_rate");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		container.setQueueNames(Blood_Pressure, Blood_oxygen_level, Heart_rate);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Consumer consumer) {
		return new MessageListenerAdapter(consumer, "receiveMessage");
	}

}
