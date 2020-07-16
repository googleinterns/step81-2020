package com.google.flourbot.entity.trigger;

import java.util.Map;

public abstract class Trigger {
    public enum TriggerType { COMMAND_TRIGGER }
    public abstract TriggerType getTiggerType();
}

