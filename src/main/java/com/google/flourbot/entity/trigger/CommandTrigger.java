package com.google.flourbot.entity.trigger;

public final class CommandTrigger extends Trigger {

  private final TriggerType triggerType = TriggerType.COMMAND_TRIGGER;
  private final String triggerCommand;

  public CommandTrigger(String triggerCommand) {
    this.triggerCommand = triggerCommand;
  }

  public TriggerType getTiggerType() {
    return this.triggerType;
  }
}
