@echo off
REM Configurações - ajuste o caminho do JavaFX SDK e o nome do seu arquivo principal
set JAVAFX_LIB="C:\Program Files (x86)\Java\javafx-sdk-24.0.1\lib"
set MAIN_CLASS=GravadorAppMelhorado
set SOURCE_FILE=%MAIN_CLASS%.java
set JAR_FILE=%MAIN_CLASS%.jar
set MANIFEST_FILE=manifest.txt

REM Apaga arquivos antigos
if exist %JAR_FILE% del %JAR_FILE%
if exist %MANIFEST_FILE% del %MANIFEST_FILE%

REM Criar o arquivo manifest para indicar a classe principal
echo Main-Class: %MAIN_CLASS% > %MANIFEST_FILE%

REM Compilar o código Java
javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml %SOURCE_FILE%
if errorlevel 1 (
    echo Erro na compilacao. Saindo...
    pause
    exit /b 1
)

REM Gerar o arquivo JAR
jar cfm %JAR_FILE% %MANIFEST_FILE% %MAIN_CLASS%.class
if errorlevel 1 (
    echo Erro ao criar o JAR. Saindo...
    pause
    exit /b 1
)

REM Executar o JAR com JavaFX configurado e menos warnings
java --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics -XX:+IgnoreUnrecognizedVMOptions -XX:+SuppressWarningsInGeneratedCode -jar %JAR_FILE%

pause
