#!/bin/bash

# Script to copy authentication-related files from em-be to backend_template
# and replace package names

SOURCE_DIR="/Volumes/BigFu/napir/energy-monitoring/em-be"
TARGET_DIR="/Volumes/BigFu/generator-template/backend_template"

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Starting file copy and package name replacement...${NC}"

# Function to copy and replace package names
copy_and_replace() {
    local source_file=$1
    local target_file=$2
    local source_pkg=$3
    local target_pkg=$4

    mkdir -p "$(dirname "$target_file")"

    if [ -f "$source_file" ]; then
        sed "s|package $source_pkg|package $target_pkg|g" "$source_file" | \
        sed "s|import $source_pkg|import $target_pkg|g" > "$target_file"
        echo -e "${GREEN}✓${NC} Copied: $(basename "$target_file")"
    else
        echo "✗ Source file not found: $source_file"
    fi
}

# 1. JWT and Security classes
echo -e "\n${BLUE}=== Copying JWT and Security classes ===${NC}"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/security/JwtTokenProvider.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/security/JwtTokenProvider.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/security/JwtAuthenticationFilter.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/security/JwtAuthenticationFilter.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/security/PasswordHashService.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/security/PasswordHashService.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/security/RateLimitService.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/security/RateLimitService.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/security/MutableHttpServletRequest.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/security/MutableHttpServletRequest.java" \
    "com.napir.em" \
    "com.partner.be"

# 2. Login related classes
echo -e "\n${BLUE}=== Copying Login classes ===${NC}"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/controller/LoginController.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/controller/LoginController.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/service/LoginService.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/service/LoginService.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/service/impl/LoginServiceImpl.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/service/impl/LoginServiceImpl.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/service/LoginServiceResult.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/service/LoginServiceResult.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/po/LoginPO.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/po/LoginPO.java" \
    "com.napir.em" \
    "com.partner.be"

# 3. Check Exist (duplicate validation) classes
echo -e "\n${BLUE}=== Copying CheckExist classes ===${NC}"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/controller/CheckExistController.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/controller/CheckExistController.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/po/CheckExistPO.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/po/CheckExistPO.java" \
    "com.napir.em" \
    "com.partner.be"

copy_and_replace \
    "$SOURCE_DIR/em-app-backend/src/main/java/com/napir/em/backend/common/po/ColumnNameValue.java" \
    "$TARGET_DIR/be-backend/src/main/java/com/partner/be/backend/common/po/ColumnNameValue.java" \
    "com.napir.em" \
    "com.partner.be"

echo -e "\n${GREEN}File copy completed!${NC}"
echo -e "${BLUE}Please review the copied files and make necessary adjustments.${NC}"
