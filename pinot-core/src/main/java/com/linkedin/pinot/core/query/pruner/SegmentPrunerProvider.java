/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.query.pruner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;


/**
 * A static SegmentPrunerProvider will give SegmentPruner instance based on prunerClassName and configuration.
 *
 *
 */
public class SegmentPrunerProvider {

  private static Map<String, Class<? extends SegmentPruner>> keyToFunction =
      new ConcurrentHashMap<String, Class<? extends SegmentPruner>>();

  static {
    keyToFunction.put("timesegmentpruner", TimeSegmentPruner.class);
    keyToFunction.put("dataschemasegmentpruner", DataSchemaSegmentPruner.class);
  }

  public static SegmentPruner getSegmentPruner(String prunerClassName, Configuration segmentPrunerConfig) {
    try {
      Class<? extends SegmentPruner> cls = keyToFunction.get(prunerClassName.toLowerCase());
      if (cls != null) {
        SegmentPruner segmentPruner = (SegmentPruner) cls.newInstance();
        segmentPruner.init(segmentPrunerConfig);
        return segmentPruner;
      }
    } catch (Exception ex) {
      throw new RuntimeException("Not support SegmentPruner type with - " + prunerClassName, ex);
    }
    throw new UnsupportedOperationException("No SegmentPruner type with - " + prunerClassName);
  }
}
