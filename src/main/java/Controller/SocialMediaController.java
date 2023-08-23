package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.*;
import Service.*;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/login", this::postLoginHandler);
        app.post("/register", this::postAccountHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesFromAccountIdHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.patch("/messages/{message_id}", this::patchUpdateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        return app;
    }

    // Verifies the login, with failure leading to 401 unauthorized
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account retrievedAccount = accountService.getLogin(account.getUsername(), account.getPassword());

        if (retrievedAccount != null) {
            ctx.json(mapper.writeValueAsString(retrievedAccount));
        } else {
            ctx.status(401);
        }
    }

    // Adds the account, with constraints handled here for a 400 error
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account existingAccount = accountService.getLogin(account.getUsername(), account.getPassword());

        if (account.getUsername().isEmpty()
                || account.getPassword().length() < 4
                || existingAccount != null) {
            ctx.status(400);
            return;
        }

        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    // Gets all messages, always 200. Empty if no message.
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    // Gets all messages from an account id, always 200. Empty if no messages.
    private void getMessagesFromAccountIdHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getAllMessagesByAccountId(account_id);
        ctx.json(messages);
    }

    // Gets one message from a given message id, always 200. Empty if no message.
    private void getMessageByMessageIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(message_id);

        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }

    }

    // Creates a new message.
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Account account = accountService.getAccountById(message.getPosted_by());
        if (account == null) {
            ctx.status(400);
            return;
        }

        Message addedMessage = messageService.addMessage(message);

        if (addedMessage == null
                || addedMessage.getMessage_text().isEmpty()
                || message.getMessage_text().length() >= 255) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    // Updates a message
    private void patchUpdateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            ctx.status(400).json("");
            return;
        }

        Message updatedMessage = messageService.updateMessage(message_id, message);
        if (updatedMessage == null
                || updatedMessage.getMessage_text().length() >= 255) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    // Deletes a message
    private void deleteMessageHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageById(message_id);
        boolean success = messageService.deleteMessage(message_id);

        if (success && deletedMessage != null) {
            ctx.status(200).json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }

}