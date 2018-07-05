package com.example.componentscanningfilters;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;

@Configuration
@ComponentScan(includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MyBean1.class, MyBean3.class})
},
		excludeFilters = {
//				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestAnnotation.class)
				@ComponentScan.Filter(type = FilterType.CUSTOM, classes = CustomTypeFilter.class)

		})
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Arrays.stream(beanDefinitionNames)
				.filter(n -> !n.contains("springframework"))
				.forEach(System.out::println);
	}
}


class CustomTypeFilter implements TypeFilter {
	private static final String TestAnnotation = TestAnnotation.class.getName();

	@Override
	public boolean match(final MetadataReader metadataReader, final MetadataReaderFactory metadataReaderFactory) throws IOException {
		AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
		Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
		return annotationTypes.stream().anyMatch(TestAnnotation::equals);
	}
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {
}


class MyBean1 {
}

class MyBean2 {
}

class MyBean3 {
}

@TestAnnotation
class MyBean4 extends MyBean3 {
}