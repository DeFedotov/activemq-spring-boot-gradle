package fiadotau;

import jakarta.jms.ConnectionFactory;
import jakarta.persistence.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableJms
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setUserName("admin");
        factory.setPassword("admin");
        return factory;
    }
}

@Entity
class ReceivedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime receivedAt;

    public ReceivedMessage() {}
    public ReceivedMessage(String content, LocalDateTime receivedAt) {
        this.content = content;
        this.receivedAt = receivedAt;
    }
    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
}

interface MessageRepository extends JpaRepository<ReceivedMessage, Long> {
}

@Component
class MessageProducer implements CommandLineRunner {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void run(String... args) {
        System.out.println(">>> [Producer]: Sending message into ActiveMQ...");
        jmsTemplate.convertAndSend("day3-queue", "Message sent to ActiveMQ!");
    }
}

@Component
class MessageConsumer {
    @Autowired
    private MessageRepository messageRepository;

    @JmsListener(destination = "day3-queue")
    public void receiveMessage(String text) {
        System.out.println("<<< [Consumer]: Message received: " + text);

        ReceivedMessage entity = new ReceivedMessage(text, LocalDateTime.now());
        ReceivedMessage saved = messageRepository.save(entity);

        System.out.println("=== [Database]: Saved into Postgres with ID: " + saved.getId());
    }
}