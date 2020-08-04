package com.google.flourbot.entity.trigger;

import java.util.Objects;

public final class CommandTrigger extends Trigger {

  private final TriggerType triggerType = TriggerType.COMMAND_TRIGGER;
  private final String triggerCommand;

  public CommandTrigger(String triggerCommand) {
    this.triggerCommand = triggerCommand;
  }

  public TriggerType getTiggerType() {
    return this.triggerType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandTrigger that = (CommandTrigger) o;
    return triggerType == that.triggerType &&
        triggerCommand.equals(that.triggerCommand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(triggerType, triggerCommand);
  }
}
