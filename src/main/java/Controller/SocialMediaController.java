package Controller;
import DAO.*;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO());
        this.messageService = new MessageService(new MessageDAO());
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);

        return app;
    }

    private void registerAccount(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account registeredAccount = accountService.addNewAccount(account, account.username, account.password);

            if (registeredAccount != null) {
                ctx.json(registeredAccount);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
        }
    }

    private void loginAccount(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
            if (loggedInAccount != null) {
                ctx.json(loggedInAccount);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.UNAUTHORIZED).result(e.getMessage());
        }
    }

    private void createMessage(Context ctx) {
        try {
            Message message = ctx.bodyAsClass(Message.class);
            Message createdMessage = messageService.createMessage(message.getPosted_by(), message.getMessage_text(),
                    message.getTime_posted_epoch());
            if (createdMessage != null) {
                ctx.json(createdMessage);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
        }
    }

    private void getAllMessages(Context ctx) {
        try {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages);
            ctx.status(HttpStatus.OK);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(e.getMessage());
        }
    }

    private void getMessageById(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.OK); 
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Invalid message ID");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(e.getMessage());
        }
    }

    private void deleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
            if (deletedMessage != null) {
                ctx.json(deletedMessage);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.OK);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Invalid message ID");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(e.getMessage());
        }
    }

    private void updateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = ctx.bodyAsClass(Message.class); 
            Message updatedMessage = messageService.updateMessage(messageId, message.getMessage_text());
            if (updatedMessage != null) {
                ctx.json(updatedMessage);
                ctx.status(HttpStatus.OK);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Invalid message ID");
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUserId(accountId);
            ctx.json(messages);
            ctx.status(HttpStatus.OK);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Invalid account ID");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(e.getMessage());
        }
    }
}
