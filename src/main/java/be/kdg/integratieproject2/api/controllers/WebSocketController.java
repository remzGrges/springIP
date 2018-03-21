package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.InputMessage;
import be.kdg.integratieproject2.Domain.OutputMessage;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SessionService sessionService;


    @MessageMapping("/socket/{sessionId}")
    @SendTo("/chat/messages/{sessionId}")
    public OutputMessage send(@DestinationVariable String sessionId, InputMessage msg) {
        return sessionService.addMessageToSession(sessionId, msg);
    }

    @MessageMapping("/session/{sessionId}")
    @SendTo("/sessionstate/{sessionId}")
    public boolean getSate(@DestinationVariable String sessionId)
    {
        return true;
    }
}
