// The Logic class of the server
public class Executor {

    private final String userEmail;
    private final DataStorage dataStorage;
    
    public Executor(String userEmail, DataStorage dataStorage) {
        this.userEmail = userEmail;
        this.dataStorage = dataStorage;
    }


    public String execute(String payload) {
        
        String macroName = payload.split(" ")[0];
        Map<String, Object> document = dataStorage.getDocument(userEmail, macroName);

        if (document != null) {

        } else {

        }
        
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