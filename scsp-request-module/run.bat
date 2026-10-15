cat > run.bat <<'EOF'
@echo off
if exist out rmdir /s /q out
if exist sources.txt del sources.txt
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 (
  del sources.txt
  exit /b 1
)
java -cp out scsp.Main
del sources.txt
EOF