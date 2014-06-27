package com.foodit.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Documentation {
	String name() default "";

	String shortDescription() default "";

	String description() default "";

	String minValue() default "";

	String maxValue() default "";

	String recommenedValue() default "";

	String help() default "";

	String[] options() default {};

	String returnClass() default "";

}
