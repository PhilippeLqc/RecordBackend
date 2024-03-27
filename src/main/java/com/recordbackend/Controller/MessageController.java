package com.recordbackend.Controller;

import com.recordbackend.Model.MessageDto;
import com.recordbackend.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    //send the message to the frontend when the message is received and save the message to the database
    public void sendMessage(MessageDto message){
        messageService.saveMessage(message);
    }
}
