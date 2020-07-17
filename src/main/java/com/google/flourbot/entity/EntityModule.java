package com.google.flourbot.entity;

import java.util.Optional;

public interface EntityModule {
  Optional<Macro> getMacro(String userEmail, String macroName);
}
