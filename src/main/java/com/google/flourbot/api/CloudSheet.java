package com.google.flourbot.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

interface CloudSheet {
  public void appendRow(List<String> values) throws IOException, GeneralSecurityException;
}