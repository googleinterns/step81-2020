import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;
import com.google.flourbot.entity.trigger.Trigger;
import com.google.flourbot.execution.MacroExecutionModuleImplementation;
import com.google.flourbot.execution.ChatResponse;
import com.google.flourbot.entity.EntityModule;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.action.sheet.SheetReadSheetAction;
import com.google.flourbot.entity.trigger.CommandTrigger;
import com.google.flourbot.api.CloudDocClient;

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
import static org.mockito.BDDMockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class MacroExecutionModuleImplementationTest {
    private static final String MACRO_NAME = "TestBot";
    private static final String ROOM1 = "spaces/AAAAAAAAAAA";
    private static final String THREAD1 = "spaces/AAAAAAAAAAA/threads/BBBBBBBBBBB";
    private static final String USER_EMAIL = "testemail@google.com";
    private static final String MESSAGE_WITH_MACRO_NAME = String.format("@MacroBot %s this is my message", MACRO_NAME);
    private static final String MESSAGE_WITHOUT_MACRO_NAME = "@MacroBot this is my message";
    private static final String EMPTY_MESSAGE_RESPONSE = "You must type a message when you message me. Please type \"@MacroBot /help\" for more instructions.";
    private static final String HELP_MESSAGE = "This is your help message";
    private static final Macro DEFAULT_MACRO = new Macro("scaulfeild@google.com", MACRO_NAME, new CommandTrigger("Command Trigger"), new SheetReadSheetAction("SheetUrl", "SheetName")); 

    private static MacroExecutionModuleImplementation execution;
    private static EntityModule mockEntityModule;
    private static CloudDocClient mockCloudDocClient;
    private static MacroExecutionModuleImplementation mockMacroExecutionModuleImplementation;

    @Before
    public void setUpForAllTests() {
        mockEntityModule = Mockito.mock(EntityModule.class);
        mockMacroExecutionModuleImplementation = Mockito.mock(MacroExecutionModuleImplementation.class);
        mockCloudDocClient = Mockito.mock(CloudDocClient.class);
        execution  = MacroExecutionModuleImplementation.initializeServer(mockEntityModule, mockCloudDocClient);
    }
    
    // Remove a macro from the roomToMacro Map
    @Test
    public void testRemoveMacro() {
        Map<String, Map<String, String>> actual = execution.getRoomToMacro();
        Map<String, String> macro = new HashMap<String, String>();
        macro.put(MACRO_NAME, "scaulfeild@google.com");
        actual.put(ROOM1, macro);

        execution.removeMacro(ROOM1);

        Map<String, Map<String, String>> expected = new HashMap<String, Map<String, String>>();
        Assert.assertEquals(expected, actual);
    }

    // Get macro name from the message when the macro has not yet been used in the thread
    @Test
    public void getMacroNameFromMessage() {
        String actual = execution.getMacroName(MESSAGE_WITH_MACRO_NAME, THREAD1);
        Assert.assertEquals(MACRO_NAME, actual);
    }

    // Get macro name from the message when the macro has already been used in the thread (ie macro name is not stated explicitly in the message)
    @Test
    public void getMacroNameFromThread() {
        addToThreadMacroMap(THREAD1, MACRO_NAME);

        String actual = execution.getMacroName(MESSAGE_WITHOUT_MACRO_NAME, THREAD1);
        Assert.assertEquals(MACRO_NAME, actual);
    }

    // Test the help message
    @Test
    public void testHelpMessage() throws IOException, GeneralSecurityException {
        ChatResponse chatResponse = execution.getReplyText("@MacroBot /help", THREAD1, ROOM1, "scaulfeild@google.com", HELP_MESSAGE);
        String actual = chatResponse.getReplyText();
        Assert.assertEquals(HELP_MESSAGE, actual);
    }

    // Test an empty message
    @Test
    public void testEmptyMessage() throws IOException, GeneralSecurityException {
        ChatResponse chatResponse = execution.getReplyText("@MacroBot", THREAD1, ROOM1, "scaulfeild@google.com", HELP_MESSAGE);
        String actual = chatResponse.getReplyText();
        Assert.assertEquals(EMPTY_MESSAGE_RESPONSE, actual);
    }

    private void addToThreadMacroMap(String thread, String macroName) {
        execution.getThreadMacroMap().put(thread, macroName);
    }

    private void addToMacroToRoom(String room) {
        Map<String, String> macroToCreator = new HashMap<String, String> ();
        macroToCreator.put(DEFAULT_MACRO.getMacroName(), DEFAULT_MACRO.getCreatorId());
        execution.getRoomToMacro().put(room, macroToCreator);
    }

}