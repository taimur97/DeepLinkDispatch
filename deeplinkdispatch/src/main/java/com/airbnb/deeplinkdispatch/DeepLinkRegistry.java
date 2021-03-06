/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DeepLinkRegistry {

  private final List<DeepLinkEntry> registry = new LinkedList<>();

  public DeepLinkRegistry(Loader loader) {
    if (loader != null) {
      loader.load(this);
    }
  }

  /**
   * Registers the deep link that DeepLinkActivity will handle, along with where to delegate
   *
   * @param hostPath      the combined host and path of the deep link
   * @param type          whether its a class level annotation or method level
   * @param activityClass the activity class to delegate the deep link to
   * @param method        the method used to generate the <code>Intent</code>
   */
  public void registerDeepLink(String hostPath, DeepLinkEntry.Type type, Class<?> activityClass,
      String method) {
    registry.add(new DeepLinkEntry(hostPath, type, activityClass, method));
  }

  /**
   * Search through the registry to see if there is a matching entry for the given deep link.
   *
   * @param hostPath the combined host and path of the deep link
   * @return the entry containing the information of which activity and method handles the deep link
   */
  public DeepLinkEntry parseUri(String hostPath) {
    for (DeepLinkEntry entry : registry) {
      Matcher matcher = Pattern.compile(entry.getRegex()).matcher(hostPath);
      if (matcher.matches()) {
        return entry;
      }
    }

    return null;
  }
}
