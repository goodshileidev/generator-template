#!/bin/bash

SOURCE="/Volumes/BigFu/napir/energy-monitoring/em-be"
TARGET="/Volumes/BigFu/generator-template/backend_template"

echo "Copying configuration files..."

# Copy and replace package names
copy_file() {
    src=$1
    dst=$2
    mkdir -p "$(dirname "$dst")"
    sed 's/com\.napir\.em/com.partner.be/g' "$src" > "$dst"
    echo "✓ $(basename "$dst")"
}

# Copy configuration files
copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/config/WebSecurityPathConfig.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/config/WebSecurityPathConfig.java"

copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/config/WebFilterConfig.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/config/WebFilterConfig.java"

copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/config/WebSecurityInterceptorConfig.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/config/WebSecurityInterceptorConfig.java"

copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/config/CorsConfig.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/config/CorsConfig.java"

echo "Configuration files copied successfully!"
