set JHHOME=\Installs\javahelp-2_0_01\jh2.0\javahelp

del /f/q JavaHelpSearch\DOCS JavaHelpSearch\DOCS.TAB JavaHelpSearch\OFFSETS JavaHelpSearch\POSITIONS JavaHelpSearch\SCHEMA JavaHelpSearch\TMAP

mkdir JavaHelpSearch

call %JHHOME%\bin\jhindexer -db JavaHelpSearch -verbose Pages\Introduction.html Pages\view.html Pages\ParameterizedQueries.html

del /f/q BasicQueryHelp.jar
jar -cvf BasicQueryHelp.jar BasicQueryHelp.hs Map.jhm BasicQueryTOC.xml BasicQueryIndex.xml Pages\Introduction.html Pages\view.html Pages\ParameterizedQueries.html Pages\BasicQueryMainScreen.png JavaHelpSearch\DOCS JavaHelpSearch\DOCS.TAB JavaHelpSearch\OFFSETS JavaHelpSearch\POSITIONS JavaHelpSearch\SCHEMA JavaHelpSearch\TMAP