package com.google.flourbot.entity.trigger;

import java.util.Map;

import com.google.flourbot.entity.trigger.Trigger;

public final class CommandTrigger extends Trigger {
    
    private final Trigger.TriggerType triggerType = Trigger.TriggerType.COMMAND_TRIGGER;
    private final String triggerCommand; 

    public CommandTrigger (String triggerCommand) {
        this.triggerCommand = triggerCommand;
    }

    public TriggerType getTiggerType() {
        return this.triggerType;
    }

}