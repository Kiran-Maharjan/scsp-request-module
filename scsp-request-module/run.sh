cat > run.sh <<'EOF'
#!/usr/bin/env bash
set -e
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out scsp.Main
EOF
chmod +x run.sh