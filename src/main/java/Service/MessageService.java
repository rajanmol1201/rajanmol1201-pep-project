package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message createMessage(int posted_by, String message_text, long time_posted_epoch) {
        if (message_text == null || message_text.length() > 255 || message_text.isBlank() ) {
            return null; 
        }

        Message newMessage = new Message(posted_by, message_text, time_posted_epoch);
        return messageDAO.createMessage(newMessage);
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }
    
    public Message updateMessage(int message_id, String newMessageText) {
        if (newMessageText == null || newMessageText.length() > 255 || newMessageText.isBlank()) {
            return null;
        }

        Message message = messageDAO.getMessageById(message_id);
        if (message != null) {
            message.setMessage_text(newMessageText);
            messageDAO.updateMessage(message_id, newMessageText);
        }
        return message;
    }

    public Message deleteMessage(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        if (message != null) {
            messageDAO.deleteMessage(message_id);
        }
        return message;
    }


    public List<Message> getMessagesByUserId(int account_id) {
        return messageDAO.getMessagesByUserId(account_id);
    }
}
