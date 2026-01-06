#!/bin/bash

SOURCE="/Volumes/BigFu/napir/energy-monitoring/em-be"
TARGET="/Volumes/BigFu/generator-template/backend_template"

echo "Copying entity classes..."

# Copy and replace package names
copy_file() {
    src=$1
    dst=$2
    mkdir -p "$(dirname "$dst")"
    sed 's/com\.napir\.em/com.partner.be/g' "$src" > "$dst"
    echo "✓ $(basename "$dst")"
}

# Copy Operator domain
copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/system/domain/Operator.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/system/domain/Operator.java"

# Copy Operator VO
copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/system/vo/OperatorVO.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/system/vo/OperatorVO.java"

# Copy Operator PO
copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/system/po/OperatorPO.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/system/po/OperatorPO.java"

# Copy ServiceResult classes
copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/result/ServiceResult.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/result/ServiceResult.java"

copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/result/ServiceResultType.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/result/ServiceResultType.java"

# Copy ApiLoginUser
copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/ApiLoginUser.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/ApiLoginUser.java"

echo "Entity classes copied successfully!"
