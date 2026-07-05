package fiadotau;

import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class MessageSteps {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @When("I send a message {string} to the queue")
    public void sendMessage(String text) {
        jmsTemplate.convertAndSend("day3-queue", text);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("the latest record in the database should have text {string}")
    public void verifyLatestDatabaseRecord(String expectedText) {
        var messages = messageRepository.findAll();

        assertFalse(messages.isEmpty(), "Database is completely empty!");

        var latestMessage = messages.stream()
                .max(Comparator.comparing(m -> m.getId())) // Сравниваем по ID
                .orElseThrow(() -> new AssertionError("Failed to find the latest message"));

        assertEquals(expectedText, latestMessage.getContent(),
                "The text of the LATEST record in the DB does not match expected!");
    }
}