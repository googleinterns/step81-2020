import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;
import com.google.flourbot.entity.trigger.Trigger;
import com.google.flourbot.execution.MacroExecutionModuleImplementation;
import com.google.flourbot.execution.ChatResponse;
import com.google.flourbot.entity.EntityModule;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.action.sheet.SheetAppendRowAction;
import com.google.flourbot.entity.action.sheet.SheetEntryType;
import com.google.flourbot.entity.trigger.CommandTrigger;
import com.google.flourbot.api.CloudDocClient;
import com.google.flourbot.api.CloudSheet;
import com.google.flourbot.api.DriveCloudSheet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class MacroExecutionModuleImplementationTest {
    private static final String MACRO_NAME = "TestBot";
    private static final String BOT_NAME = "@MacroBot";
    private static final String ROOM1 = "spaces/AAAAAAAAAAA";
    private static final String THREAD1 = "spaces/AAAAAAAAAAA/threads/BBBBBBBBBBB";
    private static final String USER_EMAIL1 = "testemail1@google.com";
    private static final String USER_EMAIL2 = "testemail2@google.com";
    private static final String MESSAGE_WITH_MACRO_NAME = String.format("@MacroBot %s this is my message", MACRO_NAME);
    private static final String MESSAGE_WITHOUT_MACRO_NAME = "@MacroBot this is my message";
    private static final String MESSAGE_CONTENT_ONLY = "this is my message";
    private static final String EMPTY_MESSAGE_RESPONSE = "You must type a message when you message me. Please type \"@MacroBot /help\" for more instructions.";
    private static final String HELP_MESSAGE_REPLY = "This is your help message";
    private static final String HELP_MESSAGE = "@MacroBot /help";
    private static final String NO_ACCESS_MESSAGE = String.format("You do not own/have access to %s.", MACRO_NAME);
    private static final String SHARE_MESSAGE_REPLY = String.format("The %s macro belonging to %s has been shared to this room. All users in this room can use this macro.", MACRO_NAME, USER_EMAIL1);
    private static final String SHARE_MESSAGE = String.format("@MacroBot /share %s", MACRO_NAME);
    private static final String SHEET_URL = "d/sheetId";
    private static final String SHEET_ID = "sheetId";

    private static SheetEntryType[] columnHeaders = {SheetEntryType.TIME, SheetEntryType.EMAIL, SheetEntryType.CONTENT};
    private static final Macro DEFAULT_MACRO = new Macro(USER_EMAIL1, MACRO_NAME, new CommandTrigger("Command Trigger"), new SheetAppendRowAction(SHEET_URL, columnHeaders)); 

    private static MacroExecutionModuleImplementation execution;
    private static EntityModule mockEntityModule;
    private static CloudDocClient mockCloudDocClient;
    private static CloudSheet mockCloudSheet;

    @BeforeClass
    public static void setUpOnce() {
        mockEntityModule = Mockito.mock(EntityModule.class);
        mockCloudDocClient = Mockito.mock(CloudDocClient.class);
        mockCloudSheet = Mockito.mock(CloudSheet.class);   
        execution  = MacroExecutionModuleImplementation.initializeServer(mockEntityModule, mockCloudDocClient);
    }

    @Before
    public void setUpForEachTest() {
        execution.getThreadMacroMap().clear();
        execution.getRoomToMacro().clear();
    }
    
    // Remove a macro from the roomToMacro Map.
    @Test
    public void testRemoveMacro() {
        shareDefaultMacroToRoom(ROOM1);

        execution.removeMacro(ROOM1);

        Map<String, Map<String, String>> expected = new HashMap<String, Map<String, String>>();
        Assert.assertEquals(expected, execution.getRoomToMacro());
    }

    // Get macro name from the message when the macro has not yet been used in the thread.
    @Test
    public void getMacroNameFromMessage() {
        String actual = execution.getMacroName(MESSAGE_WITH_MACRO_NAME, THREAD1);
        Assert.assertEquals(MACRO_NAME, actual);
    }

    // Get macro name from the message when the macro has already been used in the thread (ie macro name is not stated explicitly in the message).
    @Test
    public void getMacroNameFromThread() {
        addToThreadMacroMap(THREAD1, MACRO_NAME);
        String actual = execution.getMacroName(MESSAGE_WITHOUT_MACRO_NAME, THREAD1);
        Assert.assertEquals(MACRO_NAME, actual);
    }

    // Test the help message.
    @Test
    public void testHelpMessage() throws IOException, GeneralSecurityException {
        ChatResponse chatResponse = execution.getReplyText(HELP_MESSAGE, THREAD1, ROOM1, USER_EMAIL1, HELP_MESSAGE_REPLY);
        String actual = chatResponse.getReplyText();
        Assert.assertEquals(HELP_MESSAGE_REPLY, actual);
    }

    // Test an empty message.
    @Test
    public void testEmptyMessage() throws IOException, GeneralSecurityException {
        ChatResponse chatResponse = execution.getReplyText(BOT_NAME, THREAD1, ROOM1, USER_EMAIL1, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();
        Assert.assertEquals(EMPTY_MESSAGE_RESPONSE, actual);
    }

    // TODO: test with other action types.
    // Test using a macro for the first time when you are the creator. Testing with SheetAppendRow as action type. 
    @Test
    public void useYourMacroInNewRoom() throws IOException, GeneralSecurityException {
        setUpMocks();
        String expectedText = createDefaultChatResponse(USER_EMAIL1);

        ChatResponse chatResponse = execution.getReplyText(MESSAGE_WITH_MACRO_NAME, THREAD1, ROOM1, USER_EMAIL1, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(expectedText, actual);
    }

    // Test using a macro for the first time when you are not the creator and it hasn't been shared.
    @Test
    public void doNotHaveAccess() throws IOException, GeneralSecurityException {
        
        setUpMocks();

        ChatResponse chatResponse = execution.getReplyText(MESSAGE_WITH_MACRO_NAME, THREAD1, ROOM1, USER_EMAIL2, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(NO_ACCESS_MESSAGE, actual);
    }

    // Share DEFAULT_MACRO to the room and then let another user use it.
    @Test
    public void useASharedMacro() throws IOException, GeneralSecurityException {
        setUpMocks();
        String expectedText = createDefaultChatResponse(USER_EMAIL2);

        shareDefaultMacroToRoom(ROOM1);

        ChatResponse chatResponse = execution.getReplyText(MESSAGE_WITH_MACRO_NAME, THREAD1, ROOM1, USER_EMAIL2, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(expectedText, actual);
    }

    // Test sending a message in thread where a macro has already been used and you are the creator.
    @Test
    public void useMacroAlreadyUsedInThreadAsTheCreator() throws IOException, GeneralSecurityException {
        addToThreadMacroMap(THREAD1, MACRO_NAME);

        setUpMocks();
        String expectedText = createDefaultChatResponse(USER_EMAIL1);

        ChatResponse chatResponse = execution.getReplyText(MESSAGE_WITHOUT_MACRO_NAME, THREAD1, ROOM1, USER_EMAIL1, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(expectedText, actual);
    }

    // Test sending a message in a thread where a macro has already been used and the macro has been shared (you are not the creator).
    @Test
    public void useSharedMacroAlreadyUsedInThread() throws IOException, GeneralSecurityException {
        addToThreadMacroMap(THREAD1, MACRO_NAME);
        shareDefaultMacroToRoom(ROOM1);

        setUpMocks();
        String expectedText = createDefaultChatResponse(USER_EMAIL2);

        ChatResponse chatResponse = execution.getReplyText(MESSAGE_WITH_MACRO_NAME, THREAD1, ROOM1, USER_EMAIL2, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(expectedText, actual);
    }

    // Test using the share message.
    @Test
    public void testShareMessage() throws IOException, GeneralSecurityException {
        setUpMocks();
        
        ChatResponse chatResponse = execution.getReplyText(SHARE_MESSAGE, THREAD1, ROOM1, USER_EMAIL1, HELP_MESSAGE);
        String actual = chatResponse.getReplyText();

        Assert.assertEquals(SHARE_MESSAGE_REPLY, actual);
    }

    private void addToThreadMacroMap(String thread, String macroName) {
        execution.getThreadMacroMap().put(thread, macroName);
    }

    private void shareDefaultMacroToRoom(String room) {
        Map<String, String> macroToCreator = new HashMap<String, String> ();
        macroToCreator.put(DEFAULT_MACRO.getMacroName(), DEFAULT_MACRO.getCreatorId());
        execution.getRoomToMacro().put(room, macroToCreator);
    }

    private String getDate(String pattern) {
        // Create timestamp based on pattern provided
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return myDateObj.format(myFormatObj);
    }

    private String createDefaultChatResponse(String userEmail) {
        List<String> values = new ArrayList<String>();
        values.add(getDate("dd-MM-yyyy HH:mm:ss"));
        values.add(userEmail);
        values.add(MESSAGE_CONTENT_ONLY);
        return (ChatResponse.createChatResponseWithList(values, DEFAULT_MACRO.getAction().getActionType(), DEFAULT_MACRO.getAction().getDocumentUrl())).getReplyText();
    }

    private void setUpMocks() throws IOException, GeneralSecurityException {
        when(mockEntityModule.getMacro(USER_EMAIL1, MACRO_NAME)).thenReturn(Optional.of(DEFAULT_MACRO));
        when(mockCloudDocClient.getCloudSheet(SHEET_ID)).thenReturn(mockCloudSheet);
        doNothing().when(mockCloudSheet).appendRow(ArgumentMatchers.anyList());
    }

}