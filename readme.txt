This is simple instruction how you can compile our project’s modules.

For working with ICEcatImporter you need:
 - jdk (we use jdk1.6.0_24)
 - ant (we use apache-ant-1.8.2)

For building api (icecat-api-1.0.jar) which we used for importing products from ICEcat catalog use command line bellow

ant -buildfile build.xml BuildICEcatAPI

You can use our icecat api by LGPL (http://www.gnu.org/licenses/lgpl.html) in your own project.



For building ice.jar (our test application which use icecat-api-1.0.jar) use:

ant -buildfile build.xml BuildAll