package com.foodit.documentation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.scannotation.ClasspathUrlFinder;

import com.foodit.annotations.Documentation;

public class RestDocumentor {

	private static final String DELIM = "\n";

	public static final String BASE_PATH = "com.foodit.server.services";

	public static final String BASE_DOC_PATH = "documentation/main/restapi";

	public static void main(String[] args) throws Exception {

		String[] packages = new String[] { "restaurant", "menu", "order" };
		//String[] packages = new String[] { "menu"};

		for (String packageName : packages) {
			RestDocumentor.documentRestCalls(packageName);
		}

	}

	private static void documentRestCalls(String packageName) throws Exception {

		List<FileItem> listMethods = new LinkedList<FileItem>();

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(BASE_PATH + "." + packageName)))
				.setUrls(ClasspathUrlFinder.findClassPaths()).setScanners(new TypeAnnotationsScanner()));

		Set<Class<?>> results = reflections.getTypesAnnotatedWith(Path.class);

		for (@SuppressWarnings("rawtypes")
		Class clazz : results) {

			System.out.println("Class: " + clazz.getName());

			Path apiPath = (Path) clazz.getAnnotation(Path.class);
			Documentation classDocumentation = (Documentation) clazz.getAnnotation(Documentation.class);

			writeConcept(packageName, classDocumentation.name(), classDocumentation.shortDescription(), classDocumentation.description());

			Method[] methods = clazz.getDeclaredMethods();

			for (Method method : methods) {
				int x = 0;
				String javaClass = clazz.getName();
				String javaMethod = method.getName();

				String reference = "";
				String title = method.getName();
				String url = "";
				String description = "";
				String httpMethod = "";
				String returnClass = "";
				String responseCodeBlock = "";
				String requestCodeBlock = "";

				String[][] inputParameters = null;
				String[][] returnParameters = null;
				String[] exceptions;

				StringBuilder builder = new StringBuilder();
				Path subPath = method.getAnnotation(Path.class);
				POST post = method.getAnnotation(POST.class);
				GET get = method.getAnnotation(GET.class);
				Produces produces = method.getAnnotation(Produces.class);
				Consumes consumes = method.getAnnotation(Consumes.class);

				if (subPath == null) {
					continue;
				}

				Documentation methodDocumentation = (Documentation) method.getAnnotation(Documentation.class);

				if (methodDocumentation != null) {
					description = methodDocumentation.description();
				}

				if (subPath != null) {
					builder.append(" Path: " + "api" + apiPath.value() + subPath.value());

					reference = apiPath.value().replace("/", "") + "_" + method.getName();
					url = "api" + apiPath.value() + subPath.value();
				}

				if (get != null) {
					builder.append(" Method: get");
					httpMethod = "get";
				}
				if (post != null) {
					builder.append(" Method Post");
					httpMethod = "post";
				}

				if (consumes != null) {
					StringBuilder bld = new StringBuilder();
					for (String value : consumes.value()) {
						bld.append(value + " ");
					}
					builder.append(" Consumes: " + bld);
				}

				if (produces != null) {
					StringBuilder bld = new StringBuilder();
					for (String value : produces.value()) {
						bld.append(value + " ");
					}
					builder.append(" Produces: " + bld);
				}

				// Exceptions
				Class exceptionsTypes[] = method.getExceptionTypes();
				exceptions = new String[exceptionsTypes.length];

				StringBuilder bld = new StringBuilder();
				bld.append(" Exceptions: ");
				x = 0;
				for (Class exception : exceptionsTypes) {
					bld.append(" " + exception.getSimpleName());
					exceptions[x] = exception.getSimpleName();
					x++;
				}
				builder.append(bld);

				if ("getAllProjects".equals(method.getName())) {
					System.out.println("stop");
				}
				// input parameters
				Class parameters[] = method.getParameterTypes();
				// FIXME - this is not right ... should be getter on the class -
				// also exclude certain classes
				bld = new StringBuilder();
				bld.append(" Parameters: ");
				Set<String> listParameters = new HashSet<String>();

				for (Class parameter : parameters) {
					bld.append(" " + parameter.getSimpleName());
					if ("String".equals(parameter.getSimpleName()) || "int".equals(parameter.getSimpleName())) {
						Annotation[][] annotations = method.getParameterAnnotations();
						for (Annotation[] ann : annotations) {
							if (ann.length == 1) {
								try {
									PathParam pathParam = (PathParam) ann[0];
									if (pathParam != null) {
										listParameters.add(pathParam.value());
									}
								} catch (Exception e) {
								}

							}
						}
					} else {
						if (parameter.getSimpleName().endsWith("DTO")) {
							inputParameters = getClassFields(parameter);
							ObjectMapper mapper = new ObjectMapper();
							try {
								Class clz = Class.forName(parameter.getName());
								requestCodeBlock = mapper.writeValueAsString(clz.newInstance());

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}

				if (listParameters.size() > 0) {
					inputParameters = new String[listParameters.size()][3];
					x = 0;
					for (String value : listParameters) {
						inputParameters[x][0] = value;
						inputParameters[x][1] = "String";
						if ("modelId".equals(value)) {
							inputParameters[x][2] = "Id of the Model";
						} else if ("projectId".equals(value)) {
							inputParameters[x][2] = "Id of the Project";
						} else if ("file".equals(value)) {
							inputParameters[x][2] = "Filename";
						} else {
							inputParameters[x][2] = value;
						}
						x++;
					}
				}
				builder.append(bld);

				// return class
				Class returnClazz = method.getReturnType();
				builder.append(" Return: " + returnClazz.getSimpleName());
				returnClass = returnClazz.getSimpleName();

				if ("List".equals(returnClass)) {
					if (methodDocumentation != null && methodDocumentation.returnClass() != null
							&& !methodDocumentation.returnClass().isEmpty()) {
						Class clz = Class.forName(methodDocumentation.returnClass());
						returnParameters = getClassFields(clz);
						returnClass = "List of " + clz.getSimpleName() + "";

						ObjectMapper mapper = new ObjectMapper();
						try {
							responseCodeBlock = mapper.writeValueAsString(clz.newInstance());

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} else if (returnClass.endsWith("DTO")) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						Class clz = Class.forName(returnClazz.getName());
						responseCodeBlock = mapper.writeValueAsString(clz.newInstance());

					} catch (Exception e) {
						e.printStackTrace();
					}
					returnParameters = getClassFields(returnClazz);

				} else {
					returnParameters = getClassFields(returnClazz);
				}

				System.out.println("\tMethod: " + method.getName() + " " + builder.toString());

				responseCodeBlock = responseCodeBlock.replace(",", ",\n");
				requestCodeBlock = requestCodeBlock.replace(",", ",\n");

				StringBuilder writeBuilder = createReference(reference, title, url, description, httpMethod, returnClass, requestCodeBlock,
						responseCodeBlock, javaClass, javaMethod, inputParameters, returnParameters, exceptions);

				FileUtils.write(new File(BASE_DOC_PATH + "/" + packageName + "/references/" + apiPath.value().replace("/", "") + "/r_"
						+ method.getName() + ".dita"), writeBuilder.toString());

				listMethods.add(new FileItem(apiPath.value().replace("/", ""), method.getName()));

			}

			Collections.sort(listMethods);

			StringBuilder indexBuilder = createIndex(listMethods);
			FileUtils.write(new File(BASE_DOC_PATH + "/" + packageName + "/r_" + packageName + ".dita"), indexBuilder.toString());

			StringBuilder mapBuilder = createMap(packageName, classDocumentation.shortDescription(), clazz.getSimpleName(), listMethods);

			FileUtils.write(new File(BASE_DOC_PATH + "/" + packageName + "/" + packageName + ".ditamap"), mapBuilder.toString());

		}

	}

	private static void writeConcept(String packageName, String title, String shortDescription, String description) throws Exception {
		/**
		 * check if a file exists - if it does
		 */

		StringBuilder writeBuilder = createConcept(packageName, title, shortDescription, description);

		FileUtils.write(new File(BASE_DOC_PATH + "/" + packageName + "/concepts/c_" + packageName + ".dita"), writeBuilder.toString());

	}

	private static StringBuilder createConcept(String packageName, String title, String shortDesc, String description) {
		StringBuilder builder = new StringBuilder();

		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + DELIM);
		builder.append("<!DOCTYPE concept PUBLIC \"-//OASIS//DTD DITA Concept//EN\" \"concept.dtd\">" + DELIM);
		builder.append("<concept id=\"concept_" + packageName + "\">" + DELIM);
		builder.append("	<title>" + title + "</title>" + DELIM);
		builder.append("	<shortdesc>" + shortDesc + "</shortdesc>" + DELIM);
		builder.append("	<conbody>" + DELIM);
		builder.append("		<section>" + DELIM);
		builder.append("			<p>" + description + "</p>" + DELIM);
		builder.append("		</section>" + DELIM);
		builder.append("	</conbody>" + DELIM);
		builder.append("</concept>" + DELIM);

		return builder;
	}

	private static String[][] getClassFields(Class clazz) {

		System.out.println(clazz.getName());

		Field[] fields = clazz.getDeclaredFields();

		int x = 0;
		for (Field field : fields) {
			Documentation documentation = field.getAnnotation(Documentation.class);
			// if (documentation != null) {
			// x++;
			// }
			x++;
		}

		if (x == 0) {
			return null;
		}
		String fieldArray[][] = new String[x][3];

		x = 0;
		for (Field field : fields) {
			fieldArray[x][0] = field.getName();
			fieldArray[x][1] = field.getType().getSimpleName();

			Documentation documentation = field.getAnnotation(Documentation.class);
			if (documentation != null) {
				fieldArray[x][2] = documentation.description();
				x++;
			}
		}

		return fieldArray;
	}

	private static StringBuilder createReference(String reference, String title, String url, String description, String method,
			String returnClass, String requestCodeBlock, String responseCodeBlock, String javaClass, String javaMethod,
			String[][] inputParameters, String[][] returnParameters, String[] exceptions) {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + DELIM);
		builder.append("<!DOCTYPE reference PUBLIC \"-//OASIS//DTD DITA Reference//EN\"" + DELIM);
		builder.append("                           \"reference.dtd\">" + DELIM);
		builder.append("<reference id=\"reference_" + reference + "\">" + DELIM);
		builder.append("	<title id=\"title\">" + title + "</title>" + DELIM);
		builder.append("	<refbody>" + DELIM);
		builder.append("		<section id=\"" + reference + "\">" + DELIM);
		builder.append("			<title>" + url + "</title>" + DELIM);
		builder.append("" + DELIM);
		builder.append("			<simpletable  relcolwidth=\"1* 3*\">" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Description</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p id=\"description\">" + description + "</p>" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Method</stentry>" + DELIM);
		builder.append("					<stentry>" + method.toUpperCase() + "</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>URL Structure</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p id=\"url\">" + url + "</p>" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Input Parameters" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p>" + DELIM);
		builder.append("							<simpletable  relcolwidth=\"3* 1* 2*\">" + DELIM);
		builder.append("								<sthead>" + DELIM);
		builder.append("									<stentry>Parameter</stentry>" + DELIM);
		builder.append("									<stentry>Type</stentry>" + DELIM);
		builder.append("									<stentry>Description</stentry>" + DELIM);
		builder.append("								</sthead>" + DELIM);
		builder.append("" + DELIM);

		if (inputParameters != null && inputParameters.length > 0) {
			for (int x = 0; x < inputParameters.length; x++) {
				builder.append("								<strow>" + DELIM);
				builder.append("									<stentry>" + inputParameters[x][0] + "</stentry>" + DELIM);
				builder.append("									<stentry>" + inputParameters[x][1] + "</stentry>" + DELIM);
				builder.append("									<stentry>" + inputParameters[x][2] + "</stentry>" + DELIM);
				builder.append("								</strow>" + DELIM);
			}
		} else {
			builder.append("								<strow>" + DELIM);
			builder.append("									<stentry>None</stentry>" + DELIM);
			builder.append("									<stentry></stentry>" + DELIM);
			builder.append("									<stentry></stentry>" + DELIM);
			builder.append("								</strow>" + DELIM);
		}

		builder.append("							</simpletable>" + DELIM);
		builder.append("						</p>" + DELIM);

		if (requestCodeBlock != null && !requestCodeBlock.isEmpty()) {
			builder.append("						<codeblock>" + DELIM);
			builder.append("<![CDATA[" + DELIM);
			builder.append(requestCodeBlock);
			builder.append("]]>" + DELIM);
			builder.append("						</codeblock>" + DELIM);
		}

		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Returns</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p>" + returnClass + "</p>" + DELIM);

		if (responseCodeBlock != null && !responseCodeBlock.isEmpty()) {
			builder.append("						<codeblock>" + DELIM);
			builder.append("<![CDATA[" + DELIM);
			builder.append(responseCodeBlock);
			builder.append("]]>" + DELIM);
			builder.append("						</codeblock>" + DELIM);
		}

		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Return Value Definitions" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p>" + DELIM);
		builder.append("							<simpletable>" + DELIM);
		builder.append("								<sthead>" + DELIM);
		builder.append("									<stentry>Field</stentry>" + DELIM);
		builder.append("									<stentry>Type</stentry>" + DELIM);
		builder.append("									<stentry>Description</stentry>" + DELIM);
		builder.append("								</sthead>" + DELIM);

		if (returnParameters != null && returnParameters.length > 0) {
			for (int x = 0; x < returnParameters.length; x++) {
				builder.append("								<strow>" + DELIM);
				builder.append("									<stentry>" + returnParameters[x][0] + "</stentry>" + DELIM);
				builder.append("									<stentry>" + returnParameters[x][1] + "</stentry>" + DELIM);
				builder.append("									<stentry>" + returnParameters[x][2] + "</stentry>" + DELIM);
				builder.append("								</strow>" + DELIM);
			}
		} else {
			builder.append("								<strow>" + DELIM);
			builder.append("									<stentry>None</stentry>" + DELIM);
			builder.append("									<stentry></stentry>" + DELIM);
			builder.append("									<stentry></stentry>" + DELIM);
			builder.append("								</strow>" + DELIM);
		}

		builder.append("							</simpletable>" + DELIM);
		builder.append("						</p>" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Java Class</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p >" + javaClass + "</p>" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Java Method</stentry>" + DELIM);
		builder.append("					<stentry>" + DELIM);
		builder.append("						<p >" + javaMethod + "</p>" + DELIM);
		builder.append("					</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);
		builder.append("				<strow>" + DELIM);

		builder.append("					<stentry>Exceptions</stentry>" + DELIM);
		if (exceptions != null && exceptions.length > 0) {
			for (int x = 0; x < exceptions.length; x++) {
				builder.append("					<stentry>" + exceptions[x] + "</stentry>" + DELIM);
			}
		} else {
			builder.append("					<stentry></stentry>" + DELIM);

		}

		builder.append("				</strow>" + DELIM);
		builder.append("" + DELIM);
		builder.append("			</simpletable>" + DELIM);
		builder.append("		</section>" + DELIM);
		builder.append("	</refbody>" + DELIM);
		builder.append("</reference>" + DELIM);
		builder.append("" + DELIM);
		return builder;
	}

	private static StringBuilder createIndex(List<FileItem> listMethods) {
		String DELIM = "\n";
		StringBuilder builder = new StringBuilder();

		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + DELIM);
		builder.append("<!DOCTYPE reference PUBLIC \"-//OASIS//DTD DITA Reference//EN\"" + DELIM);
		builder.append("                           \"reference.dtd\">" + DELIM);
		builder.append("<reference id=\"reference_index\">" + DELIM);
		builder.append("	<title>Index</title>" + DELIM);
		builder.append("	<refbody>" + DELIM);
		builder.append("		<section id=\"index\">" + DELIM);
		builder.append("			<title>A list of all REST API</title>" + DELIM);
		builder.append("			<simpletable>" + DELIM);
		builder.append("				<strow>" + DELIM);
		builder.append("					<stentry>Name</stentry>" + DELIM);
		builder.append("					<stentry>URL</stentry>" + DELIM);
		builder.append("					<stentry>Description</stentry>" + DELIM);
		builder.append("				</strow>" + DELIM);

		for (FileItem fileItem : listMethods) {
			builder.append("				<strow>" + DELIM);
			builder.append("					<stentry>" + DELIM);
			builder.append("						<xref href=\"references/" + fileItem.folder + "/r_" + fileItem.method + ".dita\" />" + DELIM);
			builder.append("					</stentry>" + DELIM);
			builder.append("					<stentry>" + DELIM);
			builder.append("						<p conref=\"references/" + fileItem.folder + "/r_" + fileItem.method + ".dita#url\" />" + DELIM);
			builder.append("					</stentry>" + DELIM);
			builder.append("					<stentry>" + DELIM);
			builder.append("						<p conref=\"references/" + fileItem.folder + "/r_" + fileItem.method + ".dita#description\"></p>" + DELIM);
			builder.append("					</stentry>" + DELIM);
			builder.append("				</strow>" + DELIM);

		}

		builder.append("" + DELIM);
		builder.append("" + DELIM);
		builder.append("			</simpletable>" + DELIM);
		builder.append("		</section>" + DELIM);
		builder.append("	</refbody>" + DELIM);
		builder.append("</reference>" + DELIM);
		builder.append("" + DELIM);

		return builder;
	}

	private static StringBuilder createMap(String packageName, String shortDescription, String conceptName, List<FileItem> listMethods) {
		String DELIM = "\n";
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + DELIM);
		builder.append("<!DOCTYPE map PUBLIC \"-//OASIS//DTD DITA Map//EN\" \"map.dtd\">" + DELIM);
		builder.append("<map product=\"" + packageName + "\">" + DELIM);
		builder.append("	<title>" + shortDescription + "</title>" + DELIM);
		builder.append("	<topicref collection-type=\"family\" navtitle=\"Rest API\"" + DELIM);
		builder.append("		href=\"concepts/c_" + packageName + ".dita\">" + DELIM);
		builder.append("	<topicref href=\"r_" + packageName + ".dita\" />" + DELIM);
		builder.append("" + DELIM);

		String oldFolder = null;
		for (FileItem fileItem : listMethods) {

			if (!fileItem.folder.equals(oldFolder)) {
				if (oldFolder != null) {
					builder.append("		</topicref>" + DELIM);
				}

				oldFolder = fileItem.folder;

				// builder.append("		<topicref collection-type=\"family\" navtitle=\""
				// + fileItem.folder + "\">" + DELIM);
				builder.append("		<topicref collection-type=\"family\" navtitle=\"Rest Calls\">" + DELIM);
				builder.append("" + DELIM);

			}

			builder.append("			<topicref href=\"references/" + fileItem.folder + "/r_" + fileItem.method + ".dita\" />" + DELIM);

		}
		builder.append("		</topicref>" + DELIM);

		builder.append("	</topicref>" + DELIM);
		builder.append("</map>" + DELIM);
		builder.append("" + DELIM);
		return builder;
	}

}
