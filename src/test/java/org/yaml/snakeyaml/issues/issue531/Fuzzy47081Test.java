/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue531;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

// Stackoverflow [OSS-Fuzz - 47081]
public class Fuzzy47081Test {

  /**
   * Recursive list fails (with StackOverflowError) because it is used as a key There is no way to
   * disable recursive sequence
   */
  @Test
  public void parse47081() {
    try {
      LoaderOptions options = new LoaderOptions();
      options.setAllowRecursiveKeys(true);
      Yaml yaml = new Yaml(options);
      String strYaml = "&a\n" + "- *a\n"
      // if this line is removed, the test properly complains about the recursive keys in map ->
      // Recursive key for mapping is detected, but it is not configured to be allowed.
          + "- *a:\n"; // when the colon ir removed, the test is Ok, because the recursive list is
                       // not a key
      yaml.load(strYaml);
      fail("Should report invalid YAML");
    } catch (StackOverflowError e) {
      assertTrue(true);
    }
  }
}
