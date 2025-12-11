#!/bin/bash

SOURCE="/Volumes/BigFu/napir/energy-monitoring/em-be"
TARGET="/Volumes/BigFu/generator-template/backend_template"

echo "Copying Operator support classes..."

# Copy and replace package names
copy_file() {
    src=$1
    dst=$2
    mkdir -p "$(dirname "$dst")"
    sed 's/com\.napir\.em/com.partner.be/g' "$src" > "$dst"
    echo "✓ $(basename "$dst")"
}

# Copy search and pagination support classes
copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/BaseSearchPO.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/BaseSearchPO.java"

copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/HasCompanyIdCondition.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/HasCompanyIdCondition.java"

copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/db/SearchParam.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/db/SearchParam.java"

copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/db/PageParam.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/db/PageParam.java"

copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/db/PageSizing.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/db/PageSizing.java"

copy_file \
    "$SOURCE/em-common/src/main/java/com/napir/em/common/result/DataPage.java" \
    "$TARGET/be-common/src/main/java/com/partner/be/common/result/DataPage.java"

# Copy OperatorSearchPO
copy_file \
    "$SOURCE/em-common-biz/src/main/java/com/napir/em/common/system/po/OperatorSearchPO.java" \
    "$TARGET/be-common-biz/src/main/java/com/partner/be/common/system/po/OperatorSearchPO.java"

echo "Operator support classes copied successfully!"
