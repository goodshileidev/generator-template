#!/bin/bash

SOURCE="/Volumes/BigFu/napir/energy-monitoring/em-be"
TARGET="/Volumes/BigFu/generator-template/backend_template"

echo "Copying Captcha files..."

# Copy and replace package names
copy_file() {
    src=$1
    dst=$2
    mkdir -p "$(dirname "$dst")"
    sed 's/com\.napir\.em/com.partner.be/g' "$src" > "$dst"
    echo "✓ $(basename "$dst")"
}

# Copy CaptchaResponse
copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/backend/common/captcha/CaptchaResponse.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/backend/common/captcha/CaptchaResponse.java"

# Copy CaptchaService
copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/backend/common/captcha/CaptchaService.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/backend/common/captcha/CaptchaService.java"

# Copy CaptchaController
copy_file \
    "$SOURCE/em-app-backend/src/main/java/com/napir/em/backend/common/controller/CaptchaController.java" \
    "$TARGET/be-backend/src/main/java/com/partner/be/backend/common/controller/CaptchaController.java"

echo "Captcha files copied successfully!"
