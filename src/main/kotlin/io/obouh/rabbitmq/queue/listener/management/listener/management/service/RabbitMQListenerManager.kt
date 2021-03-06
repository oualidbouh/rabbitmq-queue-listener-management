package io.obouh.rabbitmq.queue.listener.management.listener.management.service

import io.obouh.rabbitmq.queue.listener.management.listener.management.exceptions.ListenerContainerNotFound
import io.obouh.rabbitmq.queue.listener.management.listener.management.service.dto.RabbitMQListener
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.stereotype.Service

@Service
class RabbitMQListenerManager(val rabbitListenerEndpointRegistry: RabbitListenerEndpointRegistry) {

    fun stop(id : String){
       val listenerContainer : MessageListenerContainer? = rabbitListenerEndpointRegistry.getListenerContainer(id);
        if(listenerContainer != null){
            listenerContainer.stop()
        }
        else {
            throw ListenerContainerNotFound("Listener container having id $id not found")
        }
    }

    fun start(id : String){
        val listenerContainer : MessageListenerContainer? = rabbitListenerEndpointRegistry.getListenerContainer(id)
        if(null != listenerContainer){
            listenerContainer.start()
        }
        else {
            throw ListenerContainerNotFound("Listener container having id $id not found")
        }
    }

    fun stopAll() {
        rabbitListenerEndpointRegistry.stop();
    }

    fun startAll() {
        rabbitListenerEndpointRegistry.start();
    }

    fun fetchAllListenersState():Set<RabbitMQListener>{
        val rabbitMQListeners = HashSet<RabbitMQListener>()
        rabbitListenerEndpointRegistry.listenerContainerIds.stream().forEach { listenerId ->
            val rabbitMQListener = RabbitMQListener(listenerId,rabbitListenerEndpointRegistry.getListenerContainer(listenerId).isRunning)
            rabbitMQListeners.add(rabbitMQListener)
        }
        return rabbitMQListeners;
    }
}
