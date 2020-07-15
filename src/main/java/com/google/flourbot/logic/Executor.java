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

        if (document == null) {
            throw Exception("Bot is not found");      
        } else {
            Macro macro = new Macro(document);
        }

        String actionType = macro.getAction().getSheetAction();
        switch (actionType) {
            case ("Sheet Action"):
                //Document writer stuff

                break;
            default: 
                throw Exception("Unknown action type");
        }

        return "Sucessfully executed";        
    }

} 