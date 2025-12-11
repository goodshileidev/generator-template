#!/bin/bash
# Maven Build Fix Script
# 修复 Maven 构建问题的综合脚本

set -e

echo "========================================="
echo "Maven Build Fix Script"
echo "========================================="

cd /Volumes/BigFu/generator-template/backend_template/be-parent

echo ""
echo "Step 1: Cleaning all .lastUpdated files..."
/usr/bin/find ~/.m2/repository -name "*.lastUpdated" -type f -delete 2>/dev/null || true
echo "✓ Cleaned .lastUpdated files"

echo ""
echo "Step 2: Removing problematic Maven plugins cache..."
rm -rf ~/.m2/repository/org/apache/maven/plugins/maven-resources-plugin
rm -rf ~/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin
rm -rf ~/.m2/repository/org/apache/maven/plugins/maven-clean-plugin
echo "✓ Removed Maven plugins cache"

echo ""
echo "Step 3: Removing problematic dependencies cache..."
rm -rf ~/.m2/repository/org/codehaus/plexus
rm -rf ~/.m2/repository/org/apache/commons/commons-codec
rm -rf ~/.m2/repository/org/apache/maven/shared
echo "✓ Removed problematic dependencies"

echo ""
echo "Step 4: Setting environment variables..."
export M2_HOME=/Volumes/Docs/DEVELOPER/build/apache-maven/3.6.3/
export JAVA_HOME=/usr/local/opt/openjdk@11/
export PATH=$M2_HOME/bin:$PATH
echo "✓ Environment configured"
echo "  M2_HOME=$M2_HOME"
echo "  JAVA_HOME=$JAVA_HOME"

echo ""
echo "Step 5: Verifying Maven version..."
mvn --version

echo ""
echo "Step 6: Starting Maven compile (this may take 5-10 minutes)..."
echo "========================================="

mvn clean compile -DskipTests -U

echo ""
echo "========================================="
echo "✓ Build completed successfully!"
echo "========================================="
