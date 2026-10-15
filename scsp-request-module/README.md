cat > README.md <<'EOF'
# SCSP – Service Request Lifecycle Module

Assignment 3 Part A – Complex Module Implementation
Group 18 | Smart Community Services Platform

## Requirements
- JDK 11 or later (JDK 17 recommended)
- No external libraries

## Run (macOS / Linux / WSL / Git Bash)
    chmod +x run.sh
    ./run.sh

## Run (Windows)
    run.bat

## Manual compile & run
    javac -d out $(find src -name "*.java")
    java -cp out scsp.Main

## Project Structure
    src/scsp/
      Main.java
      model/         – domain entities and enums
      strategy/      – priority strategy
      factory/       – request factory
      repository/    – persistence abstraction
      service/       – application services, integration interfaces, adapters
      exception/     – custom exceptions
EOF