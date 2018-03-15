package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.InputMessage;
import be.kdg.integratieproject2.Domain.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @MessageMapping("/socket")
    @SendTo("/chat/messages")
    public OutputMessage send(InputMessage msg) {
        return new OutputMessage(
                msg.getFrom(),
                msg.getText(),
                new SimpleDateFormat("HH:mm").format(new Date())
        );
    }
}
