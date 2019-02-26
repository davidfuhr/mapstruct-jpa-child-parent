/**
 * Copyright 2012-2017 Gunnar Morling (http://www.gunnarmorling.de/)
 * and/or other contributors as indicated by the @authors tag. See the
 * copyright.txt file in the distribution for a full listing of all
 * contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mapstruct.jpa;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sjaak Derksen
 */
public class SourceTargetMapperTest {

    public SourceTargetMapperTest() {
    }

    /**
     * Test of toTarget method, of class SourceTargetMapper.
     */
    @Test
    public void testToTarget() {

        // prepare dto's
        ParentDto parent = new ParentDto();
        parent.setName("jim");
        ChildDto childDto1 = new ChildDto();
        childDto1.setName("jack");
        ChildDto childDto2 = new ChildDto();
        childDto2.setName("jill");
        parent.setChildren(Arrays.asList(childDto1, childDto2));

        ParentEntity parentEntity = SourceTargetMapper.MAPPER.toEntity(parent);

        //results
        assertThat(parentEntity).isNotNull();
        assertThat(parentEntity.getName()).isEqualTo("jim");
        assertThat(parentEntity.getChildren()).hasSize(2);
        assertThat(parentEntity.getChildren().get(0).getName()).isEqualTo("jack");
        assertThat(parentEntity.getChildren().get(0).getMyParent()).isEqualTo(parentEntity);
        assertThat(parentEntity.getChildren().get(1).getName()).isEqualTo("jill");
        assertThat(parentEntity.getChildren().get(1).getMyParent()).isEqualTo(parentEntity);
    }

    // this test fails
    // but documentation says at http://mapstruct.org/documentation/dev/reference/html/#updating-bean-instances
    // "Collection- or map-typed properties of the target bean to be updated will be cleared and then populated with the values from the corresponding source collection or map."
    @Test
    public void givenEntityWithChildren_thenUpdate_shouldReplaceAllChildren() {

        final ParentDto parentDto = new ParentDto();
        parentDto.setName("John");
        final ChildDto childDto1 = new ChildDto();
        childDto1.setName("Mike");
        final ChildDto childDto2 = new ChildDto();
        childDto2.setName("Lynn");
        parentDto.setChildren(Arrays.asList(childDto1, childDto2));

        final ParentEntity parentEntity = new ParentEntity();
        final ChildEntity childEntity = new ChildEntity();
        childEntity.setName("Paul");
        parentEntity.addChild(childEntity);

        SourceTargetMapper.MAPPER.updateEntity(parentDto, parentEntity);

        assertThat(parentEntity).isNotNull();
        assertThat(parentEntity.getName()).isEqualTo("John");
        assertThat(parentEntity.getChildren()).hasSize(2);
        assertThat(parentEntity.getChildren().get(0).getName()).isEqualTo("Mike");
        assertThat(parentEntity.getChildren().get(0).getMyParent()).isEqualTo(parentEntity);
        assertThat(parentEntity.getChildren().get(1).getName()).isEqualTo("Lynn");
        assertThat(parentEntity.getChildren().get(1).getMyParent()).isEqualTo(parentEntity);

    }

}
