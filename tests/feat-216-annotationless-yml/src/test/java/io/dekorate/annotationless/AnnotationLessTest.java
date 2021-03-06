/**
 * Copyright 2018 The original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dekorate.annotationless;

import io.dekorate.deps.kubernetes.api.model.HasMetadata;
import io.dekorate.deps.kubernetes.api.model.KubernetesList;
import io.dekorate.deps.kubernetes.api.model.apps.Deployment;
import io.dekorate.utils.Labels;
import io.dekorate.utils.Serialization;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationLessTest {

  @Test
  public void shouldContainComponent() {
    KubernetesList list = Serialization.unmarshalAsList(AnnotationLessTest.class.getClassLoader().getResourceAsStream("META-INF/dekorate/kubernetes.yml"));
    assertNotNull(list);
    Deployment d = findFirst(list, Deployment.class).orElseThrow(() -> new IllegalStateException());
    assertNotNull(d);
    final Map<String, String> labels = d.getMetadata().getLabels();
    assertEquals(2, d.getSpec().getReplicas().intValue());
    assertEquals("bar", labels.get("foo"));
    assertEquals("baz", labels.get("zoo"));
  }


  <T extends HasMetadata> Optional<T> findFirst(KubernetesList list, Class<T> t) {
    return (Optional<T>) list.getItems().stream()
      .filter(i -> t.isInstance(i))
      .findFirst();
  }
}
