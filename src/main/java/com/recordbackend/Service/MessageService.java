package com.recordbackend.Service;

import com.recordbackend.Model.Message;
import com.recordbackend.Model.MessageDto;
import com.recordbackend.Model.Project;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

public void saveMessage(MessageDto messageDto){
        Message message = Message.builder()
                .message(messageDto.getMessage())
                .project(Project.builder().id(messageDto.getProjectId()).build())
                .user(User.builder().id(messageDto.getUserId()).build())
                .build();
        messageRepository.save(message);
    }
}
