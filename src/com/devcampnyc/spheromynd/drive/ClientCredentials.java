/*
 * Copyright (c) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.devcampnyc.spheromynd.drive;

/**
 * API key found in the <a href="https://code.google.com/apis/console/?api=tasks">Google apis
 * console</a>.
 * 
 * <p>
 * Once at the Google apis console, click on "Add project...". If you've already set up a project,
 * you may use that one instead, or create a new one by clicking on the arrow next to the project
 * name and click on "Create..." under "Other projects". Finally, click on "API Access". Look for
 * the section at the bottom called "Simple API Access".
 * </p>
 * 
 * @author Yaniv Inbar
 */
public class ClientCredentials {

  /** Value of the "API key" shown under "Simple API Access". */
  public static final String KEY = "AIzaSyBzQyiiSPX_drnu958POhMyduhJ3BgZ0xU";

  public static void errorIfNotSpecified() {
    if (KEY == null) {
      System.err.println("Please enter your API key in " + ClientCredentials.class);
    }
  }
}