/*
 * Copyright (C) 2008 Google, Inc.
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
package cc.duduhuo.util.pojo.derivation.compiler.util;

import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import java.util.List;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Processes {@link AutoService} annotations and generates the service provider
 * configuration files described in {@link java.util.ServiceLoader}.
 * <p>
 * Processor Options:<ul>
 * <li>debug - turns on debug statements</li>
 * </ul>
 */
@SupportedOptions({"debug", "verify"})
public class MirrorUtils {

    /**
     * Returns the contents of a {@code Class[]}-typed "value" field in a given {@code annotationMirror}.
     */
    public static ImmutableSet<DeclaredType> getValueFieldOfClasses(AnnotationMirror annotationMirror, String name) {
        return getAnnotationValue(annotationMirror, name)
                .accept(
                        new SimpleAnnotationValueVisitor8<ImmutableSet<DeclaredType>, Void>() {
                            @Override
                            public ImmutableSet<DeclaredType> visitType(TypeMirror typeMirror, Void v) {
                                // TODO(ronshapiro): class literals may not always be declared types, i.e. int.class,
                                // int[].class
                                return ImmutableSet.of(MoreTypes.asDeclared(typeMirror));
                            }

                            @Override
                            public ImmutableSet<DeclaredType> visitArray(
                                    List<? extends AnnotationValue> values, Void v) {
                                return values
                                        .stream()
                                        .flatMap(value -> value.accept(this, null).stream())
                                        .collect(toImmutableSet());
                            }
                        },
                        null);
    }
}
