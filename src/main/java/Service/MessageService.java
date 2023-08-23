package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    // Empty constructor
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    // Constructor with provided messageDAO
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Get all messages from a user given an account id
    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

    // Get one message given a message id
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

     // Create a new message
     public Message addMessage(Message message) {
        if (message == null){
            return null;
        }

        Message persistedMessage = messageDAO.addMessage(message);

        return persistedMessage;
    }

    // Update message given a message id
    public Message updateMessage(int message_id, Message message) {
        Message updatedMessage = messageDAO.getMessageById(message_id);

        if(updatedMessage == null){
            return null;
        } else{
            updatedMessage.setMessage_text(message.getMessage_text());
            return updatedMessage;
        }
    }

    // Delete a message given a message id
    public boolean deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

}
