/*
 * Copyright (C) 2007 The Guava Authors
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
 *
 * Change Log:
 *  - Removed public accessors
 *  - Removed checkState() methods
 *  - Removed checkElementIndex() methods
 *  - Removed CanIgnoreReturnValue annotations
 *  - Removed most checkArgument() methods
 *
 */
package com.martensigwart.fakeload;


import javax.annotation.Nullable;

final class Preconditions {
    private Preconditions() {}

    static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each {@code %s} placeholder in the template with an
     *     argument. These are matched by position - the first {@code %s} gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to strings using {@link String#valueOf(Object)}.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(
            T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */

    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj,
            @Nullable String errorMessageTemplate,
            @Nullable Object p1,
            @Nullable Object p2,
            @Nullable Object p3) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3));
        }
        return obj;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
     */
    static <T> T checkNotNull(
            T obj,
            @Nullable String errorMessageTemplate,
            @Nullable Object p1,
            @Nullable Object p2,
            @Nullable Object p3,
            @Nullable Object p4) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
        return obj;
    }


    /**
     * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
     * position: the first {@code %s} gets {@code args[0]}, etc. If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the formatted message in
     * square braces.
     *
     * @param template a string containing 0 or more {@code %s} placeholders. null is treated as
     *     "null".
     * @param args the arguments to be substituted into the message template. Arguments are converted
     *     to strings using {@link String#valueOf(Object)}. Arguments can be null.
     */
    // Note that this is somewhat-improperly used from Verify.java as well.
    static String format(@Nullable String template, @Nullable Object... args) {
        template = String.valueOf(template); // null -> "null"

        args = args == null ? new Object[]{"(Object[])null"} : args;

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
