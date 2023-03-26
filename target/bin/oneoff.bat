@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
for %%i in ("%~dp0..") do set "BASEDIR=%%~fi"

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\javacord\javacord-api\3.7.0\javacord-api-3.7.0.jar;"%REPO%"\org\javacord\javacord-core\3.7.0\javacord-core-3.7.0.jar;"%REPO%"\com\squareup\okhttp3\okhttp\4.9.3\okhttp-4.9.3.jar;"%REPO%"\com\squareup\okio\okio\2.8.0\okio-2.8.0.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-common\1.4.0\kotlin-stdlib-common-1.4.0.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib\1.4.10\kotlin-stdlib-1.4.10.jar;"%REPO%"\org\jetbrains\annotations\13.0\annotations-13.0.jar;"%REPO%"\com\squareup\okhttp3\logging-interceptor\4.9.3\logging-interceptor-4.9.3.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-jdk8\1.4.10\kotlin-stdlib-jdk8-1.4.10.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-jdk7\1.4.10\kotlin-stdlib-jdk7-1.4.10.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.12.6\jackson-databind-2.12.6.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.12.6\jackson-annotations-2.12.6.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.12.6\jackson-core-2.12.6.jar;"%REPO%"\com\neovisionaries\nv-websocket-client\2.14\nv-websocket-client-2.14.jar;"%REPO%"\com\codahale\xsalsa20poly1305\0.11.0\xsalsa20poly1305-0.11.0.jar;"%REPO%"\org\bouncycastle\bcprov-jdk15on\1.60\bcprov-jdk15on-1.60.jar;"%REPO%"\io\vavr\vavr\0.10.4\vavr-0.10.4.jar;"%REPO%"\io\vavr\vavr-match\0.10.4\vavr-match-0.10.4.jar;"%REPO%"\org\seleniumhq\selenium\selenium-java\4.7.2\selenium-java-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-api\4.7.2\selenium-api-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-chrome-driver\4.7.2\selenium-chrome-driver-4.7.2.jar;"%REPO%"\com\google\auto\service\auto-service-annotations\1.0.1\auto-service-annotations-1.0.1.jar;"%REPO%"\com\google\auto\service\auto-service\1.0.1\auto-service-1.0.1.jar;"%REPO%"\com\google\auto\auto-common\1.2\auto-common-1.2.jar;"%REPO%"\com\google\guava\guava\31.1-jre\guava-31.1-jre.jar;"%REPO%"\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;"%REPO%"\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;"%REPO%"\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;"%REPO%"\org\checkerframework\checker-qual\3.12.0\checker-qual-3.12.0.jar;"%REPO%"\com\google\errorprone\error_prone_annotations\2.11.0\error_prone_annotations-2.11.0.jar;"%REPO%"\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;"%REPO%"\org\seleniumhq\selenium\selenium-chromium-driver\4.7.2\selenium-chromium-driver-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-json\4.7.2\selenium-json-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-devtools-v106\4.7.2\selenium-devtools-v106-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-devtools-v107\4.7.2\selenium-devtools-v107-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-devtools-v108\4.7.2\selenium-devtools-v108-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-devtools-v85\4.7.2\selenium-devtools-v85-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-edge-driver\4.7.2\selenium-edge-driver-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-firefox-driver\4.7.2\selenium-firefox-driver-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-http\4.7.2\selenium-http-4.7.2.jar;"%REPO%"\dev\failsafe\failsafe\3.3.0\failsafe-3.3.0.jar;"%REPO%"\org\seleniumhq\selenium\selenium-ie-driver\4.7.2\selenium-ie-driver-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-remote-driver\4.7.2\selenium-remote-driver-4.7.2.jar;"%REPO%"\com\beust\jcommander\1.82\jcommander-1.82.jar;"%REPO%"\io\netty\netty-buffer\4.1.84.Final\netty-buffer-4.1.84.Final.jar;"%REPO%"\io\netty\netty-codec-http\4.1.84.Final\netty-codec-http-4.1.84.Final.jar;"%REPO%"\io\netty\netty-codec\4.1.84.Final\netty-codec-4.1.84.Final.jar;"%REPO%"\io\netty\netty-handler\4.1.84.Final\netty-handler-4.1.84.Final.jar;"%REPO%"\io\netty\netty-common\4.1.84.Final\netty-common-4.1.84.Final.jar;"%REPO%"\io\netty\netty-transport-classes-epoll\4.1.84.Final\netty-transport-classes-epoll-4.1.84.Final.jar;"%REPO%"\io\netty\netty-transport-classes-kqueue\4.1.84.Final\netty-transport-classes-kqueue-4.1.84.Final.jar;"%REPO%"\io\netty\netty-transport-native-unix-common\4.1.84.Final\netty-transport-native-unix-common-4.1.84.Final.jar;"%REPO%"\io\netty\netty-transport\4.1.84.Final\netty-transport-4.1.84.Final.jar;"%REPO%"\io\netty\netty-resolver\4.1.84.Final\netty-resolver-4.1.84.Final.jar;"%REPO%"\io\opentelemetry\opentelemetry-api\1.19.0\opentelemetry-api-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-context\1.19.0\opentelemetry-context-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-exporter-logging\1.19.0\opentelemetry-exporter-logging-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-metrics\1.19.0\opentelemetry-sdk-metrics-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-logs\1.19.0-alpha\opentelemetry-sdk-logs-1.19.0-alpha.jar;"%REPO%"\io\opentelemetry\opentelemetry-api-logs\1.19.0-alpha\opentelemetry-api-logs-1.19.0-alpha.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-common\1.19.0\opentelemetry-sdk-common-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-extension-autoconfigure-spi\1.19.0\opentelemetry-sdk-extension-autoconfigure-spi-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-extension-autoconfigure\1.19.0-alpha\opentelemetry-sdk-extension-autoconfigure-1.19.0-alpha.jar;"%REPO%"\io\opentelemetry\opentelemetry-exporter-common\1.19.0\opentelemetry-exporter-common-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk-trace\1.19.0\opentelemetry-sdk-trace-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-sdk\1.19.0\opentelemetry-sdk-1.19.0.jar;"%REPO%"\io\opentelemetry\opentelemetry-semconv\1.19.0-alpha\opentelemetry-semconv-1.19.0-alpha.jar;"%REPO%"\io\ous\jtoml\2.0.0\jtoml-2.0.0.jar;"%REPO%"\net\bytebuddy\byte-buddy\1.12.18\byte-buddy-1.12.18.jar;"%REPO%"\org\apache\commons\commons-exec\1.3\commons-exec-1.3.jar;"%REPO%"\org\asynchttpclient\async-http-client\2.12.3\async-http-client-2.12.3.jar;"%REPO%"\org\asynchttpclient\async-http-client-netty-utils\2.12.3\async-http-client-netty-utils-2.12.3.jar;"%REPO%"\io\netty\netty-codec-socks\4.1.60.Final\netty-codec-socks-4.1.60.Final.jar;"%REPO%"\io\netty\netty-handler-proxy\4.1.60.Final\netty-handler-proxy-4.1.60.Final.jar;"%REPO%"\io\netty\netty-transport-native-epoll\4.1.60.Final\netty-transport-native-epoll-4.1.60.Final-linux-x86_64.jar;"%REPO%"\io\netty\netty-transport-native-kqueue\4.1.60.Final\netty-transport-native-kqueue-4.1.60.Final-osx-x86_64.jar;"%REPO%"\org\reactivestreams\reactive-streams\1.0.3\reactive-streams-1.0.3.jar;"%REPO%"\com\typesafe\netty\netty-reactive-streams\2.0.4\netty-reactive-streams-2.0.4.jar;"%REPO%"\com\sun\activation\jakarta.activation\1.2.2\jakarta.activation-1.2.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-manager\4.7.2\selenium-manager-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-safari-driver\4.7.2\selenium-safari-driver-4.7.2.jar;"%REPO%"\org\seleniumhq\selenium\selenium-support\4.7.2\selenium-support-4.7.2.jar;"%REPO%"\org\jsoup\jsoup\1.15.3\jsoup-1.15.3.jar;"%REPO%"\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;"%REPO%"\org\apache\logging\log4j\log4j-core\2.19.0\log4j-core-2.19.0.jar;"%REPO%"\org\apache\logging\log4j\log4j-api\2.19.0\log4j-api-2.19.0.jar;"%REPO%"\org\slf4j\slf4j-simple\1.7.36\slf4j-simple-1.7.36.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar;"%REPO%"\ProjectMSB\ProjectMSB\1.0\ProjectMSB-1.0.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="oneoff" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" com.msb.project.OneOffProcess %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
