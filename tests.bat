@echo off

echo Hi

set HI_AGAIN=dddd^
zzzz

set JDK_CLASSPATH=C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 2016.1\lib\idea_rt.jar;^
C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 2016.1\plugins\junit\lib\junit-rt.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\charsets.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\deploy.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\access-bridge-64.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\cldrdata.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\dnsns.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\jaccess.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\jfxrt.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\localedata.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\nashorn.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\sunec.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\sunjce_provider.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\sunmscapi.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\sunpkcs11.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\ext\zipfs.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\javaws.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\jce.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\jfr.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\jfxswt.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\jsse.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\management-agent.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\plugin.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\resources.jar;^
C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar;^
C:\Program Files\Java\jdk1.8.0_66\lib\tools.jar;


set MY_CLASSPATH1=C:\Projects\kotlin-jdk8\out\test\compiler-tests;^
C:\Projects\kotlin-jdk8\out\production\descriptors;^
C:\Projects\kotlin-jdk8\dist\builtins;^
C:\Projects\kotlin-jdk8\out\production\deserialization;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\protobuf-2.5.0.jar;^
C:\Projects\kotlin-jdk8\lib\javax.inject.jar;^
C:\Projects\kotlin-jdk8\out\production\util.runtime;^
C:\Users\Nikolay.Krasko\.IdeaIC2016\config\plugins\Kotlin\kotlinc\lib\kotlin-runtime.jar;^
C:\Projects\kotlin-jdk8\out\production\backend;^
C:\Projects\kotlin-jdk8\out\production\frontend;^
C:\Projects\kotlin-jdk8\ideaSDK\core\jna.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\jdom.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\util.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\log4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\asm-all.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\jsr166e.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\trove4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\guava-17.0.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\annotations.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\intellij-core.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\picocontainer.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\xstream-1.4.8.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\xpp3-1.1.4-min.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\core\snappy-in-java-0.3.1.jar;^
C:\Projects\kotlin-jdk8\out\production\util;^
C:\Users\Nikolay.Krasko\.IdeaIC2016\config\plugins\Kotlin\kotlinc\lib\kotlin-reflect.jar;^
C:\Projects\kotlin-jdk8\out\production\Kotlin;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\jps-model.jar;^
C:\Projects\kotlin-jdk8\out\production\plugin-api;^
C:\Projects\kotlin-jdk8\out\test\container;^
C:\Projects\kotlin-jdk8\out\production\container;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\hamcrest-core-1.3.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\junit-4.12.jar;^
C:\Users\Nikolay.Krasko\.IdeaIC2016\config\plugins\Kotlin\kotlinc\lib\kotlin-test.jar;^
C:\Projects\kotlin-jdk8\out\production\resolution;^
C:\Projects\kotlin-jdk8\out\production\frontend.java;^
C:\Projects\kotlin-jdk8\out\production\descriptor.loader.java;^
C:\Projects\kotlin-jdk8\out\production\serialization;^
C:\Projects\kotlin-jdk8\out\production\backend-common;^
C:\Projects\kotlin-jdk8\out\production\cli;

set MY_CLASSPATH2=C:\Projects\kotlin-jdk8\out\production\light-classes;^
C:\Projects\kotlin-jdk8\out\production\js.translator;^
C:\Projects\kotlin-jdk8\out\production\js.dart-ast;^
C:\Projects\kotlin-jdk8\out\production\js.frontend;^
C:\Projects\kotlin-jdk8\out\production\js.parser;^
C:\Projects\kotlin-jdk8\out\production\js.serializer;^
C:\Projects\kotlin-jdk8\out\production\js.inliner;^
C:\Projects\kotlin-jdk8\dependencies\jansi.jar;^
C:\Projects\kotlin-jdk8\dependencies\jline.jar;^
C:\Projects\kotlin-jdk8\out\production\cli-common;^
C:\Projects\kotlin-jdk8\dependencies\cli-parser-1.1.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\jna.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\jdom.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\util.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\log4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\trove4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\groovy_rt.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\jps-builders.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\nanoxml-2.2.3.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\jgoodies-forms.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\protobuf-2.5.0.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\groovy-jps-plugin.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\jps\ui-designer-jps-plugin.jar;^
C:\Projects\kotlin-jdk8\out\production\annotation-collector;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jh.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\asm.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jna.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\boot.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\idea.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jdom.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jing.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\util.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\icons.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\log4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xbean.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\javac2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\asm-all.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\idea_rt.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsr166e.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\microba.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\openapi.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\trove4j.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\asm4-all.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\forms_rt.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\gson-2.5.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\isorelax.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\resolver.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\velocity.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xml-apis.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\automaton.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\batik-all.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\bootstrap.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\ecj-4.4.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jps-model.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\pty4j-0.6.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\resources.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\winp-1.23.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\extensions.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\guava-17.0.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\oromatcher.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\trang-core.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xercesImpl.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xmlrpc-2.0.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\annotations.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\asm-commons.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\cglib-2.2.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jaxen-1.1.3.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch-0.1.52.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jzlib-1.1.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jna-platform.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jps-builders.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jps-launcher.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\purejavacomm.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\resources_en.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xml-apis-ext.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\coverage-util.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\nanoxml-2.2.3.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\picocontainer.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xstream-1.4.8.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\cli-parser-1.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\coverage-agent.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\httpcore-4.4.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\httpmime-4.4.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jayatana-1.2.4.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jdkAnnotations.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jgoodies-forms.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsr173_1.0_api.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\markdown4j-2.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\rhino-js-1_7R4.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xpp3-1.1.4-min.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\commons-net-3.3.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\fluent-hc-4.4.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\miglayout-swing.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\nekohtml-1.9.14.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\serviceMessages.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\groovy-all-2.4.6.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\httpclient-4.4.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\imgscalr-lib-4.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jcip-annotations.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\slf4j-api-1.7.10.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\commons-codec-1.9.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\swingx-core-1.6.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\external-system-rt.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\commons-logging-1.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\proxy-vole_20131209.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jgoodies-looks-2.4.2.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.core.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\optimizedFileManager.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\slf4j-log4j12-1.7.10.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\snappy-in-java-0.3.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\coverage-instrumenter.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jgoodies-common-1.2.1.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\netty-all-4.1.0.Beta8.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\rngom-20051226-patched.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\sanselan-0.98-snapshot.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.pageant.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\xmlgraphics-commons-1.5.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.sshagent.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.usocket-nc.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.usocket-jna.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\markdownj-core-0.4.2-SNAPSHOT.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\commons-httpclient-3.1-patched.jar;^
C:\Projects\kotlin-jdk8\ideaSDK\lib\jsch.agentproxy.connector-factory.jar;^
C:\Projects\kotlin-jdk8\dependencies\android-5.0\lib\dx.jar;^
C:\Projects\kotlin-jdk8\out\production\builtins-serializer;^
C:\Projects\kotlin-jdk8\out\production\descriptors.runtime;^
C:\Projects\kotlin-jdk8\out\test\js.tests;^
C:\Projects\kotlin-jdk8\dependencies\rhino-1.7.6.jar;^
C:\Projects\kotlin-jdk8\out\production\preloader;^
C:\Projects\kotlin-jdk8\out\production\android-extensions-compiler;^
C:\Projects\kotlin-jdk8\out\production\daemon-client;^
C:\Projects\kotlin-jdk8\out\production\daemon-common;^
C:\Projects\kotlin-jdk8\dependencies\native-platform-uberjar.jar

echo %JDK_CLASSPATH%
echo %MY_CLASSPATH1%
echo %MY_CLASSPATH2%

echo Merge!

echo %JDK_CLASSPATH%%MY_CLASSPATH1%%MY_CLASSPATH2%

"C:\Program Files\Java\jdk1.8.0_66\bin\java"^
 -ea -Didea.launcher.port=7533^
 "-Didea.launcher.bin.path=C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 2016.1\bin"^
 -Didea.junit.sm_runner -Dfile.encoding=UTF-8 -classpath "%JDK_CLASSPATH%%MY_CLASSPATH1%%MY_CLASSPATH2%"^
 com.intellij.rt.execution.application.AppMain^ 
 com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 org.jetbrains.kotlin.jvm.runtime.JvmRuntimeDescriptorLoaderTestGenerated$CompiledJava,testClassDoesNotOverrideMethod
