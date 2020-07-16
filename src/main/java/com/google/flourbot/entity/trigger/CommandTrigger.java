package com.google.flourbot.entity.trigger;

import java.util.Map;

import com.google.flourbot.entity.trigger.Trigger;

public final class CommandTrigger extends Trigger {
    
    private final TriggerType triggerType = Trigger.COMMAND_TRIGGER;
    private final String triggerCommand; 

    public CommandTrigger(Map<String, Object> document) {
        this.triggerCommand = (String) document.get("command");
    }

    public TriggerType getTiggerType() {
        return this.triggerType;
    }

}