package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.InputMessage;
import be.kdg.integratieproject2.Domain.OutputMessage;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    private SessionService sessionService;

    public WebSocketController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @MessageMapping("/socket/{sessionId}")
    @SendTo("/chat/messages/{sessionId}")
    public OutputMessage send(@DestinationVariable String sessionId, InputMessage msg) {
        return sessionService.addMessageToSession(sessionId, msg);
    }


}
