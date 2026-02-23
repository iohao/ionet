/*
 * ionet
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.net.framework.core.kit;

import com.iohao.net.framework.core.ParameterValidator;
import com.iohao.net.common.kit.CollKit;
import jakarta.validation.*;

import lombok.Setter;
import lombok.experimental.UtilityClass;


/**
 * Jakarta Bean Validation utilities for validating action method parameters.
 *
 * @author 渔民小镇
 * @date 2022-01-16
 */
@UtilityClass
public class ValidatorKit {
    @Setter
    private ParameterValidator validator = new JakartaValidator();

    /**
     * Validate the given object against the specified validation groups.
     *
     * @param data   the object to validate
     * @param groups the validation groups to apply
     * @return the first violation message, or {@code null} if valid
     */
    public String validate(Object data, Class<?>... groups) {
        return validator.validate(data, groups);
    }

    /**
     * Whether the parameter type needs validation
     *
     * @param paramClazz Parameter type
     * @return true if this is a parameter that needs validation
     */
    public boolean isValidator(Class<?> paramClazz) {
        return validator.isValidator(paramClazz);
    }

    private final class JakartaValidator implements ParameterValidator {
        private final Validator validator;

        public JakartaValidator() {
            try (var factory = Validation.buildDefaultValidatorFactory()) {
                validator = factory.getValidator();
            }
        }

        @Override
        public String validate(Object data, Class<?>... groups) {
            var violationSet = validator.validate(data, groups);
            if (CollKit.isEmpty(violationSet)) {
                return null;
            }

            if (!violationSet.isEmpty()) {
                var violation = violationSet.iterator().next();
                String propertyName = violation.getPropertyPath().toString();
                return propertyName + " " + violation.getMessage();
            }

            return null;
        }

        @Override
        public boolean isValidator(Class<?> paramClazz) {
            var beanDescriptor = validator.getConstraintsForClass(paramClazz);
            var descriptorSet = beanDescriptor.getConstrainedProperties();
            return !descriptorSet.isEmpty();
        }
    }
}
