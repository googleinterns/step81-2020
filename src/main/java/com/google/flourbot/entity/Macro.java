package com.google.flourbot.entity;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.trigger.Trigger;

import java.util.Objects;

public final class Macro {
  private final String creatorId;
  private final String macroName;
  private final Trigger macroTrigger;
  private final Action macroAction;

  public Macro(String creatorId, String macroName, Trigger macroTrigger, Action macroAction) {
    this.creatorId = creatorId;
    this.macroName = macroName;
    this.macroTrigger = macroTrigger;
    this.macroAction = macroAction;
  }

  public Action getAction() {
    return macroAction;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public String getMacroName() {
    return macroName;
  }

  @Override
  public String toString() {
    return "Macro{" +
        "creatorId='" + creatorId + '\'' +
        ", macroName='" + macroName + '\'' +
        ", macroTrigger=" + macroTrigger +
        ", macroAction=" + macroAction +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Macro macro = (Macro) o;
    return creatorId.equals(macro.creatorId) &&
        macroName.equals(macro.macroName) &&
        macroTrigger.equals(macro.macroTrigger) &&
        macroAction.equals(macro.macroAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creatorId, macroName, macroTrigger, macroAction);
  }
}
