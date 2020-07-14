// The Logic class of the server
public class Execution {

    // Private Firestore object
    // Private Google API object
    private String message;

    public Execution(String payload) {
        this.message = payload;
    }


    public String run() {
        
        Json = Firestore.get(Bot Name, User Name)
        if (Json) {
            switch (Json["Action"]["type"]) {
                case ("Sheet Action") {
                    String sheetURL = ...
                    String content = ...

                    GoogleAPI.append(sheetURL, content);

                    return SUCCESS MESSAGE;
                }                    
            }
        } else {
            throw BotNotFoundException()
        }
    }







} 