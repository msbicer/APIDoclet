APIDoclet
=========

APIDoclet is a [Javadoc](http://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) doclet for Java WEB APIs. Aim of this project is to be able to generate user-friendly API documentation with ease.

Documentation is generated using Javadoc documentation and annotations. Thus standard Java documentation format is enough. Although there are some extra useful Javadoc tags:

### Class level
* **@module &lt;string&gt;** Entitle class with a module name

### Method level
* **@requestExample &lt;string&gt;** Provide an example request format for method
* **@responseExample &lt;string&gt;** Provide an example return format for method
* **@name &lt;string&gt;** Provide an alternative name for method

For now, only Spring MVC 3+ is supported. Supported annotations are:

### Class level
* [org.springframework.stereotype.Controller](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/stereotype/Controller.html)
* [org.springframework.web.bind.annotation.RequestMapping](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html)

### Method level
* [org.springframework.web.bind.annotation.RequestMapping](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html)

### Parameter level
* [org.springframework.web.bind.annotation.PathVariable](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/bind/annotation/PathVariable.html)
* [org.springframework.web.bind.annotation.RequestParam](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/bind/annotation/RequestParam.html)

## Output Format
Documentation requirements differ from project to project. Hence output must be configurable and flexible. APIDoclet can generate output in PDF and HTML formats. A template file must be provided for this purpose. Documentation is generated using [Freemarker](http://freemarker.org), [Flying Saucer](http://code.google.com/p/flying-saucer/) and [iText](http://itextpdf.com). Template must be in Freemarker format.

## Sample Usage
javadoc -output test.pdf -template templates/test.ftl -doclet APIDoclet ...

For more details on using doclets, please check [Doclet overview page](http://docs.oracle.com/javase/6/docs/technotes/guides/javadoc/doclet/overview.html)
