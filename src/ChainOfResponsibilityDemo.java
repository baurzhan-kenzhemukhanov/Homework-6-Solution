abstract class SupportHandler {
    protected SupportHandler nextHandler;

    public SupportHandler setNext(SupportHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public abstract void handle(String request);
}

class AutomatedBotHandler extends SupportHandler {
    public void handle(String request) {
        if (request.equals("reset_password")) {
            System.out.println("[AutomatedBot] Resolved issue: " + request);
        } else if (nextHandler != null) {
            nextHandler.handle(request);
        }
    }
}

class Tier1SupportHandler extends SupportHandler {
    public void handle(String request) {
        if (request.equals("payment_issue") || request.equals("subscription_cancel")) {
            System.out.println("[Tier1Support] Resolved issue: " + request);
        } else if (nextHandler != null) {
            nextHandler.handle(request);
        }
    }
}

class Tier2SupportHandler extends SupportHandler {
    public void handle(String request) {
        if (request.equals("account_suspension") || request.equals("missing_data")) {
            System.out.println("[Tier2Support] Resolved issue: " + request);
        } else {
            System.out.println("[Tier2Support] Unable to resolve '" + request + "' — escalate to manager");
        }
    }
}

public class SupportChainDemo {
    public static void main(String[] args) {
        SupportHandler bot = new AutomatedBotHandler();
        SupportHandler tier1 = new Tier1SupportHandler();
        SupportHandler tier2 = new Tier2SupportHandler();

        bot.setNext(tier1).setNext(tier2);

        String[] requests = {"reset_password", "payment_issue", "account_suspension", "weird_error"};
        for (String request : requests) {
            bot.handle(request);
        }
    }
}
