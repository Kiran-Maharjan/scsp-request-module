# scsp-request-module
This is a Software Design Specification (SDS) report which builds on the Software Requirements Specification (SRS) report that was submitted for the Smart Community Services Platform (SCSP) in Assignment 2 (SRS). 

The SCSP is a secure Web and mobile application that brings together the previously siloed community services of Greenfield City Council. This allows residents to register, report community issues, book facilities, register for events, receive notifications and give feedback. Council staff check and prioritize service requests, order maintenance, arrange bookings and events, monitor performance and prepare operational reports. System Administrators are responsible for user management, system configuration, performance monitoring and generate analytical dashboards.
It integrates with 4 external systems, a GIS (Address validation and Maintenance Zones), a Payment Gateway (Facility payment and event payment), a Notification Service (Email and SMS), and an Authentication Service (Single Sign-On and Identity verification) [1], [3].

**Instructions to Run and Deploy**
**1 Prerequisites**
Java Development Kit (JDK) 17 or later (JDK 11+ also works). Verify with:
java -version
javac -version
No external libraries or build tools are required.

**2 Running from the Command Line**
****1.	Clone or download the project:**
git clone [repository URL]
cd scsp-request-module

**2.	Compile all Java files:**
# macOS / Linux
javac -d out $(find src -name "*.java")

# Windows PowerShell
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

3.	Run the demo:
java -cp out scsp.Main

**3 Convenience Scripts**
run.sh (macOS / Linux):
#!/usr/bin/env bash
set -e
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out scsp.Main
run.bat (Windows):
@echo off
if exist out rmdir /s /q out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out scsp.Main
del sources.txt
Make the shell script executable:
chmod +x run.sh
./run.sh
