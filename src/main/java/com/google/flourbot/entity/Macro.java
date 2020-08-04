package com.google.flourbot.entity;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.trigger.Trigger;

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
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }

    if (!(object instanceof Macro)) {
      return false;
    }

    Macro macro = (Macro) object;

    // Compare the data members and return accordingly
    return macro.creatorId == creatorId &&
        macro.macroName == macroName &&
        macro.macroTrigger == macroTrigger &&
        macro.macroAction == macroAction;
  }
}
