@echo off
setlocal
 
set JAVA_HOME=C:\Users\vipinve\java\jdk-19
set PATH=%JAVA_HOME%\bin;%PATH%
 
REM --- Debugging lines start here ---
echo Verifying JAVA_HOME: %JAVA_HOME%
echo Verifying PATH includes JDK bin: %PATH%
where javac.exe
REM --- Debugging lines end here ---
 
set SOURCE_DIR=src
set LIB_DIR=lib
set BUILD_DIR=build
set JDBC_JAR=%LIB_DIR%\ojdbc11.jar
set JSON_JAR=%LIB_DIR%\json-20240303.jar
 
rem Create build directory if it doesn't exist
if not exist %BUILD_DIR% mkdir %BUILD_DIR%
 
echo Compiling Java code...
javac -d %BUILD_DIR% -cp "%JDBC_JAR%;%JSON_JAR%" %SOURCE_DIR%\com\plexushr\dao\*.java %SOURCE_DIR%\com\plexushr\model\*.java %SOURCE_DIR%\com\plexushr\controller\*.java %SOURCE_DIR%\com\plexushr\server\*.java
 
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    goto :eof
   
)
 
echo Running Java server...
java -cp "%BUILD_DIR%;%JDBC_JAR%;%JSON_JAR%" com.plexushr.server.SimpleHttpServer
 
endlocal
 