package com.belonk.rmq.l02workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 持久化任务发布者。
 * <p>
 * Created by sun on 2018/1/24.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DurableNewTask {
    //~ Static fields/initializers =====================================================================================

    public static final String TASK_QUEUE_NAME = "durable_queue";
    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    /**
     * 发布5项任务，这里用消息模拟任务。
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        String[] msgs = new String[]{
                "First Message.",
                "Second Message..",
                "Third Message...",
                "Fourth Message....",
                "Fifth Message....."
        };

        Channel channel = connection.createChannel();
        // 持久化设置，true时，队列在RabbitMQ重启或者挂掉时不会丢失
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        for (String msg : msgs) {
            // 设置消息持久化参数
            AMQP.BasicProperties props = MessageProperties.PERSISTENT_TEXT_PLAIN;
            // 发布消息
            channel.basicPublish("", TASK_QUEUE_NAME, props, msg.getBytes("utf-8"));
            System.out.println("[x] Sent '" + msg + "'");
        }
        channel.close();
        connection.close();
    }
}
